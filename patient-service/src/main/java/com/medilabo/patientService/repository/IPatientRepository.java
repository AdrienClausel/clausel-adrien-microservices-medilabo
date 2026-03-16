package com.medilabo.patientService.repository;

import com.medilabo.patientService.model.Patient;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPatientRepository extends CrudRepository<Patient, Long> {
}
