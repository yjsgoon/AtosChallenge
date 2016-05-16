package com.raon.im.listview;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.raon.im.connection.ConnectIntentService;
import com.raon.im.database.DataField;
import com.raon.im.database.DatabaseSource;
import com.raon.lee.im.R;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by EunBin on 2016-03-11.
 *
 * This is the adapter class for the company list.
 */
public class CompanyListAdapter extends BaseAdapter {
    private static final String TAG = "CompanyListAdapter";

    private ArrayList<CompanyData> datas;
    private LayoutInflater inflater;

    public CompanyListAdapter(LayoutInflater inflater, ArrayList<CompanyData> datas) {
        this.datas = datas;
        this.inflater = inflater;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.companylist_row, null);
        }

        ImageView img_CompanyLogo = (ImageView) convertView.findViewById(R.id.img_CompanyLogo2);
        ImageView img_PermissionItem[] = new ImageView[4];
        img_PermissionItem[0] = (ImageView) convertView.findViewById(R.id.img_OfferItem1);
        img_PermissionItem[1] = (ImageView) convertView.findViewById(R.id.img_OfferItem2);
        img_PermissionItem[2] = (ImageView) convertView.findViewById(R.id.img_OfferItem3);
        img_PermissionItem[3] = (ImageView) convertView.findViewById(R.id.img_OfferItem4);
        LinearLayout linearLayout = (LinearLayout) convertView.findViewById(R.id.linear_OfferItem);
        final TextView txt_DeadLine = (TextView) convertView.findViewById(R.id.txt_TimerDeadline);

        final CompanyData cd = datas.get(position);

        setCompanyLogo(cd.getName(), img_CompanyLogo);

        for (int i = 0; i < cd.getPermissionSize(); i++) {
            if (i > 3) { // when the given data more than equal to 4
                img_PermissionItem[3].setImageResource(R.drawable.permission_ellipsis);
                break;
            }
            setPermissionItem(cd.getPermissionItem(i), img_PermissionItem[i]);
        }

        if(cd.getPermissionSize() < 3) {
            for(int i = cd.getPermissionSize(); i < 4; i++)
                img_PermissionItem[i].setImageResource(R.drawable.permission_none);
        }

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = cd.getPermissionItem(0);
                for(int j = 1; j < cd.getPermissionSize(); j++)
                    str = str + "/" + cd.getPermissionItem(j);

                new AlertDialog.Builder(v.getContext())
                        .setTitle("Permission Items")
                        .setIcon(R.drawable.im_logo)
                        .setPositiveButton("OK",null)
                        .setMessage(cd.getName() + " requires such data.\n" + str)
                        .create().show();
            }
        });

        txt_DeadLine.setText(cd.getTimer());
        txt_DeadLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);

                // timer setting Dialog create
                DatePickerDialog dialog = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int _year, int _month, int _day) {
                        cd.setYear(_year);
                        cd.setMonth(_month + 1);
                        cd.setDay(_day);

                        txt_DeadLine.setText(_year + "-" + (_month+1) + "-" + _day);

                        // notify I'm Server of change in timer data
                        Context context = parent.getContext();
                        DatabaseSource databaseSource = new DatabaseSource(context);
                        DataField dataField = new DataField();
                        try {
                            databaseSource.open();
                            dataField = databaseSource.getData_1();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        Intent connIntent = new Intent(context, ConnectIntentService.class);
                        connIntent.putExtra(ConnectIntentService.REQUEST_STRING, TAG);
                        connIntent.putExtra("service", "expirationreset");
                        connIntent.putExtra("alias", cd.getName());
                        connIntent.putExtra("userID", dataField.getEmail());
                        connIntent.putExtra("year", cd.getYear());
                        connIntent.putExtra("month", cd.getMonth());
                        connIntent.putExtra("date", cd.getDay());
                        context.startService(connIntent);

                        databaseSource.close();

                    }
                }, date.getYear(), date.getMonth(), date.getDay());

                dialog.setTitle("Timer Setting");

                // the maximum timer date is 365 days.
                dialog.getDatePicker().setMinDate(cal.getTimeInMillis());
                cal.add(cal.DATE, 366);
                dialog.getDatePicker().setMaxDate(cal.getTimeInMillis());
                dialog.show();
            }
        });

        return convertView;
    }

    public void clear() {
        datas.clear();
    }

    // show the logo of the company
    public void setCompanyLogo(String CompanyName, View view) {
        ImageView imgView = (ImageView) view;

        switch (CompanyName) {
            case "raon":
                imgView.setImageResource(R.drawable.companylogo_raon);
                break;
            case "dmaps":
                imgView.setImageResource(R.drawable.companylogo_dmaps);
                break;
            case "cmaps":
                imgView.setImageResource(R.drawable.companylogo_cmaps);
                break;
            case "paper":
                imgView.setImageResource(R.drawable.companylogo_paper);
                break;
            case "mock":
                imgView.setImageResource(R.drawable.companylogo_mock);
                break;
            default:
                break;
        }
    }

    // show the right image for the data field
    public void setPermissionItem(String data, View view) {
        ImageView imgView = (ImageView) view;

        switch (data) {
            case "name":
                imgView.setImageResource(R.drawable.permission_name);
                break;
            case "address":
                imgView.setImageResource(R.drawable.permission_address);
                break;
            case "phone":
                imgView.setImageResource(R.drawable.permission_phonenum);
                break;
            case "birthday":
                imgView.setImageResource(R.drawable.permission_birthday);
                break;
            case "email":
                imgView.setImageResource(R.drawable.permission_email);
                break;
            default:
                break;
        }
    }
}