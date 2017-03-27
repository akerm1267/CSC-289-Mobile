package softwareheadconsulting.krankiesscheduleapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Aimee on 3/22/2017.
 */
public class BackgroundWorker extends AsyncTask<String, String, String> {
    String user_name;
    String password;
    BackgroundWorker(String _username, String _password)
    {
        user_name = _username;
        password = _password;
    }

    @Override
    protected String doInBackground(String... params) {
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
            String post_data = URLEncoder.encode("user_name","UTF-8")+"="+URLEncoder.encode(user_name,"UTF-8")+URLEncoder.encode("password","UTF-8")+
                    "="+URLEncoder.encode(password,"UTF-8");
            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*@Override
    protected String doInBackground(Void... params) {
        return null;
    }*/

    @Override
    protected void onPostExecute(final String hi){
        //startActivity(new Intent(LoginActivity.class, MainActivity.class));
    }
}
