package fi.hamk.riksu.myapplication

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
/*
import org.joda.time.DateTime
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormat.forPattern
import org.joda.time.format.DateTimeFormatter
*/
import java.io.UnsupportedEncodingException
import java.nio.charset.StandardCharsets
import java.nio.charset.StandardCharsets.*
import java.time.LocalDate

//import java.time // API 26
import java.time.format.DateTimeFormatter.ofPattern

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter

/**
 * Created by tlaitinen on 9.12.2016.
 * Custom helper functions and constants
 */

object RecViewHelper {
    const val URL_BASE = "https://opendata.hamk.fi:8443/r1/"
    var RESERVATIONS_BUILDING_URL = "reservation/building"
    var RESERVATIONS_ALL_URL = URL_BASE + RESERVATIONS_BUILDING_URL
    var RESERVATIONS_SEARCH_URL = "reservation/search"
    var RESERVATIONS_URL = URL_BASE + RESERVATIONS_SEARCH_URL

    //public static String REALIZATIONS_URL = URL_BASE + "realization/search";
    //public static String CURRICULUMS_URL = URL_BASE + "curriculum/search";
    //static String COURSEUNIT_URL = URL_BASE + "courseunit/";

    /**
     * Get current date as string
     *
     * @param days days to add to current
     * @see https://developer.android.com/reference/java/time/format/DateTimeFormatter
     * @return date as json compatible string format
     */
    fun getCurrentDateString(days: Long): String {
        //    Calendar calendar = new GregorianCalendar();
        //    calendar.setTime(now);
        var now:LocalDateTime  = LocalDateTime.now().plusDays(days) // API 26
        //val now :LocalDateTime = LocalDateTime().plusDays(days) // joda-time -kirjastosta
        //val fmt = forPattern("yyyy-MM-dd'T'HH:mm") // yoda
        //return fmt.print(now)//.split(".")[0];
        return now.format(ofPattern("yyyy-MM-dd'T'HH:mm"))
        //return now.toString(fmt)
        /*
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        //    sdf.setCalendar(calendar);
        return sdf.format(now);
*/
        /*
        StringBuilder s = new StringBuilder();
        s.append(calendar.get(Calendar.YEAR));
        s.append("-");
        s.append(String.format("%02d",(1+calendar.get(Calendar.MONTH))));
        s.append("-");
        s.append(String.format("%02d",calendar.get(Calendar.DAY_OF_MONTH)));
        s.append("T00:00");
        return s.toString();*/
    }

    /**
     * format date from yyyy-MM-dd'T'HH:mm to dd.MM.yy
     *
     * @param preformatted date string in input format
     * @param b default true to use date, false to use time
     * @return date string in output format
     */
    fun formatDate(preformatted: String?, vararg b: Boolean): String {
        val startDate: LocalDateTime
        val outfmt: DateTimeFormatter
        val outfmtTime: DateTimeFormatter
        val bUseDate = b.isEmpty() || b[0]
        try {
            // Populate the data into the template view using the data object
            val fmt =ofPattern("yyyy-MM-dd'T'HH:mm:ss") // forPattern("yyyy-MM-dd'T'HH:mm:ss")// :ss lisätty
            outfmt = ofPattern("dd.MM.yy") // forPattern("dd.MM.yy")
            outfmtTime = ofPattern("HH:mm") // forPattern("HH:mm")
            startDate = LocalDateTime.parse(preformatted,fmt)//fmt.parseDateTime(preformatted)//user.getStartDate());
        } catch (ex: UnsupportedOperationException) {
            return "Date format issue"
        } // - if parsing is not supported
        catch (ex2: IllegalArgumentException) {
            return "Aika ei saatavilla"
        }
        // - if the text to parse is invalid
        return if (bUseDate)
            startDate.format(outfmt) //toString(outfmt)//tvHome.setText(startDate.toString(outfmt));
        else
            startDate.format(outfmtTime) //toString(outfmtTime)
    }

    /**
     * Print message in as toast and console
     *
     * @param me  Context e.g. activity
     * @param msg Message to print
     */
    fun printToastErr(me: Context, msg: String) {
        System.err.println(msg)
        Toast.makeText(me, "Virhe: $msg", Toast.LENGTH_LONG).show()
    }

    /**
     * Test if code format matches HAMK student group code
     * @param code - code to test
     * @return - true on match
     */
    fun isStudentGroupCode(code: String): Boolean {
        // INTINU15A6, INTIP17A6
        return code.matches("\\w\\w\\w\\w\\w\\d\\d\\w\\d|\\w\\w\\w\\w\\w\\w\\d\\d\\w\\d".toRegex())
    }

    /**
     * @param message              Message
     * @param title                Title
     * @param ctx                  Context
     * @param strUseNegativeButton (Optional)Set button text to use negative button
     */
    internal fun ShowAlertDialog(message: String, title: String, ctx: Context, vararg strUseNegativeButton: String) {
        //var subject:String
        //var caption:String

        val alertDialogBuilder = AlertDialog.Builder(ctx)
        // set title

        // set dialog message

        val caption = String(title.toByteArray(ISO_8859_1), UTF_8)
        val subject = String(message.toByteArray(ISO_8859_1), UTF_8)

        alertDialogBuilder.setTitle(caption)
        alertDialogBuilder
                .setMessage(subject)
                .setCancelable(false)
                .setPositiveButton("OK") { dialog, _id ->
                    // if this button is clicked, close current activity
                    dialog.dismiss()
                    //MainActivity.this.finish();
                }

        if (strUseNegativeButton.isNotEmpty()) {
            alertDialogBuilder.setNegativeButton(strUseNegativeButton[0]) { dialog, _id ->
                // if this button is clicked, just close the dialog box and do nothing
                dialog.cancel()
            }
        }
        // create alert dialog
        val alertDialog = alertDialogBuilder.create()
        // show it
        alertDialog.show()
    }

    /**
     * @param context Activity/Context
     * @param view    View that had foxus
     */
    internal fun hideKeyboardFrom(context: Context, view: View) {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
