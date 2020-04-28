package cz.zcu.kiv.chatbot;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cz.zcu.kiv.chatbot.connection.InternetConnection;

/**
 * Welcome activity that shows ap icon and check internet connection.
 *
 * @author Martin Matas
 * @version 1.0
 * created on 2020-22-04
 */
public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (InternetConnection.check(getApplicationContext())) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else {
            InternetConnection.noConnectionDialog(this);
        }
    }

}
