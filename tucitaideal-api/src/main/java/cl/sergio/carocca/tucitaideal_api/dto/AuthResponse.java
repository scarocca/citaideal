package cl.sergio.carocca.tucitaideal_api.dto;

public class AuthResponse {
    private String username;
    private String rol;
    private String mensaje;

    public AuthResponse(String username, String rol, String mensaje) {
        this.username = username;
        this.rol = rol;
        this.mensaje = mensaje;
    }

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
    
}
