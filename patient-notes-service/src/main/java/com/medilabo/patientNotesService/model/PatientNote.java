package com.medilabo.patientNotesService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "notes")
public class PatientNote {

    @Id
    private String id;

    private Long patId;

    private String patient;

    private String note;

}
