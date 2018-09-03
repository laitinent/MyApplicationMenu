package fi.hamk.riksu.myapplication;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.Preference;
import android.preference.PreferenceManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.prefs.Preferences;

public class MainActivity extends AppCompatActivity {

    RecyclerView rv;
    //String s1[];
    MyAdapter myAdapter;
    int msFor30min = 1000*60*30;
    private Handler handler = new Handler();

    SharedPreferences prefs;
    String pref_key = "example_text";
    String strDefaultRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rv = findViewById(R.id.recycler);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        strDefaultRoom = getResources().getString(R.string.pref_default_display_name);//"RI-Ka-C214";

        this.setTitle(prefs.getString(pref_key, strDefaultRoom));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            GetReservationsFromPrefs();
            //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        });

        //TODO: read prefs

        //String strRoom = prefs.getString(pref_key, strDefaultRoom);
        prefs.registerOnSharedPreferenceChangeListener((sharedPreferences, s) -> {
            GetReservationsFromPrefs();
        });

        // TODO: test if needed
        GetReservationsFromPrefs(); //getRoomReservations(strRoom);
    }

    /**
     * Get reservations to fill rc view, with default parameter
     */
    private void GetReservationsFromPrefs() {
        getRoomReservations( prefs.getString(pref_key, strDefaultRoom));
    }

    /**
     * Get data from REST API and show in recycleview
     * @param strRoom - Room to search for
     */
    private void getRoomReservations(String strRoom) {
        JSONArray rooms = new JSONArray();
        rooms.put(strRoom);//"Ri-KA-C214");  // TODO: example, get from settings
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
            jsonBody.put("startDate", RecViewHelper.getCurrentDateString(0));
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

        handler.postDelayed(runnable, msFor30min);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            /* do what you need to do */
            GetReservationsFromPrefs();
            /* and here comes the "trick" */
            handler.postDelayed(this, msFor30min);
        }
    };


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
