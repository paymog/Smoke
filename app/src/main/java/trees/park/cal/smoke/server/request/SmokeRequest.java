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
import trees.park.cal.smoke.server.objects.SmokeRequestObject;

/**
 * Created by michael on 2014-09-06.
 */
public class SmokeRequest extends GoogleHttpClientSpiceRequest<Integer>{
    private static final String URL = Utils.SERVER_URL + "/smoke_request/";
    private static final Class<Integer> RESULT_CLASS = Integer.class;

    private final SmokeRequestObject smokeRequestObject;

    public SmokeRequest(String invited, String inviter) {
        super(RESULT_CLASS);
        smokeRequestObject = new SmokeRequestObject(invited, inviter);
    }

    @Override
    public Integer loadDataFromNetwork() throws Exception {
        Ln.d("Call web service " + URL);
        HttpRequest request = getHttpRequestFactory()
                .buildPostRequest(
                        new GenericUrl(URL),
                        new ByteArrayContent("application/json", Utils.serialize(smokeRequestObject)));
        request.setParser( new JacksonFactory().createJsonObjectParser() );

        return Utils.deserialize(request.execute().getContent(), getResultType());
    }

}
