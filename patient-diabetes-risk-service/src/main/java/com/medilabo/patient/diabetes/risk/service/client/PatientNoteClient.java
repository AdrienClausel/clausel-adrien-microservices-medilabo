package com.medilabo.patient.diabetes.risk.service.client;

import com.medilabo.patient.diabetes.risk.service.config.GatewayProperties;
import com.medilabo.patient.diabetes.risk.service.dto.PatientNoteDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class PatientNoteClient implements IPatientNoteClient {

    private final RestClient restClient;
    private final GatewayProperties props;

    public PatientNoteClient(GatewayProperties props) {
        this.props = props;
        this.restClient = RestClient.builder()
                .baseUrl(props.getBaseUrl())
                .build();
    }

    @Override
    public List<PatientNoteDto> getNotesById(Long patientId) {
        return restClient.get()
                .uri(props.getPatientNotesPath() + "/patient/{id}", patientId)
                .retrieve()
                .body(new ParameterizedTypeReference<List<PatientNoteDto>>() {});
    }
}