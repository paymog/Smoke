package trees.park.cal.smoke.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    private long id;
    private String email;

    public String getEmail() {
        return email;
    }

    private String friends;

    @JsonCreator
    public User(@JsonProperty("id") long id, @JsonProperty("email") String email, @JsonProperty("friends_list")String friends) {
        this.id = id;
        this.email = email;
        this.friends = friends;
    }

    @Override
    public String toString() {
        return this.email;
    }

}
