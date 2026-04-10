package com.medilabo.patient.diabetes.risk.service.service;

import com.medilabo.patient.diabetes.risk.service.client.IPatientClient;
import com.medilabo.patient.diabetes.risk.service.client.IPatientNoteClient;
import com.medilabo.patient.diabetes.risk.service.constant.Gender;
import com.medilabo.patient.diabetes.risk.service.constant.RiskLevel;
import com.medilabo.patient.diabetes.risk.service.dto.PatientDto;
import com.medilabo.patient.diabetes.risk.service.dto.PatientNoteDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PatientDiabetesRiskServiceTest {

    @InjectMocks
    private PatientDiabetesRiskService patientDiabetesRiskService;

    @Mock
    private IPatientClient patientClient;

    @Mock
    private IPatientNoteClient patientNoteClient;

    private PatientDto buildPatient(String gender, LocalDate dateOfBirth) {
        PatientDto dto = new PatientDto();
        dto.setGender(gender);
        dto.setDateOfBirth(dateOfBirth);
        return dto;
    }

    private PatientNoteDto noteWith(String text) {
        PatientNoteDto dto = new PatientNoteDto();
        dto.setNote(text);
        return dto;
    }

    private void mockPatient(Long id, String gender, int age) {
        when(patientClient.getById(id))
                .thenReturn(buildPatient(gender, LocalDate.now().minusYears(age)));
    }

    private void mockNotes(Long id, String... notes) {
        when(patientNoteClient.getNotesById(id))
                .thenReturn(Arrays.stream(notes).map(this::noteWith).toList());
    }

    @Test
    void shouldReturnNone_whenNoTriggerTermsPresent() {
        mockPatient(1L, Gender.male, 40);
        mockNotes(1L, "Aucun problème détecté", "Patient en bonne santé");

        assertEquals(RiskLevel.None, patientDiabetesRiskService.getRiskLevelForPatient(1L));
    }

    @Test
    void shouldReturnNone_whenOneTriggerTerm_youngMale() {
        mockPatient(1L, Gender.male, 25);
        mockNotes(1L, "Fumeur depuis 2 ans");

        assertEquals(RiskLevel.None, patientDiabetesRiskService.getRiskLevelForPatient(1L));
    }

    @Test
    void shouldReturnBorderline_whenTwoTriggerTerms_olderThan30() {
        mockPatient(1L, Gender.male, 80);
        mockNotes(1L, "Taille anormale", "Cholestérol élevé");

        assertEquals(RiskLevel.Borderline, patientDiabetesRiskService.getRiskLevelForPatient(1L));
    }

    @Test
    void shouldReturnBorderline_whenFiveTriggerTerms_olderThan30() {
        mockPatient(1L, Gender.female, 50);
        mockNotes(1L, "Taille , Poids excessif, Fumeur confirmé, Cholestérol élevé, Vertiges fréquents");

        assertEquals(RiskLevel.Borderline, patientDiabetesRiskService.getRiskLevelForPatient(1L));
    }

    @Test
    void shouldReturnInDanger_whenMaleUnder30_threeTerms() {
        mockPatient(1L, Gender.male, 25);
        mockNotes(1L, "Fumeur régulier", "Microalbumine détectée", "Vertiges signalés");

        assertEquals(RiskLevel.InDanger, patientDiabetesRiskService.getRiskLevelForPatient(1L));
    }

    @Test
    void shouldReturnInDanger_whenFemaleUnder30_fourTerms() {
        mockPatient(1L, Gender.female, 28);
        mockNotes(1L, "Fumeuse, Poids anormal, Taille hors norme, Cholestérol élevé");

        assertEquals(RiskLevel.InDanger, patientDiabetesRiskService.getRiskLevelForPatient(1L));
    }

    @Test
    void shouldReturnInDanger_whenAgeOver30_sixTerms() {
        mockPatient(1L, Gender.male, 55);
        mockNotes(1L, "Hémoglobine A1C, Microalbumine, Taille, Poids, Fumeur, Cholestérol");

        assertEquals(RiskLevel.InDanger, patientDiabetesRiskService.getRiskLevelForPatient(1L));
    }

    @Test
    void shouldReturnInDanger_whenAgeOver30_sevenTerms() {
        mockPatient(1L, Gender.female, 60);
        mockNotes(1L, "Hémoglobine A1C, Microalbumine, Taille, Poids, Fumeur, Cholestérol, Vertiges");

        assertEquals(RiskLevel.InDanger, patientDiabetesRiskService.getRiskLevelForPatient(1L));
    }

    @Test
    void shouldReturnEarlyOnset_whenMaleUnder30_fiveTerms() {
        mockPatient(1L, Gender.male, 20);
        mockNotes(1L, "Hémoglobine A1C détectée, Microalbumine présente, Taille anormale, Poids excessif, Fumeur");

        assertEquals(RiskLevel.EarlyOnset, patientDiabetesRiskService.getRiskLevelForPatient(1L));
    }

    @Test
    void shouldReturnEarlyOnset_whenFemaleUnder30_sevenTerms() {
        mockPatient(1L, Gender.female, 22);
        mockNotes(1L,
                "Hémoglobine A1C, Microalbumine, Taille, Poids, Fumeuse, Cholestérol, Vertiges");

        assertEquals(RiskLevel.EarlyOnset, patientDiabetesRiskService.getRiskLevelForPatient(1L));
    }

    @Test
    void shouldReturnEarlyOnset_whenAgeOver30_eightTerms() {
        mockPatient(1L, Gender.male, 45);
        mockNotes(1L,
                "Hémoglobine A1C, Microalbumine, Taille, Poids, Fumeur, Cholestérol, Vertiges, Rechute");

        assertEquals(RiskLevel.EarlyOnset, patientDiabetesRiskService.getRiskLevelForPatient(1L));
    }

    @Test
    void shouldBeCaseInsensitive_forTriggerTerms() {
        mockPatient(1L, Gender.male, 45);
        mockNotes(1L, "FUMEUR confirmé", "CHOLESTÉROL élevé");

        assertEquals(RiskLevel.Borderline, patientDiabetesRiskService.getRiskLevelForPatient(1L));
    }

    @Test
    void shouldCountEachTermOnce_evenIfRepeatedAcrossNotes() {
        mockPatient(1L, Gender.male, 45);
        mockNotes(1L, "Fumeur actif", "Fumeur depuis longtemps");

        assertEquals(RiskLevel.None, patientDiabetesRiskService.getRiskLevelForPatient(1L));
    }

    @Test
    void shouldReturnNone_whenNoteListIsEmpty() {
        mockPatient(1L, Gender.male, 45);
        mockNotes(1L);  // aucune note

        assertEquals(RiskLevel.None, patientDiabetesRiskService.getRiskLevelForPatient(1L));
    }


}
