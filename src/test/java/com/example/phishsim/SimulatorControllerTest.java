package com.example.phishsim;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@WebMvcTest(SimulatorController.class) @Import({ScenarioRepository.class,SecurityHeadersFilter.class,WebErrorHandler.class})
class SimulatorControllerTest {
 @Autowired MockMvc mvc;
 @Test void pagesLoad() throws Exception{mvc.perform(get("/")).andExpect(status().isOk()).andExpect(view().name("index"));mvc.perform(get("/scenario/1")).andExpect(status().isOk());mvc.perform(get("/score")).andExpect(status().isOk());}
 @Test void answersValidate() throws Exception{mvc.perform(post("/scenario/1/answer").param("choice","PHISHING")).andExpect(redirectedUrl("/scenario/1?result=1"));mvc.perform(post("/scenario/1/answer")).andExpect(status().isBadRequest());mvc.perform(post("/scenario/1/answer").param("choice","MAYBE")).andExpect(status().isBadRequest());}
 @Test void missingIs404() throws Exception{mvc.perform(get("/scenario/999")).andExpect(status().isNotFound());}
 @Test void routesRedirect() throws Exception{mvc.perform(post("/reset")).andExpect(redirectedUrl("/"));mvc.perform(get("/scenario/1/next")).andExpect(redirectedUrl("/scenario/2"));mvc.perform(get("/scenario/12/next")).andExpect(redirectedUrl("/score"));}
 @Test void headersAreDefensive() throws Exception{mvc.perform(get("/")).andExpect(header().string("X-Content-Type-Options","nosniff")).andExpect(header().string("Referrer-Policy","no-referrer")).andExpect(header().exists("Content-Security-Policy"));}
}
