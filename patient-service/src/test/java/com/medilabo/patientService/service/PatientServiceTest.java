package com.medilabo.patientService.service;

import com.medilabo.patientService.model.Patient;
import com.medilabo.patientService.repository.IPatientRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class PatientServiceTest {

    @Autowired
    private IPatientService patientService;

    @Autowired
    private IPatientRepository patientRepository;

    private Patient createPatient(String firstName, String lastName) {
        Patient patient = new Patient();
        patient.setFirstName(firstName);
        patient.setLastName(lastName);
        patient.setGender("M");
        patient.setDateOfBirth(new Date());
        return patientRepository.save(patient);
    }

    @Test
    void shouldReturnEmptyListWhenNoPatients() {
        List<Patient> patients = patientService.getAll();
        assertNotNull(patients);
        assertTrue(patients.isEmpty());
    }

    @Test
    void shouldReturnAllPatients() {
        createPatient("Alexandre", "Dumas");
        createPatient("Jean", "Moulin");

        List<Patient> patients = patientService.getAll();

        assertNotNull(patients);
        assertEquals(2, patients.size());
    }

    @Test
    void shouldReturnPatientById() throws Exception {
        Patient saved = createPatient("Jean", "Moulin");

        Patient found = patientService.getById(saved.getId());

        assertNotNull(found);
        assertEquals("Jean", found.getFirstName());
        assertEquals("Moulin", found.getLastName());
    }

    @Test
    void shouldThrowExceptionWhenPatientNotFound() {
        assertThrows(Exception.class, () -> patientService.getById(0L));
    }

}
