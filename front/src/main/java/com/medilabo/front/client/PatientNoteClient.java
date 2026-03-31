package com.medilabo.front.client;

import com.medilabo.front.config.GatewayProperties;
import com.medilabo.front.dto.PatientDto;
import com.medilabo.front.dto.PatientNoteDto;
import com.medilabo.front.interceptor.UserHeaderInterceptor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class PatientNoteClient implements IPatientNoteClient {

    private final RestClient restClient;
    private final GatewayProperties props;

    public PatientNoteClient(GatewayProperties props, UserHeaderInterceptor userHeaderInterceptor) {
        this.props = props;
        this.restClient = RestClient.builder()
                .baseUrl(props.getBaseUrl())
                .requestInterceptor(userHeaderInterceptor)
                .build();
    }

    @Override
    public List<PatientNoteDto> getNotesById(Long patientId) {
        return restClient.get()
                .uri(props.getPatientNotesPath() + "/patient/{id}", patientId)
                .retrieve()
                .body(new ParameterizedTypeReference<List<PatientNoteDto>>() {});
    }

    @Override
    public PatientNoteDto add(PatientNoteDto patientNoteDto) {
        return restClient.post()
                .uri(props.getPatientNotesPath())
                .contentType(MediaType.APPLICATION_JSON)
                .body(patientNoteDto)
                .retrieve()
                .body(PatientNoteDto.class);
    }
}
