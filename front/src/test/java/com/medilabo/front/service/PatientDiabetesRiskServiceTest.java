package com.medilabo.front.service;

import com.medilabo.front.client.IPatientDiabetesRiskClient;
import com.medilabo.front.constant.RiskLevel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PatientDiabetesRiskServiceTest {

    @InjectMocks
    private PatientDiabetesRiskService patientDiabetesRiskService;

    @Mock
    private IPatientDiabetesRiskClient patientDiabetesRiskClient;

    static Stream<Arguments> riskLevelCases() {
        return Stream.of(
                Arguments.of(RiskLevel.None,        "aucun risque"),
                Arguments.of(RiskLevel.Borderline,  "risque limité"),
                Arguments.of(RiskLevel.InDanger,    "danger"),
                Arguments.of(RiskLevel.EarlyOnset,  "apparition précoce")
        );
    }

    @ParameterizedTest(name = "{0} -> {1}")
    @MethodSource("riskLevelCases")
    void shouldReturnRiskLevelForPatient(RiskLevel riskLevel, String label){

        when(patientDiabetesRiskClient.getRiskLevelForPatient(1L)).thenReturn(riskLevel);

        assertEquals(label, patientDiabetesRiskService.getRiskLevelForPatient(1L));



    }
}
