package com.medilabo.front.client;

import com.medilabo.front.constant.RiskLevel;

public interface IPatientDiabetesRiskClient {

    RiskLevel getRiskLevelForPatient(Long id);
}
