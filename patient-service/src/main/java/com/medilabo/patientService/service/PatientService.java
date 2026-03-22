package com.medilabo.patientService.service;

import com.medilabo.patientService.dto.PatientDto;
import com.medilabo.patientService.mapper.IPatientMapper;
import com.medilabo.patientService.model.Patient;
import com.medilabo.patientService.repository.IPatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService implements IPatientService {

    @Autowired
    private IPatientRepository patientRepository;

    @Autowired
    private IPatientMapper patientMapper;


    @Override
    public List<Patient> getAll() {
        return patientRepository.findAll();
    }

    @Override
    public Patient getById(Long id) throws Exception {
        return patientRepository.findById(id)
                .orElseThrow(() -> new Exception(""));
    }

    @Override
    public void add(Patient patient) {
        patientRepository.save(patient);
    }

    @Override
    public void update(Patient patientUpdate, Long id) throws Exception {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new Exception(""));
        patientRepository.save(patient);
    }
}
