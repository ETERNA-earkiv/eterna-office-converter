Kontorsdokumentkonverterare
---------------------------

[🇬🇧 English](README.md)

[![License: LGPL v3](https://img.shields.io/badge/License-LGPL%20v3-blue.svg)](LICENSE.md)

Ett plugin för ETERNA som konverterar kontorsdokument till bevarandevänliga format.
Pluginet normaliserar vanliga kontorsdokument till standardiserade format lämpliga för långtidsarkivering och framtida åtkomst.

## Funktioner

- **Normalisering av kontorsdokument**  
  Konverterar vanliga kontorsdokumentformat till standardiserade utdataformat för att säkerställa konsekvens och tillgänglighet i arkivmiljöer.

- **Stödda indataformat**  
  Kontorsdokumentkonverteraren stöder konvertering av följande dokumenttyper:
    - Microsoft Word (`.doc`, `.docx`)
    - Microsoft Excel (`.xls`, `.xlsx`)
    - Microsoft PowerPoint (`.ppt`, `.pptx`)
    - OpenDocument-format (`.odt`, `.ods`, `.odp`)
    - Oformaterade textfiler (`.txt`)

- **Stödda utdataformat**
    - **PDF/A-1B** *(rekommenderas för arkivering)*: Uppfyller Riksarkivets krav för kontorsdokument (RA-FS, ISO 19005-1:2005)
    - **PDF/A-1A, PDF/A-2B, PDF/A-3B**: Ytterligare arkivprofiler
    - **PDF**: Standardformat för distribution och utskrift
    - **ODT, ODS, ODP**: Redigerbara OpenDocument-format
    - **DOCX, XLSX, PPTX**: Microsoft Office OOXML-format
    - **DOC, XLS, PPT, RTF, CSV, TXT, HTML, OTT, SVG**: Övriga format

### Kända begränsningar

Kontorsdokumentkonverteraren hanterar för närvarande inte:

- **Makron och inbäddade skript**  
  Dokument med makron, inbäddade skript eller aktivt innehåll kan förlora denna funktionalitet vid konvertering.

- **Komplex layouttrohet**  
  Dokument med mycket komplexa layouter, avancerad styling eller ej inbäddade typsnitt kanske inte återges identiskt i utdata-PDF:en.

- **Lösenordsskyddade dokument**  
  Krypterade eller lösenordsskyddade filer kan inte konverteras utan att skyddet tas bort före inläsning.

- **Externa eller inbäddade bilagor**  
  Länkade filer, externa referenser eller inbäddade objekt i kontorsdokument bevaras inte i konverterad utdata.

## Installation

Ladda ned senaste versionen av pluginet Kontorsdokumentkonverterare och packa upp det i:
`/.roda/config/plugins/`
Starta om ETERNA för att aktivera pluginet.

> **Obs:** Konverteraren kräver externa konverteringsverktyg (t.ex. LibreOffice och `unoconvert`). Se till att alla nödvändiga systemberoenden är installerade och tillgängliga i körningsmiljön.

## Licens

Se [LICENSE.md](LICENSE.md) för mer information.
