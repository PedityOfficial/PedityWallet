package com.example.pwallet.peditywallet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.stellar.sdk.KeyPair;
import org.stellar.sdk.Network;
import org.stellar.sdk.Server;

/**
 * A login screen that offers login via secret key.
 */
public class LoginActivity extends AppCompatActivity {

    private EditText mSecretKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.loadingPanel).setVisibility(View.GONE);

        mSecretKey = findViewById(R.id.secret_key);
        mSecretKey.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = findViewById(R.id.sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
                attemptLogin();
            }
        });

    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
            UserLoginTask mAuthTask = new UserLoginTask(mSecretKey.getText().toString());
            mAuthTask.execute((Void) null);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
     class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mPassword;

        UserLoginTask(String password) {
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                Network.usePublicNetwork();
                //Log.v("EditText : ", mPassword);

                KeyPair keyPair = KeyPair.fromSecretSeed(mPassword);
                Server server = new Server("http://horizon.stellar.org");

                Network.usePublicNetwork();
                server.accounts().account(keyPair);

                //secret feed encryption and storing it in Android KeyStore
                if(!new Encryptor(getApplicationContext()).encryptText("SecretKeyAlias_PEDITY6", mPassword).equals("")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast toast = Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    });
                }

                //Log.v("LA","Calling MainActivity");
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
            catch (Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       // Log.v("Exception", e.toString());
                        Toast toast = Toast.makeText(getApplicationContext() ,"Invalid Secret Key!", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                });
                return false;
            }

            // TODO: register the new account here.
            return true;
        }
    }

    public void onBackPressed() {
        //do nothing if Back Pressed
    }
}

