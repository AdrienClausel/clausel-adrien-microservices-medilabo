package com.medilabo.front.controller;

import com.medilabo.front.constant.RiskLevel;
import com.medilabo.front.dto.PatientDto;
import com.medilabo.front.dto.PatientNoteDto;
import com.medilabo.front.service.IPatientDiabetesRiskService;
import com.medilabo.front.service.IPatientNoteService;
import com.medilabo.front.service.IPatientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PatientController.class)
public class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IPatientService patientService;

    @MockitoBean
    private IPatientNoteService patientNoteService;

    @MockitoBean
    private IPatientDiabetesRiskService patientDiabetesRiskService;

    @Test
    @WithMockUser(username = "user")
    void souldDisplayPatientList() throws Exception {

        when(patientService.getALL()).thenReturn(List.of(
                new PatientDto(1L, "Jean", "Moulin", LocalDate.of(2001,1,1), "M", null, null)
        ));

        mockMvc.perform(get("/patients"))
                .andExpect(status().isOk())
                .andExpect(view().name("patients/list"))
                .andExpect(model().attributeExists("patients"));

    }

    @Test
    @WithMockUser(username = "user")
    void shouldDisplayAddForm() throws Exception {
        mockMvc.perform(get("/patients/form"))
                .andExpect(status().isOk())
                .andExpect(view().name("patients/form"))
                .andExpect(model().attributeExists("patient"));
    }

    @Test
    @WithMockUser(username = "user")
    void shouldAddPatientAndRedirect() throws Exception {
        mockMvc.perform(post("/patients/form")
                        .with(csrf())
                        .param("firstName", "Jean")
                        .param("lastName", "Moulin")
                        .param("dateOfBirth", "2001-01-01")
                        .param("gender", "M"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/patients"));
    }

    @Test
    @WithMockUser(username = "user")
    void shouldReturnFormWhenValidationFails() throws Exception {
        mockMvc.perform(post("/patients/form")
                        .with(csrf())
                        .param("firstName", "")
                        .param("lastName", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("patients/form"));
    }

    @Test
    @WithMockUser(username = "user")
    void shouldDisplayEditForm() throws Exception {
        when(patientService.getById(1L)).thenReturn(
                new PatientDto(1L, "Jean", "Moulin", LocalDate.of(2001,1,1), "M", null, null)
        );

        mockMvc.perform(get("/patients/1/form"))
                .andExpect(status().isOk())
                .andExpect(view().name("patients/form"))
                .andExpect(model().attributeExists("patient"));
    }

    @Test
    @WithMockUser(username = "user")
    void shouldUpdatePatientAndRedirect() throws Exception {
        mockMvc.perform(post("/patients/1/form")
                        .with(csrf())
                        .param("firstName", "Jean")
                        .param("lastName", "Moulin")
                        .param("dateOfBirth", "2001-01-01")
                        .param("gender", "M"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/patients"));
    }

    @Test
    @WithMockUser(username = "user")
    void shouldDisplayPatientDetail() throws Exception {
        PatientDto patientDto = new PatientDto(1L, "Jean", "Moulin", LocalDate.of(2001,1,1), "M", null, null);
        PatientNoteDto patientNoteDto = new PatientNoteDto("1245hjh", 1L, "Moulin", "une note");

        when(patientService.getById(1L)).thenReturn(patientDto);
        when(patientNoteService.getNotesByPatientId(1L)).thenReturn(List.of(patientNoteDto));
        when(patientDiabetesRiskService.getRiskLevelForPatient(1L)).thenReturn(String.valueOf(RiskLevel.None));

        mockMvc.perform(get("/patients/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("patients/form"))
                .andExpect(model().attribute("readOnly", true))
                .andExpect(model().attributeExists("patient"))
                .andExpect(model().attributeExists("notes"))
                .andExpect(model().attribute("diabetesRisk", String.valueOf(RiskLevel.None)));;
    }

    @Test
    @WithMockUser(username = "user")
    void shouldAddNoteAndRedirect() throws Exception {
        mockMvc.perform(post("/patients/1/notes")
                        .with(csrf())
                        .param("note", "une note")
                        .param("lastName", "Moulin"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/patients/1"));
    }

}
