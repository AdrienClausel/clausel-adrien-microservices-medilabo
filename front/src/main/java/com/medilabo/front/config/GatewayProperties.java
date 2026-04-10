package com.medilabo.front.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Propriétés de configuration du Gateway permettant de centraliser
 * les informations nécessaires à la construction des URLs des services externes.
 * Les informations sont récupérés depuis le fichier application.yml
 */
@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "gateway")
public class GatewayProperties {

    private String baseUrl;
    private String patientsPath;
    private String patientNotesPath;
    private String patientDiabetesRiskPath;

}
