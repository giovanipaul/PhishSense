package com.example.phishsim;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/** Prevents unsafe or unbalanced training data from reaching learners. */
final class ScenarioCatalogValidator {
    private static final Pattern RESERVED_SENDER = Pattern.compile(".+@.+\\.(example|invalid)");

    private ScenarioCatalogValidator() { }

    static void validate(List<Scenario> scenarios) {
        require(!scenarios.isEmpty(), "The training catalog cannot be empty.");
        require(scenarios.stream().map(Scenario::id).distinct().count() == scenarios.size(), "Scenario IDs must be unique.");
        Arrays.stream(Difficulty.values()).forEach(difficulty -> {
            List<Scenario> byDifficulty = scenarios.stream().filter(scenario -> scenario.difficulty() == difficulty).toList();
            require(!byDifficulty.isEmpty(), "Every difficulty needs at least one scenario.");
            require(byDifficulty.stream().anyMatch(Scenario::isPhishing),
                    "Every difficulty needs a phishing scenario.");
            require(byDifficulty.stream().anyMatch(scenario -> !scenario.isPhishing()),
                    "Every difficulty needs a legitimate scenario.");
        });
        require(scenarios.stream().filter(Scenario::isPhishing).count() * 2 == scenarios.size(),
                "The training catalog must have an even phishing and legitimate split.");

        scenarios.forEach(ScenarioCatalogValidator::validateScenario);
    }

    private static void validateScenario(Scenario scenario) {
        require(scenario.id() > 0, "Scenario IDs must be positive.");
        require(notBlank(scenario.title()) && notBlank(scenario.fromDisplay()) && notBlank(scenario.fromEmail())
                        && notBlank(scenario.subject()) && notBlank(scenario.body()) && notBlank(scenario.safeAction()),
                "Scenario text fields cannot be blank.");
        require(scenario.indicators() != null && scenario.indicators().size() >= 3
                        && scenario.indicators().stream().allMatch(ScenarioCatalogValidator::notBlank),
                "Every scenario needs at least three evidence indicators.");
        require(scenario.difficulty() != null, "Every scenario needs a difficulty.");
        require(RESERVED_SENDER.matcher(scenario.fromEmail()).matches(),
                "Scenario sender domains must use .example or .invalid.");
        if (scenario.isPhishing()) {
            require(scenario.body().contains(".invalid"), "Phishing scenarios must use .invalid links.");
            require(scenario.tactics() != null && !scenario.tactics().isEmpty(),
                    "Every phishing scenario needs at least one tactic.");
        } else {
            require(scenario.tactics() == null || scenario.tactics().isEmpty(),
                    "Legitimate scenarios cannot have phishing tactics.");
        }
    }

    private static boolean notBlank(String value) { return value != null && !value.isBlank(); }
    private static void require(boolean condition, String message) { if (!condition) throw new IllegalArgumentException(message); }
}
