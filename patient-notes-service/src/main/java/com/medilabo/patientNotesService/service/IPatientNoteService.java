package com.medilabo.patientNotesService.service;

import com.medilabo.patientNotesService.model.PatientNote;

import java.util.List;

public interface IPatientNoteService {
    List<PatientNote> getNotesByPatientId(Long patientId);

    PatientNote add(PatientNote patientNote);
}
