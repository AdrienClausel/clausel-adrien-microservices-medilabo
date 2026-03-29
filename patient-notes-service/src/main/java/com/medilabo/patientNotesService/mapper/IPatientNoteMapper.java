package com.medilabo.patientNotesService.mapper;

import com.medilabo.patientNotesService.dto.PatientNoteDto;
import com.medilabo.patientNotesService.model.PatientNote;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IPatientNoteMapper {

    PatientNoteDto toDTO(PatientNote patientNote);

    List<PatientNoteDto> toDTOList(List<PatientNote> patients);

    PatientNote toEntity(PatientNoteDto dto);
}
