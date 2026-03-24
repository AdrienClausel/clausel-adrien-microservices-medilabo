package com.medilabo.front.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientDto {

    private Long id;

    @NotBlank(message = "le prénom est obligatoire")
    private String firstName;

    @NotBlank(message = "le nom est obligatoire")
    private String lastName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "la date de naissance est obligatoire")
    private LocalDate dateOfBirth;

    @NotBlank(message = "le genre est obligatoire")
    private String gender;

    private String postalAddress;

    private String phoneNumber;
}
