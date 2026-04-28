# Changelog
## [1.1.0]
### Added
- PDF/A-1a support alongside existing PDF/A-1b
- 5-minute timeout for conversion operations
- Swedish localization: plugin name, description, format profiles, and README
- RA-FS compliance references in PDF/A profile descriptions

### Changed
- Conversion now uses locally installed unoserver instead of uno-container
- Switched to stdin/stdout-based conversion pipeline
- Improved error logging for unoconvert operations

### Fixed
- Pipe deadlock: stderr is now read concurrently during conversion
- Replaced CompletableFuture with Thread to avoid ForkJoinPool blocking
- Thread-safe stderr buffer and correct thread constructor argument order
- Partial output files are deleted on error paths
- Process and threads are cleaned up on all error paths
- stdin IOException is propagated and logged correctly
- stderrReader is interrupted on stdin error path
- stdout streamed directly to file to avoid heap buffering
- Correct `.pdf` extension used for all PDF/A format variants

## [1.0.0]
### First release
- Initial release of the Office Documents Converter plugin
- Converts office documents from one form to the other
- Supports all formats/extensions commonly used with LIBRE office