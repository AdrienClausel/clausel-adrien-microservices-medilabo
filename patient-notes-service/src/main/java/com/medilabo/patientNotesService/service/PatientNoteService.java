package com.medilabo.patientNotesService.service;

import com.medilabo.patientNotesService.model.PatientNote;
import com.medilabo.patientNotesService.repository.IPatientNotesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientNoteService implements IPatientNoteService {

    @Autowired
    private IPatientNotesRepository patientNotesRepository;

    @Override
    public List<PatientNote> getNotesByPatientId(Long patientId) {
        return patientNotesRepository.findByPatId(patientId);
    }

    @Override
    public PatientNote add(PatientNote patientNote){
        return patientNotesRepository.save(patientNote);
    }
}
