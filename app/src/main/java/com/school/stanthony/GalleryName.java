package com.school.stanthony;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class GalleryName extends AppCompatActivity {

    ResultSet rs;
    PreparedStatement stmt;
    Connection conn;
    String ConnectionResult,fullname,count,offercount;
    boolean isSuccess;
    private ArrayList<ClassListItems1> itemArrayList;
    private MyAppAdapter myAppAdapter;
    private ListView gridView;
    private boolean success = false;
    private ConnectionHelper connectionClass;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_name);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        gridView = findViewById(R.id.gridView);
        connectionClass = new ConnectionHelper();
        itemArrayList = new ArrayList<ClassListItems1>();

        SyncData orderData = new SyncData();
        orderData.execute("");


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = itemArrayList.get(position). getName();
                Intent i=new Intent(getApplicationContext(),Gallery.class);
                i.putExtra("name",name);
                startActivity(i);
            }
        });
    }

    // Async Task has three overrided methods,
    private class SyncData extends AsyncTask<String, String, String>
    {
        String msg = "No Internet Connection";
        ProgressDialog progress;

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(GalleryName.this, "Loading Items",
                    "Please Wait...", true);
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
                    String query = "SELECT COUNT(imgtype)count,imgtype,ROW_NUMBER() OVER (order by imgtype) as srno\n" +
                            " FROM tblalbummaster GROUP BY imgtype";
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs != null)
                    {
                        while (rs.next())
                        {
                            try {
                                itemArrayList.add(new ClassListItems1(rs.getString("imgtype"),rs.getString("count"),rs.getString("srno")));
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
            //    Toast.makeText(GalleryName.this, msg + "", Toast.LENGTH_LONG).show();
            if (success == false)
            {
            }
            else {
                try {
                    myAppAdapter = new MyAppAdapter(itemArrayList, GalleryName.this);
                    gridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);
                    gridView.setAdapter(myAppAdapter);
                    myAppAdapter.notifyDataSetChanged();
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
            TextView name,count,srno;
            // ImageView imageView1;
        }

        public List<ClassListItems1> parkingList;

        public Context context;
        ArrayList<ClassListItems1> arraylist;

        private MyAppAdapter(List<ClassListItems1> apps, Context context)
        {
            this.parkingList = apps;
            this.context = context;
            arraylist = new ArrayList<ClassListItems1>();
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
                rowView = inflater.inflate(R.layout.list_content1, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.count = rowView.findViewById(R.id.count);
                viewHolder.name = rowView.findViewById(R.id.name);
                viewHolder.srno = rowView.findViewById(R.id.srno);
                //    viewHolder.imageView1 = rowView.findViewById(R.id.imageView);
                rowView.setTag(viewHolder);
            }
            else
            {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            // here setting up names and images
            viewHolder.srno.setText(parkingList.get(position).getSrno()+"");
            viewHolder.count.setText(parkingList.get(position).getCount()+"");
            viewHolder.name.setText(parkingList.get(position).getName()+"");
            //     Picasso.with(context).load(parkingList.get(position).getImg()).into(viewHolder.imageView1);

            return rowView;
        }
    }
}