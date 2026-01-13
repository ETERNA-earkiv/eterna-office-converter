# Kontorsdokumentkonverterare – Användarhandbok

## Översikt

Jobbet för konvertering av kontorsdokument är utformat för att stödja digitala bevarandearbetsflöden i ETERNA-systemet. Verktyget konverterar olika kontorsformat till bevarandevänliga format för att säkerställa långsiktig tillgänglighet och arkivstabilitet för kontorsdokument.

## Hur man använder jobbet för konvertering av kontorsdokument

1. **Starta konvertering**: Du kan starta en konverteringsprocess genom att välja en intellektuell entitet, en representation eller en enskild fil via katalogsidan eller söksidan och starta ett nytt bevarandejobb.
2. **Välj utdataformat**: Välj önskat utdataformat bland tillgängliga alternativ (PDF, PDF/A-1A, PDF/A-1B, PDF/A-2B, PDF/A-3B, ODT, OTT, DOC, DOCX, RTF, TXT, HTML, XLS, XLSX, CSV, ODS, ODP, PPT, PPTX, SVG).
3. **Konfigurera konvertering**: Ange om du vill skapa en ny representation eller en spridningskopia.
4. **Kör konvertering**: Kör verktyget för att starta konverteringsprocessen.

### Stödda indataformat
Verktyget stöder konvertering från ett brett utbud av kontorsdokumentformat, inklusive:
- 📄 **Textdokument**:
    - Vanliga format: DOC, DOCX, ODT, RTF, TXT
    - Äldre format: DOC (Word 97–2003), SXW
    - Märkspråksformat: HTML, XHTML
    - Övrigt: XML-baserade textdokument
- 📊 **Kalkylblad**:
    - Vanliga format: XLS, XLSX, ODS, CSV
    - Äldre format: XLS (Excel 97–2003), SXC
    - Övrigt: TSV, DIF
- 📽 **Presentationer**:
    - Vanliga format: PPT, PPTX, ODP
    - Äldre format: PPT (PowerPoint 97–2003), SXI
- 🧮 **Ritningar och grafik**:
    - LibreOffice Draw-format: ODG
    - Vektorgrafik: SVG, SVGZ
    - Andra stödda importer: WMF, EMF
- 🗂 **Mallar**:
    - Textmallar: DOT, DOTX, OTT
    - Kalkylblads­mallar: XLT, XLTX, OTS
    - Presentationsmallar: POT, POTX, OTP
- 🧾 **Övrigt**:
    - OpenDocument-format: ODT, ODS, ODP, ODG
    - StarOffice äldre format: SXW, SXC, SXI, SXD

### Utdataformat
Du kan konvertera kontorsdokument till följande format för bevarande och åtkomst:

- **PDF**: Format med fast layout som lämpar sig för långsiktigt bevarande och distribution; bevarar typsnitt, layout och inbäddat innehåll
- **PDF/A**: Arkivkompatibelt PDF-format utformat för långsiktigt bevarande; säkerställer självförsörjande dokument med inbäddade typsnitt och metadata
- **ODT**: OpenDocument Text-format för redigerbara textdokument med stöd för öppna standarder
- **ODS**: OpenDocument Spreadsheet-format för strukturerade och redigerbara tabeller
- **ODP**: OpenDocument Presentation-format för redigerbara presentationer
- **DOCX**: Microsoft Word Open XML-format för kompatibilitet med moderna ordbehandlare
- **XLSX**: Microsoft Excel Open XML-format för kalkylblads­interoperabilitet
- **PPTX**: Microsoft PowerPoint Open XML-format för delning av presentationer
- **HTML**: Webbvänligt format lämpligt för publicering online
- **TXT**: Oformaterat textformat för enkel innehållsextraktion

### Konverteringsprocess
1. **Formatidentifiering**: Verktyget identifierar automatiskt indataformatet för kontorsdokument med hjälp av LibreOffices importfilter.
2. **Dokumentnormalisering**: Dokument normaliseras för målformatet, vilket säkerställer att layout, typsnitt, kodning, sidstorlek och inbäddade objekt hanteras korrekt.
3. **Innehållsvalidering**: Filer utvärderas för att säkerställa strukturell integritet och minimala förluster av innehåll eller formatering under konvertering.
4. **Utförande av konvertering**: Dokument konverteras med LibreOffice i headless-läge och utnyttjar dess inbyggda dokumentfilter för tillförlitlig och standardkompatibel utdata.
5. **Skapande av representation**: Under konverteringen placeras de nya filerna i samma intellektuella entitet som originalen, i en representation med vald representationstyp och status: `Preservation`. Om en sådan representation inte redan finns skapas en ny.

