package trees.park.cal.smoke.server.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.json.jackson.JacksonFactory;
import com.octo.android.robospice.request.googlehttpclient.GoogleHttpClientSpiceRequest;

import roboguice.util.temp.Ln;
import trees.park.cal.smoke.Utils;
import trees.park.cal.smoke.models.User;
import trees.park.cal.smoke.server.objects.AddFriendObject;

public class AddFriendRequest extends GoogleHttpClientSpiceRequest<User>{
    private final static String URL = Utils.SERVER_URL + "/add_friend/";

    private final AddFriendObject addFriendObject;


    public AddFriendRequest(String adder, String addee) {
        super(User.class);
        addFriendObject = new AddFriendObject(adder, addee);
    }

    @Override
    public User loadDataFromNetwork() throws Exception {
        Ln.d("Call web service " + URL);
        HttpRequest request = getHttpRequestFactory()
                .buildPostRequest(
                        new GenericUrl(URL),
                        new ByteArrayContent("application/json", Utils.serialize(addFriendObject)));
        request.setParser( new JacksonFactory().createJsonObjectParser() );

        return Utils.deserialize(request.execute().getContent(), getResultType());
    }
}
