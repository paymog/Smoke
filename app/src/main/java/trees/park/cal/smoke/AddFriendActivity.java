package trees.park.cal.smoke;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import trees.park.cal.smoke.models.SignedInUser;
import trees.park.cal.smoke.models.SpiceManagerSingleton;
import trees.park.cal.smoke.models.User;
import trees.park.cal.smoke.server.request.AddFriendRequest;


public class AddFriendActivity extends Activity {
    private final SpiceManager spiceManager = SpiceManagerSingleton.getSpiceManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_friend, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void cancel(View view) {
        setResult(1);
        finish();
    }

    public void addFriend(View view) {
        EditText emailText = (EditText) findViewById(R.id.emailText);

        AddFriendRequest request = new AddFriendRequest(SignedInUser.getSignedInUser().getEmail(),
                emailText.getText().toString());
        request.setRetryPolicy(null);
        spiceManager.execute(request, new AddFriendRequestListener());
    }

    public final class AddFriendRequestListener implements RequestListener<User> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(AddFriendActivity.this, "Could not find friend", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRequestSuccess(User user) {
            setResult(0);
            finish();
        }
    }
}
