package trees.park.cal.smoke.server.request;

import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.json.jackson.JacksonFactory;
import com.octo.android.robospice.request.googlehttpclient.GoogleHttpClientSpiceRequest;

import roboguice.util.temp.Ln;
import trees.park.cal.smoke.Utils;
import trees.park.cal.smoke.models.FriendList;
import trees.park.cal.smoke.models.SignedInUser;
import trees.park.cal.smoke.server.objects.FriendRequestObject;

public class GetFriendsRequest extends GoogleHttpClientSpiceRequest<FriendList>{

    private static final String URL = Utils.SERVER_URL + "/get_friends/";
    private FriendRequestObject friendRequestObject;

    public GetFriendsRequest() {
        super(FriendList.class);
        this.friendRequestObject = new FriendRequestObject(SignedInUser.getSignedInUser().getEmail());
    }

    @Override
    public FriendList loadDataFromNetwork() throws Exception {
        Ln.d("Call web service " + URL);
        HttpRequest request = getHttpRequestFactory()
                .buildPostRequest(
                        new GenericUrl(URL),
                        new ByteArrayContent("application/json", Utils.serialize(friendRequestObject)));
        request.setParser( new JacksonFactory().createJsonObjectParser() );
        return Utils.deserialize(request.execute().getContent(), getResultType());
    }
}
