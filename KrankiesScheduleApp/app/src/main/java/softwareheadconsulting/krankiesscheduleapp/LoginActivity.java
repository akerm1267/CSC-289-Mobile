package softwareheadconsulting.krankiesscheduleapp;

import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.INTERNET;

/**
 * A login screen that offers login via username/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_INTERNET = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mUsernameView = (AutoCompleteTextView) findViewById(R.id.txtUsername);
        mayRequestInternet();

        mPasswordView = (EditText) findViewById(R.id.txtPassword);

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mUsernameSignInButton = (Button) findViewById(R.id.btnSignIn);
        mUsernameSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        Button mForgotPasswordButton = (Button) findViewById(R.id.btnForgotPassword);
        mForgotPasswordButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgotPassword1 = new Intent(LoginActivity.this, ForgotPasswordActivity1.class);
                startActivity(forgotPassword1);
            }
        });

        //mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    @Override
    public void onBackPressed()
    {
        // code here to show dialog
        //super.onBackPressed();  // optional depending on your needs
        AlertDialog.Builder backAlert = new AlertDialog.Builder(LoginActivity.this);
        backAlert.setMessage("Are you sure you want to leave?")
        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        })
        .setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //nothing
            }
        });
        backAlert.show();
    }

    private void populateAutoComplete() {
        if (!mayRequestInternet()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestInternet() {
        Toast.makeText(LoginActivity.this, "No", Toast.LENGTH_SHORT);

        if (checkSelfPermission(INTERNET) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }

        if (shouldShowRequestPermissionRationale(INTERNET)) {
            Snackbar.make(mUsernameView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{INTERNET}, REQUEST_INTERNET);
                        }
                    });
        } else {
            requestPermissions(new String[]{INTERNET}, REQUEST_INTERNET);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_INTERNET) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String user = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(user)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            mAuthTask = new UserLoginTask(user, password);
            mAuthTask.execute();
        }
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mUsernameView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private String mUsername;
        private String mPassword;

        UserLoginTask(String user, String password) {
            mUsername = user;
            mPassword = password;
        }
        String strResult = "";

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            URL url = null;
            String login_URL = "http://167.160.84.186/login.php";

            try {
                url = new URL(login_URL);
                HttpURLConnection httpUrlConnection = (HttpURLConnection)url.openConnection();
                httpUrlConnection.setRequestMethod("POST");
                httpUrlConnection.setDoOutput(true);
                httpUrlConnection.setDoInput(true);

                OutputStream outputStream = httpUrlConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("user_name","UTF-8")+"="+URLEncoder.encode(mUsername,"UTF-8")+"&"+URLEncoder.encode("password","UTF-8")+
                        "="+URLEncoder.encode(mPassword,"UTF-8");
                strResult = post_data;
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpUrlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while((line = bufferedReader.readLine()) != null)
                {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpUrlConnection.disconnect();
                strResult = result;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                strResult = e.toString();
            } catch (ProtocolException e) {
                e.printStackTrace();
                strResult = e.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                strResult = e.toString();
            } catch (IOException e) {
                e.printStackTrace();
                //strResult = e.toString();
            }
            catch (Exception e) {
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            //showProgress(false);

            if (strResult.equals("1")) {
                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                Intent main = new Intent(LoginActivity.this, MainActivity.class);
                main.putExtra("EXTRA_USERNAME", mUsername);
                mUsernameView.setText("");
                mPasswordView.setText("");
                startActivity(main);

            } else if(strResult.equals("0")) {
                mPasswordView.setError("Incorrect Username or Password");
                mPasswordView.requestFocus();
            }
            else
                Toast.makeText(LoginActivity.this, strResult, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            //showProgress(false);
        }



    }

}


