package trees.park.cal.smoke;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.HashMap;
import java.util.List;

import trees.park.cal.smoke.models.FriendList;
import trees.park.cal.smoke.models.SpiceManagerSingleton;
import trees.park.cal.smoke.server.request.GetFriendsRequest;

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

            System.out.println("Success retrieving users!");
            final ListView listview = (ListView) findViewById(R.id.listview);

            final StableArrayAdapter adapter = new StableArrayAdapter(context,
                    R.layout.list_view_item, friendList.getFriends());
            listview.setAdapter(adapter);

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