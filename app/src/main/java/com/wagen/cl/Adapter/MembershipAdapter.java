package com.wagen.cl.Adapter;

/**
 * Created by zhan on 4/19/2018.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wagen.cl.Activity.MembershipActivity;
import com.wagen.cl.Activity.MybookingActivity;
import com.wagen.cl.Model.MembershipModel;
import com.wagen.cl.Model.OrderModel;
import com.wagen.cl.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MembershipAdapter extends BaseAdapter {
    private MembershipActivity _context;
    private Boolean purcahsestatus = false;

    private ArrayList<MembershipModel> navitems = new ArrayList<>();
    String usertype="";

    public MembershipAdapter(MembershipActivity context, ArrayList<MembershipModel> orders, Boolean purchasestatus1) {
        super();
        this._context = context;
        this.navitems = orders;
        this.purcahsestatus = purchasestatus1;
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
            convertView = inflater.inflate(R.layout.membershipitem, parent, false);

            holder.txvtitle = (TextView) convertView.findViewById(R.id.txv_title);
            holder.txvprice=(TextView) convertView.findViewById(R.id.txv_price);
            holder.txvpurchasedate=(TextView) convertView.findViewById(R.id.txv_purchasedate);
            holder.txvavailabelcount=(TextView)convertView.findViewById(R.id.txv_availableorder);
            holder.txvpurchasenow =(TextView)convertView.findViewById(R.id.txv_purcahsenow);
            holder.lyt_purchasedetail =(LinearLayout)convertView.findViewById(R.id.lyt_purchasedetail);

            convertView.setTag(holder);
        } else {
            holder = (CustomHolder) convertView.getTag();
        }


        final MembershipModel trip1 = (MembershipModel) navitems.get(position);
        if(trip1.purchase_status){
            holder.txvpurchasenow.setVisibility(View.GONE);
            holder.lyt_purchasedetail.setVisibility(View.VISIBLE);
        }else{
            //holder.txvpurchasenow.setVisibility(View.VISIBLE);
            if(purcahsestatus == true) holder.txvpurchasenow.setVisibility(View.GONE);
            else holder.txvpurchasenow.setVisibility(View.VISIBLE);
            holder.lyt_purchasedetail.setVisibility(View.GONE);
        }

        holder.txvtitle.setText(trip1.title);
        holder.txvprice.setText("$"+chagnenumberformat(trip1.price));
        holder.txvavailabelcount.setText(" "+trip1.remaining_order);
        holder.txvpurchasedate.setText(" "+changedateformattoappstyle(trip1.renew_date));

        holder.txvpurchasenow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return convertView;
    }


    public class CustomHolder {
        public TextView txvtitle, txvprice, txvpurchasedate, txvavailabelcount, txvpurchasenow;
        public LinearLayout lyt_purchasedetail;

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
