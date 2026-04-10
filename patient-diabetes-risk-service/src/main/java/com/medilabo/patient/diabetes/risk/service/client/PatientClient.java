package com.medilabo.patient.diabetes.risk.service.client;

import com.medilabo.patient.diabetes.risk.service.config.ServiceProperties;
import com.medilabo.patient.diabetes.risk.service.dto.PatientDto;
import com.medilabo.patient.diabetes.risk.service.interceptor.BearerTokenRelayInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/**
 * Client REST responsable de la communication avec le service Patient.
 * Ce client utilise RestClient pour effectuer des appels HTTP vers
 * l'API distante, en relayant automatiquement le token JWT grâce à
 * BearerTokenRelayInterceptor.
 * Les URLs du service Patient sont fournies par ServiceProperties
 */
@Component
public class PatientClient implements IPatientClient {

    private final RestClient restClient;
    private final ServiceProperties props;

    /**
     * Constructeur injectant les propriétés du service et l'intercepteur
     * permettant de relayer le token JWT dans les requêtes sortantes.
     * @param props configuration contenant l'URL du service Patient
     * @param bearerTokenRelayInterceptor intercepteur ajoutant le header Authorization
     */
    public PatientClient(ServiceProperties props, BearerTokenRelayInterceptor bearerTokenRelayInterceptor){
        this.restClient = RestClient.builder()
                .requestInterceptor(bearerTokenRelayInterceptor)
                .build();
        this.props = props;
    }

    /**
     * Récupère un patient à partir de son identifiant en appelant le service Patient.
     * @param id identifiant du patient
     * @return un patient correspondant au patient trouvé
     */
    @Override
    public PatientDto getById(Long id) {
        return restClient.get()
                .uri(props.getPatientsUrl() + "/patients/{id}", id)
                .retrieve()
                .body(PatientDto.class);
    }
}
