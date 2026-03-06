package cl.sergio.carocca.tucitaideal_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage; // Asegúrate de usar jakarta.mail

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // AHORA RECIBE 5 PARÁMETROS
    public void enviarCorreoConfirmacion(String destinatario, String nombreCliente, String nombrePlan, String fechaCita, String codigo) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("scarocca@gmail.com"); 
            helper.setTo(destinatario);
            helper.setSubject("✨ ¡Tu Reserva en CitaIdeal.cl ha sido Confirmada! ✨");

            String contenidoHtml = 
                "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: auto; border: 1px solid #f0f0f0; border-radius: 15px; overflow: hidden;'>" +
                "  <div style='background-color: #e91e63; padding: 20px; text-align: center;'>" +
                "    <h1 style='color: white; margin: 0;'>CitaIdeal.cl</h1>" +
                "  </div>" +
                "  <div style='padding: 30px; line-height: 1.6; color: #333;'>" +
                "    <h2 style='color: #e91e63;'>¡Hola, " + nombreCliente + "!</h2>" +
                "    <p>Tu reserva para la experiencia <strong>" + nombrePlan + "</strong> ha sido confirmada.</p>" +
                "    <div style='background-color: #fff0f5; border-radius: 10px; padding: 20px; margin: 20px 0; border-left: 6px solid #e91e63;'>" +
                "      <p style='margin: 5px 0;'><strong>📅 Fecha y Hora:</strong> " + fechaCita + "</p>" +
                "      <p style='margin: 5px 0;'><strong>🔑 Código:</strong> " + codigo + "</p>" +
                "    </div>" +
                "    <p>¡Estamos preparando todo para tu momento mágico!</p>" +
                "  </div>" +
                "</div>";

            helper.setText(contenidoHtml, true);
            mailSender.send(message);
            System.out.println("✅ Correo enviado con éxito a: " + destinatario);
            
        } catch (Exception e) {
            System.err.println("Error al enviar el correo HTML: " + e.getMessage());
            e.printStackTrace();
        }
    }
}