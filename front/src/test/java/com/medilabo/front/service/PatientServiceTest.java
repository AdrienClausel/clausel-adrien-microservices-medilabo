package com.medilabo.front.service;

import com.medilabo.front.client.IPatientClient;
import com.medilabo.front.dto.PatientDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PatientServiceTest {

    @InjectMocks
    private PatientService patientService;

    @Mock
    private IPatientClient patientClient;

    @Test
    void shouldReturnAllPatients() {

        when(patientClient.getAll()).thenReturn(List.of(
                new PatientDto(1L, "Jean", "Moulin", LocalDate.of(2001,1,1), "M", null, null)
        ));

        List<PatientDto> patients = patientService.getALL();

        assertNotNull(patients);
        assertEquals(1, patients.size());
        assertEquals("Jean", patients.get(0).getFirstName());
    }

}
