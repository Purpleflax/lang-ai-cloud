package com.langai.cloud.dto;

import java.time.Instant;
import java.util.UUID;

public record GpuSessionResponse(
        UUID sessionId,
        String requestedBy,
        Instant createdAt,
        Instant expiresAt
) {
}
