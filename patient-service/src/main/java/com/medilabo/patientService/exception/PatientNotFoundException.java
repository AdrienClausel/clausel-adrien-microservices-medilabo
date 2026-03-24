package com.medilabo.patientService.exception;

public class PatientNotFoundException extends RuntimeException {
    public PatientNotFoundException(Long id) {
        super("Patient non trouvé avec l'id : " + id);
    }
}
