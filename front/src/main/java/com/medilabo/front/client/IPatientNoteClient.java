package com.medilabo.front.client;

import com.medilabo.front.dto.PatientDto;
import com.medilabo.front.dto.PatientNoteDto;

import java.util.List;

public interface IPatientNoteClient {

    List<PatientNoteDto> getNotesById(Long patientId);

    PatientNoteDto add(PatientNoteDto patientNoteDto);

}
