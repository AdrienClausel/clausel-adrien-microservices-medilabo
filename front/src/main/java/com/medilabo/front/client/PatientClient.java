package com.medilabo.front.client;

import com.medilabo.front.config.GatewayProperties;
import com.medilabo.front.dto.PatientDto;
import com.medilabo.front.interceptor.JwtHeaderInterceptor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import java.util.List;

/**
 * Client REST permettant de communiquer avec le service Patient via le Gateway.
 * Utilise l'intercepteur pour insérer le header et passer le token jwt
 */
@Component
public class PatientClient implements IPatientClient {

    private final RestClient restClient;
    private final GatewayProperties props;

    /**
     * Construit un client REST configuré pour communiquer avec le service Patient.
     *
     * @param props                propriétés contenant l'URL de base et les chemins d'API
     * @param jwtHeaderInterceptor intercepteur ajoutant le header Authorization (JWT)
     */
    public PatientClient(GatewayProperties props, JwtHeaderInterceptor jwtHeaderInterceptor){
        this.restClient = RestClient.builder()
                .baseUrl(props.getBaseUrl())
                .requestInterceptor(jwtHeaderInterceptor)
                .build();
        this.props = props;
    }

    /**
     * Récupère la liste complète des patients depuis le service Patient.
     *
     * @return une liste de patient
     */
    @Override
    public List<PatientDto> getAll() {
        return restClient.get()
                .uri(props.getPatientsPath())
                .retrieve()
                .body(new ParameterizedTypeReference<List<PatientDto>>() {});
    }

    /**
     * Récupère un patient par son identifiant.
     *
     * @param id identifiant du patient
     * @return le patient
     */
    @Override
    public PatientDto getById(Long id) {
        return restClient.get()
                .uri(props.getPatientsPath() + "/{id}", id)
                .retrieve()
                .body(PatientDto.class);
    }

    /**
     * Ajoute un nouveau patient dans le service Patient.
     *
     * @param patientDto données du patient à créer
     * @return le patient créé
     */
    @Override
    public PatientDto add(PatientDto patientDto) {
        return restClient.post()
                .uri(props.getPatientsPath())
                .contentType(MediaType.APPLICATION_JSON)
                .body(patientDto)
                .retrieve()
                .body(PatientDto.class);
    }

    /**
     * Met à jour un patient existant.
     *
     * @param patientDto nouvelles données du patient
     * @param id         identifiant du patient à mettre à jour
     * @return le patient mis à jour
     */
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
