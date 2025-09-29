package com.mohdiop.deme_api.dto.response;

import com.mohdiop.deme_api.entity.enumeration.FileType;
import com.mohdiop.deme_api.entity.enumeration.ProofType;

import java.time.LocalDateTime;

public record ProofResponse(
        Long id,
        FileType fileType,
        ProofType type,
        String url,
        LocalDateTime uploadAt
) {
}
