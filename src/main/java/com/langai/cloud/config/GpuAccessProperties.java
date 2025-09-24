package com.langai.cloud.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "gpu")
public class GpuAccessProperties {

    /**
     * Maximum number of concurrent GPU sessions allowed.
     */
    private int maxConcurrentSessions = 1;

    /**
     * Default session duration in minutes when a client does not request a specific duration.
     */
    private int defaultSessionMinutes = 30;

    public int getMaxConcurrentSessions() {
        return maxConcurrentSessions;
    }

    public void setMaxConcurrentSessions(int maxConcurrentSessions) {
        this.maxConcurrentSessions = maxConcurrentSessions;
    }

    public int getDefaultSessionMinutes() {
        return defaultSessionMinutes;
    }

    public void setDefaultSessionMinutes(int defaultSessionMinutes) {
        this.defaultSessionMinutes = defaultSessionMinutes;
    }
}
