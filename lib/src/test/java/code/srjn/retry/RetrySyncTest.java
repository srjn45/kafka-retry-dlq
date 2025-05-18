package code.srjn.retry;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        RetrySync<String> retrySync = new RetrySync<>(RetryConfig.builder().backoff(100).build());
        String actual = retrySync.execute(() -> hello("srjn"));
        assertThat(actual).isEqualTo("hello srjn");
        Instant endTime = Instant.now();

        long execTime = endTime.toEpochMilli() - startTime.toEpochMilli();
        assertThat(execTime).isGreaterThanOrEqualTo(200);
    }

    @Test
    void execute_shouldRetryWithExponentialBackoff() {
        Instant startTime = Instant.now();
        counter.set(4);
        RetrySync<String> retrySync =
                new RetrySync<>(
                        RetryConfig.builder().attempts(4).backoff(100).backoffMultiplier(2).build());
        String actual = retrySync.execute(() -> hello("srjn"));
        assertThat(actual).isEqualTo("hello srjn");
        Instant endTime = Instant.now();

        long execTime = endTime.toEpochMilli() - startTime.toEpochMilli();
        assertThat(execTime).isGreaterThanOrEqualTo(700);
    }

    private String hello(String name) {
        if (counter.decrementAndGet() > 0) {
            throw new RuntimeException("something went wrong");
        }
        return String.format("hello %s", name);
    }
}
