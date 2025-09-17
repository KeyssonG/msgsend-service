package keysson.apis.msgsend.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger logger = LoggerFactory.getLogger(EmailConsumer.class);

    private final EmailService emailService;
    private final ObjectMapper objectMapper;
    private final MsgRepository msgRepository;

    @RabbitListener(queues = "empresa.fila")
    public void processCompanyMessage(String messageJson) {
        try {
            MailQueueCompany request = objectMapper.readValue(messageJson, MailQueueCompany.class);
            emailService.sendCompanyEmail(request);
            logger.info("E-mail enviado para: {}", request.getEmail());
        } catch (Exception e) {
            logger.error("Falha ao enviar e-mail para empresa: {}", e.getMessage(), e);
        }
    }

    @RabbitListener(queues = "funcionario.fila")
    public void processEmployeeMessage(String messageJson) {
        try {
            MailQueueEmployee request = objectMapper.readValue(messageJson, MailQueueEmployee.class);
            emailService.sendEmployeeEmail(request);
            logger.info("E-mail enviado para: {}", request.getEmail());
        } catch (Exception e) {
            logger.error("Falha ao enviar e-mail para funcionário: {}", e.getMessage(), e);
        }
    }

    @RabbitListener(queues = "funcionario-cliente.fila")
    public void processEmployeeClientMessage(String messageJson) {
        try {
            MailQueueEmployeeClient request = objectMapper.readValue(messageJson, MailQueueEmployeeClient.class);
            emailService.sendEmployeeClientEmail(request);
            logger.info("E-mail enviado para: {}", request.getEmail());
        } catch (Exception e) {
            logger.error("Falha ao enviar e-mail para funcionário-cliente: {}", e.getMessage(), e);
        }
    }

    @RabbitListener(queues = "alteraStatus.fila")
    public void processStatusChangeMessage(String messageJson) {
        try {
            MailQueueChangeStatus request = objectMapper.readValue(messageJson, MailQueueChangeStatus.class);
            MailUser userMail = msgRepository.fetchUserMail(request.getNumeroConta());
            if (request.getNewStatus() == 2) {
                emailService.sendAccountActivationEmail(userMail);
            } else if (request.getNewStatus() == 3) {
                emailService.sendAccountRejectionEmail(userMail);
            }
            logger.info("E-mail enviado para: {}", userMail.getEmail());
        } catch (Exception e) {
            logger.error("Falha ao alterar status e enviar e-mail: {}", e.getMessage(), e);
        }
    }

    @RabbitListener(queues = "password.reset.queue")
    public void processSendTokenResetPassword(String messageJson) {
        try {
            MailQueueRequestPasswordChange request = objectMapper.readValue(messageJson, MailQueueRequestPasswordChange.class);
            emailService.sendTokenResetPassword(request);
            logger.info("E-mail com Token para redefinição de senha enviado para: {}", request.getEmail());
        } catch (Exception e) {
            logger.error("Falha ao enviar e-mail com Token para redefinição de senha: {}", e.getMessage(), e);
        }
    }
}