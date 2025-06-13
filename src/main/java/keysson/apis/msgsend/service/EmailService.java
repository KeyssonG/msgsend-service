package keysson.apis.msgsend.service;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import keysson.apis.msgsend.model.SendMailQueueEmpresa;
import keysson.apis.msgsend.model.SendMailQueueFuncionario;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendEmailEmpresa(SendMailQueueEmpresa request) throws MessagingException {
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

    public void sendEmailFuncionario(SendMailQueueFuncionario request) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        String subject = "Bem-vindo(a), " + request.getUsername() + "!";
        String text = String.format("""
                Olá %s,
                
                
                Seja bem vindo(a) ao time Multithread!
                
                A sua conta já está ativa, para fazer o seu login, acesse o link abaixo:
                
                http://localhost:31008/login
                
                Utilize o seu login e senha.
                
                Atenciosamente,
                Equipe da Multithread
                """, request.getUsername());

        helper.setTo(request.getEmail());
        helper.setSubject(subject);
        helper.setText(text, false);

        mailSender.send(message);

    }
}
