package com.medilabo.patientService.service;

import com.medilabo.patientService.dto.PatientDto;
import com.medilabo.patientService.model.Patient;

import java.util.List;

public interface IPatientService {

    List<Patient> getAll();

    Patient getById(Long id) throws Exception;
    void add(Patient patient);
    void update(Patient patient, Long id) throws Exception;
}
