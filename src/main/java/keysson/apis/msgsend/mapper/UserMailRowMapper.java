package keysson.apis.msgsend.mapper;

import keysson.apis.msgsend.model.UserMail;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserMailRowMapper implements RowMapper<UserMail> {

    @Override
    public UserMail mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserMail userMail = new UserMail();
        userMail.setId("company_id");
        userMail.setUsername("username");
        userMail.setEmail("email");

        return userMail;
    }
}
