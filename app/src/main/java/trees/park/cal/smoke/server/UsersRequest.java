package trees.park.cal.smoke.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.json.jackson.JacksonFactory;
import com.octo.android.robospice.request.googlehttpclient.GoogleHttpClientSpiceRequest;

import java.io.InputStream;

import roboguice.util.temp.Ln;
import trees.park.cal.smoke.Utils;
import trees.park.cal.smoke.models.UserList;

public class UsersRequest extends GoogleHttpClientSpiceRequest<UserList>{

    private static final String BASE_URL = "http://192.168.2.66:8181/users/";

    public UsersRequest() {
        super(UserList.class);
    }


    @Override
    public UserList loadDataFromNetwork() throws Exception {
        Ln.d("Call web service " + BASE_URL);
        HttpRequest request = getHttpRequestFactory()
                .buildGetRequest(new GenericUrl(BASE_URL));
        request.setParser( new JacksonFactory().createJsonObjectParser() );

        return Utils.deserialize(request.execute().getContent(), getResultType());
    }
}
