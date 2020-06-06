package com.alvcohen.travelhelper.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private final Auth auth = new Auth();

    public static class Auth {
        private String tokenSecret;
        private long tokenExpirationMs;

        public String getTokenSecret() {
            return tokenSecret;
        }

        public void setTokenSecret(String tokenSecret) {
            this.tokenSecret = tokenSecret;
        }

        public long getTokenExpirationMs() {
            return tokenExpirationMs;
        }

        public void setTokenExpirationMs(long tokenExpirationMs) {
            this.tokenExpirationMs = tokenExpirationMs;
        }
    }

    public Auth getAuth() {
        return auth;
    }
}
