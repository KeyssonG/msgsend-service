package keysson.apis.msgsend.service;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import keysson.apis.msgsend.model.EmailRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

        private final JavaMailSender mailSender;

        public void sendEmail(EmailRequest request) throws MessagingException, MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        String subject= "Bem-vindo(a), " + request.getName() + "!";
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
}
