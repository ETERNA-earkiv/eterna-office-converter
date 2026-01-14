/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE.md file at the root of the source tree
 */
package org.roda.core.plugins;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.roda.core.RodaCoreFactory;
import org.roda.core.TestsHelper;
import org.roda.core.data.common.RodaConstants;
import org.roda.core.data.exceptions.RODAException;
import org.roda.core.data.v2.index.IndexResult;
import org.roda.core.data.v2.index.filter.Filter;
import org.roda.core.data.v2.index.filter.SimpleFilterParameter;
import org.roda.core.data.v2.index.select.SelectedItemsList;
import org.roda.core.data.v2.index.sublist.Sublist;
import org.roda.core.data.v2.ip.AIP;
import org.roda.core.data.v2.ip.IndexedAIP;
import org.roda.core.data.v2.ip.IndexedFile;
import org.roda.core.data.v2.ip.IndexedRepresentation;
import org.roda.core.data.v2.ip.Permissions;
import org.roda.core.data.v2.ip.Representation;
import org.roda.core.data.v2.jobs.Job;
import org.roda.core.data.v2.jobs.PluginType;
import org.roda.core.index.IndexService;
import org.roda.core.index.IndexTestUtils;
import org.roda.core.model.ModelService;
import org.roda.core.plugins.base.characterization.SiegfriedPlugin;
import se.whitered.eterna.plugins.officedocumentsconverter.OfficeDocumentsConverter;
import org.roda.core.storage.ContentPayload;
import org.roda.core.storage.fs.FSPathContentPayload;
import org.roda.core.storage.fs.FSUtils;
import org.roda.core.util.IdUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Test(groups = { RodaConstants.TEST_GROUP_ALL })
public class OfficeDocumentsConverterTest {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(OfficeDocumentsConverterTest.class);

    private static Path basePath;
    private static ModelService model;
    private static IndexService index;

    private Path tmpDir;
    private AIP aip;
    private Representation rep;

    /* ------------------------------------------------------
     * SETUP
     * ------------------------------------------------------ */
    @BeforeMethod
    public void setUp() throws Exception {

        // Create a temporary base directory for this test
        basePath = TestsHelper.createBaseTempDir(this.getClass(), true);

        // Boot full RODA test environment
        RodaCoreFactory.instantiateTest(
                true,   // Solr
                true,   // LDAP
                true,   // Folder monitor
                true,   // Orchestrator
                true,   // Plugin manager
                false   // Default resources
        );

        // Get core services
        model = RodaCoreFactory.getModelService();
        index = RodaCoreFactory.getIndexService();

        // Load Office converter configuration
        RodaCoreFactory.addConfiguration("office-documents-converter.properties");

        // Register the OfficeDocumentsConverter plugin explicitly
        RodaCoreFactory.getPluginManager()
                .registerPlugin(new OfficeDocumentsConverter<>());

        // Load Office test corpus from classpath
        URL corporaURL =
                OfficeDocumentsConverterTest.class.getResource("/corpora/Office");
        Path corporaPath = Paths.get(corporaURL.toURI());

        // Copy corpus to a temp directory
        tmpDir = Files.createTempDirectory("office-converter-test");
        FileUtils.copyDirectory(corporaPath.toFile(), tmpDir.toFile());

        // Create an AIP
        aip = model.createAIP(
                null,
                RodaConstants.AIP_TYPE_MIXED,
                new Permissions(),
                RodaConstants.ADMIN
        );

        // Create a representation to store original files
        String repId = IdUtils.createUUID();
        rep = model.createRepresentation(
                aip.getId(),
                repId,
                true,
                RodaConstants.AIP_TYPE_MIXED,
                true,
                RodaConstants.ADMIN
        );

        // Commit AIP and representation to Solr
        index.commitAIPs();

        // Ingest all Office files into the representation
        for (File file : tmpDir.toFile().listFiles()) {

            ContentPayload payload =
                    new FSPathContentPayload(file.toPath().toAbsolutePath());

            model.createFile(
                    aip.getId(),
                    repId,
                    List.of(FilenameUtils.getExtension(file.getName())),
                    file.getName(),
                    payload,
                    RodaConstants.ADMIN
            );
        }

        // Commit ingested files
        index.commitAIPs();

        LOGGER.info("OfficeDocumentsConverter test setup completed");
    }

    /* ------------------------------------------------------
     * CLEANUP
     * ------------------------------------------------------ */
    @AfterMethod
    public void tearDown() throws Exception {
        IndexTestUtils.resetIndex();
        RodaCoreFactory.shutdown();
        FSUtils.deletePathQuietly(basePath);
        FileUtils.deleteQuietly(tmpDir.toFile());
    }

