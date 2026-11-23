package com.example.phishsim;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TrainingReportTest {

    private final ScenarioRepository repository = new ScenarioRepository();

    @Test
    void summarizesDifficultyAndMissedPhishingMessages() {
        var state = new TrainingState();
        state.recordAnswer(1, false);
        state.recordAnswer(2, true);
        state.recordAnswer(4, false);

        var report = TrainingReport.from(state, repository.findAll());

        assertThat(report.missedPhishing()).isEqualTo(1);
        assertThat(report.missedLegitimate()).isEqualTo(1);
        assertThat(report.phishingSignalsToReview()).contains("Lookalike domain (micros0ft with a zero)");
        assertThat(report.difficultyBreakdown())
                .extracting(TrainingReport.DifficultyPerformance::label,
                        TrainingReport.DifficultyPerformance::answered,
                        TrainingReport.DifficultyPerformance::correct)
                .containsExactly(
                        org.assertj.core.groups.Tuple.tuple("Beginner", 1, 0),
                        org.assertj.core.groups.Tuple.tuple("Intermediate", 1, 1),
                        org.assertj.core.groups.Tuple.tuple("Advanced", 1, 0));
        assertThat(report.tacticBreakdown())
                .extracting(TrainingReport.TacticPerformance::label,
                        TrainingReport.TacticPerformance::answered,
                        TrainingReport.TacticPerformance::correct)
                .contains(
                        org.assertj.core.groups.Tuple.tuple("Domain deception", 1, 0),
                        org.assertj.core.groups.Tuple.tuple("Credential harvesting", 1, 0));
    }

    @Test
    void givesFirstTimeLearnersAUsefulStartingPrompt() {
        var report = TrainingReport.from(new TrainingState(), repository.findAll());

        assertThat(report.focusMessage()).contains("Complete a scenario");
        assertThat(report.phishingSignalsToReview()).isEmpty();
        assertThat(report.difficultyBreakdown())
                .allSatisfy(difficulty -> assertThat(difficulty.accuracyPercentage()).isZero());
        assertThat(report.tacticBreakdown())
                .allSatisfy(tactic -> assertThat(tactic.accuracyPercentage()).isZero());
    }

    @Test
    void aggregatesRecognitionAcrossScenariosThatShareATactic() {
        var state = new TrainingState();
        state.recordAnswer(1, false);
        state.recordAnswer(3, true);

        var report = TrainingReport.from(state, repository.findAll());

        assertThat(report.tacticBreakdown())
                .filteredOn(tactic -> tactic.label().equals("Domain deception"))
                .singleElement()
                .satisfies(tactic -> {
                    assertThat(tactic.answered()).isEqualTo(2);
                    assertThat(tactic.correct()).isEqualTo(1);
                    assertThat(tactic.accuracyPercentage()).isEqualTo(50);
                });
    }
}
