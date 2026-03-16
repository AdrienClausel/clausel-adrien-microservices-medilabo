package com.medilabo.patientService.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name="patient")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private long id;

    @NotBlank(message = "le prénom est obligatoire")
    private String firstName;

    @NotBlank(message = "le nom est obligatoire")
    private String lastName;

    @NotBlank(message = "la date de naissance est obligatoire")
    private Date dateOfBirth;

    @NotBlank(message = "le genre est obligatoire")
    private String gender;
    private String postalAddress;
    private String phoneNumber;
}
