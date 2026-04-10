package com.medilabo.patient.diabetes.risk.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.medilabo.patient.diabetes.risk.service.constant.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientDto {

    private Long id;

    @NotBlank(message = "le prénom est obligatoire")
    private String firstName;

    @NotBlank(message = "le nom est obligatoire")
    private String lastName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull(message = "la date de naissance est obligatoire")
    private LocalDate dateOfBirth;

    @NotBlank(message = "le genre est obligatoire")
    private String gender;

    private String postalAddress;

    private String phoneNumber;

}
