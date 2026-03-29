package com.medilabo.patientNotesService.repository;

import com.medilabo.patientNotesService.model.PatientNote;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface IPatientNotesRepository extends MongoRepository<PatientNote, String> {

    List<PatientNote> findByPatId(Long patientId);
}
