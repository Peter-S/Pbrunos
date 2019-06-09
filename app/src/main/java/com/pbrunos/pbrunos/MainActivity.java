package com.pbrunos.pbrunos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    private EditText mDisplayName, mEmail, mPassword;



    public static final String EXTRA_MESSAGE = "com.pbrunos.pbrunos.MESSAGE";
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: main activity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar =  findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        mDisplayName = findViewById(R.id.editDisplayName);
        mEmail = findViewById(R.id.editEmail);
        mPassword = findViewById(R.id.editText);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPreferences.edit();

        checkSharedPrefs();

    }

    private void checkSharedPrefs() {
        String displayName = mPreferences.getString(getString(R.string.displayName), "");
        String email = mPreferences.getString(getString(R.string.email), "");
        String password = mPreferences.getString(getString(R.string.password), "");

        mDisplayName.setText(displayName);
        mEmail.setText(email);
        mPassword.setText(password);

    }


    public void sendMessage(View view) {

        String displayName = mDisplayName.getText().toString();
        mEditor.putString(getString(R.string.displayName), displayName);
        mEditor.commit();

        String email = mEmail.getText().toString();
        mEditor.putString(getString(R.string.email), email);
        mEditor.commit();

        String password = mPassword.getText().toString();
        mEditor.putString(getString(R.string.password), password);
        mEditor.commit();

        EditText iemail = findViewById(R.id.editEmail);
        EditText iname = findViewById(R.id.editDisplayName);
        EditText itext = findViewById(R.id.editText);

        if(iname.getText().toString().length() == 0){
            iname.setError("Name is required");}

        else if(iemail.getText().toString().length() == 0){
            iemail.setError("Email is required");}

        else if(itext.getText().toString().length() == 0) {
            itext.setError("Password is required");}

        else {
            Intent intent = new Intent(this, DisplayMessageActivity.class);
            EditText editText = findViewById(R.id.editText);
            String message = editText.getText().toString();
            intent.putExtra(EXTRA_MESSAGE, message);
            startActivity(intent);
        }
    }

    public void zombieMovies(View view) {
        Intent intent = new Intent(this, DisplayZombieActivity.class);
        startActivity(intent);
    }

    public void trafficCams(View view) {
        Intent intent = new Intent(this, TrafficCamActivity.class);
        startActivity(intent);
    }

    public void map(View view) {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }

    public void toast1(View view) {
        Toast.makeText(MainActivity.this, "B o t t o m    T o a s t", Toast.LENGTH_SHORT).show();
    }

    public void toast2(View view) {
        Toast toast = Toast.makeText(MainActivity.this,
                "T o p    T o a s t",
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP | Gravity.CENTER,0,0);
        toast.show();
    }

    public void toast3(View view) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast,
                (ViewGroup) findViewById(R.id.custom_toast_container));

        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText("This is a custom toast");

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    public void toast4(View view) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.alert_toast,
                (ViewGroup) findViewById(R.id.custom_toast_container));

        TextView text = (TextView) layout.findViewById(R.id.text2);
        text.setText("A  L  E  R  T!!");

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    // This code is for the toolbar

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.go_about:
                Intent intent = new Intent(this, About.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actions, menu);
        return true;
    }


}
