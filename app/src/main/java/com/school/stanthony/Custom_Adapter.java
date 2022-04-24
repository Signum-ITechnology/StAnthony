package com.school.stanthony;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Custom_Adapter extends BaseAdapter {

    Context context;
    String unames[];
    String contacts[];
    LayoutInflater inflater;

    public Custom_Adapter(Context appliContext,String[] unames,String[] contacts)
    {
        this.context=appliContext;
        this.unames=unames;
        this.contacts=contacts;
        inflater=(LayoutInflater.from(appliContext));
    }

    @Override
    public int getCount() {
        return unames.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        convertView=inflater.inflate(R.layout.listview_layout,null);
        TextView uname= convertView.findViewById(R.id.tvname);
   //     TextView contact=(TextView)convertView.findViewById(R.id.tvname1);
        uname.setText(unames[position]);
    //    contact.setText(contacts[position]);
        return convertView;
    }
}