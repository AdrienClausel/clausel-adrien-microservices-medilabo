package com.medilabo.patient.diabetes.risk.service.client;

import com.medilabo.patient.diabetes.risk.service.dto.PatientDto;

import java.util.List;

public interface IPatientClient {

    PatientDto getById(Long id);
}
