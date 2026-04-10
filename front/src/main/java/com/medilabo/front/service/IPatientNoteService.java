package com.medilabo.front.service;

import com.medilabo.front.dto.PatientNoteDto;

import java.util.List;

public interface IPatientNoteService {
    List<PatientNoteDto> getNotesByPatientId(Long patientId);

    PatientNoteDto add(PatientNoteDto patientNoteDto);
}
