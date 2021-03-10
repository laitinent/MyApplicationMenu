package fi.hamk.riksu.myapplication

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import fi.hamk.riksu.myapplication.RecViewHelper.getCurrentDateString
import fi.hamk.riksu.myapplication.building.BuildingData
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface SearchService {
    @POST("reservation/search")
    fun searchReservations(@Body search: MySearch /*@Path("user") user: String?*/): Call<Reservations>?
}

interface BuildingsService {
    @GET("reservation/building")
    fun listRepos(): Call<Buildings>?
}

interface BuildingsServiceRMK {
    @GET("reservation/building/38477")
    fun listBuilding(): Call<BuildingData>?
}

// note: var names must match those of the request
class MySearch(var room: Array<String?>, var startDate: String, var endDate: String)

class BasicAuthInterceptor(username: String, password: String) : Interceptor {
    private var credentials: String = Credentials.basic(username, password)

    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        var request = chain.request()
        request = request.newBuilder().header("Authorization", credentials).build()
        return chain.proceed(request)
    }
}


class MainActivity : AppCompatActivity(R.layout.activity_main) {

    //private lateinit var rv: RecyclerView
    //String s1[];
    private lateinit var myAdapter: MyAdapter
    private var msFor30min = 1000 * 60 * 30
    private var bReservations:Boolean=false

    private lateinit var prefs: SharedPreferences
    private var prefKey = "example_text"
    private lateinit var strDefaultRoom: String
    var roomIndex=0

    fun findRoom(index:Int): String {
        val prefix=resources.getString(R.string.pref_default_display_name).substring(0,6)
        val roomsList = listOf("A102","A103","A104","A114","C111","C114",
                "B104","B121","B125","B126","D106","D107","D110","D112","D114",
                "A201","A205","B201","B204","B207","B208","B208","B212","B214","B216","B219",
                "D205","D206","D207","D208","D209","D210","D211"
        )
        return prefix+roomsList[index%roomsList.size]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)  // in class def
        //rv = findViewById(R.id.recycler)
        //val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)




        prefs = PreferenceManager.getDefaultSharedPreferences(this)
        strDefaultRoom = resources.getString(R.string.pref_default_display_name)//"RI-Ka-C214";

        this.title = prefs.getString(prefKey, strDefaultRoom)

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener { _ ->
            //getReservationsFromPrefs()
            bReservations = getData(!bReservations) // toggle request
        }

        //TODO: read prefs

        //String strRoom = prefs.getString(pref_key, strDefaultRoom);
        //prefs.registerOnSharedPreferenceChangeListener { _, _ -> getReservationsFromPrefs() }

