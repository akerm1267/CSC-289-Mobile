package softwareheadconsulting.krankiesscheduleapp;

import android.content.DialogInterface;
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

public class ResetPasswordActivity extends AppCompatActivity {
    EditText Password1;
    EditText Password2;
    String strPass1;
    String strPass2;
    private ResetPasswordTask mAuthTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        Password1 = (EditText) findViewById(R.id.etNewPass);
        Password2 = (EditText) findViewById(R.id.etConfirmPass);
        final String username = getIntent().getStringExtra("EXTRA_USERNAME");

        Button UpdatePassButton = (Button) findViewById(R.id.btnUpdatePass);
        UpdatePassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strPass1 = Password1.getText().toString();
                strPass2 = Password2.getText().toString();

                if(!TextUtils.isEmpty(strPass1)) {
                    if (strPass1.equals(strPass2)) {
                        mAuthTask = new ResetPasswordTask(username, strPass1);
                        mAuthTask.execute();
                    } else {
                        Password2.setError("Inputs do not match");
                    }
                }
                else
                    Password1.setError("Enter a password");
            }
        });
    }

    public void onBackPressed()
    {
        AlertDialog.Builder backAlert = new AlertDialog.Builder(ResetPasswordActivity.this);
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

    public class ResetPasswordTask extends AsyncTask<Void, Void, Boolean> {

        private String mUsername;
        private String mNewPassword;

        ResetPasswordTask(String user, String newPass) {
            mUsername = user;
            mNewPassword = newPass;
        }
        String strResult = "";

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            URL url = null;
            String login_URL = "http://167.160.84.186/updatepassword.php";

            try {
                url = new URL(login_URL);
                HttpURLConnection httpUrlConnection = (HttpURLConnection)url.openConnection();
                httpUrlConnection.setRequestMethod("POST");
                httpUrlConnection.setDoOutput(true);
                httpUrlConnection.setDoInput(true);
                OutputStream outputStream = httpUrlConnection.getOutputStream();

                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String post_data = URLEncoder.encode("user_name","UTF-8")+"="+URLEncoder.encode(mUsername,"UTF-8")+"&"+URLEncoder.encode("password","UTF-8")+
                        "="+URLEncoder.encode(mNewPassword,"UTF-8");
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
            return false;
            // TODO: register the new account here.
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;

            if (strResult.equals("1")) {
                Toast.makeText(ResetPasswordActivity.this, "Password successfully reset", Toast.LENGTH_SHORT).show();
                finish();
            } else if(strResult.equals("0")) {
                Toast.makeText(ResetPasswordActivity.this, "Password reset unsuccessful", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(ResetPasswordActivity.this, strResult, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }
}
