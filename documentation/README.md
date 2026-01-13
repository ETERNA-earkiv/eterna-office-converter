# Office Documents Converter - User Guide

## Overview

The office document conversion job is designed to support digital preservation workflows in the ETERNA system. The tool converts various office formats to preservation-friendly formats to ensure long-term accessibility and archival stability for office documents.

## How to Use the Office Documents Conversion Job

1. **Start conversion**: You can begin a conversion process by selecting an Intellectual entity, a Representation, or an individual file via the Catalog page or Search page and starting a new Preservation Job.
2. **Select output format**: Choose the desired output format from available options (PDF, PDFA-1A, PDFA-1B, PDFA-2B, PDFA-3B, ODT, OTT, DOC, DOCX, RTF, TXT, HTML, XLS, XLSX, CSV, ODS, ODP, PPT, PPTX, SVG)
3. **Configure conversion**: Specify whether you want to create a new representation or dissemination copy
4. **Run conversion**: Run the tool to start the conversion process

### Supported Input Formats
The tool supports conversion from a wide range of office document's formats including:
- 📄 **Text Documents**:
  - Common formats: DOC, DOCX, ODT, RTF, TXT
  - Legacy formats: DOC (Word 97–2003), SXW
  - Markup formats: HTML, XHTML
  - Other: XML-based text documents
- 📊 **Spreadsheets**:
  - Common formats: XLS, XLSX, ODS, CSV
  - Legacy formats: XLS (Excel 97–2003), SXC
  - Other: TSV, DIF
- 📽 **Presentations**:
  - Common formats: PPT, PPTX, ODP
  - Legacy formats: PPT (PowerPoint 97–2003), SXI
- 🧮 **Drawings & Graphics**:
  - LibreOffice Draw formats: ODG
  - Vector graphics: SVG, SVGZ
  - Other supported imports: WMF, EMF
- 🗂 **Templates**:
  - Text templates: DOT, DOTX, OTT
  - Spreadsheet templates: XLT, XLTX, OTS
  - Presentation templates: POT, POTX, OTP
- 🧾 **Miscellaneous**:
  - OpenDocument formats: ODT, ODS, ODP, ODG
  - StarOffice legacy formats: SXW, SXC, SXI, SXD

### Output Formats
You can convert office documents to the following preservation and access formats:

- **PDF**: Fixed-layout format ideal for long-term preservation and distribution, preserves fonts, layout, and embedded content
- **PDF/A**: Archival-compliant PDF format designed for long-term preservation, ensures self-contained documents with embedded fonts and metadata
- **ODT**: OpenDocument Text format for editable text documents with open standards support
- **ODS**: OpenDocument Spreadsheet format for structured and editable tabular data
- **ODP**: OpenDocument Presentation format for editable slide-based documents
- **DOCX**: Microsoft Word Open XML format for compatibility with modern Word processors
- **XLSX**: Microsoft Excel Open XML format for spreadsheet interoperability
- **PPTX**: Microsoft PowerPoint Open XML format for presentation sharing
- **HTML**: Web-friendly format suitable for publishing documents online
- **TXT**: Plain text format for simple content extraction without formatting

### Conversion Process
1. **Format detection**: The tool automatically detects the input office document format using LibreOffice’s import filters
2. **Document normalization**: Documents are normalized for the target format, ensuring correct layout, fonts, encoding, page size, and embedded objects are handled appropriately
3. **Content validation**: Files are evaluated to ensure structural integrity and minimal content or formatting loss during conversion
4. **Conversion execution**: Documents are converted using LibreOffice in headless mode, leveraging its native document filters for reliable and standards-compliant output
5. **Representation creation**: During conversion, the new files are placed in the same intellectual entity as the originals, in a representation with the chosen representation type and status: Preservation. If such a representation does not already exist in the intellectual entity, a new one will be created.

## Known Limitations

### File Handling Notes
- Files that are already in the target format are not converted
- If multiple files have the same name, only one may be preserved due to system limitations

### Conversion Fidelity
- Document layout, pagination, and styling are preserved as accurately as possible during conversion
- Fonts are embedded in output formats where supported (e.g., PDF/PDF-A)
- Embedded images and objects are retained at their original resolution whenever possible
- Conversions rely on LibreOffice’s native import and export filters for standards-compliant results
- **Interactive or executable PDF features (including JavaScript, XFA forms, and embedded media) are not preserved and are normalized to static content during conversion to meet long-term preservation requirements.**

## Best Practices
1. **Choose appropriate formats**:
Use PDF/A for long-term preservation, PDF for access and distribution, and ODF formats (ODT, ODS, ODP) when future editability is required
2. **Preserve originals**:
Always retain the original office documents alongside converted versions to ensure authenticity and enable future reprocessing if needed
3. **Use consistent naming**:
Ensure output files follow a consistent and unique naming convention to avoid overwrites and maintain clear relationships between originals and derivatives
4. **Test conversions**:
Perform test conversions on representative sample files, especially for complex documents containing macros, embedded objects, or custom fonts
5. **Review results**:
Verify layout, pagination, fonts, tables, and embedded content after conversion to confirm the output meets preservation and access requirements
6. **Be aware of limitations**:
Some features (e.g., macros, form controls, or proprietary elements) may not be fully preserved during conversion; document any known limitations in the preservation metadata

## Troubleshooting
### Common Issues

- **Conversion Fails**
  - **Cause**: Input format is not supported by LibreOffice, file is corrupted, or required fonts/resources are missing
  - **Solution**: Verify the input format is supported, check file integrity, and ensure LibreOffice is correctly installed and accessible in headless mode

- **Files Not Converted**
  - **Cause**: File is already in the target format or the conversion is skipped by the framework
  - **Solution**: Confirm whether conversion is necessary and review plugin logs to verify that the file was intentionally skipped

- **Unexpected Results**
  - **Layout changes**: Minor differences in pagination, spacing, or alignment may occur due to rendering engine differences
  - **Font substitution**: Missing fonts are replaced with available system fonts, which may affect appearance
  - **Embedded objects**: Some embedded elements (e.g., OLE objects, macros) may not be fully preserved
  - **Spreadsheet formulas**: Formulas are preserved, but calculated values may differ if dependent data or locale settings vary

- **Performance Issues**
  - **Large or complex documents**: Files with many pages, images, or embedded objects require more processing time and memory
  - **Batch processing delays**: Converting large collections may take longer; consider processing in smaller batches or allocating additional system resources

### Additional Notes

- **Format selection**: Choose the output format based on intended use—PDF/A for long-term preservation, PDF for access and sharing, and ODF formats (ODT, ODS, ODP) when future editing or reuse is required
- **Unsupported formats**: Some proprietary, encrypted, or highly specialized document formats may not be supported by LibreOffice and therefore cannot be converted; such files will be reported as unsupported by the conversion framework

For technical support or questions about specific formats, contact your system administrator or raise an issue on our GitHub repository.
