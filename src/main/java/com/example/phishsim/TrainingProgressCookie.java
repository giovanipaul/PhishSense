package com.example.phishsim;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Arrays;
import java.util.Set;

/** Stores only local training outcomes in a first-party browser cookie. */
@Component
public class TrainingProgressCookie {
    static final String NAME = "phishsense-progress";
    private static final Duration MAX_AGE = Duration.ofDays(30);

    public TrainingState load(HttpServletRequest request, Set<Integer> scenarioIds) {
        TrainingState state = new TrainingState();
        if (request.getCookies() == null) {
            return state;
        }
        Arrays.stream(request.getCookies()).filter(cookie -> NAME.equals(cookie.getName())).findFirst()
                .ifPresent(cookie -> restore(cookie.getValue(), scenarioIds, state));
        return state;
    }

    public void save(HttpServletRequest request, HttpServletResponse response, TrainingState state) {
        String value = state.getAnsweredCorrectly().entrySet().stream()
                .sorted(java.util.Map.Entry.comparingByKey())
                .map(entry -> entry.getKey() + "." + (entry.getValue() ? "1" : "0"))
                .reduce("v1", (progress, answer) -> progress + "-" + answer);
        write(request, response, value, MAX_AGE);
    }

    public void clear(HttpServletRequest request, HttpServletResponse response) { write(request, response, "", Duration.ZERO); }

    private void restore(String value, Set<Integer> scenarioIds, TrainingState state) {
        if (value == null || !value.startsWith("v1")) return;
        Arrays.stream(value.substring(2).split("-")).filter(answer -> answer.matches("\\d+\\.[01]"))
                .map(answer -> answer.split("\\.")).filter(parts -> scenarioIds.contains(Integer.parseInt(parts[0])))
                .forEach(parts -> state.recordAnswer(Integer.parseInt(parts[0]), "1".equals(parts[1])));
    }

    private void write(HttpServletRequest request, HttpServletResponse response, String value, Duration maxAge) {
        ResponseCookie cookie = ResponseCookie.from(NAME, value).httpOnly(true).sameSite("Lax").path("/")
                .secure(request.isSecure()).maxAge(maxAge).build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
