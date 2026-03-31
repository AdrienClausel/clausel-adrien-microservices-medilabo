package com.medilabo.patientNotesService.service;

import com.medilabo.patientNotesService.dto.PatientNoteDto;
import com.medilabo.patientNotesService.model.PatientNote;
import com.medilabo.patientNotesService.repository.IPatientNotesRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PatientNoteServiceTest {

    @InjectMocks
    private PatientNoteService patientNoteService;

    @Mock
    private IPatientNotesRepository patientNotesRepository;

    @Test
    void shouldReturnNotesByPatientId() {
        Long patId = 1L;

        PatientNote patientNote = new PatientNote("121ddd4",patId,"Moulin","une note");
        when(patientNotesRepository.findByPatId(patId)).thenReturn(List.of(patientNote));

        List<PatientNote> patientNotes = patientNoteService.getNotesByPatientId(patId);
        assertNotNull(patientNotes);
        assertEquals("Moulin", patientNotes.get(0).getPatient());
    }

    @Test
    void shouldAddPatientNote() {
        PatientNote patientNote = new PatientNote("121ddd4",1L,"Moulin","une note");
        when(patientNotesRepository.save(patientNote)).thenReturn(patientNote);

        patientNoteService.add(patientNote);

        verify(patientNotesRepository, times(1)).save(patientNote);
    }
}
