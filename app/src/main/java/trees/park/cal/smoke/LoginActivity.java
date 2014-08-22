package trees.park.cal.smoke;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

    private LoginActivity loginContext;
    private SpiceManager spiceManager = SpiceManagerSingleton.getSpiceManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginContext = this;

        addStartSmokingListener();

        StartSmokingRequest request = new StartSmokingRequest("michaeldubyu@gmail.com", "password");
        request.setRetryPolicy(null);
//        spiceManager.execute(request, "json", DurationInMillis.ALWAYS_EXPIRED, new StartSmokingRequestListener());
    }

    @Override
    protected void onStart() {
        spiceManager.start(this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void addStartSmokingListener() {
        final Button smokingButton = (Button) findViewById(R.id.startSmokingButton);
        final EditText passwordField = (EditText) findViewById(R.id.passwordText);
        final EditText emailText = (EditText) findViewById(R.id.emailText);

        smokingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String password = passwordField.getText().toString();
                final String email = emailText.getText().toString();

                StartSmokingRequest request = new StartSmokingRequest(email, password);
                request.setRetryPolicy(null);
                spiceManager.execute(request, "json", DurationInMillis.ALWAYS_EXPIRED, new StartSmokingRequestListener());
            }
        });
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

            Intent intent = new Intent(loginContext, MainActivity.class);
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
