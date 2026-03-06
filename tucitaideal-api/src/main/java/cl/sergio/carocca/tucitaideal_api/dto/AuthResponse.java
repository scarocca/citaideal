package cl.sergio.carocca.tucitaideal_api.dto;

public class AuthResponse {
    private String email;
    private String mensaje;

    // Constructor simplificado sin ROL
    public AuthResponse(String email, String mensaje) {
        this.email = email;
        this.mensaje = mensaje;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}