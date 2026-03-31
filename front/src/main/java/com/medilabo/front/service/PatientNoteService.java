package com.medilabo.front.service;

import com.medilabo.front.client.IPatientNoteClient;
import com.medilabo.front.dto.PatientNoteDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientNoteService implements IPatientNoteService {

    @Autowired
    private IPatientNoteClient patientNoteClient;

    @Override
    public List<PatientNoteDto> getNotesByPatientId(Long patientId) {
        return patientNoteClient.getNotesById(patientId);
    }

    @Override
    public PatientNoteDto add(PatientNoteDto patientNoteDto) {
        return patientNoteClient.add(patientNoteDto);
    }
}
