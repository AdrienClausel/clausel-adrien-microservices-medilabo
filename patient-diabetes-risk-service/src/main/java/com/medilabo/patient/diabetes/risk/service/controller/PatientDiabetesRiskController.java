package com.medilabo.patient.diabetes.risk.service.controller;

import com.medilabo.patient.diabetes.risk.service.constant.RiskLevel;
import com.medilabo.patient.diabetes.risk.service.service.IPatientDiabetesRiskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patient-diabetes-risk")
public class PatientDiabetesRiskController {

    @Autowired
    private IPatientDiabetesRiskService patientDiabetesRiskService;

    @GetMapping("/{patientId}")
    public RiskLevel getRiskLevel(@PathVariable Long patientId){
        return patientDiabetesRiskService.getRiskLevelForPatient(patientId);
    }

}
