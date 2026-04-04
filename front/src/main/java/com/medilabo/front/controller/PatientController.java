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

@Controller
@RequestMapping("/patients")
public class PatientController {

    @Autowired
    private IPatientService patientService;

    @Autowired
    private IPatientNoteService patientNoteService;

    @Autowired
    private IPatientDiabetesRiskService patientDiabetesRiskService;

    @GetMapping()
    public String getAll(Model model){
        model.addAttribute("patients", patientService.getALL());
        return "patients/list";
    }

    @GetMapping("/{id}")
    public String viewPatient(@PathVariable Long id, Model model) {
        model.addAttribute("patient", patientService.getById(id));
        model.addAttribute("notes", patientNoteService.getNotesByPatientId(id));
        model.addAttribute("diabetesRisk", patientDiabetesRiskService.getRiskLevelForPatient(id));
        model.addAttribute("readOnly", true);
        model.addAttribute("pageTitle", "Patient");
        return "patients/form";
    }

    @GetMapping("/form")
    public String showForm(Model model){
        model.addAttribute("patient", new PatientDto());
        model.addAttribute("readOnly", false);
        model.addAttribute("pageTitle", "Nouveau patient");
        return "patients/form";
    }

    @PostMapping("/form")
    public String add(@Valid @ModelAttribute("patient") PatientDto patientDto, BindingResult result, Model model){
        if (result.hasErrors()) {
            return "patients/form";
        }

        patientService.add(patientDto);
        return "redirect:/patients";
    }

    @GetMapping("/{id}/form")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("patient", patientService.getById(id));
        model.addAttribute("readOnly", false);
        model.addAttribute("pageTitle", "Modifier le patient");
        return "patients/form";
    }

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
