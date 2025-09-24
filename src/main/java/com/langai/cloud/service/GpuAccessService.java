package com.langai.cloud.service;

import com.langai.cloud.config.GpuAccessProperties;
import com.langai.cloud.dto.GpuSessionRequest;
import com.langai.cloud.dto.GpuStatusResponse;
import com.langai.cloud.model.GpuSession;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GpuAccessService {

    private final GpuAccessProperties properties;
    private final Map<UUID, GpuSession> activeSessions = new ConcurrentHashMap<>();

    public GpuAccessService(GpuAccessProperties properties) {
        this.properties = properties;
    }

    public GpuStatusResponse getStatus() {
        purgeExpiredSessions();
        boolean available = activeSessions.size() < properties.getMaxConcurrentSessions();
        return new GpuStatusResponse(available, activeSessions.size(), Instant.now());
    }

    public Optional<GpuSession> findSession(UUID sessionId) {
        purgeExpiredSessions();
        return Optional.ofNullable(activeSessions.get(sessionId));
    }

    public GpuSession createSession(GpuSessionRequest request) {
        purgeExpiredSessions();
        if (activeSessions.size() >= properties.getMaxConcurrentSessions()) {
            throw new IllegalStateException("GPU is currently at capacity");
        }

        int durationMinutes = request.durationMinutes() > 0
                ? request.durationMinutes()
                : properties.getDefaultSessionMinutes();

        Instant now = Instant.now();
        Instant expiresAt = now.plus(Duration.ofMinutes(durationMinutes));
        GpuSession session = new GpuSession(UUID.randomUUID(), request.requestedBy(), now, expiresAt);
        activeSessions.put(session.getId(), session);
        return session;
    }

    public boolean releaseSession(UUID sessionId) {
        return activeSessions.remove(sessionId) != null;
    }

    public Collection<GpuSession> listActiveSessions() {
        purgeExpiredSessions();
        return activeSessions.values();
    }

    private void purgeExpiredSessions() {
        Instant now = Instant.now();
        activeSessions.values().removeIf(session -> session.getExpiresAt().isBefore(now));
    }
}
