Fault Tolerance Demonstration - generated 2025-10-10T23:09:16.315844Z

Endpoints to test (after running the service hosting the controller on port 8080):

GET /fault/retry       -> demonstrates Retry (retrying transient failures)
GET /fault/cb          -> demonstrates Circuit Breaker + fallback
GET /fault/bulk        -> demonstrates Bulkhead isolation (threadpool)
GET /fault/rate        -> demonstrates Rate Limiter (limits calls per second)
GET /fault/time        -> demonstrates TimeLimiter (async timeout)
GET /fault/cache/{id} -> demonstrates Cacheable with possible fallback to cached value
GET /fault/combined    -> demonstrates Combined patterns (CB + Retry + Bulkhead)

Notes:
- Tune parameters in FaultToleranceConfig.java (max attempts, timeouts, sizes) to visualize different behaviors.
- Check service logs to see Resilience4j events (circuit open/close, retries).
- If your module poms don't include resilience4j annotations dependency, add the following to the module's pom.xml:
  - io.github.resilience4j:resilience4j-spring-boot2, resilience4j-ratelimiter, resilience4j-timelimiter, resilience4j-bulkhead, resilience4j-circuitbreaker, resilience4j-retry
