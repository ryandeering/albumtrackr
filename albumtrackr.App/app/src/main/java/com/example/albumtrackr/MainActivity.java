// add Internet permission

package com.example.albumtrackr;

import android.content.Context;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.util.Log;
import com.google.gson.*;
import com.android.volley.*;
import com.android.volley.toolbox.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;


// in build.gradle for Module:app
// implementation 'com.android.volley:volley:1.1.1'
// implementation 'com.google.code.gson:gson:2.8.5'

public class MainActivity extends AppCompatActivity {

    // uri of RESTful service on Azure, note: https, cleartext support disabled by default
    private String SERVICE_URI = "https://albumtrackrapi.azurewebsites.net/api/Album?Name=halfaxa&Artist=grimes";          // or https
    private String TAG = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // floating action button, call the service
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                callService(view);
            }
        });
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

        return super.onOptionsItemSelected(item);
    }

    // call RESTful service using volley and display results
    public void callService(View v)
    {
        // get TextView for displaying result
        final TextView outputTextView = (TextView) findViewById(R.id.outputTextView);

        try
        {
            // make a string request (JSON request an alternative)
            RequestQueue queue = Volley.newRequestQueue(this);
            Log.d(TAG, "Making request");
            try
            {
                StringRequest strObjRequest = new StringRequest(Request.Method.POST, SERVICE_URI,
                        new Response.Listener<String>()
                        {
                            @Override
                            public void onResponse(String response)
                            {
                                // parse resulting string containing JSON to Greeting object

                                // model = new Gson().fromJson(response, Model.class);
                                // outputTextView.setText(model.toString());
                                // Log.d(TAG, "Displaying data" + model.toString());

            // List BEGIN
                                Type listType = new TypeToken<List<Model>>() {}.getType();
                                List<Model> yourList = new Gson().fromJson(response.toString(), listType);



                                CustomListAdapter adapter = new CustomListAdapter(this, yourList);
                                ListView listView = (ListView) findViewById(R.id.list);

                                listView.setAdapter(adapter);
                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error)
                            {
                                outputTextView.setText(error.toString());
                                Log.d(TAG, "Error" + error.toString());
                            }
                        });
                queue.add(strObjRequest);           // can have multiple in a queue, and can cancel
            }
            catch (Exception e1)
            {
                Log.d(TAG, e1.toString());
                outputTextView.setText(e1.toString());
            }
        }
        catch (Exception e2)
        {
            Log.d(TAG, e2.toString());
            outputTextView.setText(e2.toString());
        }
    }


    // Adapter for list

    public class CustomListAdapter extends BaseAdapter {
        private Context activity;
        private LayoutInflater inflater;
        private List<Model> items;

        public CustomListAdapter(Response.Listener<String> activity, List<Model> items) {
            this.activity = (Context) activity;
            this.items = items;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int location) {
            return items.get(location);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (inflater == null)
                inflater = (LayoutInflater) activity
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null)
                convertView = inflater.inflate(R.layout.list_row, null);

            TextView name = (TextView) convertView.findViewById(R.id.outputTextView);

            // getting album data for the row
            Model m = items.get(position);

            name.setText(m.toString());

            return convertView;


        }}}