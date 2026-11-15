package com.example.phishsim;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@Controller public class SimulatorController {
    private final ScenarioRepository repo; private final TrainingProgressCookie progressCookie;
    public SimulatorController(ScenarioRepository repo, TrainingProgressCookie progressCookie){this.repo=repo;this.progressCookie=progressCookie;}
    @GetMapping("/") public String home(Model m,HttpServletRequest q){m.addAttribute("scenarios",repo.findAll());m.addAttribute("state",state(q));m.addAttribute("totalScenarios",repo.findAll().size());return"index";}
    @GetMapping("/scenario/{id}") public String scenario(@PathVariable int id,Model m,HttpServletRequest q){m.addAttribute("scenario",find(id));m.addAttribute("state",state(q));m.addAttribute("totalScenarios",repo.findAll().size());return"scenario";}
    @PostMapping("/scenario/{id}/answer") public String answer(@PathVariable int id,@RequestParam AnswerChoice choice,HttpServletRequest q,HttpServletResponse r){Scenario s=find(id);TrainingState state=state(q);state.recordAnswer(id,(choice==AnswerChoice.PHISHING)==s.isPhishing());progressCookie.save(q,r,state);return"redirect:/scenario/"+id+"?result=1";}
    @GetMapping("/scenario/{id}/next") public String next(@PathVariable int id){return repo.findAll().stream().dropWhile(x->x.id()!=id).skip(1).findFirst().map(x->"redirect:/scenario/"+x.id()).orElse("redirect:/score");}
    @GetMapping("/review") public String review(HttpServletRequest q){return missedAfter(state(q),0).map(scenario->"redirect:/scenario/"+scenario.id()+"?review=1").orElse("redirect:/score");}
    @GetMapping("/review/next/{id}") public String nextReview(@PathVariable int id,HttpServletRequest q){return missedAfter(state(q),id).map(scenario->"redirect:/scenario/"+scenario.id()+"?review=1").orElse("redirect:/score");}
    @PostMapping("/reset") public String reset(HttpServletRequest q,HttpServletResponse r){state(q).reset();progressCookie.clear(q,r);return"redirect:/";}
    @GetMapping("/score") public String score(Model m,HttpServletRequest q){TrainingState state=state(q);m.addAttribute("state",state);m.addAttribute("report",TrainingReport.from(state,repo.findAll()));m.addAttribute("totalScenarios",repo.findAll().size());return"score";}
    private Scenario find(int id){return repo.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));}
    private java.util.Optional<Scenario> missedAfter(TrainingState state,int id){return repo.findAll().stream().filter(scenario->scenario.id()>id).filter(scenario->Boolean.FALSE.equals(state.getAnsweredCorrectly().get(scenario.id()))).findFirst();}
    private TrainingState state(HttpServletRequest q){HttpSession h=q.getSession();if(h.getAttribute("state")instanceof TrainingState s)return s;TrainingState state=progressCookie.load(q,scenarioIds());h.setAttribute("state",state);return state;}
    private Set<Integer> scenarioIds(){return repo.findAll().stream().map(Scenario::id).collect(java.util.stream.Collectors.toSet());}
}
