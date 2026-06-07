package com.example;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.faulttolerance.Fallback;

@FtStereotype
@ApplicationScoped
public class GuardedService {

    @Inject
    Dependency dependency;

    @Fallback(fallbackMethod = "guardedMethodFallback")
    public String guardedMethod() {
        return dependency.call();
    }

    String guardedMethodFallback() {
        return "fallback-value";
    }
}
