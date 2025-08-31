package keysson.apis.msgsend.repository;


import keysson.apis.msgsend.mapper.UserMailRowMapper;
import keysson.apis.msgsend.model.MailUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MsgRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    private UserMailRowMapper userMailRowMapper;


    private static final String FIND_USER_MAIL = """
            SELECT
              c.id AS company_id,
              u.username,
              ct.email
            FROM companies c
            LEFT JOIN users u ON c.id = u.company_id
            LEFT JOIN contatos ct ON c.id = ct.company_id
            WHERE c.numero_conta = ?
            """;

    public MailUser fetchUserMail(int accountNumber) {
        return jdbcTemplate.query(FIND_USER_MAIL, new Object[]{accountNumber}, rs -> {
            if (rs.next()) {
                return userMailRowMapper.mapRow(rs, 1);
            }
            return null;
        });
    }
}
