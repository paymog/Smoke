package trees.park.cal.smoke;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.io.IOException;

import roboguice.util.temp.Ln;
import trees.park.cal.smoke.models.SignedInUser;
import trees.park.cal.smoke.models.SpiceManagerSingleton;
import trees.park.cal.smoke.server.request.PostGcmRequest;
import trees.park.cal.smoke.server.request.StartSmokingRequest;
import trees.park.cal.smoke.models.User;

public class LoginActivity extends Activity{

    private final static SpiceManager SPICE_MANAGER = SpiceManagerSingleton.getSpiceManager();
    GoogleCloudMessaging gcm;
    Context context;
    String regId;

    public static final String REG_ID = "regId";
    private static final String APP_VERSION = "0.2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = getApplicationContext();

        if (!SPICE_MANAGER.isStarted()) {
            SPICE_MANAGER.start(context);
        }
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

    public final class PostGcmRequestListener implements RequestListener<User> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Ln.d("Error posting GCM for user.");
        }

        @Override
        public void onRequestSuccess(User user) {
            Ln.d("Posted GCM for user " + user.toString());
        }
    }

    public final class StartSmokingRequestListener implements RequestListener<User> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(LoginActivity.this, "There was an error, please try again.", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRequestSuccess(User user) {
            SignedInUser.setSignedUser(user);

            final EditText passwordField = (EditText) findViewById(R.id.passwordText);
            final EditText emailText = (EditText) findViewById(R.id.emailText);

            passwordField.setText("");
            emailText.setText("");

            if (TextUtils.isEmpty(regId)) {
                regId = registerGCM();
                Ln.d("GCM RegId: " + regId);
            } else {
                Ln.d("Already registered with GCM server!");
            }

            PostGcmRequest request = new PostGcmRequest();
            request.setRetryPolicy(null);
            SPICE_MANAGER.execute(request, "json", DurationInMillis.ALWAYS_EXPIRED, new PostGcmRequestListener());

            Toast.makeText(LoginActivity.this, "Logged in/created your account successfully!", Toast.LENGTH_SHORT).show();

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

    public String registerGCM() {

        gcm = GoogleCloudMessaging.getInstance(this);
        regId = getRegistrationId(context);

        if (TextUtils.isEmpty(regId)) {
            registerInBackground();
            Ln.d("registerGCM - successfully registered with GCM server - regId: "
                            + regId);
        } else {
            Ln.d("RegId already available. RegId: " + regId);
        }
        User signedInUser = SignedInUser.getSignedInUser();
        signedInUser.setGcm_id(regId);
        return regId;
    }

    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getSharedPreferences(
                LoginActivity.class.getSimpleName(), Context.MODE_PRIVATE);
        String registrationId = prefs.getString(REG_ID, "");
        if (registrationId.isEmpty()) {
            Ln.i("Registration not found.");
            return "";
        }
        int registeredVersion = prefs.getInt(APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Ln.i("App version changed.");
            return "";
        }
        return registrationId;
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Ln.d("I never expected this! Going down, going down!" + e);
            throw new RuntimeException(e);
        }
    }

    private void registerInBackground() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regId = gcm.register(Utils.GOOGLE_PROJECT_NUMBER);
                    Ln.d("registerInBackground - regId: "
                            + regId);
                    msg = "Device registered, registration ID=" + regId;

                    storeRegistrationId(context, regId);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    Ln.d("Error: " + msg);
                }
                Ln.d("AsyncTask completed: " + msg);
                return null;
            }

            protected void onPostExecute(String msg) {
                Toast.makeText(getApplicationContext(),
                        "Registered with GCM Server." + msg, Toast.LENGTH_LONG)
                        .show();
            }
        }.execute(null, null, null);
    }

    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getSharedPreferences(
                LoginActivity.class.getSimpleName(), Context.MODE_PRIVATE);
        int appVersion = getAppVersion(context);
        Ln.i("Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(REG_ID, regId);
        editor.putInt(APP_VERSION, appVersion);
        editor.commit();
    }
}
