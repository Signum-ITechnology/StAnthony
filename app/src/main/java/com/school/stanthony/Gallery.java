package com.school.stanthony;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class Gallery extends AppCompatActivity {

    private ArrayList<ClassListItems2> itemArrayList;
    private MyAppAdapter myAppAdapter;
    private GridView gridView;
    private boolean success = false;
    Connection conn;
    private ConnectionHelper connectionClass;
    String name;
    TextView name1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        gridView = findViewById(R.id.gridView);
        name1=findViewById(R.id.name);
        connectionClass = new ConnectionHelper();
        itemArrayList = new ArrayList<ClassListItems2>();
        name=getIntent().getExtras().getString("name");
        name1.setText(name);

        // Calling Async Task
        SyncData orderData = new SyncData();
        orderData.execute("");

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String message = itemArrayList.get(position). getId();
                Intent i=new Intent(getApplicationContext(),ZoomImage.class);
                i.putExtra("id",message);
                startActivity(i);
            }
        });
    }

    // Async Task has three overrided methods,
    private class SyncData extends AsyncTask<String, String, String>
    {
        String msg = "";
        ProgressDialog progress;

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(Gallery.this, "",
                    "Loading Please Wait...", true);
        }

        @Override
        protected String doInBackground(String... strings)
        {
            try
            {
                Connection conn = connectionClass.connectionclasss();
                if (conn == null)
                {
                    success = false;
                }
                else {

                    String query = "select id,'http://stanthony.edusofterp.co.in'+replace(replace(imagepath,' ','%20'),'..','') url\n" +
                            "from tblalbummaster where imgtype='"+name+"'";
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs != null)
                    {
                        while (rs.next())
                        {
                            try {
                                itemArrayList.add(new ClassListItems2(rs.getString("id"),rs.getString("url")));
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                        msg = "";
                        success = true;
                    } else {
                        msg = "";
                        success = false;
                    }
                }
            } catch (Exception e)
            {
                e.printStackTrace();
                Writer writer = new StringWriter();
                e.printStackTrace(new PrintWriter(writer));
                msg = writer.toString();
                success = false;
            }
            return msg;
        }

        @Override
        protected void onPostExecute(String msg)
        {
            progress.dismiss();
            if (success == false)
            {
            }
            else {
                try {
                    myAppAdapter = new MyAppAdapter(itemArrayList, Gallery.this);
                    gridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);
                    gridView.setAdapter(myAppAdapter);
                } catch (Exception ex)
                {

                }

            }
        }
    }

    public class MyAppAdapter extends BaseAdapter
    {
        public class ViewHolder
        {
            TextView textName;
            ImageView imageView;
        }

        public List<ClassListItems2> parkingList;

        public Context context;
        ArrayList<ClassListItems2> arraylist;

        private MyAppAdapter(List<ClassListItems2> apps, Context context)
        {
            this.parkingList = apps;
            this.context = context;
            arraylist = new ArrayList<ClassListItems2>();
            arraylist.addAll(parkingList);
        }

        @Override
        public int getCount() {
            return parkingList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {

            View rowView = convertView;
            ViewHolder viewHolder= null;
            if (rowView == null)
            {
                LayoutInflater inflater = getLayoutInflater();
                rowView = inflater.inflate(R.layout.gallery, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.textName = rowView.findViewById(R.id.id);
                viewHolder.imageView = rowView.findViewById(R.id.image);
                rowView.setTag(viewHolder);
            }
            else
            {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            // here setting up names and images
            viewHolder.textName.setText(parkingList.get(position).getId()+"");
            //Picasso.with(context).load(parkingList.get(position).getImg()).into(viewHolder.imageView);
            Glide.with(context).load(parkingList.get(position).getImg()).into(viewHolder.imageView);

            return rowView;
        }
    }

}

