package com.example.phishsim;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
class TrainingStateTest {
 @Test void revisesWithoutInflatingCompletion(){var s=new TrainingState();s.recordAnswer(1,true);s.recordAnswer(1,false);assertThat(s.getTotalAnswered()).isEqualTo(1);assertThat(s.getCorrect()).isZero();}
 @Test void calculatesAndResets(){var s=new TrainingState();s.recordAnswer(1,true);s.recordAnswer(2,false);assertThat(s.getAccuracyPercentage()).isEqualTo(50);s.reset();assertThat(s.getAnsweredCorrectly()).isEmpty();assertThat(s.getTotalAnswered()).isZero();}
}
