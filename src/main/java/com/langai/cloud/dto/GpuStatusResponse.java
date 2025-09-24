package com.langai.cloud.dto;

import java.time.Instant;

public record GpuStatusResponse(
        boolean available,
        int activeSessions,
        Instant lastUpdated
) {
}
