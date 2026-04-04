package com.medilabo.patient.diabetes.risk.service.client;

import com.medilabo.patient.diabetes.risk.service.config.ServiceProperties;
import com.medilabo.patient.diabetes.risk.service.dto.PatientNoteDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class PatientNoteClient implements IPatientNoteClient {

    private final RestClient restClient;
    private final ServiceProperties props;

    public PatientNoteClient(ServiceProperties props) {
        this.props = props;
        this.restClient = RestClient.builder()
                .build();
    }

    @Override
    public List<PatientNoteDto> getNotesById(Long patientId) {
        return restClient.get()
                .uri(props.getPatientNotesUrl() + "/patient-notes/patient/{id}", patientId)
                .retrieve()
                .body(new ParameterizedTypeReference<List<PatientNoteDto>>() {});
    }
}