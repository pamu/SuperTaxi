package com.pamulabs.pamu.supertaxi;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

        Button userRegister;
        Button driverRegister;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            userRegister = (Button) rootView.findViewById(R.id.register_as_user);
            driverRegister = (Button) rootView.findViewById(R.id.register_as_driver);

            userRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog(1);
                }
            });

            driverRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            return rootView;
        }

        public void dialog(final int id) {
            final Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.register_dialog);
            dialog.setTitle("Register");
            EditText editText = (EditText) dialog.findViewById(R.id.register_editText);
            Button done = (Button) dialog.findViewById(R.id.done);
            final String idString = editText.getText().toString();
            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(idString)) {
                        try {
                            long personId = Long.valueOf(idString);
                            Bundle bundle = new Bundle();
                            bundle.putLong("ID", personId);
                            if (id == 1) {
                                Intent intent = new Intent(getActivity(), UserActivity.class);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(getActivity(), DriverActivity.class);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        } catch (NumberFormatException ex) {
                            Toast.makeText(getActivity(), "Number Format Exception", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
            dialog.show();
        }
    }
}
