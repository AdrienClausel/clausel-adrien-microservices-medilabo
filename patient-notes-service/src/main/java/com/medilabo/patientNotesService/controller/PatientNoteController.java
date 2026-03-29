package com.medilabo.patientNotesService.controller;

import com.medilabo.patientNotesService.dto.PatientNoteDto;
import com.medilabo.patientNotesService.mapper.IPatientNoteMapper;
import com.medilabo.patientNotesService.service.IPatientNoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patient-notes")
public class PatientNoteController {

    @Autowired
    private IPatientNoteService patientNoteService;

    @Autowired
    private IPatientNoteMapper patientNoteMapper;

    @GetMapping("/patient/{patientId}")
    public List<PatientNoteDto> getNotesByPatientId(@PathVariable Long patientId) {
        return patientNoteMapper.toDTOList(patientNoteService.getNotesByPatientId(patientId));
    }

    @PostMapping()
    public PatientNoteDto add(@RequestBody PatientNoteDto patientNoteDto) {
        return patientNoteMapper.toDTO(patientNoteService.add(patientNoteMapper.toEntity(patientNoteDto)));
    }


}
