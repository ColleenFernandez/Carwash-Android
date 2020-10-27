package com.wagen.cl.Adapter;

/**
 * Created by zhan on 4/19/2018.
 */

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wagen.cl.Activity.MybookingActivity;
import com.wagen.cl.Model.OrderModel;
import com.wagen.cl.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class BookingAdapter extends BaseAdapter {
    private MybookingActivity _context;

    private ArrayList<OrderModel> navitems = new ArrayList<>();
    String usertype="";

    public BookingAdapter(MybookingActivity context, ArrayList<OrderModel> orders) {
        super();
        this._context = context;
        this.navitems = orders;
    }



    @Override
    public int getCount() {
        return navitems.size();
    }

    @Override
    public Object getItem(int position) {
        return navitems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final CustomHolder holder;


        if (convertView == null) {
            holder = new CustomHolder();

            LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.bookingitem, parent, false);

            holder.txvtime = (TextView) convertView.findViewById(R.id.txv_time);
            holder.txvdate=(TextView) convertView.findViewById(R.id.txv_date);
            holder.txvduration=(TextView) convertView.findViewById(R.id.txv_duration);
            holder.txvprice=(TextView)convertView.findViewById(R.id.txv_price);

            convertView.setTag(holder);
        } else {
            holder = (CustomHolder) convertView.getTag();
        }


        final OrderModel trip1 = (OrderModel) navitems.get(position);

        holder.txvtime.setText(trip1.order_time);
        holder.txvdate.setText(changedateformattoappstyle(trip1.order_date));
        holder.txvprice.setText(" : "+chagnenumberformat(trip1.total_price)+" CLP");
        holder.txvduration.setText(" : "+trip1.duration_time+"MINS");

        return convertView;
    }


    public class CustomHolder {
        public TextView txvdate, txvtime, txvduration, txvprice;

    }

    public String changedateformattoappstyle(String date){ // from 2018-12-28 to 12/28/2018
        String[] datearray=date.split("-");
        String newformate=datearray[2]+"/"+datearray[1]+"/"+datearray[0];
        return newformate;
    }
    public String chagnenumberformat(String number){

        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String yourFormattedString = formatter.format(Long.parseLong(String.valueOf(number)));
        return  yourFormattedString.replaceAll(",",".");
    }


}
