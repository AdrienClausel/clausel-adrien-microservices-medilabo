package com.medilabo.patientNotesService.service;

import com.medilabo.patientNotesService.model.PatientNote;
import com.medilabo.patientNotesService.repository.IPatientNotesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service métier chargé de la gestion des notes médicales des patients.
 */
@Service
public class PatientNoteService implements IPatientNoteService {

    @Autowired
    private IPatientNotesRepository patientNotesRepository;

    /**
     * Récupère toutes les notes médicales liées à un patient donné.
     * @param patientId l'identifiant unique du patient
     * @return une liste de {@link PatientNote} correspondant aux notes du patient
     */
    @Override
    public List<PatientNote> getNotesByPatientId(Long patientId) {
        return patientNotesRepository.findByPatId(patientId);
    }

    /**
     * Ajoute une nouvelle note médicale en base de données.
     *
     * @param patientNote la note à enregistrer
     * @return la note enregistrée, incluant son identifiant généré
     */
    @Override
    public PatientNote add(PatientNote patientNote){
        return patientNotesRepository.save(patientNote);
    }
}
