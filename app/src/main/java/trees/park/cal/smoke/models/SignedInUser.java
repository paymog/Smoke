package trees.park.cal.smoke.models;

public class SignedInUser {

    private static User INSTANCE;

    /**
     * Sets the user. Can only be called once. Every other call will finished without
     * changing anything.
     * @param user
     */
    public static void setSignedUser(User user) {
        if(INSTANCE == null) {
            INSTANCE = user;
        }
    }

    public static User getSignedInUser() {
        return INSTANCE;
    }
}
