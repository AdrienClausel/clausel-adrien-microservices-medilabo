package com.medilabo.patientService.controller;

import com.medilabo.patientService.dto.PatientDto;
import com.medilabo.patientService.mapper.IPatientMapper;
import com.medilabo.patientService.service.IPatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class PatientController {

    @Autowired
    private IPatientService patientService;

    @Autowired
    private IPatientMapper patientMapper;

    @GetMapping("/patients")
    public List<PatientDto> getAll(){
        return patientMapper.toDTOList(patientService.getAll());
    }

    @GetMapping("/patient/{id}")
    public PatientDto getById(@PathVariable Long id) throws Exception {

        var patient = patientService.getById(id);

        return patientMapper.toDTO(patient);
    }

    @PutMapping("/patient/{id}")
    public void update(@PathVariable Long id, @RequestBody PatientDto patientDto) throws Exception {
        patientService.update(patientMapper.toEntity(patientDto), id);
    }

    @PostMapping("/patient")
    public void add(@RequestBody PatientDto patientDto){
        patientService.add(patientMapper.toEntity(patientDto));
    }

}
