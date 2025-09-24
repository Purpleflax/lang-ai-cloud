package com.langai.cloud.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public record GpuSessionRequest(
        @NotBlank(message = "Requester name is required")
        String requestedBy,

        @PositiveOrZero(message = "Duration must be zero or a positive number of minutes")
        int durationMinutes
) {
}
