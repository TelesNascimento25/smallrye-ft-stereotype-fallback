package com.example;

import io.quarkus.test.Mock;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

@Mock
@ApplicationScoped
public class MockDependencyBean implements Dependency {

    private final AtomicReference<Supplier<String>> behavior =
        new AtomicReference<>(() -> "not-configured");

    public void setBehavior(Supplier<String> supplier) {
        behavior.set(supplier);
    }

    public void reset() {
        behavior.set(() -> "not-configured");
    }

    @Override
    public String call() {
        return behavior.get().get();
    }
}
