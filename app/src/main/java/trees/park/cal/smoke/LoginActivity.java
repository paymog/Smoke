package trees.park.cal.smoke;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import org.w3c.dom.Text;

import trees.park.cal.smoke.models.User;


public class LoginActivity extends BaseSpiceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        addSignInListener();
    }

    private void addSignInListener() {
        final Button button = (Button) findViewById(R.id.signInButton);
        final Gson gson = new Gson();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText passwordField =  (EditText) findViewById(R.id.passwordText);
                EditText emailField = (EditText) findViewById(R.id.emailText);

                //build user object
                User user = new User(emailField.getText().toString(), passwordField.getText().toString());


                //get json for user object
                String userJson = gson.toJson(user, User.class);
                getSpiceManager().exe
                //post request to server
                boolean test = true;
                //wait for response
            }
        });
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
