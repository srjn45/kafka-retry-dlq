package code.srjn.retry;

public class RetryConfig {

    private int attempts;
    private long backoff;
    private int backoffMultiplier;

    public int getAttempts() {
        return attempts;
    }

    public long getBackoff() {
        return backoff;
    }

    public int getBackoffMultiplier() {
        return backoffMultiplier;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private static final int DEFAULT_ATTEMPTS = 3;
        private static final long DEFAULT_BACKOFF = 0;
        private static final int DEFAULT_BACKOFF_MULTIPLIER = 1;

        RetryConfig config;

        public Builder() {
            config = new RetryConfig();
            config.attempts = DEFAULT_ATTEMPTS;
            config.backoff = DEFAULT_BACKOFF;
            config.backoffMultiplier = DEFAULT_BACKOFF_MULTIPLIER;
        }

        public Builder attempts(int attempts) {
            config.attempts = attempts;
            return this;
        }

        public Builder backoff(long backoff) {
            config.backoff = backoff;
            return this;
        }

        public Builder backoffMultiplier(int backoffMultiplier) {
            config.backoffMultiplier = backoffMultiplier;
            return this;
        }

        public RetryConfig build() {
            return config;
        }
    }
}
