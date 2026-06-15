Office Documents Converter
-------------------------

[🇸🇪 Svenska](README_sv_SE.md)

[![License: LGPL v3](https://img.shields.io/badge/License-LGPL%20v3-blue.svg)](LICENSE.md)

A plugin for ETERNA providing conversion of office document formats into preservation-friendly formats.  
This plugin normalizes commonly used office documents into standardized formats suitable for archival storage and long-term access.

## Features

- **Office Document Normalization**  
  Converts widely used office document formats into standardized output formats to ensure consistency and accessibility in archival environments.

- **Supported Input Formats**  
  The Office Documents Converter supports conversion of the following document types:
    - Microsoft Word (`.doc`, `.docx`)
    - Microsoft Excel (`.xls`, `.xlsx`)
    - Microsoft PowerPoint (`.ppt`, `.pptx`)
    - PDF files (`.pdf`)
    - OpenDocument formats (`.odt`, `.ods`, `.odp`)
    - Plain text files (`.txt`)

- **Supported Output Formats**
    - **PDF**: Primary output format for normalized access and storage
    
### Known Limitations

The Office Documents Converter currently does not handle:

- **Macros and Embedded Scripts**  
  Documents containing macros, embedded scripts, or active content may lose this functionality during conversion.

- **Complex Layout Fidelity**  
  Documents with highly complex layouts, advanced styling, or non-embedded fonts may not render identically in the output PDF.

- **Password-Protected Documents**  
  Encrypted or password-protected files cannot be converted unless protection is removed prior to ingestion.

- **External or Embedded Attachments**  
  Linked files, external references, or embedded objects within office documents are not preserved in the converted output.

## Installation

Download the latest release of the Office Documents Converter plugin and extract it into:
`/.roda/config/plugins/`
Restart ETERNA to activate the plugin.

> **Note:** The converter depends on external document conversion tools (such as LibreOffice or `unoconvert`). Ensure all required system dependencies are installed and available in the runtime environment.

## License

See [LICENSE.md](LICENSE.md) for details.

