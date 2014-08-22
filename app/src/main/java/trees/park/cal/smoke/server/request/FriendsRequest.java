package trees.park.cal.smoke.server.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.json.jackson.JacksonFactory;
import com.octo.android.robospice.request.googlehttpclient.GoogleHttpClientSpiceRequest;

import java.io.InputStream;

import roboguice.util.temp.Ln;
import trees.park.cal.smoke.Utils;
import trees.park.cal.smoke.models.Friend;
import trees.park.cal.smoke.models.FriendList;
import trees.park.cal.smoke.models.SignedInUser;
import trees.park.cal.smoke.models.User;
import trees.park.cal.smoke.models.UserList;
import trees.park.cal.smoke.server.models.ServerFriendRequest;

public class FriendsRequest extends GoogleHttpClientSpiceRequest<FriendList>{

    private static final String BASE_URL = "http://192.168.2.66:8181/get_friends/";
    private ServerFriendRequest serverFriendRequest;
    private static final ObjectMapper MAPPER = new ObjectMapper();


    public FriendsRequest() {
        super(FriendList.class);
        this.serverFriendRequest = new ServerFriendRequest(SignedInUser.getSignedInUser().getEmail());
    }



    @Override
    public FriendList loadDataFromNetwork() throws Exception {
        Ln.d("Call web service " + BASE_URL);
        String content = MAPPER.writeValueAsString(serverFriendRequest);
        HttpRequest request = getHttpRequestFactory()
                .buildPostRequest(new GenericUrl(BASE_URL), new ByteArrayContent("application/json", content.getBytes()));
        request.setParser( new JacksonFactory().createJsonObjectParser() );
        return Utils.deserialize(request.execute().getContent(), getResultType());
    }
}
