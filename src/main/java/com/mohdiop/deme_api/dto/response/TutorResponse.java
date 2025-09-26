package com.mohdiop.deme_api.dto.response;

public record TutorResponse(
        Long studentId,
        String firstName,
        String lastName,
        String phoneNumber,
        String email,
        String address
) {
}
