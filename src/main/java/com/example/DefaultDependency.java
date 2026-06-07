package com.example;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DefaultDependency implements Dependency {
    @Override
    public String call() {
        throw new UnsupportedOperationException("Replace with @InjectMock in tests");
    }
}
