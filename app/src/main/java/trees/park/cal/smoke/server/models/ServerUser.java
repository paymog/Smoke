package trees.park.cal.smoke.server.models;

import org.apache.commons.lang3.Validate;

/**
 * Created by paymahn on 2014-08-20.
 */
public class ServerUser {

    private final String email;
    private final String password;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public ServerUser(String email, String password) {
        this.email = Validate.notBlank(email);
        this.password = Validate.notBlank(password);
    }
}
