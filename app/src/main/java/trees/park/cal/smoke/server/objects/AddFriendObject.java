package trees.park.cal.smoke.server.objects;

public class AddFriendObject {

    private final String adder;
    private final String addee;

    public String getAdder() {
        return adder;
    }

    public String getAddee() {
        return addee;
    }

    public AddFriendObject(String adder, String addee) {
        this.addee = addee;
        this.adder = adder;
    }
}
