package com.medilabo.patientService.service;

import com.medilabo.patientService.exception.PatientNotFoundException;
import com.medilabo.patientService.model.Patient;
import com.medilabo.patientService.repository.IPatientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PatientServiceTest {

    @InjectMocks
    private PatientService patientService;

    @Mock
    private IPatientRepository patientRepository;

    @Test
    void shouldReturnAllPatients() {
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setFirstName("Jean");
        when(patientRepository.findAll()).thenReturn(List.of(patient));

        List<Patient> patients = patientService.getAll();

        assertNotNull(patients);
        assertEquals(1, patients.size());
        assertEquals("Jean", patients.get(0).getFirstName());
    }

    @Test
    void shouldReturnPatientById() throws Exception {
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setFirstName("Jean");
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        Patient found = patientService.getById(1L);

        assertNotNull(found);
        assertEquals("Jean", found.getFirstName());
    }

    @Test
    void shouldThrowExceptionWhenPatientNotFound() {
        when(patientRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(PatientNotFoundException.class, () -> patientService.getById(999L));
    }

    @Test
    void shouldSavePatient() {
        Patient patient = new Patient();
        patient.setFirstName("Jean");
        when(patientRepository.save(patient)).thenReturn(patient);

        patientService.add(patient);

        verify(patientRepository, times(1)).save(patient);
    }

    @Test
    void shouldUpdatePatient() throws Exception {
        Patient existing = new Patient();
        existing.setId(1L);
        existing.setFirstName("Jean");

        Patient update = new Patient();
        update.setFirstName("Gérard");
        update.setLastName("Oury");
        update.setGender("M");
        update.setDateOfBirth(new Date(1990,1,1));

        when(patientRepository.findById(1L)).thenReturn(Optional.of(existing));

        patientService.update(update, 1L);

        verify(patientRepository, times(1)).save(existing);
        assertEquals("Gérard", existing.getFirstName());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentPatient() {
        when(patientRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(PatientNotFoundException.class,
                () -> patientService.update(new Patient(), 999L));
    }

}
