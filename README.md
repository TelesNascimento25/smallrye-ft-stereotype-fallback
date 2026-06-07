# smallrye-ft-stereotype-fallback

Minimal repro for a SmallRye FT bug. When fault tolerance annotations come from a
class-level `@Stereotype` and `@Fallback` is on the method, retry doesn't run and
the circuit breaker never opens.

```java
@FtStereotype  // @Retry(maxRetries=2) + @CircuitBreaker + @Timeout + @Bulkhead
@ApplicationScoped
class GuardedService {

    @Fallback(fallbackMethod = "guardedMethodFallback")
    public String guardedMethod() { ... }
}
```

With this setup, `guardedMethod()` is called once regardless of `maxRetries=2`.
The circuit breaker never opens either.

## Run

```bash
mvn test
```

The test fails with `expected: 3 but was: 1`. With `@Retry(maxRetries=2)` I'd expect
the method to be called 3 times before fallback kicks in.

To reproduce on other versions:

```bash
mvn test -Dquarkus.platform.version=3.34.3
mvn test -Dquarkus.platform.version=3.36.1
```

Same failure on all three (Quarkus 3.20.6, 3.34.3, 3.36.1).
