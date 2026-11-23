# PhishSense

[![Java 17](https://img.shields.io/badge/Java-17-007396)](https://adoptium.net/)
[![License: MIT](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)
[![Live Demo](https://img.shields.io/badge/Live_Demo-Open_PhishSense-42d3a2)](https://phishsense-7ado.onrender.com)
[![Deploy to Render](https://render.com/images/deploy-to-render-button.svg)](https://render.com/deploy?repo=https://github.com/giovanipaul/PhishSense)

PhishSense is a privacy-friendly phishing awareness simulator. Learners inspect realistic—but fictional—email scenarios, classify each message, and receive immediate evidence-based feedback.

![PhishSense training dashboard](docs/images/phishsense-home.png)

> [!IMPORTANT]
> This project is strictly for defensive education. It does not send or receive email, collect credentials, track users, or connect to scenario domains.

## Features

- Scenarios across multiple difficulty levels
- Evidence-based explanations and safe-action guidance
- Private progress saved for 30 days in an HttpOnly, first-party cookie; no account, database, or analytics
- Focused review path for scenarios a learner previously missed
- Personalized “signals to revisit” guidance for missed phishing messages
- Per-tactic recognition scores for domain deception, urgency, credential theft, financial fraud, and impersonation
- Responsive, keyboard-accessible interface
- Reserved `.invalid` and `.example` domains
- Defensive HTTP headers and privacy-preserving session settings
- Tests, JaCoCo coverage, GitHub Actions CI, and Dependabot

## Run

Requires Java 17 or newer. Maven is not required globally.

```bash
./mvnw spring-boot:run
```

Open [http://localhost:8080](http://localhost:8080). On Windows use `.\mvnw.cmd spring-boot:run`.

## Test and package

```bash
./mvnw verify
java -jar target/phish-awareness-sim-1.0.0-SNAPSHOT.jar
```

Reports are generated under `target/surefire-reports/` and `target/site/jacoco/`.

## Deploy

The repository includes a production-ready multi-stage Docker build and a Render Blueprint. Click **Deploy to Render** above, review the free web service, and approve the Blueprint.

Render uses the platform-provided `PORT`, checks `/` for service health, and deploys updates from `main` only after CI succeeds.

> [!NOTE]
> Render's free web services can spin down while idle, so the first request after a quiet period may take longer.

## Customize scenarios

Edit [`scenarios.json`](src/main/resources/scenarios.json). Use a unique ID, reserved domains, balanced evidence, a safe next action, and at least one tactic for each phishing scenario. The catalog validates that each difficulty has both phishing and legitimate examples, that phishing links use `.invalid`, and that sender domains are reserved. Never add credential collection, active phishing infrastructure, real personal information, or outbound messaging.

## Privacy and limitations

Progress is saved only in a first-party, HttpOnly browser cookie for up to 30 days. There is no database, analytics, authentication, outbound email, or credential field. Resetting progress removes the cookie. This is an awareness exercise—not an email-security scanner—and a single indicator rarely proves intent.

See [CONTRIBUTING.md](CONTRIBUTING.md), [SECURITY.md](SECURITY.md), and the [MIT License](LICENSE).
