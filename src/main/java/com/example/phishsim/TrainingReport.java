package com.example.phishsim;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * A session-only summary of a learner's answers. No report data is stored outside
 * the current HTTP session.
 */
public record TrainingReport(
        List<DifficultyPerformance> difficultyBreakdown,
        int missedPhishing,
        int missedLegitimate,
        String focusMessage
) {

    public static TrainingReport from(TrainingState state, List<Scenario> scenarios) {
        Map<Integer, Boolean> answers = state.getAnsweredCorrectly();
        List<DifficultyPerformance> breakdown = Arrays.stream(Difficulty.values())
                .map(difficulty -> performanceFor(difficulty, scenarios, answers))
                .toList();

        int missedPhishing = (int) scenarios.stream()
                .filter(Scenario::isPhishing)
                .filter(scenario -> Boolean.FALSE.equals(answers.get(scenario.id())))
                .count();
        int missedLegitimate = (int) scenarios.stream()
                .filter(scenario -> !scenario.isPhishing())
                .filter(scenario -> Boolean.FALSE.equals(answers.get(scenario.id())))
                .count();

        return new TrainingReport(breakdown, missedPhishing, missedLegitimate,
                focusMessage(state.getTotalAnswered(), missedPhishing, missedLegitimate));
    }

    private static DifficultyPerformance performanceFor(
            Difficulty difficulty, List<Scenario> scenarios, Map<Integer, Boolean> answers) {
        List<Boolean> results = scenarios.stream()
                .filter(scenario -> scenario.difficulty() == difficulty)
                .map(scenario -> answers.get(scenario.id()))
                .filter(result -> result != null)
                .toList();
        int correct = (int) results.stream().filter(Boolean::booleanValue).count();
        return new DifficultyPerformance(difficulty.label(), results.size(), correct);
    }

    private static String focusMessage(int answered, int missedPhishing, int missedLegitimate) {
        if (answered == 0) {
            return "Complete a scenario to receive focused practice guidance.";
        }
        if (missedPhishing > missedLegitimate) {
            return "Focus on suspicious urgency, lookalike domains, and unexpected requests for money or credentials.";
        }
        if (missedLegitimate > missedPhishing) {
            return "Practice separating a message that is merely unexpected from one that asks you to take an unsafe action.";
        }
        if (missedPhishing > 0) {
            return "Review both message types and verify important requests through an official app or known contact channel.";
        }
        return "Excellent work. Keep verifying important requests through official channels, even when a message looks familiar.";
    }

    public record DifficultyPerformance(String label, int answered, int correct) {
        public int accuracyPercentage() {
            return answered == 0 ? 0 : correct * 100 / answered;
        }
    }
}
