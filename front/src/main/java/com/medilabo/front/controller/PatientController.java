package com.medilabo.front.controller;


import com.medilabo.front.dto.PatientDto;
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

    @GetMapping()
    public String getAll(Model model){
        model.addAttribute("patients", patientService.getALL());
        return "patients/list";
    }

    @GetMapping("/form")
    public String showForm(Model model){
        model.addAttribute("patient", new PatientDto());
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

}
