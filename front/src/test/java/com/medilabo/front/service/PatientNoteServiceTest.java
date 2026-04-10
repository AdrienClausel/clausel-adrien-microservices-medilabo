package com.medilabo.front.service;

import com.medilabo.front.client.IPatientClient;
import com.medilabo.front.client.IPatientNoteClient;
import com.medilabo.front.dto.PatientNoteDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PatientNoteServiceTest {

    @InjectMocks
    private PatientNoteService patientNoteService;

    @Mock
    private IPatientNoteClient patientNoteClient;

    @Test
    void shouldReturnNotesByPatientId() {
        PatientNoteDto note = new PatientNoteDto();
        note.setPatId(1L);
        note.setNote("une note");

        when(patientNoteClient.getNotesById(1L)).thenReturn(List.of(note));

        List<PatientNoteDto> notes = patientNoteService.getNotesByPatientId(1L);

        assertNotNull(notes);
        assertEquals(1, notes.size());
        assertEquals("une note", notes.get(0).getNote());
    }

    @Test
    void shouldReturnEmptyListWhenNoNotes() {
        when(patientNoteClient.getNotesById(999L)).thenReturn(List.of());

        List<PatientNoteDto> notes = patientNoteService.getNotesByPatientId(999L);

        assertNotNull(notes);
        assertTrue(notes.isEmpty());
    }

    @Test
    void shouldAddNote() {
        PatientNoteDto noteDto = new PatientNoteDto(null,1L,"Moulin","une note");

        when(patientNoteClient.add(noteDto)).thenReturn(noteDto);

        PatientNoteDto saved = patientNoteService.add(noteDto);

        assertNotNull(saved);
        verify(patientNoteClient, times(1)).add(noteDto);
        assertEquals("une note", saved.getNote());
    }
}
