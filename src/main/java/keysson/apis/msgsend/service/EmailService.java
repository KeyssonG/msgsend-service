package keysson.apis.msgsend.service;

import keysson.apis.msgsend.model.*;
import keysson.apis.msgsend.repository.MessageTemplateRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final MessageTemplateRepository templateRepository;

    @Cacheable(value = "emailTemplates", key = "#code")
    public MessageTemplate getTemplate(String code) {
        return templateRepository.findByTemplateCode(code)
                .orElseThrow(() -> new RuntimeException("Template de email não encontrado: " + code));
    }

    private void sendEmail(String to, String subjectFormat, String bodyFormat, Object... args) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        String subject = subjectFormat.contains("%s") ? String.format(subjectFormat, args[0]) : subjectFormat;

        String text = String.format(bodyFormat, args);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, false);

        mailSender.send(message);
    }

    public void sendCompanyEmail(MailQueueCompany request) throws MessagingException {
        MessageTemplate template = getTemplate("COMPANY_WELCOME_PENDING");
        sendEmail(request.getEmail(), template.getSubjectTemplate(), template.getBodyTemplate(), request.getName());
    }

    public void sendEmployeeEmail(MailQueueEmployee request) throws MessagingException {
        MessageTemplate template = getTemplate("EMPLOYEE_WELCOME");
        sendEmail(request.getEmail(), template.getSubjectTemplate(), template.getBodyTemplate(),
                request.getUsername(), request.getUsername(), request.getPlainPassword());

    }

    public void sendEmployeeClientEmail(MailQueueEmployeeClient request) throws MessagingException {
        MessageTemplate template = getTemplate("EMPLOYEE_CLIENT_WELCOME");
        sendEmail(request.getEmail(), template.getSubjectTemplate(), template.getBodyTemplate(),
                request.getUsername(), request.getUsername(), request.getPlainPassword(), request.getIdEmpresa());
    }

    public void sendAccountActivationEmail(MailUser userMail) throws MessagingException {
        MessageTemplate template = getTemplate("ACCOUNT_ACTIVATION");
        sendEmail(userMail.getEmail(), template.getSubjectTemplate(), template.getBodyTemplate(),
                userMail.getUsername(), userMail.getId());
    }

    public void sendAccountRejectionEmail(MailUser userMail) throws MessagingException {
       MessageTemplate template = getTemplate("ACCOUNT_REJECTION");
       sendEmail(userMail.getEmail(), template.getSubjectTemplate(), template.getBodyTemplate(), userMail.getId());
    }

    public void sendTokenResetPassword(MailQueueRequestPasswordChange request) throws MessagingException {
      MessageTemplate template = getTemplate("PASSWORD_RESET");
      sendEmail(request.getEmail(), template.getSubjectTemplate(), template.getBodyTemplate(),
              request.getUsername(), request.getToken());
    }
}
