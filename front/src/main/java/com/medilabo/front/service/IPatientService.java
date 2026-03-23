package com.medilabo.front.service;

import com.medilabo.front.dto.PatientDto;
import org.springframework.stereotype.Service;

import java.util.List;


public interface IPatientService {

    List<PatientDto> getALL();

    PatientDto getById(Long id);

    PatientDto add(PatientDto patientDto);

    PatientDto update(PatientDto patientDto, Long id);
}
