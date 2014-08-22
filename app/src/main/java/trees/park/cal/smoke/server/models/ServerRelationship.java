package trees.park.cal.smoke.server.models;

/**
 * Created by paymahn on 2014-08-21.
 */
public class ServerRelationship {

    private final String adder;
    private final String addee;

    public String getAdder() {
        return adder;
    }

    public String getAddee() {
        return addee;
    }

    public ServerRelationship(String adder, String addee) {
        this.addee = addee;
        this.adder = adder;
    }
}
