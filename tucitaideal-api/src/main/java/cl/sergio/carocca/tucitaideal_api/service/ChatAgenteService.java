package cl.sergio.carocca.tucitaideal_api.service;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ChatAgenteService {

    private final ChatModel chatModel;

    @Autowired
    public ChatAgenteService(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public String generarRespuesta(String mensaje) {
        try {
            // 1. Definimos las instrucciones de personalidad (System Prompt)
            String instrucciones = """
                Eres Valentín, el asistente virtual experto de 'Tu Cita Ideal'. 
                Tu misión es ayudar a los clientes a elegir la mejor experiencia romántica. 
                Eres amable, elegante y siempre respondes con un toque de romance.
                """;

            // 2. Creamos la lista de mensajes (El rol del sistema y lo que dice el usuario)
            SystemMessage systemMessage = new SystemMessage(instrucciones);
            UserMessage userMessage = new UserMessage(mensaje);
            
            // 3. Construimos el Prompt
            Prompt prompt = new Prompt(List.of(systemMessage, userMessage));
            
            // 4. Llamamos al modelo y extraemos el contenido
            return chatModel.call(prompt).getResult().getOutput().getContent();

         // Dentro del catch de generarRespuesta
        } catch (Exception e) {
            System.err.println("Error de IA: " + e.getMessage());

            // Reemplaza '569XXXXXXXX' con tu número real
            String whatsappUrl = "https://wa.me/56986343735"; 
            
            return "¡Hola! Soy Valentín. En este momento no puedo procesar tu solicitud, pero Sergio está disponible para ayudarte. " + 
                   "Escríbenos aquí: " + whatsappUrl + " 💖";
        }
    }
} // <--- Asegúrate de que esta llave cierre la clase