        // TODO: test if needed
        //THIS WAS THE ORIGINAL //getReservationsFromPrefs() //getRoomReservations(strRoom);
        getData(bReservations);
    }

    var roomsDataList = mutableListOf<fi.hamk.riksu.myapplication.building.Resource>()

    private fun getData( bReservations: Boolean = false) : Boolean{

        val client = OkHttpClient.Builder()
                .addInterceptor(BasicAuthInterceptor("NQFytJqel1hXIaeqsh4D", ""))
                .build()
        val retrofit = Retrofit.Builder()
                .baseUrl(RecViewHelper.URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

        // Request
        if (bReservations) {
            // get RMK room details on first call
            val serviceB = retrofit.create(BuildingsServiceRMK::class.java)
            val callB = serviceB.listBuilding()

            // Response
            callB?.enqueue(object : Callback<BuildingData> {

                override fun onResponse(call: Call<BuildingData>, response: Response<BuildingData>) {
                    if (response.code() == 200) {    // = HTTP OK
                        val portsResponse = response.body()!!
                        roomsDataList.addAll(portsResponse.resources)
                    }
                }
                override fun onFailure(call: Call<BuildingData>, t: Throwable) {
                    RecViewHelper.printToastErr(this@MainActivity, "Request Error")
                }
            })

            // WAIT UNTIL ALL ROOM DATA ACQUIRED
            if(roomsDataList.isEmpty())return(bReservations)

            val thisRoom = findRoom(roomIndex)
            this.title = thisRoom
            val rooms2 = arrayOf(prefs.getString(prefKey, thisRoom))//strDefaultRoom))
            roomIndex++
            val mySearch = MySearch(rooms2, getCurrentDateString(0), getCurrentDateString(7))

            val service = retrofit.create(SearchService::class.java)
            val call = service.searchReservations(mySearch /* parametrit */)

            // Find reservations
            call?.enqueue(object : Callback<Reservations> {

                override fun onResponse(call: Call<Reservations>, response: Response<Reservations>) {
                    if (response.code() == 200) {    // = HTTP OK

                        val theResponse = response.body()!!
                        // additional room info
                        val room = roomsDataList.find { it.code.equals(thisRoom, ignoreCase = true) }
                        val s = if(room?.name == null || room.name.isEmpty())
                        {
                            room?.resourceType
                        }
                        else room.name

                        if(s!=null) { this@MainActivity.title = "$thisRoom $s" }

                        if (theResponse.reservations?.size == 0) {
                            Toast.makeText(this@MainActivity, "No reservations", Toast.LENGTH_SHORT).show()
                        }
                        myAdapter = MyAdapter(this@MainActivity, theResponse.reservations)// as Reservations)
                        recycler.adapter = myAdapter
                        recycler.layoutManager = LinearLayoutManager(this@MainActivity)
                    }
                }

                // virhetilanne
                override fun onFailure(call: Call<Reservations>, t: Throwable) {
                    RecViewHelper.printToastErr(this@MainActivity, "Request Error")
                }
            })
        } else {   // buildings
            // alternate data about all buildings
            val service = retrofit.create(BuildingsService::class.java)
            val call = service.listRepos()

            // Response
            call?.enqueue(object : Callback<Buildings> {

                override fun onResponse(call: Call<Buildings>, response: Response<Buildings>) {
                    if (response.code() == 200) {    // = HTTP OK
                        // tässä portsResponse on tyyppiä Ports
                        val portsResponse = response.body()!!
                        // kullakin rajapinnalla on omat propertyt vastauksessa(response), tässä .features...

                        myAdapter = MyAdapter(this@MainActivity, portsResponse.resources)// as Reservations)
                        recycler.adapter = myAdapter
                        recycler.layoutManager = LinearLayoutManager(this@MainActivity)
                    }
                }

                override fun onFailure(call: Call<Buildings>, t: Throwable) {
                    RecViewHelper.printToastErr(this@MainActivity, "Request Error")
                }
            })
        }
        return bReservations
    }


        /**
         * Get reservations to fill rc view, with default parameter

        private fun getReservationsFromPrefs() {
        getRoomReservations(prefs.getString(prefKey, strDefaultRoom))
        }

        /**
         * Get data from REST API and show in recycleview
         * @param strRoom - Room to search for
        */
        private fun getRoomReservations(strRoom: String?) {
        val rooms = JSONArray()
        var rooms2 = arrayOf(strRoom)
        rooms.put(strRoom)//"Ri-KA-C214");  // TODO: example, get from settings
        /*
         * Example:
         * {
        "startDate": "2018-05-06T13:30",
        "endDate": "2018-06-06T13:30",
        "room": [
        "RI-Ka-C214"
        ]
        }
         * */
        var jsonBody: JSONObject? = null
        try {
        // Search for given student group current week schedule
        jsonBody = JSONObject()
        with(jsonBody) {
        put("startDate", getCurrentDateString(0))
        put("endDate", getCurrentDateString(7))
        put("room", rooms)

        }
        val mySearch = MySearch(rooms2, getCurrentDateString(0),getCurrentDateString(7) )
        } catch (ex: JSONException) {
        System.err.println(ex.message)
        }

        //TODO: check URL (was GsonRequest<Reservations>)
        val jsObjRequest = GsonRequest(RecViewHelper.RESERVATIONS_URL, Reservations::class.java, jsonBody!!,
        { response ->

        myAdapter = MyAdapter(this@MainActivity, response.reservations)// as Reservations)
        recycler.adapter = myAdapter
        recycler.layoutManager = LinearLayoutManager(this@MainActivity)
        },
        { _ -> RecViewHelper.printToastErr(this@MainActivity, "Request Error") } //error.message.toString()) }
        )

        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest)

        handler.postDelayed(runnable, msFor30min.toLong())
        }*/


        override fun onCreateOptionsMenu(menu: Menu): Boolean {
            // Inflate the menu; this adds items to the action bar if it is present.
            menuInflater.inflate(R.menu.menu_main, menu)
            return true
        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            val id = item.itemId

            if (id == R.id.action_settings) {
                val intent = Intent(this, SettingsActivity2::class.java)
                startActivity(intent)
                return true
            }

            return super.onOptionsItemSelected(item)
        }
    }
