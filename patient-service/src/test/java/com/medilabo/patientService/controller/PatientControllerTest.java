package com.medilabo.patientService.controller;

import com.medilabo.patientService.dto.PatientDto;
import com.medilabo.patientService.exception.PatientNotFoundException;
import com.medilabo.patientService.mapper.IPatientMapper;
import com.medilabo.patientService.model.Patient;
import com.medilabo.patientService.service.IPatientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.http.MediaType;

@WebMvcTest(PatientController.class)
public class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IPatientService patientService;

    @MockitoBean
    private IPatientMapper patientMapper;

    @Test
    void shouldReturnAllPatients() throws Exception {
        PatientDto dto = new PatientDto(1L, "Jean", "Moulin", new Date(1990,1,1), "M", null, null);
        when(patientMapper.toDTOList(any())).thenReturn(List.of(dto));

        mockMvc.perform(get("/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].firstName").value("Jean"));
    }

    @Test
    void shouldReturnPatientById() throws Exception {
        Patient patient = new Patient();
        PatientDto dto = new PatientDto(1L, "Jean", "Moulin", new Date(1990,1,1), "M", null, null);

        when(patientService.getById(1L)).thenReturn(patient);
        when(patientMapper.toDTO(patient)).thenReturn(dto);

        mockMvc.perform(get("/patients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("Jean"));
    }

    @Test
    void shouldAddPatient() throws Exception {
        mockMvc.perform(post("/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "firstName": "Jean",
                        "lastName": "Moulin",
                        "dateOfBirth": "2001-01-01",
                        "gender": "M"
                    }
                """))
                .andExpect(status().isOk());
    }

    @Test
    void shouldUpdatePatient() throws Exception {
        mockMvc.perform(put("/patients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "firstName": "Jean",
                        "lastName": "Moulin",
                        "dateOfBirth": "2001-01-01",
                        "gender": "M"
                    }
                """))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn404WhenPatientNotFound() throws Exception {
        when(patientService.getById(999L)).thenThrow(new PatientNotFoundException(999L));

        mockMvc.perform(get("/patients/999"))
                .andExpect(status().isNotFound());
    }
}
