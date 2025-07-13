package keysson.apis.msgsend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendMailQueueFuncionarioCliente {

    private String email;
    private String username;
    private String plainPassword;
    private int idEmpresa;
}

