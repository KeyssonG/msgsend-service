package keysson.apis.msgsend.service;

import keysson.apis.msgsend.model.MailQueueEmployeeClient;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import keysson.apis.msgsend.model.MailQueueCompany;
import keysson.apis.msgsend.model.MailQueueEmployee;
import keysson.apis.msgsend.model.MailUser;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendCompanyEmail(MailQueueCompany request) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        String subject = "Bem-vindo(a), " + request.getName() + "!";
        String text = String.format("""
                Olá %s,

                Seja bem vindo(a) ao Multithread!

                A sua conta neste momento está pendente passando por uma breve análise, 
                logo você receberá um e-mail com mais informações para login.

                Atenciosamente,
                Equipe da Multithread
                """, request.getName());

        helper.setTo(request.getEmail());
        helper.setSubject(subject);
        helper.setText(text, false);

        mailSender.send(message);

    }

    public void sendEmployeeEmail(MailQueueEmployee request) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        String subject = "Bem-vindo(a), " + request.getUsername() + "!";
        String text = String.format("""
                Olá %s,

               Seja bem vindo(a) ao Multithread!

                Para acessar a plataforma, clique no link abaixo:

                http://localhost:31008/login

                Para realizar o primeiro login, use o seu username: %s e senha provisória: %s

                Você deve definir uma nova senha no seu primeiro acesso.

                Atenciosamente,
                Equipe da Multithread
                """, request.getUsername(), request.getUsername(), request.getPlainPassword());

        helper.setTo(request.getEmail());
        helper.setSubject(subject);
        helper.setText(text, false);

        mailSender.send(message);

    }

    public void sendEmployeeClientEmail(MailQueueEmployeeClient request) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        String subject = "Bem-vindo(a), " + request.getUsername() + "!";
        String text = String.format("""
                Olá %s,

               Seja bem vindo(a) ao Multithread!

                Para acessar a plataforma, clique no link abaixo:

                http://localhost:31006/login

                Para realizar o primeiro login, use o seu username: %s e senha provisória: %s e o ID da sua empresa: %s

                Você deve definir uma nova senha no seu primeiro acesso.

                Atenciosamente,
                Equipe da Multithread
                """, request.getUsername(), request.getUsername(), request.getPlainPassword(), request.getIdEmpresa());

        helper.setTo(request.getEmail());
        helper.setSubject(subject);
        helper.setText(text, false);

        mailSender.send(message);

    }

    public void sendAccountActivationEmail(MailUser userMail) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        String subject = "Bem-vindo(a), " + userMail.getUsername() + "!";
        String text = String.format("""
            Olá %s,

            Seja bem-vindo(a) ao time Multithread!

            Sua conta já está cadastrada. Para fazer login, acesse o link abaixo:

            http://localhost:31006/login

            Utilize seu login, senha e o ID da sua empresa: %s

            Atenciosamente,
            Equipe Multithread
            """, userMail.getUsername(), userMail.getId());

        helper.setTo(userMail.getEmail());
        helper.setSubject(subject);
        helper.setText(text, false);

        mailSender.send(message);

    }

    public void sendAccountRejectionEmail(MailUser userMail) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        String subject = "Conta não aprovada";
        String text = String.format("""
            Olá %s,

            Sua conta não foi aprovada neste momento.
            Para mais informações, entre em contato com a equipe Multithread.

            Atenciosamente,
            Equipe Multithread
            """, userMail.getUsername());

        helper.setTo(userMail.getEmail());
        helper.setSubject(subject);
        helper.setText(text, false);

        mailSender.send(message);
    }
}
