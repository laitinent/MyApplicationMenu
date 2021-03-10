package fi.hamk.riksu.myapplication

import android.content.Context
import androidx.recyclerview.widget.RecyclerView

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.extensions.LayoutContainer
import fi.hamk.riksu.myapplication.reservations.*
//import org.joda.time.DateTime

import kotlinx.android.synthetic.main.my_row.view.*
import java.time.LocalDateTime

class MyAdapter(private val c: Context, //String s1[];// CUSTOM
                 private val reservations: List<Any>? /*private val reservations: List<Reservation>?  */)
                 : RecyclerView.Adapter<MyAdapter.MyHolder>() {
    private val timeprefix = c.getString(R.string.item_time_prefix)

    init {       // s[]) {
    }//s1=s;// CUSTOM

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val mInf = LayoutInflater.from(c)
        val v = mInf.inflate(R.layout.my_row, parent, false)// CUSTOM my_row
        return MyHolder(v, parent)
    }

    //TODO: reservations
    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        reservations?.get(position)?.let { holder.bindReservations(it, timeprefix) }
    }

    override fun getItemCount(): Int {
        return reservations!!.size
    }

    class MyHolder(itemView: View, override val containerView: View?) : RecyclerView.ViewHolder(itemView), LayoutContainer {
        var t1: TextView = itemView.textView //findViewById(R.id.textView)// CUSTOM
        var tMore: TextView = itemView.textViewMore //findViewById(R.id.textViewMore)// CUSTOM

        fun bindReservations(r: Any/*Resource  Reservation */, timeprefix: String) {
            //val r:Reservation = reservations.reservations!![position]
            with(r) {
                if (r is Resource) {
                    val lista = this as Resource//resources

                    /*holder.*/ tMore.text = name //strGroups.insert(0, subject).toString()// CUSTOMized

                    /*holder.*/itemView.textView.text = code //s.toString()// was t1, CUSTOMized

                } else // Reservation
                {
                    val lista = this as Reservation

                    val strGroups = StringBuilder()
                    lista.resources?.forEach { rr -> // }
                        //for (rr in lista!!) {
                        if (rr.type!!.contains("student_group")) strGroups.append(" ").append(rr.name)
                    }

                    /*holder.*/ tMore.text = strGroups.insert(0, subject).toString()// CUSTOMized

                    val s: StringBuilder = StringBuilder(RecViewHelper.formatDate(startDate))
                    with(s) {
                        append(" ").append(timeprefix).append(" ")   //s.append(" klo ");
                        append(RecViewHelper.formatDate(startDate, false))
                        append(" - ")
                        append(RecViewHelper.formatDate(endDate, false))// End date defaults same as start date
                    }
                    /*holder.*/itemView.textView.text = s.toString()// was t1, CUSTOMized

                    // bold if today
                    val dt = LocalDateTime.parse(startDate) //DateTime(startDate)
                    val now = LocalDateTime.now()//DateTime.now()
                    if (dt.toLocalDate().isEqual(now.toLocalDate())) {
                        /*holder.*/t1.setTypeface(/*holder.*/t1.typeface, Typeface.BOLD)
                    }

                }
            }
        }
    }
}
