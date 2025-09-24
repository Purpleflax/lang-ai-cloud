package com.langai.cloud.controller;

import com.langai.cloud.dto.GpuSessionRequest;
import com.langai.cloud.dto.GpuSessionResponse;
import com.langai.cloud.dto.GpuStatusResponse;
import com.langai.cloud.model.GpuSession;
import com.langai.cloud.service.GpuAccessService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/gpu")
public class GpuAccessController {

    private final GpuAccessService gpuAccessService;

    public GpuAccessController(GpuAccessService gpuAccessService) {
        this.gpuAccessService = gpuAccessService;
    }

    @GetMapping("/status")
    public GpuStatusResponse getStatus() {
        return gpuAccessService.getStatus();
    }

    @GetMapping("/sessions")
    public List<GpuSessionResponse> listSessions() {
        return gpuAccessService.listActiveSessions().stream()
                .map(this::toResponse)
                .toList();
    }

    @PostMapping("/sessions")
    public ResponseEntity<GpuSessionResponse> createSession(@Valid @RequestBody GpuSessionRequest request) {
        GpuSession session = gpuAccessService.createSession(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(session));
    }

    @DeleteMapping("/sessions/{sessionId}")
    public ResponseEntity<Void> releaseSession(@PathVariable UUID sessionId) {
        boolean removed = gpuAccessService.releaseSession(sessionId);
        if (!removed) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Session not found");
        }
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleCapacityReached(IllegalStateException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }

    private GpuSessionResponse toResponse(GpuSession session) {
        return new GpuSessionResponse(
                session.getId(),
                session.getRequestedBy(),
                session.getCreatedAt(),
                session.getExpiresAt()
        );
    }
}
