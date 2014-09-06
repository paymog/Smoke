package trees.park.cal.smoke.server.request;

import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.json.jackson.JacksonFactory;
import com.octo.android.robospice.request.googlehttpclient.GoogleHttpClientSpiceRequest;

import roboguice.util.temp.Ln;
import trees.park.cal.smoke.Utils;
import trees.park.cal.smoke.models.SignedInUser;
import trees.park.cal.smoke.models.User;

public class PostGcmRequest extends GoogleHttpClientSpiceRequest<User> {
    private static final String URL = Utils.SERVER_URL + "/update_gcm/";
    private static final Class<User> RESULT_CLASS = User.class;

    private final User user;

    public PostGcmRequest() {
        super(RESULT_CLASS);
        user = SignedInUser.getSignedInUser();
        Ln.d("User in question " + user.toString());
    }

    @Override
    public User loadDataFromNetwork() throws Exception {
        Ln.d("Call web service " + URL);
        Ln.d("Serializing user " + user.toString());
        HttpRequest request = getHttpRequestFactory()
                .buildPostRequest(
                        new GenericUrl(URL),
                        new ByteArrayContent("application/json", Utils.serialize(user)));
        request.setParser( new JacksonFactory().createJsonObjectParser() );

        return Utils.deserialize(request.execute().getContent(), getResultType());
    }
}
