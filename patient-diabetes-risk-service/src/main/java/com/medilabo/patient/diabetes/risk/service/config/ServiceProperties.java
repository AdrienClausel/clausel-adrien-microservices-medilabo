package com.medilabo.patient.diabetes.risk.service.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "services")
public class ServiceProperties {

    private String patientsUrl;
    private String patientNotesUrl;

}
