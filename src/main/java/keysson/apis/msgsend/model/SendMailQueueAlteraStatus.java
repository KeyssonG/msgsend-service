package keysson.apis.msgsend.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendMailQueueAlteraStatus {

    private int numeroConta;
    private int newStatus;
}
