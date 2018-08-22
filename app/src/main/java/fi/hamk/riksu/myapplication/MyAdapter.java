package fi.hamk.riksu.myapplication;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {
    private Context c;
    String s1[];// CUSTOM
    private Reservations reservations;

    MyAdapter(Context ctx, Reservations reservations){       // s[]) {
        c = ctx;
        //s1=s;// CUSTOM
        this.reservations = reservations;
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
        holder.tMore.setText(r.getSubject());// CUSTOMized
        StringBuilder s;
        s = new StringBuilder(RecViewHelper.formatDate(r.getStartDate()));
        s.append(" klo ");
        s.append(RecViewHelper.formatDate(r.getStartDate(),false));
        s.append(" - ");
        s.append(RecViewHelper.formatDate(r.getEndDate(),false));// End date defaults same as start date
        holder.t1.setText(s.toString() );// CUSTOMized
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
