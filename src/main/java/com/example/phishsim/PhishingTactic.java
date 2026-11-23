package com.example.phishsim;

/** Common tactics learners can practice recognizing across phishing scenarios. */
public enum PhishingTactic {
    DOMAIN_DECEPTION("Domain deception"),
    URGENCY_AND_PRESSURE("Urgency and pressure"),
    CREDENTIAL_HARVESTING("Credential harvesting"),
    FINANCIAL_FRAUD("Financial fraud"),
    IMPERSONATION("Impersonation");

    private final String label;

    PhishingTactic(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}
