package keysson.apis.msgsend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MailQueueBackup {
    private String app;
    private String status;
    private String database;
    private String timestamp;
    private String filename;
    private String message;
    private String email;
}
