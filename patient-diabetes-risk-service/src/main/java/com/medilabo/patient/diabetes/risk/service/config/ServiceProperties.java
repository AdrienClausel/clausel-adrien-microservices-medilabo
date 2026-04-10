package com.medilabo.patient.diabetes.risk.service.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Propriétés de configuration permettant de centraliser les URLs des services externes.
 * Cette classe est alimentée automatiquement par Spring grâce à
 * ConfigurationProperties, en lisant les valeurs définies dans le fichier
 * de configuration application.yml sous le préfixe services.
 */
@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "services")
public class ServiceProperties {

    private String patientsUrl;
    private String patientNotesUrl;

}
