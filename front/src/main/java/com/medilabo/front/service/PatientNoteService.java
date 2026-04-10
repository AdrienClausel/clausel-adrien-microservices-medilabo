package com.medilabo.front.service;

import com.medilabo.front.client.IPatientNoteClient;
import com.medilabo.front.dto.PatientNoteDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service métier chargé de gérer les notes médicales associées aux patients.
 */
@Service
public class PatientNoteService implements IPatientNoteService {

    @Autowired
    private IPatientNoteClient patientNoteClient;

    /**
     * Récupère toutes les notes associées à un patient donné.
     * @param patientId identifiant du patient
     * @return une liste des notes du patient
     */
    @Override
    public List<PatientNoteDto> getNotesByPatientId(Long patientId) {
        return patientNoteClient.getNotesById(patientId);
    }

    /**
     * Ajoute une nouvelle note médicale pour un patient.
     * @param patientNoteDto données de la note à créer
     * @return la note créée
     */
    @Override
    public PatientNoteDto add(PatientNoteDto patientNoteDto) {
        return patientNoteClient.add(patientNoteDto);
    }
}
