package code.srjn.retry;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

class RetrySyncTest {

  private final AtomicInteger counter = new AtomicInteger();

  @Test
  void execute_shouldRetryFuncOnFailure() {
    Instant startTime = Instant.now();
    counter.set(3);
    RetrySync<String> retrySync = new RetrySync<>(RetryConfig.builder().build());
    String actual = retrySync.execute(() -> hello("srjn"));
    assertThat(actual).isEqualTo("hello srjn");
    Instant endTime = Instant.now();

    long execTime = endTime.toEpochMilli() - startTime.toEpochMilli();
    assertThat(execTime).isLessThan(1000);
  }

  @Test
  void execute_shouldThrowExceptionsWhenRetryExhausts() {
    Instant startTime = Instant.now();
    counter.set(3);
    RetrySync<String> retrySync = new RetrySync<>(RetryConfig.builder().attempts(2).build());
    assertThrows(RuntimeException.class, () -> retrySync.execute(() -> hello("srjn")));
    Instant endTime = Instant.now();

    long execTime = endTime.toEpochMilli() - startTime.toEpochMilli();
    assertThat(execTime).isLessThan(1000);
  }

  @Test
  void execute_shouldRetryWithLinearBackoff() {
    Instant startTime = Instant.now();
    counter.set(3);
    RetrySync<String> retrySync = new RetrySync<>(RetryConfig.builder().backoff(1000).build());
    String actual = retrySync.execute(() -> hello("srjn"));
    assertThat(actual).isEqualTo("hello srjn");
    Instant endTime = Instant.now();

    long execTime = endTime.toEpochMilli() - startTime.toEpochMilli();
    assertThat(execTime).isGreaterThanOrEqualTo(2000);
  }

  @Test
  void execute_shouldRetryWithExponentialBackoff() {
    Instant startTime = Instant.now();
    counter.set(4);
    RetrySync<String> retrySync =
        new RetrySync<>(
            RetryConfig.builder().attempts(4).backoff(1000).backoffMultiplier(2).build());
    String actual = retrySync.execute(() -> hello("srjn"));
    assertThat(actual).isEqualTo("hello srjn");
    Instant endTime = Instant.now();

    long execTime = endTime.toEpochMilli() - startTime.toEpochMilli();
    assertThat(execTime).isGreaterThanOrEqualTo(7000);
  }

  private String hello(String name) {
    if (counter.decrementAndGet() > 0) {
      throw new RuntimeException("something went wrong");
    }
    return String.format("hello %s", name);
  }
}
