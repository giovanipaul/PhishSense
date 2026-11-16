package com.example.phishsim;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class ScenarioCatalogValidatorTest {
    private final ScenarioRepository repository = new ScenarioRepository();

    @Test void acceptsThePublishedCatalog(){assertThatCode(()->ScenarioCatalogValidator.validate(repository.findAll())).doesNotThrowAnyException();}
    @Test void rejectsACollectionWithoutBothMessageTypesAtEveryDifficulty(){List<Scenario> unbalanced=repository.findAll().stream().filter(scenario->scenario.id()!=4&&scenario.id()!=10&&scenario.id()!=20).toList();assertThatIllegalArgumentException().isThrownBy(()->ScenarioCatalogValidator.validate(unbalanced)).withMessageContaining("Every difficulty needs a legitimate scenario");}
    @Test void rejectsAPhishingScenarioWithoutAnInvalidLink(){Scenario original=repository.findById(1).orElseThrow();Scenario unsafe=new Scenario(original.id(),original.title(),original.fromDisplay(),original.fromEmail(),original.subject(),"Please verify at https://support.example/verify",true,original.indicators(),original.safeAction(),original.difficulty());List<Scenario> invalid=repository.findAll().stream().map(scenario->scenario.id()==1?unsafe:scenario).toList();assertThatIllegalArgumentException().isThrownBy(()->ScenarioCatalogValidator.validate(invalid)).withMessageContaining(".invalid");}
}