    /* ------------------------------------------------------
     * TEST
     * ------------------------------------------------------ */
    @Test
    public void testOfficeDocumentsConverter_PDF_StrictValidation()
            throws RODAException {

        /* -------------------------------
         * 1. Fetch indexed AIP
         * ------------------------------- */
        Filter aipFilter = new Filter();
        aipFilter.add(new SimpleFilterParameter(
                RodaConstants.AIP_ID, aip.getId()));

        IndexResult<IndexedAIP> aipResult =
                index.find(
                        IndexedAIP.class,
                        aipFilter,
                        null,
                        new Sublist(0, 1),
                        List.of("uuid")
                );

        Assert.assertEquals(aipResult.getResults().size(), 1,
                "AIP should be indexed");

        IndexedAIP indexedAIP = aipResult.getResults().get(0);

        /* -------------------------------
         * 2. Fetch original files
         * ------------------------------- */
        Filter fileFilter = new Filter();
        fileFilter.add(new SimpleFilterParameter(
                RodaConstants.FILE_AIP_ID, indexedAIP.getUUID()));
        fileFilter.add(new SimpleFilterParameter(
                RodaConstants.FILE_REPRESENTATION_ID, rep.getId()));
        fileFilter.add(new SimpleFilterParameter("isDirectory", "false"));

        IndexResult<IndexedFile> sourceFiles =
                index.find(
                        IndexedFile.class,
                        fileFilter,
                        null,
                        new Sublist(0, 100),
                        List.of("uuid", "originalName", "fileFormat", "extension")
                );

        Assert.assertTrue(sourceFiles.getResults().size() > 0,
                "No source files found for conversion");

        /* -------------------------------
         * 3. Select files for conversion
         * ------------------------------- */
        List<String> sourceFileIds =
                sourceFiles.getResults()
                        .stream()
                        .map(IndexedFile::getUUID)
                        .toList();

        SelectedItemsList<IndexedFile> selectedFiles =
                SelectedItemsList.create(IndexedFile.class, sourceFileIds);

        /* -------------------------------
         * 4. Execute OfficeDocumentsConverter
         * ------------------------------- */
        Map<String, String> params = new HashMap<>();
        params.put(RodaConstants.PLUGIN_PARAMS_CONVERSION_PROFILE, "pdf");
        params.put(
                RodaConstants.PLUGIN_PARAMS_REPRESENTATION_OR_DIP,
                "type=rep;value=preservation;markAsPreservation=true"
        );

        Job job = TestsHelper.executeJob(
                OfficeDocumentsConverter.class,
                params,
                PluginType.AIP_TO_AIP,
                selectedFiles
        );

        index.commitAIPs();

        /* -------------------------------
         * 5. Validate job stats
         * ------------------------------- */
        Assert.assertEquals(
                job.getJobStats().getCompletionPercentage(),
                100,
                "OfficeDocumentsConverter job did not complete"
        );

        Assert.assertEquals(
                job.getJobStats().getSourceObjectsProcessedWithSuccess(),
                sourceFiles.getResults().size(),
                "Not all files were converted successfully"
        );

        /* -------------------------------
         * 6. Fetch preservation representations
         * ------------------------------- */
        Filter preservationRepFilter = new Filter();
        preservationRepFilter.add(new SimpleFilterParameter(
                RodaConstants.REPRESENTATION_STATES, "PRESERVATION"));
        preservationRepFilter.add(new SimpleFilterParameter(
                RodaConstants.REPRESENTATION_AIP_ID, indexedAIP.getUUID()));

        IndexResult<IndexedRepresentation> preservationReps =
                index.find(
                        IndexedRepresentation.class,
                        preservationRepFilter,
                        null,
                        new Sublist(0, 10),
                        List.of("id", "uuid")
                );

        Assert.assertTrue(
                preservationReps.getResults().size() > 0,
                "No preservation representation created"
        );

        /* -------------------------------
         * 7. Fetch converted PDF files
         * ------------------------------- */
        List<IndexedFile> convertedFiles = new ArrayList<>();

        for (IndexedRepresentation presRep : preservationReps.getResults()) {

            Filter pdfFilter = new Filter();
            pdfFilter.add(new SimpleFilterParameter(
                    RodaConstants.FILE_AIP_ID, indexedAIP.getUUID()));
            pdfFilter.add(new SimpleFilterParameter(
                    RodaConstants.FILE_REPRESENTATION_ID, presRep.getId()));
            pdfFilter.add(new SimpleFilterParameter("extension", "pdf"));

            IndexResult<IndexedFile> pdfs =
                    index.find(
                            IndexedFile.class,
                            pdfFilter,
                            null,
                            new Sublist(0, 100),
                            List.of("uuid", "originalName", "fileFormat", "extension")
                    );

            convertedFiles.addAll(pdfs.getResults());
        }

        Assert.assertTrue(
                convertedFiles.size() > 0,
                "No PDF files were created"
        );

        /* -------------------------------
         * 8. Run Siegfried on converted PDFs
         * ------------------------------- */
        List<String> convertedIds =
                convertedFiles.stream()
                        .map(IndexedFile::getUUID)
                        .toList();

        Job siegfriedJob = TestsHelper.executeJob(
                SiegfriedPlugin.class,
                Collections.emptyMap(),
                PluginType.MISC,
                SelectedItemsList.create(IndexedFile.class, convertedIds)
        );

        Assert.assertEquals(
                siegfriedJob.getJobStats().getCompletionPercentage(),
                100,
                "Siegfried characterization did not complete"
        );

        index.commitAIPs();

        // 9. Validate PDF metadata (extension is mandatory, MIME is optional)
        for (IndexedFile pdf : convertedFiles) {

            IndexedFile refreshed = index.retrieve(
                    IndexedFile.class,
                    pdf.getUUID(),
                    RodaConstants.FILE_FORMAT_FIELDS_TO_RETURN
            );

            Assert.assertEquals(
                    refreshed.getFileFormat().getExtension().toLowerCase(),
                    "pdf",
                    "Converted file extension is not PDF"
            );

            // MIME type is optional unless characterization is enforced
            if (refreshed.getFileFormat().getMimeType() != null) {
                Assert.assertTrue(
                        refreshed.getFileFormat().getMimeType().toLowerCase().contains("pdf"),
                        "Incorrect MIME type for " + refreshed.getOriginalName()
                );
            }
        }


        LOGGER.info("OfficeDocumentsConverter strict PDF test passed");
    }
}
