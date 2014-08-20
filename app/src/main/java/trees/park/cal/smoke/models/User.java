package trees.park.cal.smoke.models;

import java.util.Collections;
import java.util.List;

import lombok.experimental.Wither;

public class User {
    private static long NEXT_ID = 1;

    private long id;
    private String email;
    private String password;
    private List<Long> friends;

    public User(String email, String password) {
        this.id = NEXT_ID++;
        this.email = email;
        this.password = password;
        this.friends = Collections.EMPTY_LIST;
    }
}
