package com.medilabo.patient.diabetes.risk.service.client;

import com.medilabo.patient.diabetes.risk.service.config.ServiceProperties;
import com.medilabo.patient.diabetes.risk.service.dto.PatientDto;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class PatientClient implements IPatientClient {

    private final RestClient restClient;
    private final ServiceProperties props;

    public PatientClient(ServiceProperties props){
        this.restClient = RestClient.builder()
                .build();
        this.props = props;
    }

    @Override
    public PatientDto getById(Long id) {
        return restClient.get()
                .uri(props.getPatientsUrl() + "/patients/{id}", id)
                .retrieve()
                .body(PatientDto.class);
    }
}
