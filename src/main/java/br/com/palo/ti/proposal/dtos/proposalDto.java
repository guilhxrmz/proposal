package br.com.palo.ti.proposal.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(description = "Data Transfer Object for job proposals")
public record proposalDto(
        @Schema(description = "Unique identifier of the proposal", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID id,

        @Schema(description = "Title of the job proposal", example = "Software Engineer")
        @NotBlank String title,

        @Schema(description = "Description of the job proposal", example = "Looking for a skilled Java developer")
        String description,

        @Schema(description = "Salary offered for the job", example = "75000")
        @NotNull Double salary,

        @Schema(description = "Status of the job proposal", example = "OPEN")
        @NotBlank String status,

        @Schema(description = "Contact email for the job proposal", example = "hr@example.com")
        @Email String email
) {
}