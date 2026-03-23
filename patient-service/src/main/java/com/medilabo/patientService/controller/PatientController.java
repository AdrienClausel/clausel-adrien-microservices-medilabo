package com.medilabo.patientService.controller;

import com.medilabo.patientService.dto.PatientDto;
import com.medilabo.patientService.mapper.IPatientMapper;
import com.medilabo.patientService.model.Patient;
import com.medilabo.patientService.service.IPatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {

    @Autowired
    private IPatientService patientService;

    @Autowired
    private IPatientMapper patientMapper;

    @GetMapping()
    public List<PatientDto> getAll(){
        return patientMapper.toDTOList(patientService.getAll());
    }

    @GetMapping("/{id}")
    public PatientDto getById(@PathVariable Long id) throws Exception {
        var patient = patientService.getById(id);
        return patientMapper.toDTO(patient);
    }

    @PutMapping("/{id}")
    public PatientDto update(@PathVariable Long id, @RequestBody PatientDto patientDto) throws Exception {
        var patient = patientService.update(patientMapper.toEntity(patientDto), id);
        return patientMapper.toDTO(patient);
    }

    @PostMapping()
    public PatientDto add(@RequestBody PatientDto patientDto){
        var patient = patientService.add(patientMapper.toEntity(patientDto));
        return patientMapper.toDTO(patient);
    }

}
