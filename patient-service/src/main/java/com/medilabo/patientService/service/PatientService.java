package com.medilabo.patientService.service;

import com.medilabo.patientService.exception.PatientNotFoundException;
import com.medilabo.patientService.mapper.IPatientMapper;
import com.medilabo.patientService.model.Patient;
import com.medilabo.patientService.repository.IPatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service métier chargé de la gestion des patients.
 */
@Service
public class PatientService implements IPatientService {

    @Autowired
    private IPatientRepository patientRepository;

    @Autowired
    private IPatientMapper patientMapper;

    /**
     * Récupère l’ensemble des patients enregistrés.
     * @return une liste de patients
     */
    @Override
    public List<Patient> getAll() {
        return patientRepository.findAll();
    }

    /**
     * Récupère un patient à partir de son identifiant unique.
     * @param id l’identifiant du patient à rechercher
     * @return le patient correspondant
     * @throws Exception si aucun patient ne correspond à l’identifiant fourni
     */
    @Override
    public Patient getById(Long id) throws Exception {
        return patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException(id));
    }

    /**
     * Ajoute un nouveau patient en base de données.
     * @param patient le patient à enregistrer
     * @return le patient enregistré, incluant son identifiant généré
     */
    @Override
    public Patient add(Patient patient) {
        return patientRepository.save(patient);
    }

    /**
     * Met à jour les informations d’un patient existant.
     * @param patientUpdate les nouvelles informations du patient
     * @param id l’identifiant du patient à mettre à jour
     * @return le patient mis à jour
     * @throws Exception si le patient à mettre à jour n’existe pas
     */
    @Override
    public Patient update(Patient patientUpdate, Long id) throws Exception {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException(id));

        patient.setFirstName(patientUpdate.getFirstName());
        patient.setLastName(patientUpdate.getLastName());
        patient.setDateOfBirth(patientUpdate.getDateOfBirth());
        patient.setGender(patientUpdate.getGender());
        patient.setPostalAddress(patientUpdate.getPostalAddress());
        patient.setPhoneNumber(patientUpdate.getPhoneNumber());

        return patientRepository.save(patient);
    }
}
