package com.mohdiop.deme_api.dto.request.update;

import com.mohdiop.deme_api.entity.enumeration.ReportState;
import jakarta.validation.constraints.NotNull;

public record UpdateReportStateRequest(
        @NotNull(message = "L'état du signalement est obligatoire.")
        ReportState state
) {
}
