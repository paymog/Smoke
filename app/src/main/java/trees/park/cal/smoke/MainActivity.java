package trees.park.cal.smoke;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.HashMap;
import java.util.List;

import roboguice.util.temp.Ln;
import trees.park.cal.smoke.models.FriendList;
import trees.park.cal.smoke.models.SignedInUser;
import trees.park.cal.smoke.models.SpiceManagerSingleton;
import trees.park.cal.smoke.server.objects.SmokeRequestResponseObject;
import trees.park.cal.smoke.server.request.GetFriendsRequest;
import trees.park.cal.smoke.server.request.SmokeRequest;

import static android.widget.AdapterView.*;

public class MainActivity extends Activity {

    private final static SpiceManager SPICE_MANAGER = SpiceManagerSingleton.getSpiceManager();

    private final FriendsRequestListener friendsRequestListener = new FriendsRequestListener();
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();
    }

    @Override
    public void onResume() {
        super.onResume();
        executeGetFriendsRequest();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == AddFriendActivity.SUCCESS_CODE) {
            executeGetFriendsRequest();
        }
    }

    private void executeGetFriendsRequest() {
        GetFriendsRequest request = new GetFriendsRequest();
        request.setRetryPolicy(null);
        SPICE_MANAGER.execute(request, friendsRequestListener);
    }

    public void addFriend(View view) {
        Intent intent = new Intent(this, AddFriendActivity.class);
        startActivityForResult(intent, 0);
    }

    public final class FriendsRequestListener implements RequestListener<FriendList> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(context, "Could not get users!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRequestSuccess(final FriendList friendList) {

            Ln.d("Success retrieving users!");
            final ListView listview = (ListView) findViewById(R.id.listview);

            final StableArrayAdapter adapter = new StableArrayAdapter(context,
                    R.layout.list_view_item, friendList.getFriends());
            listview.setAdapter(adapter);
            listview.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    String clickedName = friendList.getFriends().get(position);
                    Toast.makeText(context, "Pinging " + clickedName + ".", Toast.LENGTH_SHORT).show();

                    context.sendBroadcast(new Intent("com.google.android.intent.action.GTALK_HEARTBEAT"));
                    context.sendBroadcast(new Intent("com.google.android.intent.action.MCS_HEARTBEAT"));

                    SmokeRequest request = new SmokeRequest(clickedName, SignedInUser.getSignedInUser().getEmail());
                    request.setRetryPolicy(null);
                    SPICE_MANAGER.execute(request, new SmokeRequestListener());
                }
            });
        }
    }

    public final class SmokeRequestListener implements RequestListener<SmokeRequestResponseObject> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(context, "Could not send a request to specified user!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRequestSuccess(SmokeRequestResponseObject smokeRequestResponseObject) {
            Toast.makeText(context, "Successfully sent request to specified user!", Toast.LENGTH_SHORT).show();
            Ln.d(smokeRequestResponseObject.toString());
        }
    }

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }

} 