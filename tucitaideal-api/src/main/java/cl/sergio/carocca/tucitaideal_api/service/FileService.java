package cl.sergio.carocca.tucitaideal_api.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

@Service
public class FileService {

    private final Cloudinary cloudinary;

    // Spring inyecta automáticamente los valores desde application.properties
    public FileService(
            @Value("${cloudinary.cloud_name}") String cloudName,
            @Value("${cloudinary.api_key}") String apiKey,
            @Value("${cloudinary.api_secret}") String apiSecret) {
        
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", cloudName,
            "api_key", apiKey,
            "api_secret", apiSecret,
            "secure", true
        ));
    }

    /**
     * Sube la imagen a Cloudinary en lugar de guardarla en el disco local.
     * @return La URL segura (https) de la imagen en la nube.
     */
    public String guardarArchivo(MultipartFile archivo, String subCarpeta) {
        try {
            if (archivo == null || archivo.isEmpty()) return null;

            // Subimos a Cloudinary dentro de una carpeta organizada para CitaIdeal
            Map uploadResult = cloudinary.uploader().upload(archivo.getBytes(), 
                ObjectUtils.asMap(
                    "folder", "cita_ideal/" + subCarpeta,
                    "resource_type", "auto"
                ));

            // Retornamos la URL pública que nos da Cloudinary (ej: https://res.cloudinary.com/...)
            return uploadResult.get("secure_url").toString();
        } catch (Exception e) {
            System.err.println("Error al subir a Cloudinary: " + e.getMessage());
            return null;
        }
    }

    /**
     * Para eliminar en Cloudinary se necesita el "Public ID". 
     * Por ahora, este método imprimirá la URL para que no te de error el PlanService.
     */
    public void eliminarArchivo(String urlPublica) {
        System.out.println("Solicitud de eliminación para Cloudinary (URL): " + urlPublica);
        // Nota: En el futuro podemos extraer el ID de la URL para borrarlo físicamente en la nube.
    }
}