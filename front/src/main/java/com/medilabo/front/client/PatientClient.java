package com.medilabo.front.client;

import com.medilabo.front.config.GatewayProperties;
import com.medilabo.front.dto.PatientDto;
import com.medilabo.front.interceptor.UserHeaderInterceptor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import java.util.List;

@Component
public class PatientClient implements IPatientClient {

    private final RestClient restClient;
    private final GatewayProperties props;

    public PatientClient(GatewayProperties props, UserHeaderInterceptor userHeaderInterceptor){
        this.restClient = RestClient.builder()
                .baseUrl(props.getBaseUrl())
                .requestInterceptor(userHeaderInterceptor)
                .build();
        this.props = props;
    }

    @Override
    public List<PatientDto> getAll() {
        return restClient.get()
                .uri(props.getPatientsPath())
                .retrieve()
                .body(new ParameterizedTypeReference<List<PatientDto>>() {});
    }

    @Override
    public PatientDto getById(Long id) {
        return restClient.get()
                .uri(props.getPatientsPath() + "/{id}", id)
                .retrieve()
                .body(PatientDto.class);
    }

    @Override
    public PatientDto add(PatientDto patientDto) {
        return restClient.post()
                .uri(props.getPatientsPath())
                .contentType(MediaType.APPLICATION_JSON)
                .body(patientDto)
                .retrieve()
                .body(PatientDto.class);
    }

    @Override
    public PatientDto update(PatientDto patientDto, Long id) {
        return restClient.put()
                .uri(props.getPatientsPath() + "/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .body(patientDto)
                .retrieve()
                .body(PatientDto.class);
    }
}
