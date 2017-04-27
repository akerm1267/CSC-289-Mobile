package softwareheadconsulting.krankiesscheduleapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class ForgotPasswordActivity1 extends AppCompatActivity {
    EditText mUsernameView;
    Button mEnterButton;
    private UsernameTask mAuthTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password1);
        mUsernameView = (EditText) findViewById(R.id.txtUsername);
        mEnterButton = (Button) findViewById(R.id.btnEnter);

        mEnterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });
    }

    public void onBackPressed()
    {
        // code here to show dialog
        //super.onBackPressed();  // optional depending on your needs

        AlertDialog.Builder backAlert = new AlertDialog.Builder(ForgotPasswordActivity1.this);
        backAlert.setMessage("Return to login?")
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

    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mUsernameView.setError(null);

        // Store values at the time of the login attempt.
        String user = mUsernameView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(user)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //showProgress(true);
            mAuthTask = new UsernameTask(user);
            mAuthTask.execute();
        }
    }

    public class UsernameTask extends AsyncTask<Void, Void, Boolean> {

        private String mUsername;

        UsernameTask(String user) {
            mUsername = user;
        }
        String testM = "testABC";

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            URL url = null;
            String login_URL = "http://167.160.84.186/checkforuser.php";

            try {
                //Thread.sleep(2000);
                url = new URL(login_URL);
                HttpURLConnection httpUrlConnection = (HttpURLConnection)url.openConnection();
                httpUrlConnection.setRequestMethod("POST");
                httpUrlConnection.setDoOutput(true);
                httpUrlConnection.setDoInput(true);
                OutputStream outputStream = httpUrlConnection.getOutputStream();

                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String post_data = URLEncoder.encode("user_name","UTF-8")+"="+URLEncoder.encode(mUsername,"UTF-8");
                testM = post_data;
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                /*int status = httpUrlConnection.getResponseCode();
                testM = String.valueOf(status);*/

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
                testM = result;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                testM = e.toString();
            } catch (ProtocolException e) {
                e.printStackTrace();
                testM = e.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                testM = e.toString();
            } catch (IOException e) {
                e.printStackTrace();
                //testM = e.toString();
            } /*catch (InterruptedException e) {
                e.printStackTrace();
                testM = e.toString();
            }*/

            /*for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");

                testM = pieces[1];

                if (pieces[0].equals(mUsername)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }*/


            return false;

            // TODO: register the new account here.
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            //showProgress(false);

            if (testM.equals("1")) {
                //finish();
                Intent forgotPassword2 = new Intent(ForgotPasswordActivity1.this, ForgotPasswordActivity2.class);
                forgotPassword2.putExtra("EXTRA_USERNAME", mUsername);
                startActivity(forgotPassword2);
                finish();

            } else if(testM.equals("0")) {
                mUsernameView.setError("Invalid Username");
            }
            else
                Toast.makeText(ForgotPasswordActivity1.this, testM, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            //showProgress(false);
        }



    }
}
