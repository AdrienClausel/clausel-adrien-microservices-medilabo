package com.medilabo.patient.diabetes.risk.service.service;

import com.medilabo.patient.diabetes.risk.service.constant.Gender;
import com.medilabo.patient.diabetes.risk.service.constant.RiskLevel;
import com.medilabo.patient.diabetes.risk.service.client.IPatientClient;
import com.medilabo.patient.diabetes.risk.service.client.IPatientNoteClient;
import com.medilabo.patient.diabetes.risk.service.dto.PatientDto;
import com.medilabo.patient.diabetes.risk.service.dto.PatientNoteDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Objects;

@Service
public class PatientDiabetesRiskService implements IPatientDiabetesRiskService {

    @Autowired
    private IPatientClient patientClient;

    @Autowired
    private IPatientNoteClient patientNoteClient;

    private final List<String> triggerTerms;

    PatientDiabetesRiskService() {
        triggerTerms = List.of(
                "Hémoglobine A1C",
                "Microalbumine",
                "Taille",
                "Poids",
                "Fumeur",
                "Fumeuse",
                "Anormal",
                "Cholestérol",
                "Vertiges",
                "Rechute",
                "Réaction",
                "Anticorps"
        );
    }

    @Override
    public RiskLevel getRiskLevelForPatient(Long id) {

        PatientDto patientDto = patientClient.getById(id);
        List<PatientNoteDto> patientNoteDto = patientNoteClient.getNotesById(id);
        long numberTriggerTermInNote = getNumberTriggerTermInNote(triggerTerms,patientNoteDto);
        int patientAge = getAge(patientDto);
        boolean isMale = isMale(patientDto);
        boolean isFemale = isFemale(patientDto);

        //None
        if (numberTriggerTermInNote == 0) {
            return RiskLevel.None;
        }

        //Early onset
        if (isMale && patientAge < 30 && numberTriggerTermInNote >= 5) {
            return RiskLevel.EarlyOnset;
        }
        if (isFemale && patientAge < 30 && numberTriggerTermInNote >= 7) {
            return RiskLevel.EarlyOnset;
        }
        if (patientAge >= 30 && numberTriggerTermInNote >= 8) {
            return RiskLevel.EarlyOnset;
        }

        //In Danger
        if (isMale && patientAge < 30 && numberTriggerTermInNote >= 3) {
            return RiskLevel.InDanger;
        }
        if (isFemale && patientAge < 30 && numberTriggerTermInNote >= 4) {
            return RiskLevel.InDanger;
        }
        if (patientAge >= 30 && numberTriggerTermInNote >= 6 && numberTriggerTermInNote <= 7) {
            return RiskLevel.InDanger;
        }

        //Borderline
        if (numberTriggerTermInNote >= 2 && numberTriggerTermInNote <= 5 && patientAge > 30) {
            return RiskLevel.Borderline;
        }

        return RiskLevel.None;
    }

    private long getNumberTriggerTermInNote(List<String> triggerTerms, List<PatientNoteDto> notes) {
        return triggerTerms.stream()
                .filter(triggerTerm ->
                        notes.stream()
                                .anyMatch(note -> note.getNote().contains(triggerTerm))
                )
                .count();
    }

    private int getAge(PatientDto patientDto) {
        return Period.between(patientDto.getDateOfBirth(), LocalDate.now()).getYears();
    }

    private boolean isMale(PatientDto patientDto) {
        return Objects.equals(patientDto.getGender(), Gender.male);
    }

    private boolean isFemale(PatientDto patientDto) {
        return Objects.equals(patientDto.getGender(), Gender.female);
    }
}
