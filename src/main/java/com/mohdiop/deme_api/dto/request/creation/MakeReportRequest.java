package com.mohdiop.deme_api.dto.request.creation;

import com.mohdiop.deme_api.entity.Report;
import com.mohdiop.deme_api.entity.enumeration.ReportState;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;

public record MakeReportRequest(
        @Pattern(
                regexp = "^(?!\\s*$).+",
                message = "La description ne doit pas être vide si elle est fournie."
        )
        String description
) {

    public Report toReport() {
        return Report.builder()
                .reportId(null)
                .reportDescription(description)
                .reportState(ReportState.OPEN)
                .reportOpenedAt(LocalDateTime.now())
                .reportClosedAt(null)
                .build();
    }
}
