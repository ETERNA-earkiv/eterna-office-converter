# Licensing Information

This project uses multiple licenses to cover different components. Please read this file carefully to understand which license applies to which part of the project.

## Base Project License

The core code of this project (excluding dependencies) is licensed under the GNU Lesser General Public License (LGPL) v3.
Copyright (C) 2024
White Red Consulting AB / ETERNA Contributors

For the full text of this license, see below.

## ETERNA Office Documents Converter Plugin
<details open> 
<summary>ETERNA Office Documents Converter Plugin - LGPLv3 license
</summary>

### GNU LESSER GENERAL PUBLIC LICENSE
### Version 3, 29 June 2007


Copyright (C) 2007 Free Software Foundation, Inc. http://fsf.org/

Everyone is permitted to copy and distribute verbatim copies
of this license document, but changing it is not allowed.

This version of the GNU Lesser General Public License incorporates
the terms and conditions of version 3 of the GNU General Public License,
supplemented by the additional permissions listed below.

(→ Insert full LGPLv3 text here, identical to Image Converter plugin)

</details>
Dependencies
Document Conversion Dependencies
<details> 
<summary>UnoServer / UnoConvert - Apache License 2.0</summary>

### Apache License 2.0

UnoServer and UnoConvert provide the underlying document conversion capabilities used by this plugin, via headless LibreOffice.

License:
https://www.apache.org/licenses/LICENSE-2.0

Permissions include:

* Use, modify, distribute

* Private or commercial use

* Patent protection

Obligations:

Preserve copyright notices

Include the Apache 2.0 license

</details>
<details> <summary>LibreOffice UNO Runtime - Mozilla Public License 2.0</summary>

### Mozilla Public License 2.0 (MPL 2.0)

LibreOffice and its associated UNO runtime used by UnoServer are licensed under MPL 2.0.

License:
https://www.mozilla.org/MPL/2.0/

Highlights:

* Modifications to MPL-covered files must remain under MPL

* Can be combined with other licenses (weak copyleft)

</details>
<details> <summary>Python runtime dependencies used by UnoServer - MIT License</summary>

### MIT License

UnoServer uses several Python libraries that are MIT-licensed.

Permissions:

* Use, copy, modify, merge, publish, distribute

Conditions:

* Include copyright

* Include license text

</details>
Summary Table
Component	License Type	Notes
Office Documents Converter plugin (Java)	LGPLv3	Applies only to plugin source code
UnoServer / UnoConvert	Apache 2.0	External component, not redistributed
LibreOffice UNO runtime	MPL 2.0	LibreOffice license
Python dependencies of UnoServer	MIT	External libraries
Additional Notes

This plugin only communicates with the UnoServer container; it does not distribute LibreOffice, Python, or UnoServer binaries.

Redistribution of LibreOffice components must comply with the Mozilla Public License 2.0.

The plugin generates derivative output files (PDF, ODT, etc.) via external conversion tools and does not modify or link to those tools internally.

### Full LGPLv3 License Text

(Insert same LGPLv3 license text from your Image Converter plugin to maintain consistency.)