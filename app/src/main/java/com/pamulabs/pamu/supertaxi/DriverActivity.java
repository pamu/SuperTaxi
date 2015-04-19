package com.pamulabs.pamu.supertaxi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.net.SocketTimeoutException;
import java.net.URL;

public class DriverActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_driver, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_driver, container, false);

            Intent intent = getActivity().getIntent();
            if (intent != null && intent.getExtras() != null) {
                Bundle bundle = intent.getExtras();
                if (bundle.containsKey("SOURCE") &&
                        bundle.containsKey("DESTINATION")) {
                    String source = bundle.getString("SOURCE");
                    String destination = bundle.getString("DESTINATION");
                    Long id = PreferenceManager.getDefaultSharedPreferences(getActivity()).getLong("ID", 123);
                    new PostTask(getActivity()).execute(new String[]{id.toString(), source, destination});
                }
            }
            return rootView;
        }
    }

    public static class PostTask extends AsyncTask<String, Void, Integer> {
        ProgressDialog dialog;
        Activity activity;

        String URL = "http://supertaxi.herokuapp.com/client";

        String LOG_TAG = PostTask.class.getSimpleName();

        public PostTask(Activity activity) {
            super();
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
        }

        @Override
        protected void onCancelled(Integer integer) {
            super.onCancelled(integer);
            if (dialog != null && dialog.isShowing()) dialog.dismiss();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            if (dialog != null && dialog.isShowing()) dialog.dismiss();
        }

        @Override
        protected Integer doInBackground(String... params) {
            Uri uri = Uri.parse(URL).buildUpon().appendQueryParameter("id", params[0])
                    .appendQueryParameter("source", params[1])
                    .appendQueryParameter("destination", params[2])
                    .build();

            String result = null;
            try {
                URL url = new URL(uri.toString());
                Log.d(LOG_TAG, url.toString());

                HttpGet get = new HttpGet(url.toString());

                HttpParams httpParams = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParams, 10000);

                HttpClient client = new DefaultHttpClient(httpParams);

                HttpResponse response = client.execute(get);

                if(response == null) return null;
                HttpEntity entity = response.getEntity();
                if(entity == null) return null;
                if(response.getStatusLine().getStatusCode() == 200) {
                    result = EntityUtils.toString(entity);
                    Log.i(LOG_TAG, result);
                    Log.i(LOG_TAG, "Got data successfully");
                }
            }
            catch (ConnectTimeoutException cte) {
                cte.printStackTrace();
                Log.i(LOG_TAG, "Connection timed out");
            }
            catch (SocketTimeoutException ste) {
                Log.i(LOG_TAG, "Socket timed out");
            }
            catch (Exception ex) {
                ex.printStackTrace();
                Log.i(LOG_TAG, "Fetching data failed");
                Log.d(LOG_TAG, ex.getMessage());
            }
            if (result == null) return null;
            return Integer.valueOf(result);
        }
    }
}
