package com.langai.cloud.model;

import java.time.Instant;
import java.util.UUID;

public class GpuSession {
    private final UUID id;
    private final String requestedBy;
    private final Instant createdAt;
    private final Instant expiresAt;

    public GpuSession(UUID id, String requestedBy, Instant createdAt, Instant expiresAt) {
        this.id = id;
        this.requestedBy = requestedBy;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

    public UUID getId() {
        return id;
    }

    public String getRequestedBy() {
        return requestedBy;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }
}
