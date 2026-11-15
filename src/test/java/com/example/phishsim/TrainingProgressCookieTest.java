package com.example.phishsim;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;

class TrainingProgressCookieTest {
    private final TrainingProgressCookie progressCookie = new TrainingProgressCookie();
    @Test void restoresOnlyKnownScenarioAnswers(){var q=new MockHttpServletRequest();q.setCookies(new Cookie(TrainingProgressCookie.NAME,"v1-1.1-2.0-999.1"));TrainingState state=progressCookie.load(q,Set.of(1,2));assertThat(state.getAnsweredCorrectly()).containsExactlyInAnyOrderEntriesOf(java.util.Map.of(1,true,2,false));assertThat(state.getTotalAnswered()).isEqualTo(2);assertThat(state.getCorrect()).isEqualTo(1);}
    @Test void writesAnHttpOnlyCookieAndCanClearIt(){var q=new MockHttpServletRequest();q.setSecure(true);var r=new MockHttpServletResponse();var state=new TrainingState();state.recordAnswer(2,true);progressCookie.save(q,r,state);assertThat(r.getHeader("Set-Cookie")).contains("phishsense-progress=v1-2.1","HttpOnly","SameSite=Lax","Secure","Max-Age=2592000");progressCookie.clear(q,r);assertThat(r.getHeaders("Set-Cookie").get(1)).contains("Max-Age=0");}
}
