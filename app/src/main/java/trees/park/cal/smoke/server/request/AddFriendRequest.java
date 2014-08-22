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
import trees.park.cal.smoke.server.models.ServerRelationship;

public class AddFriendRequest extends GoogleHttpClientSpiceRequest<User>{
    private static final String BASE_URL = "http://192.168.2.66:8181/add_friend/";
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final ServerRelationship relationship;


    public AddFriendRequest(String adder, String addee) {
        super(User.class);
        relationship = new ServerRelationship(adder, addee);
    }

    @Override
    public User loadDataFromNetwork() throws Exception {
        Ln.d("Call web service " + BASE_URL);
        String content = MAPPER.writeValueAsString(relationship);
        HttpRequest request = getHttpRequestFactory()
                .buildPostRequest(new GenericUrl(BASE_URL), new ByteArrayContent("application/json", content.getBytes()));
        request.setParser( new JacksonFactory().createJsonObjectParser() );

        return Utils.deserialize(request.execute().getContent(), getResultType());
    }
}
