package com.medilabo.front.client;

import com.medilabo.front.config.GatewayProperties;
import com.medilabo.front.constant.RiskLevel;
import com.medilabo.front.interceptor.UserHeaderInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class PatientDiabetesRiskClient implements IPatientDiabetesRiskClient{

    private final RestClient restClient;
    private final GatewayProperties props;

    public PatientDiabetesRiskClient(GatewayProperties props, UserHeaderInterceptor userHeaderInterceptor) {
        this.restClient = RestClient.builder()
                .baseUrl(props.getBaseUrl())
                .requestInterceptor(userHeaderInterceptor)
                .build();
        this.props = props;
    }

    @Override
    public RiskLevel getRiskLevelForPatient(Long id) {
        return restClient.get()
                .uri(props.getPatientDiabetesRiskPath() + "/{id}", id)
                .retrieve()
                .body(RiskLevel.class);
    }
}
