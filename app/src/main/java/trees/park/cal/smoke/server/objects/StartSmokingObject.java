package trees.park.cal.smoke.server.objects;

import org.apache.commons.lang3.Validate;

public class StartSmokingObject {

    private final String email;
    private final String password;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public StartSmokingObject(String email, String password) {
        this.email = Validate.notBlank(email);
        this.password = Validate.notBlank(password);
    }
}
