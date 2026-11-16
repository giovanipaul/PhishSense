package com.example.phishsim;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Component
public class ScenarioRepository {

    static final String CATALOG_RESOURCE = "/scenarios.json";

    private final List<Scenario> scenarios;

    public ScenarioRepository() {
        this(loadCatalog());
    }

    ScenarioRepository(List<Scenario> scenarios) {
        ScenarioCatalogValidator.validate(scenarios);
        this.scenarios = List.copyOf(scenarios);
    }

    static List<Scenario> loadCatalog() {
        try (InputStream in = ScenarioRepository.class.getResourceAsStream(CATALOG_RESOURCE)) {
            if (in == null) {
                throw new IllegalStateException("Scenario catalog not found: " + CATALOG_RESOURCE);
            }
            return parseCatalog(in);
        } catch (IOException e) {
            throw new IllegalStateException("Could not read scenario catalog: " + CATALOG_RESOURCE, e);
        }
    }

    static List<Scenario> parseCatalog(InputStream in) {
        try {
            return new ObjectMapper().readValue(in, new TypeReference<>() {});
        } catch (RuntimeException e) {
            throw new IllegalStateException("Could not parse scenario catalog: " + CATALOG_RESOURCE, e);
        }
    }

    public List<Scenario> findAll() {
        return scenarios;
    }

    public Optional<Scenario> findById(int id) {
        return scenarios.stream().filter(scenario -> scenario.id() == id).findFirst();
    }
}
