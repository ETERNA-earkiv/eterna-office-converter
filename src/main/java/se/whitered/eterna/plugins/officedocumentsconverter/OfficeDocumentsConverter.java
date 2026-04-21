/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE.md file at the root of the source tree.
 */
package se.whitered.eterna.plugins.officedocumentsconverter;

import org.roda.core.RodaCoreFactory;
import org.roda.core.common.FileFormatUtils;
import org.roda.core.data.common.RodaConstants;
import org.roda.core.data.exceptions.InvalidParameterException;
import org.roda.core.data.v2.IsRODAObject;
import org.roda.core.data.v2.jobs.PluginParameter;
import org.roda.core.data.v2.jobs.PluginParameter.PluginParameterType;
import org.roda.core.data.v2.jobs.Report;
import org.roda.core.index.IndexService;
import org.roda.core.model.ModelService;
import org.roda.core.plugins.Plugin;
import org.roda.core.plugins.PluginException;
import org.roda.core.plugins.base.conversion.AbstractConvertPlugin;
import org.roda.core.util.CommandException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Office Documents Converter Plugin
 * Converts Word, Excel, PowerPoint etc. to PDF (or other formats)
 * using UNO server.
 */
@SuppressWarnings("deprecation")
public class OfficeDocumentsConverter<T extends IsRODAObject> extends AbstractConvertPlugin<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(OfficeDocumentsConverter.class);

    private static final String CONVERSION_PROFILE_PARAM_KEY = "parameter.conversion_profile";

    private static final Map<String, PluginParameter> pluginParameters = new LinkedHashMap<>();

    static {


        pluginParameters.put(RodaConstants.PLUGIN_PARAMS_REPRESENTATION_OR_DIP,
                PluginParameter.getBuilder(RodaConstants.PLUGIN_PARAMS_REPRESENTATION_OR_DIP, "Outcome",
                                PluginParameterType.CONVERSION)
                        .withDescription(
                                "A conversion can create a representation or a dissemination. Please choose which option to output")
                        .build());
    }


    public OfficeDocumentsConverter() {
        super();
    }

    @Override
    public void init() throws PluginException {
        LOGGER.info("OfficeDocumentsConverter initialized");
    }

    @Override
    public String getName() {
        return "Office Document Converter";
    }

    @Override
    public String getDescription() {
        return "Converts Word, Excel, PowerPoint and other office formats to preservation formats via UNO server.";
    }

    @Override
    public String getVersionImpl() {
        return "1.0.0";
    }

    @Override
    public Plugin<T> cloneMe() {
        return new OfficeDocumentsConverter<>();
    }

    @Override
    protected Map<String, PluginParameter> getDefaultParameters() {
        return pluginParameters.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> new PluginParameter(e.getValue()),
                        (u, v) -> u,
                        LinkedHashMap::new));
    }

    @Override
    public List<PluginParameter> getParameters() {
        return this.orderParameters(this.getDefaultParameters());
    }


    @Override
    protected List<PluginParameter> orderParameters(Map<String, PluginParameter> params) {
        return new ArrayList<>(params.values());
    }

    @Override
    public void setParameterValues(Map<String, String> parameters) throws InvalidParameterException {
        String profileValue = parameters.get(CONVERSION_PROFILE_PARAM_KEY);
        if (profileValue == null || profileValue.trim().isEmpty()) {
            LOGGER.warn("Conversion profile parameter '{}' is missing or empty in the provided parameters.",
                    CONVERSION_PROFILE_PARAM_KEY);
            throw new InvalidParameterException(
                    "Required conversion profile parameter '" + CONVERSION_PROFILE_PARAM_KEY + "' is missing.");
        }
        profileValue = profileValue.trim().toLowerCase();
        parameters.put("parameter.option." + profileValue, "[parameter.output_format]");
        parameters.put(RodaConstants.PLUGIN_PARAMS_OUTPUT_FORMAT, profileValue);
        LOGGER.debug("Setting output format from conversion profile parameter '{}': {}", CONVERSION_PROFILE_PARAM_KEY,
                profileValue);
        super.setParameterValues(parameters);
    }


    @Override
    public boolean areParameterValuesValid() {

        Map<String, String> params = getParameterValues();

        boolean outcomeSet = params.containsKey(RodaConstants.PLUGIN_PARAMS_REPRESENTATION_OR_DIP)
                && !params.get(RodaConstants.PLUGIN_PARAMS_REPRESENTATION_OR_DIP).isEmpty();

        boolean profileSet = params.containsKey(CONVERSION_PROFILE_PARAM_KEY)
                && !params.get(CONVERSION_PROFILE_PARAM_KEY).isEmpty();

        boolean outputSet = super.getOutputFormat() != null && !super.getOutputFormat().isEmpty();

        return outcomeSet && profileSet && outputSet;
    }

    @Override
    public Report beforeAllExecute(IndexService index, ModelService model) {
        return new Report();
    }

    @Override
    public Report afterAllExecute(IndexService index, ModelService model) {
        return new Report();
    }

    @Override
    public List<String> getApplicableTo() {
        return FileFormatUtils.getInputExtensions("office-documents-converter");
    }

    @Override
    public List<String> getConvertableTo() {
        String cfg = RodaCoreFactory.getRodaConfigurationAsString(
                "core", "tools", "office-documents-converter", "outputFormats");
        return Arrays.asList(cfg.split("\\s+"));
    }

    @Override
    public Map<String, List<String>> getPronomToExtension() {
        return FileFormatUtils.getPronomToExtension("office-documents-converter");
    }

    @Override
    public Map<String, List<String>> getMimetypeToExtension() {
        return FileFormatUtils.getMimetypeToExtension("office-documents-converter");
    }

    public List<String> getExcludedExtensions() {
        String excluded = RodaCoreFactory.getRodaConfigurationAsString(
                "core", "tools", "office-documents-converter", "excludedExtensions");
        if (excluded == null || excluded.isBlank()) {
            return new ArrayList<>();
        }
        return Arrays.asList(excluded.split("\\s+"));
    }


    @Override
    public String executePlugin(Path inputPath, Path outputPath, String fileFormat)
            throws IOException, CommandException {

        LOGGER.info("OfficeDocumentsConverter: {} -> {}", inputPath, outputPath);

        String host = getEnvOrDefault("UNOSERVER_HOST", "localhost");
        String port = getEnvOrDefault("UNOSERVER_PORT", "2003");

        String outputFormat = super.getOutputFormat();
        if (outputFormat == null || outputFormat.isBlank()) {
            throw new CommandException("Output format was not set.");
        }

        /* ------------------------------------------------------------------
         * 1. PDF/A handling
         * ------------------------------------------------------------------ */
        String unoFormat = outputFormat;
        List<String> exportOptions = new ArrayList<>();

        if (outputFormat.startsWith("pdfa-")) {
            unoFormat = "pdf";
            switch (outputFormat) {
                case "pdfa-1a", "pdfa-1b" -> exportOptions.add("SelectPdfVersion=1");
                case "pdfa-2b" -> exportOptions.add("SelectPdfVersion=2");
                case "pdfa-3b" -> exportOptions.add("SelectPdfVersion=3");
                default -> throw new CommandException(
                        "Unsupported PDF/A profile: " + outputFormat
                );
            }
        }

        /* ------------------------------------------------------------------
         * 2. Build unoconvert command using stdin/stdout so no file paths
         *    are shared with the unoserver container.
         * ------------------------------------------------------------------ */
        List<String> command = new ArrayList<>();

        command.addAll(List.of(
                "unoconvert",
                "--host", host,
                "--port", port,
                "--convert-to", unoFormat
        ));

        if (!exportOptions.isEmpty()) {
            command.add("--filter-options");
            command.add(String.join(",", exportOptions));
        }

        command.add("-");  // read input from stdin
        command.add("-");  // write output to stdout

        LOGGER.info("Executing: {}", String.join(" ", command));

        Process process = new ProcessBuilder(command).start();

        // Write input file to stdin in a separate thread to avoid deadlock.
        Thread stdinWriter = new Thread(() -> {
            try (OutputStream stdin = process.getOutputStream()) {
                Files.copy(inputPath, stdin);
            } catch (IOException e) {
                LOGGER.warn("Error writing to unoconvert stdin", e);
            }
        });
        stdinWriter.start();

        StringBuilder stderrBuffer = new StringBuilder();
        Thread stderrReader = new Thread(
                "unoconvert-stderr-" + inputPath.getFileName(),
                () -> {
                    try {
                        stderrBuffer.append(
                            new String(process.getErrorStream().readAllBytes(), StandardCharsets.UTF_8));
                    } catch (IOException e) {
                        LOGGER.warn("Failed to read unoconvert stderr", e);
                    }
                });
        stderrReader.start();

        byte[] outputBytes = process.getInputStream().readAllBytes();

        int exitCode;
        try {
            boolean finished = process.waitFor(5, TimeUnit.MINUTES);
            if (!finished) {
                process.destroyForcibly();
                throw new CommandException("unoconvert timed out after 5 minutes");
            }
            exitCode = process.exitValue();
            stdinWriter.join();
            // stderrReader.join() anropas efter waitFor (Task 3 hanterar join med timeout)
            stderrReader.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new CommandException("Conversion interrupted", e);
        }

        String errorOutput = stderrBuffer.toString();

        if (exitCode != 0) {
            LOGGER.error("unoconvert failed (host={}, port={}):\n{}", host, port, errorOutput);
            throw new CommandException("Unoconvert failed:\n" + errorOutput);
        }

        if (outputBytes.length == 0) {
            throw new CommandException("Unoconvert finished but produced no output");
        }

        Files.write(outputPath, outputBytes);

        LOGGER.info("Conversion successful → {}", outputPath);
        return outputPath.toString();
    }

    private String getEnvOrDefault(String key, String def) {
        String v = System.getenv(key);
        return (v == null || v.isBlank()) ? def : v.trim();
    }
}
