package com.example.phishsim;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.mock.web.MockHttpSession;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@WebMvcTest(SimulatorController.class) @Import({ScenarioRepository.class,TrainingProgressCookie.class,SecurityHeadersFilter.class,WebErrorHandler.class})
class SimulatorControllerTest {
 @Autowired MockMvc mvc;
 @Test void pagesLoad() throws Exception{mvc.perform(get("/")).andExpect(status().isOk()).andExpect(view().name("index"));mvc.perform(get("/scenario/1")).andExpect(status().isOk());mvc.perform(get("/score")).andExpect(status().isOk()).andExpect(model().attributeExists("report"));}
 @Test void answersValidate() throws Exception{mvc.perform(post("/scenario/1/answer").param("choice","PHISHING")).andExpect(redirectedUrl("/scenario/1?result=1")).andExpect(header().string("Set-Cookie",org.hamcrest.Matchers.containsString("phishsense-progress=")));mvc.perform(post("/scenario/1/answer")).andExpect(status().isBadRequest());mvc.perform(post("/scenario/1/answer").param("choice","MAYBE")).andExpect(status().isBadRequest());}
 @Test void missingIs404() throws Exception{mvc.perform(get("/scenario/999")).andExpect(status().isNotFound());}
 @Test void routesRedirect() throws Exception{mvc.perform(post("/reset")).andExpect(redirectedUrl("/"));mvc.perform(get("/scenario/1/next")).andExpect(redirectedUrl("/scenario/2"));mvc.perform(get("/scenario/22/next")).andExpect(redirectedUrl("/score"));}
 @Test void reviewRoutesOnlyToMissedScenarios() throws Exception{var answer=mvc.perform(post("/scenario/1/answer").param("choice","LEGIT")).andReturn();var session=(MockHttpSession)answer.getRequest().getSession(false);mvc.perform(get("/review").session(session)).andExpect(redirectedUrl("/scenario/1?review=1"));mvc.perform(get("/review/next/1").session(session)).andExpect(redirectedUrl("/score"));}
 @Test void headersAreDefensive() throws Exception{mvc.perform(get("/")).andExpect(header().string("X-Content-Type-Options","nosniff")).andExpect(header().string("Referrer-Policy","no-referrer")).andExpect(header().exists("Content-Security-Policy"));}
}
