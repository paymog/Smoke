package trees.park.cal.smoke.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;


@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    private long id;
    private String email;
    private String gcm_id;

    public String getEmail() {
        return email;
    }

    private String friends;

    @JsonCreator
    public User(@JsonProperty("id") long id, @JsonProperty("email") String email,
                @JsonProperty("friends_list")String friends, @JsonProperty("gcm_id")String gcm_id) {
        this.id = id;
        this.email = email;
        this.friends = friends;
        this.gcm_id = gcm_id;
    }

    public long getId() {
        return id;
    }

    public String getGcm_id() {
        return gcm_id;
    }

    public String getFriends() {
        return friends;
    }

    public void setGcm_id(String gcm_id) {
        this.gcm_id = gcm_id;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

}
