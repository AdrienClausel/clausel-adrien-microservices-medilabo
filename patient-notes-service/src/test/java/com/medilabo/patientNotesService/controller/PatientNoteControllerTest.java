package com.medilabo.patientNotesService.controller;

import com.medilabo.patientNotesService.dto.PatientNoteDto;
import com.medilabo.patientNotesService.mapper.IPatientNoteMapper;
import com.medilabo.patientNotesService.service.IPatientNoteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(PatientNoteController.class)
public class PatientNoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IPatientNoteService patientNoteService;

    @MockitoBean
    private IPatientNoteMapper patientNoteMapper;

    @Test
    void shouldReturnNotesByPatientId() throws Exception {
        Long patientId = 1L;

        PatientNoteDto dto = new PatientNoteDto("12545ffd",1L,"Test","Une note");
        when(patientNoteMapper.toDTOList(any())).thenReturn(List.of(dto));

        mockMvc.perform(get("/patient-notes/patient/{id}", patientId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].patId").value(1L))
                .andExpect(jsonPath("$[0].note").value("Une note"));
    }

    @Test
    void shouldAddPatientNote() throws Exception {
        mockMvc.perform(post("/patient-notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "patId": "1",
                        "patient": "Moulin",
                        "note": "Une note"
                    }
                """))
                .andExpect(status().isOk());
    }

}
