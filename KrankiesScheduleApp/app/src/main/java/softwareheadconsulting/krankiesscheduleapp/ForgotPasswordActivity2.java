package softwareheadconsulting.krankiesscheduleapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

public class ForgotPasswordActivity2 extends AppCompatActivity {
    TextView QuestionText;
    EditText AnswerEditText;

    String securityQuestion;
    String securityAnswer;
    private QuesionAnswerTask mAuthTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password2);
        QuestionText = (TextView) findViewById(R.id.tvQuestion);
        AnswerEditText = (EditText) findViewById(R.id.etAnswer);
        final String username = getIntent().getStringExtra("EXTRA_USERNAME");

        mAuthTask = new QuesionAnswerTask(username);
        mAuthTask.execute();

        Button EnterButton = (Button) findViewById(R.id.btnEnter);
        EnterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String answer = AnswerEditText.getText().toString();
                if(securityAnswer.equals(answer))
                {
                    Intent resetPass = new Intent(ForgotPasswordActivity2.this, ResetPasswordActivity.class);
                    resetPass.putExtra("EXTRA_USERNAME", username);
                    startActivity(resetPass);
                    finish();
                }
                else
                    AnswerEditText.setError("Incorrect Answer");

                QuestionText.setText(securityQuestion);
            }
        });
    }

    public void onBackPressed()
    {
        AlertDialog.Builder backAlert = new AlertDialog.Builder(ForgotPasswordActivity2.this);
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

    public void SetQuestionText(String question) {
        QuestionText.setText(question);
    }

    public class QuesionAnswerTask extends AsyncTask<Void, Void, Boolean> {

        private String mUsername;

        QuesionAnswerTask(String user) {
            mUsername = user;
        }

        String strResult = "";

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            URL url = null;
            String login_URL = "http://167.160.84.186/securityquestion.php";

            try {
                url = new URL(login_URL);
                HttpURLConnection httpUrlConnection = (HttpURLConnection)url.openConnection();
                httpUrlConnection.setRequestMethod("POST");
                httpUrlConnection.setDoOutput(true);
                httpUrlConnection.setDoInput(true);
                OutputStream outputStream = httpUrlConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("user_name","UTF-8")+"="+URLEncoder.encode(mUsername,"UTF-8");
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
                strResult = e.toString();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return false;
            // TODO: register the new account here.
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            String[] QnA = strResult.split("&");

            SetQuestionText(QnA[0]);
            securityAnswer = QnA[1];
        }

        @Override
        protected void onCancelled() {

        }
    }
}
