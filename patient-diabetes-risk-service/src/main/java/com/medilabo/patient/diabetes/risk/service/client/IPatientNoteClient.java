package com.medilabo.patient.diabetes.risk.service.client;

import com.medilabo.patient.diabetes.risk.service.dto.PatientNoteDto;

import java.util.List;

public interface IPatientNoteClient {

    List<PatientNoteDto> getNotesById(Long patientId);

}
