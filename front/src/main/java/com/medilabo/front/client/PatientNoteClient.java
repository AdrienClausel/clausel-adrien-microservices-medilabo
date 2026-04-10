package com.medilabo.front.client;

import com.medilabo.front.config.GatewayProperties;
import com.medilabo.front.dto.PatientNoteDto;
import com.medilabo.front.interceptor.JwtHeaderInterceptor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

/**
 * Client REST permettant de communiquer avec le service de gestion des notes patients.
 * Utilise l'intercepteur pour insérer le header et passer le token jwt
 */
@Component
public class PatientNoteClient implements IPatientNoteClient {

    private final RestClient restClient;
    private final GatewayProperties props;

    /**
     * Construit un client REST configuré pour communiquer avec le service des notes patients.
     *
     * @param props                propriétés contenant l'URL de base et les chemins d'API
     * @param jwtHeaderInterceptor intercepteur ajoutant le header Authorization (JWT)
     */
    public PatientNoteClient(GatewayProperties props, JwtHeaderInterceptor jwtHeaderInterceptor) {
        this.props = props;
        this.restClient = RestClient.builder()
                .baseUrl(props.getBaseUrl())
                .requestInterceptor(jwtHeaderInterceptor)
                .build();
    }

    /**
     * Récupère toutes les notes associées à un patient donné.
     *
     * @param patientId identifiant du patient
     * @return une liste des notes du patient
     */
    @Override
    public List<PatientNoteDto> getNotesById(Long patientId) {
        return restClient.get()
                .uri(props.getPatientNotesPath() + "/patient/{id}", patientId)
                .retrieve()
                .body(new ParameterizedTypeReference<List<PatientNoteDto>>() {});
    }

    /**
     * Ajoute une nouvelle note pour un patient.
     *
     * @param patientNoteDto données de la note à créer
     * @return la note créée
     */
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
