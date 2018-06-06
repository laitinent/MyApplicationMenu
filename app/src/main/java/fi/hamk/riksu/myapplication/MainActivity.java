package fi.hamk.riksu.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    RecyclerView rv;
    //String s1[];
    MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rv = findViewById(R.id.recycler);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        JSONArray rooms = new JSONArray();
        rooms.put("Ri-KA-C214");  // TODO: example, get from settings
        /*
        * Example:
        * {
             "startDate": "2018-05-06T13:30",
            "endDate": "2018-06-06T13:30",
            "room": [
                "Ri-KA-C214"
            ]
            }
        * */
        JSONObject jsonBody=null;
        try {
            // Search for given student group current week schedule
            jsonBody = new JSONObject();
            jsonBody.put("startDate", "2018-05-06T13:30");//RecViewHelper.getCurrentDateString(0));
            jsonBody.put("endDate", RecViewHelper.getCurrentDateString(7));
            jsonBody.put("room", rooms);
        } catch (JSONException ex) {
            System.err.println(ex.getMessage());
        }
        //TODO: check URL
        final GsonPostRequest jsObjRequest = new GsonPostRequest<>(RecViewHelper.RESERVATIONS_URL, Reservations.class, jsonBody,
                response -> {
                    myAdapter = new MyAdapter(MainActivity.this, response);
                    rv.setAdapter(myAdapter);
                    rv.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                },
                error -> RecViewHelper.printToastErr(MainActivity.this, error.getMessage())
        );

        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
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
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
