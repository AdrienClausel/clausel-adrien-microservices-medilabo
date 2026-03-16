package com.medilabo.patientService.service;

import com.medilabo.patientService.dto.PatientDto;
import com.medilabo.patientService.model.Patient;

public interface IPatientService {

    Patient getById(Long id) throws Exception;
    void add(Patient patient);
    void update(Patient patient, Long id) throws Exception;
}
