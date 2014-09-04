package trees.park.cal.smoke;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import trees.park.cal.smoke.models.SignedInUser;
import trees.park.cal.smoke.models.SpiceManagerSingleton;
import trees.park.cal.smoke.server.request.StartSmokingRequest;
import trees.park.cal.smoke.models.User;


public class LoginActivity extends Activity{

    private final static SpiceManager SPICE_MANAGER = SpiceManagerSingleton.getSpiceManager();
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = getApplicationContext();

        if (!SPICE_MANAGER.isStarted()) {
            SPICE_MANAGER.start(context);
        }

        StartSmokingRequest request = new StartSmokingRequest("michaeldubyu@gmail.com", "password");
        request.setRetryPolicy(null);
        SPICE_MANAGER.execute(request, new StartSmokingRequestListener());
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void signInUser(View view) {
        final EditText passwordField = (EditText) findViewById(R.id.passwordText);
        final EditText emailText = (EditText) findViewById(R.id.emailText);

        final String password = passwordField.getText().toString();
        final String email = emailText.getText().toString();

        StartSmokingRequest request = new StartSmokingRequest(email, password);
        request.setRetryPolicy(null);
        SPICE_MANAGER.execute(request, "json", DurationInMillis.ALWAYS_EXPIRED, new StartSmokingRequestListener());
    }

    public final class StartSmokingRequestListener implements RequestListener<User> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(LoginActivity.this, "There was an error, please try again.", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRequestSuccess(User user) {
            SignedInUser.setSignedUser(user);
            Toast.makeText(LoginActivity.this, "You're in!.", Toast.LENGTH_SHORT).show();

            final EditText passwordField = (EditText) findViewById(R.id.passwordText);
            final EditText emailText = (EditText) findViewById(R.id.emailText);

            passwordField.setText("");
            emailText.setText("");

            Intent intent = new Intent(context, MainActivity.class);
            startActivity(intent);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
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

}
