package cl.sergio.carocca.tucitaideal_api.util;

import java.util.UUID;

public class GeneradorCodigo {

    public static String generar() {
        // Genera un código corto de 8 caracteres basado en UUID
        return "CITA-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}