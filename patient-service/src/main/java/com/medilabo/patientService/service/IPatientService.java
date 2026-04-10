package com.medilabo.patientService.service;

import com.medilabo.patientService.model.Patient;

import java.util.List;

public interface IPatientService {

    List<Patient> getAll();

    Patient getById(Long id) throws Exception;
    Patient add(Patient patient);
    Patient update(Patient patient, Long id) throws Exception;
}
