package keysson.apis.msgsend.consumer;


import keysson.apis.msgsend.model.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import keysson.apis.msgsend.repository.MsgRepository;
import keysson.apis.msgsend.service.EmailService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor 
public class EmailConsumer {

    private final EmailService emailService;
    private final ObjectMapper objectMapper;
    private final MsgRepository msgRepository;

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
            emailService.sendEmailFuncionario(request);
            System.out.println("E-mail enviado para: " + request.getEmail());
        } catch (Exception e) {
            System.err.println("Erro ao processar mensagem: " + e.getMessage());
        }
    }

    @RabbitListener(queues = "funcionario-cliente.fila")
    public void processMessageFuncionarioCliente(String messageJson) {
        try {
            SendMailQueueFuncionarioCliente request = objectMapper.readValue(messageJson, SendMailQueueFuncionarioCliente.class);
            emailService.sendEmailFuncionarioCliente(request);
            System.out.println("E-mail enviado para: " + request.getEmail());
        } catch (Exception e) {
            System.err.println("Erro ao processar mensagem: " + e.getMessage());
        }
    }

    @RabbitListener(queues = "alteraStatus.fila")
    public void processMessageAlteraStatus(String messageJson) {
        try {
            SendMailQueueAlteraStatus request = objectMapper.readValue(messageJson, SendMailQueueAlteraStatus.class);

            UserMail userMail = msgRepository.getUserMail(request.getNumeroConta());

            switch (request.getNewStatus()) {
                case 2:
                    emailService.sendEmailAtivaConta(userMail);
                case 3: 
                    emailService.sendEmailContaRejeitada(userMail);
            }

            System.out.println("E-mail enviado para: " + userMail.getEmail());
        } catch (Exception e) {
            System.err.println("Erro ao processar mensagem: " + e.getMessage());
        }
    }
}