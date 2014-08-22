package trees.park.cal.smoke;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import trees.park.cal.smoke.models.FriendList;
import trees.park.cal.smoke.models.SpiceManagerSingleton;
import trees.park.cal.smoke.server.request.FriendsRequest;

public class MainActivity extends Activity {

    private SpiceManager spiceManager = SpiceManagerSingleton.getSpiceManager();
    private List<String> currentUsersFriends;
    private Context loginContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginContext = getApplicationContext();
        currentUsersFriends = Collections.EMPTY_LIST;

        final ListView listview = (ListView) findViewById(R.id.listview);
        final StableArrayAdapter adapter = new StableArrayAdapter(loginContext,
                R.layout.list_view_item, currentUsersFriends);
        listview.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        FriendsRequest request = new FriendsRequest();
        request.setRetryPolicy(null);
        spiceManager.execute(request, "json", DurationInMillis.ALWAYS_EXPIRED, new FriendsRequestListener());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 0) {
            FriendsRequest request = new FriendsRequest();
            request.setRetryPolicy(null);
            spiceManager.execute(request, "json", DurationInMillis.ALWAYS_EXPIRED, new FriendsRequestListener());
        }
    }

    public void addFriend(View view) {
        Toast.makeText(MainActivity.this, "Tried to add friend", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, AddFriendActivity.class);
        startActivityForResult(intent, 0);
    }

    public final class FriendsRequestListener implements RequestListener<FriendList> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            System.out.println("Failure getting users");
            Toast.makeText(loginContext, "Could not get users", Toast.LENGTH_SHORT);
        }

        @Override
        public void onRequestSuccess(final FriendList friendList) {

            System.out.println("Success getting users");
            final ListView listview = (ListView) findViewById(R.id.listview);

//            currentUsersFriends = friendList.getFriends();
//            ((BaseAdapter)listview.getAdapter()).notifyDataSetChanged();
            final StableArrayAdapter adapter = new StableArrayAdapter(loginContext,
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