package fi.hamk.riksu.myapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {
    Context c;
    String s1[];// CUSTOM
    Reservations reservations;

    public MyAdapter(Context ctx, Reservations reservations){       // s[]) {
        c = ctx;
        //s1=s;// CUSTOM
        this.reservations = reservations;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInf = LayoutInflater.from(c);
        View v = mInf.inflate(R.layout.my_row,parent,false);// CUSTOM my_row
        return new MyHolder(v);
    }
    //TODO: reservations
    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
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

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView t1;// CUSTOM
        TextView tMore;// CUSTOM
        public MyHolder(View itemView) {
            super(itemView);
            t1 = itemView.findViewById(R.id.textView);// CUSTOM
            tMore = itemView.findViewById(R.id.textViewMore);// CUSTOM
        }
    }
}
