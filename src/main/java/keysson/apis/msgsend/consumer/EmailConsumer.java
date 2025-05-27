package keysson.apis.msgsend.consumer;


import com.fasterxml.jackson.databind.ObjectMapper;
import keysson.apis.msgsend.model.EmailRequest;
import keysson.apis.msgsend.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor // Injeção automática via construtor (Lombok)
public class EmailConsumer {

    private final EmailService emailService;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = "empresa.fila")
    public void processMessage(String messageJson) {
        try {
            EmailRequest request = objectMapper.readValue(messageJson, EmailRequest.class);
           emailService.sendEmail(request);
            System.out.println("E-mail enviado para: " + request.getEmail());
        } catch (Exception e) {
            System.err.println("Erro ao processar mensagem: " + e.getMessage());
        }
    }
}