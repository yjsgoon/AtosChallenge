package com.raon.im.listview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.raon.im.connection.ConnectIntentService;
import com.raon.im.database.DataField;
import com.raon.im.database.DatabaseSource;
import com.raon.lee.im.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by EunBin on 2016-03-11.
 *
 * This is the adapter class for the permission list.
 */
public class PermissionListAdapter extends BaseAdapter {
    private static final String TAG = "PermissionListAdapter";

    ArrayList<CompanyData> datas;
    LayoutInflater inflater;

    public PermissionListAdapter(LayoutInflater inflater, ArrayList<CompanyData> datas) {
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
            convertView = inflater.inflate(R.layout.permissionlist_row, null);
        }

        ImageView img_CompanyLogo = (ImageView) convertView.findViewById(R.id.img_CompanyLogo);
        ImageView img_PermissionItem[] = new ImageView[4];
        img_PermissionItem[0] = (ImageView) convertView.findViewById(R.id.img_PermissionItem1);
        img_PermissionItem[1] = (ImageView) convertView.findViewById(R.id.img_PermissionItem2);
        img_PermissionItem[2] = (ImageView) convertView.findViewById(R.id.img_PermissionItem3);
        img_PermissionItem[3] = (ImageView) convertView.findViewById(R.id.img_PermissionItem4);
        LinearLayout linearLayout = (LinearLayout) convertView.findViewById(R.id.linear_PermissionItem);
        final RadioGroup rg_DataProvision = (RadioGroup) convertView.findViewById(R.id.rg_DataProvision);
        final RadioButton rv_DataProvision_Approve = (RadioButton) convertView.findViewById(R.id.rv_DataProvision_Approve);
        final RadioButton rv_DataProvision_Refuse = (RadioButton) convertView.findViewById(R.id.rv_DataProvision_Refuse);

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
                for (int j = 1; j < cd.getPermissionSize(); j++)
                    str = str + "/" + cd.getPermissionItem(j);

                new AlertDialog.Builder(v.getContext())
                        .setTitle("Permission Items")
                        .setIcon(R.drawable.im_logo)
                        .setPositiveButton("OK", null)
                        .setMessage(cd.getName() + " requires such data.\n" + str)
                        .create().show();
            }
        });

        rg_DataProvision.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                Context context = parent.getContext();
                DatabaseSource databaseSource = new DatabaseSource(context);
                DataField dataField = new DataField();
                try {
                    databaseSource.open();
                    dataField = databaseSource.getData_1();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                PermissionListAdapter.this.datas.get(position).setDataProvision(false);
                Intent connIntent = new Intent(context, ConnectIntentService.class);
                connIntent.putExtra(ConnectIntentService.REQUEST_STRING, TAG);
                connIntent.putExtra("service", "imresponse");
                connIntent.putExtra("alias", cd.getName());
                connIntent.putExtra("userID", dataField.getEmail());

                switch (checkedId) {
                    // agree on providing personal data
                    case R.id.rv_DataProvision_Approve:
                        PermissionListAdapter.this.datas.get(position).setDataProvision(true);
                        try {
                            JSONObject jsonObject = new JSONObject();

                            int dataSize = cd.getPermissionSize();
                            for (int i = 0; i < dataSize; i++) {
                                Log.d(TAG, cd.getPermissionItem(i));
                                switch (cd.getPermissionItem(i)) {
                                    case "email":
                                        jsonObject.put("email", dataField.getEmail());
                                        break;
                                    case "name":
                                        jsonObject.put("name", dataField.getName());
                                        break;
                                    case "country":
                                        jsonObject.put("country", dataField.getCountry());
                                        break;
                                    case "city":
                                        jsonObject.put("city", dataField.getCity());
                                        break;
                                    case "address":
                                        jsonObject.put("address", dataField.getAddress());
                                        break;
                                    case "phone":
                                        jsonObject.put("phone", dataField.getPhone());
                                        break;
                                    case "birthday":
                                        jsonObject.put("birthday", dataField.getBirthday_year() + "-"
                                                + dataField.getBirthday_month() + "-"
                                                + dataField.getBirthday_day());
                                        break;
                                }
                            }
                            connIntent.putExtra("res", "accept");
                            connIntent.putExtra("personalData", jsonObject.toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    // disagree on providing personal data
                    case R.id.rv_DataProvision_Refuse:
                        PermissionListAdapter.this.datas.get(position).setDataProvision(false);
                        connIntent.putExtra("res", "deny");
                        break;
                    default:
                        break;
                }

                /* Adapter Refresh */
                datas.remove(position);
                refreshAdapter(datas);

                rv_DataProvision_Approve.setChecked(false);
                rv_DataProvision_Refuse.setChecked(false);

                context.startService(connIntent);
                databaseSource.close();
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

    // Refresh listview
    public void refreshAdapter(ArrayList<CompanyData> arrayList) {
        this.datas = arrayList;
        notifyDataSetChanged();
    }
}
