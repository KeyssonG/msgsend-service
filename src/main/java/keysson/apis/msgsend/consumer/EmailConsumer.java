package keysson.apis.msgsend.consumer;


import com.fasterxml.jackson.databind.ObjectMapper;
import keysson.apis.msgsend.model.SendMailQueueEmpresa;
import keysson.apis.msgsend.model.SendMailQueueFuncionario;
import keysson.apis.msgsend.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor 
public class EmailConsumer {

    private final EmailService emailService;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = "empresa.fila")
    public void processMessageEmpresa(String messageJson) {
        try {
            SendMailQueueEmpresa request = objectMapper.readValue(messageJson, SendMailQueueEmpresa.class);
           emailService.sendEmailEmpresa(request);
            System.out.println("E-mail enviado para: " + request.getEmail());
        } catch (Exception e) {
            System.err.println("Erro ao processar mensagem: " + e.getMessage());
        }
    }

    @RabbitListener(queues = "funcionario.fila")
    public void processMessageFuncionario(String messageJson) {
        try {
            SendMailQueueFuncionario request = objectMapper.readValue(messageJson, SendMailQueueFuncionario.class);
            emailService.sendEmail(request);
            System.out.println("E-mail enviado para: " + request.getEmail());
        } catch (Exception e) {
            System.err.println("Erro ao processar mensagem: " + e.getMessage());
        }
    }
}