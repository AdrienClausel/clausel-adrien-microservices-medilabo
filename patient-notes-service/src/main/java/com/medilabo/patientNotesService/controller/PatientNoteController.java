package com.medilabo.patientNotesService.controller;

import com.medilabo.patientNotesService.dto.PatientNoteDto;
import com.medilabo.patientNotesService.mapper.IPatientNoteMapper;
import com.medilabo.patientNotesService.service.IPatientNoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST chargé de la gestion des notes médicales associées aux patients.
 */
@RestController
@RequestMapping("/patient-notes")
public class PatientNoteController {

    @Autowired
    private IPatientNoteService patientNoteService;

    @Autowired
    private IPatientNoteMapper patientNoteMapper;

    /**
     * Récupère l’ensemble des notes médicales associées à un patient.
     * @param patientId l’identifiant unique du patient dont on souhaite obtenir les notes
     * @return une liste de notes du patient
     */
    @GetMapping("/patient/{patientId}")
    public List<PatientNoteDto> getNotesByPatientId(@PathVariable Long patientId) {
        return patientNoteMapper.toDTOList(patientNoteService.getNotesByPatientId(patientId));
    }

    /**
     * Ajoute une nouvelle note médicale pour un patient.
     * @param patientNoteDto la note à créer
     * @return la note créée
     */
    @PostMapping()
    public PatientNoteDto add(@RequestBody PatientNoteDto patientNoteDto) {
        return patientNoteMapper.toDTO(patientNoteService.add(patientNoteMapper.toEntity(patientNoteDto)));
    }
}
