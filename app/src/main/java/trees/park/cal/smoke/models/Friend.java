package trees.park.cal.smoke.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Friend {

    private final String email;

    @JsonCreator
    public Friend(@JsonProperty("email") String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return email;
    }
}
