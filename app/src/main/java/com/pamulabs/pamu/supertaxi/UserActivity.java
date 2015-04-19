package com.pamulabs.pamu.supertaxi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.Driver;
import java.util.ArrayList;


public class UserActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user, menu);
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

        Spinner source;
        Spinner destination;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_user, container, false);

            Button done = (Button) rootView.findViewById(R.id.source_destination_done);

            if (getActivity().getIntent() != null && getActivity().getIntent().getExtras() != null) {
                Intent intent = getActivity().getIntent();
                Bundle bundle = intent.getExtras();
                if (bundle.containsKey("ID")) {
                    long id = bundle.getLong("ID");

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putLong("ID", id);
                    editor.commit();

                    Toast.makeText(getActivity(), "id: -"+id, Toast.LENGTH_SHORT).show();
                }
            }

            source = (Spinner) rootView.findViewById(R.id.source);
            destination = (Spinner) rootView.findViewById(R.id.destination);

            ArrayList<String> cities = new ArrayList<>();
            cities.add("Hyderabad");
            cities.add("Delhi");
            cities.add("Banglore");
            cities.add("Mumbai");
            cities.add("Chennai");
            cities.add("Mandi");

            ArrayAdapter<String> arrayAdapter =
                    new ArrayAdapter<>(getActivity(),
                            android.R.layout.simple_spinner_item, cities);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            source.setAdapter(arrayAdapter);
            destination.setAdapter(arrayAdapter);

            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (source.getSelectedItemId() != source.getSelectedItemId()) {

                        Intent intent = new Intent(getActivity(), DriverActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("SOURCE", (String) source.getSelectedItem());
                        bundle.putString("DESTINATION", (String) destination.getSelectedItem());
                        intent.putExtras(bundle);

                        startActivity(intent);
                    } else {
                        Toast.makeText(getActivity(), "source and destination cannot be same !!!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            return rootView;
        }
    }
}
