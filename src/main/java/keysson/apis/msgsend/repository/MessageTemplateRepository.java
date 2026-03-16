package keysson.apis.msgsend.repository;

import keysson.apis.msgsend.model.MessageTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MessageTemplateRepository extends JpaRepository<MessageTemplate, Integer> {
    Optional<MessageTemplate> findByTemplateCode(String templateCode);
}
