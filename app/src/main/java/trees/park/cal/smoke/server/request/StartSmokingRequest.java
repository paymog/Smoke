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
import trees.park.cal.smoke.server.models.ServerUser;

public class StartSmokingRequest extends GoogleHttpClientSpiceRequest<User> {
    private static final String BASE_URL = "http://192.168.100.111:8181/login/";
    private static final Class<User> RESULT_CLASS = User.class;
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final ServerUser user;

    public StartSmokingRequest(String email, String password) {
        super(RESULT_CLASS);
        user = new ServerUser(email, password);
    }


    @Override
    public User loadDataFromNetwork() throws Exception {
        Ln.d("Call web service " + BASE_URL);
        HttpRequest request = getHttpRequestFactory()
                    .buildPostRequest(
                            new GenericUrl(BASE_URL),
                            new ByteArrayContent("application/json", Utils.serialize(user)));
        request.setParser( new JacksonFactory().createJsonObjectParser() );

        return Utils.deserialize(request.execute().getContent(), getResultType());
    }
}
