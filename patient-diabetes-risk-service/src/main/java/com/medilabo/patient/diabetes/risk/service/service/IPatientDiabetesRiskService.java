package com.medilabo.patient.diabetes.risk.service.service;

import com.medilabo.patient.diabetes.risk.service.constant.RiskLevel;

public interface IPatientDiabetesRiskService {

    RiskLevel getRiskLevelForPatient(Long id);
}
