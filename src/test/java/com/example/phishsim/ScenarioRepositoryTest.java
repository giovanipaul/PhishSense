package com.example.phishsim;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
class ScenarioRepositoryTest {
 private final ScenarioRepository repo=new ScenarioRepository();
 @Test void contentIsComplete(){assertThat(repo.findAll()).isNotEmpty().extracting(Scenario::id).doesNotHaveDuplicates();assertThat(repo.findAll()).allSatisfy(s->{assertThat(s.indicators()).isNotEmpty();assertThat(s.safeAction()).isNotBlank();assertThat(s.difficulty()).isNotNull();});}
 @Test void phishingLinksAreReserved(){assertThat(repo.findAll()).filteredOn(Scenario::isPhishing).allSatisfy(s->assertThat(s.body()).contains(".invalid"));}
}
