package trees.park.cal.smoke.server.objects;

public class SmokeRequestObject {

    private final String inviter;
    private final String invited;

    public SmokeRequestObject(String invited, String inviter) {
        this.inviter = inviter;
        this.invited = invited;
    }

    public String getInviter() {
        return inviter;
    }

    public String getInvited() {
        return invited;
    }
}
