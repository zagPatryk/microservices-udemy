package com.microservices.currencyexchangeservice;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class CircuitBreakerController {

    private Logger logger = LoggerFactory.getLogger(CircuitBreakerController.class);

    // in case of exception 3 (def) times request will be retried
    @Retry(
    // it is possible to create retry configuration w app.p for sample-api
            name = "sample-api",
    // fallbackMethod in case of failure use method
            fallbackMethod = "hardcodedResponse")
    // @Retry(name = "default")
    // max of simultaneously calls
    @Bulkhead(name = "default")
    @GetMapping("/sample-api")
    public String sampleApi() {
        // this will fail
        ResponseEntity<String> forEntity = new RestTemplate().getForEntity("http://localhost:8080/dummy", String.class);

        logger.info("Sample API call received");

        return forEntity.getBody();
    }

    // method calling will be skipped if service return exception before because e.g. service is down
    // fallback will be returned
    // it is possible to configure how often service will be checked if it is working correctly
    @CircuitBreaker(name = "sample-api", fallbackMethod = "hardcodedResponse")
    // number of calls will be limited in specific time
    @RateLimiter(name = "default")
    @GetMapping("/a-sample-api")
    public String anotherSampleApi() {
        // this will fail
        ResponseEntity<String> forEntity = new RestTemplate().getForEntity("http://localhost:8080/dummy", String.class);

        logger.info("Sample API call received");

        return forEntity.getBody();
    }

    // possible multiple methods for different type of exceptions
    public String hardcodedResponse(Exception ex) {
        return "fallback " + ex.getMessage();
    }
}
