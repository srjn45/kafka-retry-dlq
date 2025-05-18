# kafka-retry-dlq ![Build Status](https://github.com/srjn45/kafka-retry-dlq/actions/workflows/ci.yml/badge.svg)

java library that provide retry &amp; dlq mechanism for any java func

## Usage

### Synchronous Retry

this can be used to retry any function in a synchronous code flow

- retry on failure for configured number of attempts
- also support linear & exponential backoff
- throws exception once all the attempts are exhausted

```java

RetrySync<String> retrySync = new RetrySync<>(RetryConfig.builder().attempts(4).build());

retrySync.

execute(() ->

yourFunc(args));

```