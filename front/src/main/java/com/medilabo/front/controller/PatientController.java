package com.medilabo.front.controller;

import com.medilabo.front.dto.PatientDto;
import com.medilabo.front.dto.PatientNoteDto;
import com.medilabo.front.service.IPatientDiabetesRiskService;
import com.medilabo.front.service.IPatientNoteService;
import com.medilabo.front.service.IPatientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur gérant l'affichage, la création, la modification
 * et la consultation des patients.
 */
@Controller
@RequestMapping("/patients")
public class PatientController {

    @Autowired
    private IPatientService patientService;

    @Autowired
    private IPatientNoteService patientNoteService;

    @Autowired
    private IPatientDiabetesRiskService patientDiabetesRiskService;

    /**
     * Affiche la liste complète des patients.
     *
     * @param model modèle utilisé pour transmettre les données à la vue
     * @return la vue patients/list
     */
    @GetMapping()
    public String getAll(Model model){
        model.addAttribute("patients", patientService.getALL());
        return "patients/list";
    }

    /**
     * Affiche la fiche détaillée d'un patient, incluant :
     *  ses informations personnelles,
     *  ses notes médicales,
     *  son niveau de risque de diabète.
     * @param id identifiant du patient
     * @param model modèle pour la vue
     * @return le formulaire du patient en mode lecture seule
     */
    @GetMapping("/{id}")
    public String viewPatient(@PathVariable Long id, Model model) {
        model.addAttribute("patient", patientService.getById(id));
        model.addAttribute("notes", patientNoteService.getNotesByPatientId(id));
        model.addAttribute("diabetesRisk", patientDiabetesRiskService.getRiskLevelForPatient(id));
        model.addAttribute("readOnly", true);
        model.addAttribute("pageTitle", "Patient");
        return "patients/form";
    }

    /**
     * Affiche le formulaire de création d'un nouveau patient.
     * @param model modèle pour la vue
     * @return le formulaire du patient
     */
    @GetMapping("/form")
    public String showForm(Model model){
        model.addAttribute("patient", new PatientDto());
        model.addAttribute("readOnly", false);
        model.addAttribute("pageTitle", "Nouveau patient");
        return "patients/form";
    }

    /**
     * Traite la soumission du formulaire de création d'un patient.
     * @param patientDto données du patient à créer
     * @param result résultat de la validation
     * @param model modèle pour la vue
     * @return redirection vers la liste des patients ou retour au formulaire en cas d'erreur
     */
    @PostMapping("/form")
    public String add(@Valid @ModelAttribute("patient") PatientDto patientDto, BindingResult result, Model model){
        if (result.hasErrors()) {
            return "patients/form";
        }

        patientService.add(patientDto);
        return "redirect:/patients";
    }

    /**
     * Affiche le formulaire d'édition d'un patient existant.
     * @param id identifiant du patient
     * @param model modèle pour la vue
     * @return la vue <code>patients/form</code> en mode édition
     */
    @GetMapping("/{id}/form")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("patient", patientService.getById(id));
        model.addAttribute("readOnly", false);
        model.addAttribute("pageTitle", "Modifier le patient");
        return "patients/form";
    }

    /**
     * Traite la soumission du formulaire de modification d'un patient.
     * @param id identifiant du patient à mettre à jour
     * @param patientDto données modifiées
     * @param result résultat de la validation
     * @return redirection vers la liste des patients ou retour au formulaire en cas d'erreur
     */
    @PostMapping("/{id}/form")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("patient") PatientDto patientDto,
                         BindingResult result) {
        if (result.hasErrors()) {
            return "patients/form";
        }
        patientService.update(patientDto, id);
        return "redirect:/patients";
    }

    /**
     * Ajoute une nouvelle note médicale à un patient.
     * @param id identifiant du patient
     * @param note contenu de la note
     * @param lastName nom du patient (utilisé par le service de notes)
     * @return redirection vers la fiche du patient
     */
    @PostMapping("/{id}/notes")
    public String addNote(@PathVariable Long id, @RequestParam String note, @RequestParam String lastName){
        PatientNoteDto patientNoteDto = new PatientNoteDto();
        patientNoteDto.setPatId(id);
        patientNoteDto.setNote(note);
        patientNoteDto.setPatient(lastName);
        patientNoteService.add(patientNoteDto);
        return "redirect:/patients/" + id;
    }
}
