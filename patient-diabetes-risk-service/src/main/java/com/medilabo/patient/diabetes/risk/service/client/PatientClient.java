package com.medilabo.patient.diabetes.risk.service.client;

import com.medilabo.patient.diabetes.risk.service.config.GatewayProperties;
import com.medilabo.patient.diabetes.risk.service.dto.PatientDto;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class PatientClient implements IPatientClient {

    private final RestClient restClient;
    private final GatewayProperties props;

    public PatientClient(GatewayProperties props){
        this.restClient = RestClient.builder()
                .baseUrl(props.getBaseUrl())
                .build();
        this.props = props;
    }

    @Override
    public PatientDto getById(Long id) {
        return restClient.get()
                .uri(props.getPatientsPath() + "/{id}", id)
                .retrieve()
                .body(PatientDto.class);
    }
}
