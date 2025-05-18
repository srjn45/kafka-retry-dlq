# kafka-retry-dlq ![Build Status](https://github.com/srjn45/kafka-retry-dlq/actions/workflows/ci.yml/badge.svg)

java library that provide retry &amp; dlq mechanism for any java func

## Usage

### Synchronous Retry

this can be used to retry any function in a synchronous code flow

- retry on failure for configured number of attempts
- also support linear & exponential backoff
- can config when to retry & when to skip
- throws exception once all the attempts are exhausted

```
RetrySync<String> retrySync = new RetrySync<>(retryConfig);

var result = retrySync.execute(() -> funcWithReturnValue(args));

retrySync.execute(() -> {funcThatReturnsNothing(args); return null;});

T funcWithReturnValue(...) {}

void funcThatReturnsNothing(...) {}
```

### RetryConfig

#### attempts

- **Description:** number of attempts before throwing error (including the original call)
- **Default:** 1 (executes once without any retry)

#### backoff

- **Description:** delay (in milliseconds) between subsequent retry calls
- **Default:** 0 (instant retry)

#### backoffMultiplier

- **Description:** exponential backoff
- **Default:** 1 (linear backoff)

#### retryableException

- **Description:** exceptions that are retry should be attempted
- **Default:** empty (retry on all exceptions)

#### skippableException

- **Description:** exceptions on which retry should **NOT** be attempted
- **Default:** empty (skip nothing)