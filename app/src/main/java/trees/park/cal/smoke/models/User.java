package trees.park.cal.smoke.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;


public class User {

    private long id;
    private String email;
    private String friends;

    @JsonCreator
    public User(@JsonProperty("id") long id, @JsonProperty("email") String email, @JsonProperty("friends_list")String friends) {
        this.id = id;
        this.email = email;
        this.friends = friends;
    }

}
