package com.medilabo.patientService.controller;

import com.medilabo.patientService.dto.PatientDto;
import com.medilabo.patientService.model.Patient;
import com.medilabo.patientService.service.IPatientService;
import com.medilabo.patientService.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@Controller
public class PatientController {

    @Autowired
    private IPatientService patientService;

    @GetMapping("/patient/{id}")
    public PatientDto getById(Long id) throws Exception {

        var patient = patientService.getById(id);

        return new PatientDto(
                patient.getFirstName(),
                patient.getLastName(),
                patient.getDateOfBirth(),
                patient.getGender(),
                patient.getPostalAddress(),
                patient.getPhoneNumber()
        );
    }

    @PutMapping("/patient/{id}")
    public void update(Patient patient, Long id) throws Exception {
        patientService.update(patient, id);
    }

    @PostMapping("/patient")
    public void add(Patient patient){
        patientService.add(patient);
    }

}
