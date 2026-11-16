# Changelog

## [Unreleased]

### Added

- JaCoCo coverage gate: the build fails below 70% line coverage
- Personalized learner report with difficulty-level performance and focused review guidance
- Private, 30-day browser progress persistence with an HttpOnly first-party cookie
- Focused review flow for missed scenarios
- Runtime catalog validation for safe, balanced difficulty coverage and phishing-signal review guidance
- Responsive PhishSense interface, difficulty levels, safe-action guidance, completion, and accuracy
- Input validation, defensive browser headers, and reserved scenario domains
- Unit/MVC tests, JaCoCo, Maven Wrapper, CI, Dependabot, and community files
- Scenarios loaded from a `scenarios.json` data file, with 10 new scenarios including QR-code phishing, MFA fatigue, and invoice fraud

### Changed

- Upgraded to Spring Boot 4.1.0
