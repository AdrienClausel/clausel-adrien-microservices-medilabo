package com.medilabo.front.service;

import com.medilabo.front.client.IPatientClient;
import com.medilabo.front.dto.PatientDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service métier chargé de gérer les opérations relatives aux patients.
 */
@Service
public class PatientService implements IPatientService {

    @Autowired
    private IPatientClient patientClient;

    /**
     * Récupère l'ensemble des patients.
     * @return une liste de tous les patients
     */
    @Override
    public List<PatientDto> getALL() {
        return patientClient.getAll();
    }

    /**
     * Récupère un patient à partir de son identifiant.
     * @param id identifiant du patient
     * @return les données du patient correspondant au patient recherché
     */
    @Override
    public PatientDto getById(Long id) {
        return patientClient.getById(id);
    }

    /**
     * Ajoute un nouveau patient.
     * @param patientDto données du patient à créer
     * @return le patient créé
     */
    @Override
    public PatientDto add(PatientDto patientDto) {
        return patientClient.add(patientDto);
    }

    /**
     * Met à jour un patient existant.
     * @param patientDto données modifiées du patient
     * @param id identifiant du patient à mettre à jour
     * @return le patient mis à jour
     */
    @Override
    public PatientDto update(PatientDto patientDto, Long id) {
        return patientClient.update(patientDto, id);
    }
}
