package com.example.phishsim;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ScenarioRepositoryTest {

    private final ScenarioRepository repository = new ScenarioRepository();

    @Test
    void containsTwelveBalancedScenarios() {
        var scenarios = repository.findAll();

        assertThat(scenarios).hasSize(12);
        assertThat(scenarios).filteredOn(Scenario::isPhishing).hasSize(6);
        assertThat(scenarios).filteredOn(scenario -> !scenario.isPhishing()).hasSize(6);
        assertThat(scenarios).extracting(Scenario::difficulty)
                .contains(Difficulty.BEGINNER, Difficulty.INTERMEDIATE, Difficulty.ADVANCED);
    }

    @Test
    void contentIsCompleteAndIdsAreUnique() {
        assertThat(repository.findAll()).extracting(Scenario::id).doesNotHaveDuplicates();
        assertThat(repository.findAll()).allSatisfy(scenario -> {
            assertThat(scenario.title()).isNotBlank();
            assertThat(scenario.fromDisplay()).isNotBlank();
            assertThat(scenario.fromEmail()).isNotBlank();
            assertThat(scenario.subject()).isNotBlank();
            assertThat(scenario.body()).isNotBlank();
            assertThat(scenario.indicators()).hasSizeGreaterThanOrEqualTo(3);
            assertThat(scenario.safeAction()).isNotBlank();
            assertThat(scenario.difficulty()).isNotNull();
        });
    }

    @Test
    void phishingLinksOnlyUseReservedDomains() {
        assertThat(repository.findAll())
                .filteredOn(Scenario::isPhishing)
                .allSatisfy(scenario -> assertThat(scenario.body()).contains(".invalid"));
    }

    @Test
    void senderDomainsAreReservedForTraining() {
        assertThat(repository.findAll()).allSatisfy(scenario ->
                assertThat(scenario.fromEmail()).matches(".+@.+\\.(example|invalid)"));
    }
}
