package com.medilabo.front.client;

import com.medilabo.front.dto.PatientDto;

import java.util.List;

public interface IPatientClient {

    List<PatientDto> getAll();
    PatientDto getById(Long id);

    PatientDto add(PatientDto patientDto);

    PatientDto update(PatientDto patientDto, Long id);
}
