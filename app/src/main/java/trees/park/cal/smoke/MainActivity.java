package trees.park.cal.smoke;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.HashMap;
import java.util.List;

import trees.park.cal.smoke.models.SpiceManagerSingleton;
import trees.park.cal.smoke.models.User;
import trees.park.cal.smoke.models.UserList;
import trees.park.cal.smoke.server.UsersRequest;

import static android.widget.AdapterView.*;

public class MainActivity extends Activity {

    private SpiceManager spiceManager = SpiceManagerSingleton.getSpiceManager();
    private Context loginContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginContext = getApplicationContext();

        UsersRequest request = new UsersRequest();
        request.setRetryPolicy(null);
        spiceManager.execute(request, "json", DurationInMillis.ALWAYS_EXPIRED, new UsersRequestListener());

    }

    public final class UsersRequestListener implements RequestListener<UserList> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            System.out.println("Failure getting users");
            Toast.makeText(loginContext, "Could not get users", Toast.LENGTH_SHORT);
        }

        @Override
        public void onRequestSuccess(final UserList userList) {
            System.out.println("Success getting users");
            final ListView listview = (ListView) findViewById(R.id.listview);

            final StableArrayAdapter adapter = new StableArrayAdapter(loginContext,
                    R.layout.list_view_item, userList);
            listview.setAdapter(adapter);

            listview.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, final View view,
                                        int position, long id) {
                    final User item = (User) parent.getItemAtPosition(position);
                    view.animate().setDuration(2000).alpha(0)
                            .withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    userList.remove(item);
                                    adapter.notifyDataSetChanged();
                                    view.setAlpha(1);
                                }
                            });
                }

            });
        }
    }


    private class StableArrayAdapter extends ArrayAdapter<User> {

        HashMap<User, Integer> mIdMap = new HashMap<User, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<User> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            User item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }

} 