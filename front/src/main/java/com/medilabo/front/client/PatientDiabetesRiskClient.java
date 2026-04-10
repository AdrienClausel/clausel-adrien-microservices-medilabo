package com.medilabo.front.client;

import com.medilabo.front.config.GatewayProperties;
import com.medilabo.front.constant.RiskLevel;
import com.medilabo.front.interceptor.JwtHeaderInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/**
 * Client REST permettant de communiquer avec le service d'évaluation du risque de diabète.
 * Utilise l'intercepteur pour insérer le header et passer le token jwt
 */
@Component
public class PatientDiabetesRiskClient implements IPatientDiabetesRiskClient{

    private final RestClient restClient;
    private final GatewayProperties props;

    /**
     * Construit un client REST configuré pour communiquer avec le service de risque diabète.
     *
     * @param props                propriétés contenant l'URL de base et les chemins d'API
     * @param jwtHeaderInterceptor intercepteur ajoutant le header Authorization (JWT)
     */
    public PatientDiabetesRiskClient(GatewayProperties props, JwtHeaderInterceptor jwtHeaderInterceptor) {
        this.restClient = RestClient.builder()
                .baseUrl(props.getBaseUrl())
                .requestInterceptor(jwtHeaderInterceptor)
                .build();
        this.props = props;
    }

    /**
     * Récupère le niveau de risque de diabète pour un patient donné.
     *
     * @param id identifiant du patient
     * @return le niveau de risque
     */
    @Override
    public RiskLevel getRiskLevelForPatient(Long id) {
        return restClient.get()
                .uri(props.getPatientDiabetesRiskPath() + "/{id}", id)
                .retrieve()
                .body(RiskLevel.class);
    }
}
