package com.mohdiop.deme_api.dto.request.creation;

import com.mohdiop.deme_api.entity.Activity;
import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

public record CreateActivityRequest(
        @NotBlank(message = "Le nom est obligatoire et ne doit pas être vide.")
        String name,

        @Pattern(
                regexp = "^(?!\\s*$).+",
                message = "La description ne doit pas être vide si elle est fournie."
        )
        String description,

        @NotBlank(message = "Les mots de l'étudiant sont obligatoires et ne doivent pas être vides.")
        String studentWords,

        @NotNull(message = "La date de réalisation est obligatoire.")
        @PastOrPresent(message = "La date de réalisation doit être dans le passé ou aujourd'hui.")
        LocalDateTime doneAt,

        @NotNull(message = "La preuve est obligatoire.")
        MultipartFile proof
) {

    public Activity toActivity() {
        return Activity.builder()
                .activityId(null)
                .activityName(name)
                .activityDescription(description)
                .studentWords(studentWords)
                .activityDoneAt(doneAt)
                .activityCreatedAt(LocalDateTime.now())
                .build();
    }
}
