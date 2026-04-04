package com.medilabo.patientNotesService.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientNoteDto {

    private String id;

    @NotBlank(message = "l'identifiant du patient est obligatoire")
    private Long patId;

    @NotBlank(message = "le nom du patient est obligatoire")
    private String patient;

    @NotBlank(message = "la note est obligatoire")
    private String note;
}
