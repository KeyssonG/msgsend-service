package keysson.apis.msgsend.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MailQueueRequestPasswordChange {

    private String email;
    private String token;
    private String username;
}
