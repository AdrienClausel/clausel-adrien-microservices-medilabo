package com.medilabo.patient.diabetes.risk.service.client;

import com.medilabo.patient.diabetes.risk.service.config.ServiceProperties;
import com.medilabo.patient.diabetes.risk.service.dto.PatientNoteDto;
import com.medilabo.patient.diabetes.risk.service.interceptor.BearerTokenRelayInterceptor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

/**
 * Client REST responsable de la communication avec le service de gestion des notes des patients.
 * Ce client utilise RestClient pour effectuer des appels HTTP vers l'API
 * distante, tout en relayant automatiquement le token JWT grâce au
 * BearerTokenRelayInterceptor.
 * Les URLs du service sont fournies par ServiceProperties.
 */
@Component
public class PatientNoteClient implements IPatientNoteClient {

    private final RestClient restClient;
    private final ServiceProperties props;

    /**
     * Constructeur injectant les propriétés du service et l'intercepteur chargé
     * d'ajouter le header Authorization aux requêtes sortantes.
     * @param props configuration contenant l'URL du service Patient Notes
     * @param bearerTokenRelayInterceptor intercepteur ajoutant le token JWT aux requêtes
     */
    public PatientNoteClient(ServiceProperties props, BearerTokenRelayInterceptor bearerTokenRelayInterceptor) {
        this.props = props;
        this.restClient = RestClient.builder()
                .requestInterceptor(bearerTokenRelayInterceptor)
                .build();
    }

    /**
     * Récupère toutes les notes associées à un patient donné.
     * @param patientId identifiant du patient
     * @return une liste des notes du patient
     */
    @Override
    public List<PatientNoteDto> getNotesById(Long patientId) {
        return restClient.get()
                .uri(props.getPatientNotesUrl() + "/patient-notes/patient/{id}", patientId)
                .retrieve()
                .body(new ParameterizedTypeReference<List<PatientNoteDto>>() {});
    }
}