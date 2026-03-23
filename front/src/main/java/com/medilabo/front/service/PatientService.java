package com.medilabo.front.service;

import com.medilabo.front.client.IPatientClient;
import com.medilabo.front.dto.PatientDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService implements IPatientService {

    @Autowired
    private IPatientClient patientClient;

    @Override
    public List<PatientDto> getALL() {
        return patientClient.getAll();
    }

    @Override
    public PatientDto getById(Long id) {
        return patientClient.getById(id);
    }

    @Override
    public PatientDto add(PatientDto patientDto) {
        return patientClient.add(patientDto);
    }

    @Override
    public PatientDto update(PatientDto patientDto, Long id) {
        return patientClient.update(patientDto, id);
    }
}