## Kända begränsningar

### Anmärkningar om filhantering
- Filer som redan är i målformatet konverteras inte.
- Om flera filer har samma namn kan endast en bevaras på grund av systembegränsningar.

### Konverteringens kvalitet och noggrannhet
- Dokumentlayout, sidindelning och stil bevaras så noggrant som möjligt under konverteringen.
- Typsnitt bäddas in i utdataformat där detta stöds (t.ex. PDF/PDF-A).
- Inbäddade bilder och objekt bevaras med sin ursprungliga upplösning när det är möjligt.
- Konverteringar baseras på LibreOffices inbyggda import- och exportfilter för standardkompatibla resultat.
- **Interaktiva eller exekverbara PDF-funktioner (inklusive JavaScript, XFA-formulär och inbäddade medier) bevaras inte utan normaliseras till statiskt innehåll under konverteringen för att uppfylla krav på långsiktigt bevarande.**

## Bästa praxis

1. **Välj lämpliga format**:  
   Använd PDF/A för långsiktigt bevarande, PDF för åtkomst och distribution samt ODF-format (ODT, ODS, ODP) när framtida redigering krävs.
2. **Bevara original**:  
   Behåll alltid originaldokumenten tillsammans med de konverterade versionerna för autenticitet och framtida ombearbetning.
3. **Använd konsekvent namngivning**:  
   Säkerställ att utdatafiler följer en konsekvent och unik namngivningskonvention för att undvika överskrivningar och bibehålla tydliga relationer mellan original och derivat.
4. **Testa konverteringar**:  
   Utför testkonverteringar på representativa exempel, särskilt för komplexa dokument med makron, inbäddade objekt eller anpassade typsnitt.
5. **Granska resultat**:  
   Verifiera layout, sidindelning, typsnitt, tabeller och inbäddat innehåll efter konvertering för att säkerställa att utdata uppfyller krav på bevarande och åtkomst.
6. **Var medveten om begränsningar**:  
   Vissa funktioner (t.ex. makron, formulärkontroller eller proprietära element) kan inte bevaras fullt ut vid konvertering; dokumentera kända begränsningar i bevarandemetadata.

## Felsökning
### Vanliga problem

- **Konverteringen misslyckas**
    - **Orsak**: Indataformatet stöds inte av LibreOffice, filen är skadad eller nödvändiga typsnitt/resurser saknas
    - **Lösning**: Kontrollera att filformatet stöds, verifiera filens integritet och säkerställ att LibreOffice är korrekt installerat och tillgängligt i headless-läge

- **Filer konverteras inte**
    - **Orsak**: Filen är redan i målformatet eller konverteringen hoppas över av ramverket
    - **Lösning**: Bekräfta om konvertering är nödvändig och granska plugin-loggarna för att verifiera att filen avsiktligt hoppades över

- **Oväntade resultat**
    - **Layoutförändringar**: Mindre skillnader i sidindelning, mellanrum eller justering kan uppstå på grund av skillnader i renderingsmotorer
    - **Typsnittsersättning**: Saknade typsnitt ersätts med tillgängliga systemtypsnitt, vilket kan påverka utseendet
    - **Inbäddade objekt**: Vissa inbäddade element (t.ex. OLE-objekt eller makron) kan inte bevaras fullt ut
    - **Kalkylbladsformler**: Formler bevaras, men beräknade värden kan skilja sig om beroende data eller språkinställningar varierar

- **Prestandaproblem**
    - **Stora eller komplexa dokument**: Filer med många sidor, bilder eller inbäddade objekt kräver mer bearbetningstid och minne
    - **Fördröjningar vid batchbearbetning**: Konvertering av stora samlingar kan ta längre tid; överväg att bearbeta i mindre batcher eller tilldela ytterligare systemresurser

### Ytterligare anmärkningar

- **Val av format**: Välj utdataformat baserat på avsedd användning – PDF/A för långsiktigt bevarande, PDF för åtkomst och delning samt ODF-format (ODT, ODS, ODP) för framtida redigering eller återanvändning.
- **Icke stödda format**: Vissa proprietära, krypterade eller mycket specialiserade dokumentformat stöds inte av LibreOffice och kan därför inte konverteras; sådana filer rapporteras som icke stödda av konverteringsramverket.

För teknisk support eller frågor om specifika format, kontakta din systemadministratör eller skapa ett ärende i vårt GitHub-repository.
