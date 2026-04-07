package com.medilabo.patient.diabetes.risk.service.service;

import com.medilabo.patient.diabetes.risk.service.constant.Gender;
import com.medilabo.patient.diabetes.risk.service.constant.RiskLevel;
import com.medilabo.patient.diabetes.risk.service.client.IPatientClient;
import com.medilabo.patient.diabetes.risk.service.client.IPatientNoteClient;
import com.medilabo.patient.diabetes.risk.service.dto.PatientDto;
import com.medilabo.patient.diabetes.risk.service.dto.PatientNoteDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
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

    private List<String> triggerTerms;

    PatientDiabetesRiskService() throws IOException {
        loadTriggerTerms();
    }

    /**
     * Renvoie le niveau de risque d'un patient en fonction de son profil et de ses notes médicales
     * @param id identifiant du patient
     * @return Le niveau de risque évalué
     */
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

    /**
     * Compte le nombre de termes déclencheurs distincts présents dans les notes du patient
     * @param triggerTerms la liste des termes déclencheurs à rechercher
     * @param notes notes du patient
     * @return le nombre de termes déclencheurs distincts détectés
     */
    private long getNumberTriggerTermInNote(List<String> triggerTerms, List<PatientNoteDto> notes) {
        return triggerTerms.stream()
                .filter(triggerTerm ->
                        notes.stream()
                                .anyMatch(note -> note.getNote().toLowerCase().contains(triggerTerm.toLowerCase()))
                )
                .count();
    }

    /**
     * Renvoie l'âge du patient
     * @param patientDto information du patient
     * @return l'âge du patient en années
     */
    private int getAge(PatientDto patientDto) {
        return Period.between(patientDto.getDateOfBirth(), LocalDate.now()).getYears();
    }

    /**
     * Détermine si le patient est de genre masculin
     * @param patientDto information du patient
     * @return vrai si le patient est de genre masculin sinon faux
     */
    private boolean isMale(PatientDto patientDto) {
        return Objects.equals(patientDto.getGender(), Gender.male);
    }

    /**
     * Détermine si le patient est de genre féminin
     * @param patientDto information du patient
     * @return vrai si le patient est de genre féminin sinon faux
     */
    private boolean isFemale(PatientDto patientDto) {
        return Objects.equals(patientDto.getGender(), Gender.female);
    }

    /**
     * Charge les termes déclencheurs depuis un fichier
     * Chaque ligne non vide dans le fichier est pris en compte comme un terme déclencheur.
     * @throws IOException génération d'une exception si le fichier est introuvable ou illisible
     */
    private void loadTriggerTerms() throws IOException {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("triggerTerms.txt")) {
            assert is != null;
            triggerTerms = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))
                    .lines()
                    .filter(line -> !line.isBlank())
                    .toList();
        }
    }
}
