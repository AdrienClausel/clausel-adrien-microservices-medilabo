package com.medilabo.patientService.controller;

import com.medilabo.patientService.dto.PatientDto;
import com.medilabo.patientService.mapper.IPatientMapper;
import com.medilabo.patientService.model.Patient;
import com.medilabo.patientService.service.IPatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Contrôleur REST permettant la gestion des patients.
 */
@RestController
@RequestMapping("/patients")
public class PatientController {

    @Autowired
    private IPatientService patientService;

    @Autowired
    private IPatientMapper patientMapper;

    /**
     * Récupère la liste complète des patients.
     * @return une liste de tous les patients enregistrés
     */
    @GetMapping()
    public List<PatientDto> getAll(){
        return patientMapper.toDTOList(patientService.getAll());
    }

    /**
     * Récupère un patient à partir de son identifiant unique.
     * @param id l'identifiant du patient à rechercher
     * @return le patient correspondant à l'identifiant
     * @throws Exception si aucun patient ne correspond à l'identifiant fourni
     */
    @GetMapping("/{id}")
    public PatientDto getById(@PathVariable Long id) throws Exception {
        var patient = patientService.getById(id);
        return patientMapper.toDTO(patient);
    }

    /**
     * Met à jour les informations d’un patient existant.
     * @param id l'identifiant du patient à mettre à jour
     * @param patientDto les nouvelles informations du patient
     * @return le patient mis à jour
     * @throws Exception si le patient n'existe pas
     */
    @PutMapping("/{id}")
    public PatientDto update(@PathVariable Long id, @RequestBody PatientDto patientDto) throws Exception {
        var patient = patientService.update(patientMapper.toEntity(patientDto), id);
        return patientMapper.toDTO(patient);
    }

    /**
     * Ajoute un nouveau patient.
     * @param patientDto les informations du patient à créer
     * @return le patient créé
     */
    @PostMapping()
    public PatientDto add(@RequestBody PatientDto patientDto){
        var patient = patientService.add(patientMapper.toEntity(patientDto));
        return patientMapper.toDTO(patient);
    }

}
