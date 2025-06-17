package keysson.apis.msgsend.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendMailQueueFuncionario {

    private String email;
    private String username;
    private String password;
}
