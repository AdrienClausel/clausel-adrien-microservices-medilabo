package com.medilabo.patient.diabetes.risk.service.controller;

import com.medilabo.patient.diabetes.risk.service.constant.RiskLevel;
import com.medilabo.patient.diabetes.risk.service.service.IPatientDiabetesRiskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PatientDiabetesRiskController.class)
public class PatientDiabetesRiskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IPatientDiabetesRiskService patientDiabetesRiskService;

    @Test
    void getRiskLevel_shouldReturnNone() throws Exception {
        when(patientDiabetesRiskService.getRiskLevelForPatient(1L)).thenReturn(RiskLevel.None);

        mockMvc.perform(get("/patient-diabetes-risk/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("\"None\""));
    }

    @Test
    void getRiskLevel_shouldReturnBorderline() throws Exception {
        when(patientDiabetesRiskService.getRiskLevelForPatient(2L)).thenReturn(RiskLevel.Borderline);

        mockMvc.perform(get("/patient-diabetes-risk/2"))
                .andExpect(status().isOk())
                .andExpect(content().string("\"Borderline\""));
    }

    @Test
    void getRiskLevel_shouldReturnInDanger() throws Exception {
        when(patientDiabetesRiskService.getRiskLevelForPatient(3L)).thenReturn(RiskLevel.InDanger);

        mockMvc.perform(get("/patient-diabetes-risk/3"))
                .andExpect(status().isOk())
                .andExpect(content().string("\"InDanger\""));
    }

    @Test
    void getRiskLevel_shouldReturnEarlyOnset() throws Exception {
        when(patientDiabetesRiskService.getRiskLevelForPatient(4L)).thenReturn(RiskLevel.EarlyOnset);

        mockMvc.perform(get("/patient-diabetes-risk/4"))
                .andExpect(status().isOk())
                .andExpect(content().string("\"EarlyOnset\""));
    }
}
