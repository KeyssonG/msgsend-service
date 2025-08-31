package keysson.apis.msgsend.mapper;

import keysson.apis.msgsend.model.MailUser;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserMailRowMapper implements RowMapper<MailUser> {

    @Override
    public MailUser mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        MailUser userMail = new MailUser();
        userMail.setId(resultSet.getInt("company_id"));
        userMail.setUsername(resultSet.getString("username"));
        userMail.setEmail(resultSet.getString("email"));
        return userMail;
    }
}
