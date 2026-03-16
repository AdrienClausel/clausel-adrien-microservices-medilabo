package com.medilabo.patientService.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;

public record PatientDto (
        @NotBlank(message = "le prénom est obligatoire")
        String firstName,
        @NotBlank(message = "le nom est obligatoire")
        String lastName,
        @NotBlank(message = "la date de naissance est obligatoire")
        Date dateOfBirth,
        @NotBlank(message = "le genre est obligatoire")
        String gender,
        String postalAddress,
        String phoneNumber
){}
