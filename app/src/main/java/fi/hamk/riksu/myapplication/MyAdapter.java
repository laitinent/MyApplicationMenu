package fi.hamk.riksu.myapplication;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.joda.time.DateTime;

import java.util.List;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {
    private Context c;
    //String s1[];// CUSTOM
    private Reservations reservations;
    String timeprefix;

    MyAdapter(Context ctx, Reservations reservations){       // s[]) {
        c = ctx;
        //s1=s;// CUSTOM
        this.reservations = reservations;
        timeprefix=c.getString(R.string.item_time_prefix);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater mInf = LayoutInflater.from(c);
        View v = mInf.inflate(R.layout.my_row,parent,false);// CUSTOM my_row
        return new MyHolder(v);
    }
    //TODO: reservations
    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        Reservation r = reservations.getReservations().get(position);
        List<ReservationResource> lista = r.getResources();

        String strGroups="";
        for (ReservationResource rr: lista ) {
            if(rr.getType().contains("student_group"))strGroups += (" "+rr.getName());
        }

        holder.tMore.setText(r.getSubject()+strGroups);// CUSTOMized
        StringBuilder s;
        s = new StringBuilder(RecViewHelper.formatDate(r.getStartDate()));
        s.append(" "+timeprefix+" ");   //s.append(" klo ");
        s.append(RecViewHelper.formatDate(r.getStartDate(),false));
        s.append(" - ");
        s.append(RecViewHelper.formatDate(r.getEndDate(),false));// End date defaults same as start date
        holder.t1.setText(s.toString() );// CUSTOMized

        // bold if today
        DateTime dt = new DateTime(r.getStartDate());
        DateTime now = DateTime.now();
        if(dt.toLocalDate().isEqual(now.toLocalDate()))
        {
            holder.t1.setTypeface(holder.t1.getTypeface(), Typeface.BOLD);
        }
    }

    @Override
    public int getItemCount() {
        return reservations.getReservations().size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView t1;// CUSTOM
        TextView tMore;// CUSTOM
        MyHolder(View itemView) {
            super(itemView);
            t1 = itemView.findViewById(R.id.textView);// CUSTOM
            tMore = itemView.findViewById(R.id.textViewMore);// CUSTOM
        }
    }
}
