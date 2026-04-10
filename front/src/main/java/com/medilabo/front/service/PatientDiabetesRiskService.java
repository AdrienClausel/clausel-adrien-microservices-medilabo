package com.medilabo.front.service;

import com.medilabo.front.client.IPatientDiabetesRiskClient;
import com.medilabo.front.constant.RiskLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service métier qui récupère le niveau de risque de diabète
 * d'un patient.
 */
@Service
public class PatientDiabetesRiskService implements IPatientDiabetesRiskService {

    @Autowired
    private IPatientDiabetesRiskClient patientDiabetesRiskClient;

    /**
     * Récupère le niveau de risque de diabète d'un patient
     * @param patientId identifiant du patient
     * @return une chaîne décrivant le niveau de risque ("aucun risque", "risque limité", etc.)
     */
    @Override
    public String getRiskLevelForPatient(Long patientId) {
        RiskLevel riskLevel = patientDiabetesRiskClient.getRiskLevelForPatient(patientId);

        return switch (riskLevel) {
            case None -> "aucun risque";
            case Borderline -> "risque limité";
            case InDanger -> "danger";
            case EarlyOnset -> "apparition précoce";
        };
    }
}
