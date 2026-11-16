package com.example.phishsim;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ScenarioRepositoryTest {

    private final ScenarioRepository repository = new ScenarioRepository();

    @Test
    void containsTwentyTwoBalancedScenarios() {
        var scenarios = repository.findAll();

        assertThat(scenarios).hasSize(22);
        assertThat(scenarios).filteredOn(Scenario::isPhishing).hasSize(11);
        assertThat(scenarios).filteredOn(scenario -> !scenario.isPhishing()).hasSize(11);
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

    @Test
    void catalogLoadsFromDataFile() throws IOException {
        try (InputStream in = ScenarioRepository.class.getResourceAsStream(ScenarioRepository.CATALOG_RESOURCE)) {
            assertThat(in).isNotNull();
            assertThat(ScenarioRepository.parseCatalog(in)).hasSize(22);
        }
    }

    @Test
    void catalogIncludesNonEmailScenarios() {
        assertThat(repository.findById(13)).hasValueSatisfying(scenario -> {
            assertThat(scenario.title()).contains("QR code");
            assertThat(scenario.isPhishing()).isTrue();
        });
        assertThat(repository.findById(14)).hasValueSatisfying(scenario -> {
            assertThat(scenario.title()).contains("approval prompts");
            assertThat(scenario.isPhishing()).isTrue();
        });
        assertThat(repository.findById(15)).hasValueSatisfying(scenario -> {
            assertThat(scenario.title()).contains("invoice");
            assertThat(scenario.isPhishing()).isTrue();
        });
    }

    @Test
    void malformedCatalogIsRejected() {
        assertThatThrownBy(() -> ScenarioRepository.parseCatalog(stream("{not json")))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Could not parse scenario catalog");
    }

    @Test
    void unknownDifficultyIsRejected() {
        String json = """
                [{"id":1,"title":"t","fromDisplay":"f","fromEmail":"a@b.example","subject":"s",
                "body":"b","isPhishing":true,"indicators":["a","b","c"],"safeAction":"s",
                "difficulty":"EXPERT"}]""";
        assertThatThrownBy(() -> ScenarioRepository.parseCatalog(stream(json)))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Could not parse scenario catalog");
    }

    @Test
    void duplicateIdsAreRejected() {
        List<Scenario> scenarios = List.of(
                scenario(1, true, Difficulty.BEGINNER),
                scenario(1, false, Difficulty.BEGINNER));
        assertThatThrownBy(() -> new ScenarioRepository(scenarios))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("unique");
    }

    @Test
    void unbalancedCatalogIsRejected() {
        List<Scenario> scenarios = List.of(
                scenario(1, true, Difficulty.BEGINNER),
                scenario(2, true, Difficulty.INTERMEDIATE),
                scenario(3, true, Difficulty.ADVANCED));
        assertThatThrownBy(() -> new ScenarioRepository(scenarios))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("legitimate");
    }

    private static InputStream stream(String json) {
        return new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
    }

    private static Scenario scenario(int id, boolean phishing, Difficulty difficulty) {
        return new Scenario(id, "title", "from", "sender@sender.example", "subject",
                phishing ? "see https://sender.invalid/info" : "body",
                phishing, List.of("one", "two", "three"), "safe action", difficulty);
    }
}
