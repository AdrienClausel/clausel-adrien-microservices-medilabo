package com.medilabo.patientService.mapper;

import com.medilabo.patientService.dto.PatientDto;
import com.medilabo.patientService.model.Patient;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IPatientMapper {
    PatientDto toDTO(Patient patient);

    List<PatientDto> toDTOList(List<Patient> patients);

    Patient toEntity(PatientDto dto);
}
