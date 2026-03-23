package com.medilabo.patientService.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@Data
@Entity
@Table(name="patient")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @NotBlank(message = "le prénom est obligatoire")
    private String firstName;

    @NotBlank(message = "le nom est obligatoire")
    private String lastName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "la date de naissance est obligatoire")
    private Date dateOfBirth;

    @NotBlank(message = "le genre est obligatoire")
    private String gender;
    private String postalAddress;
    private String phoneNumber;
}
