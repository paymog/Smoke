package trees.park.cal.smoke.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class FriendList {

    private final List<String> friends;

    public List<String> getFriends() {
        return friends;
    }

    public FriendList(@JsonProperty("friends_list") List<String> friends) {
        this.friends = friends;

    }
}
