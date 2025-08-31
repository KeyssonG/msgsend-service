package keysson.apis.msgsend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MailQueueCompany {
    private Integer idEmpresa;
    private String name;
    private String email;
    private String cnpj;
    private String username;
}
