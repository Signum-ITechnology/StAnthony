package com.school.stanthony;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.ProgrammingViewholder> {
    Context context;
    List<Team> clients;

    public TeamAdapter(Context context, List<Team> clients) {
        this.context = context;
        this.clients = clients;
    }

    @Override
    public ProgrammingViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.teamlayout,null);
        return new ProgrammingViewholder(view,context, (ArrayList<Team>) clients);
    }

    @Override
    public void onBindViewHolder(ProgrammingViewholder holder, int position) {
        Team client=clients.get(position);
        holder.title.setText(client.getTitle());
        holder.desc.setText(client.getDesc());
        holder.desc2.setText(client.getDesc2());
        holder.imageView.setImageDrawable(context.getResources().getDrawable(client.getImage()));
    }

    @Override
    public int getItemCount() {
        return clients.size();
    }

    class ProgrammingViewholder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        ImageView imageView;
        TextView title,desc,desc2,desc3;
        ArrayList<Team> clients;
        Context context;

        public ProgrammingViewholder(View itemView, Context context, ArrayList<Team> clients) {
            super(itemView);
            this.clients=clients;
            this.context=context;

            itemView.setOnClickListener(this);
            imageView= itemView.findViewById(R.id.img);
            title= itemView.findViewById(R.id.text1);
            desc= itemView.findViewById(R.id.text2);
            desc2= itemView.findViewById(R.id.text3);
        }

        @Override
        public void onClick(View v) {

            int postion=getAdapterPosition();
            Team client=this.clients.get(postion);

            Intent i=new Intent(context,TeamDetails.class);
            i.putExtra("img",client.getImage());
            i.putExtra("name1",client.getTitle());
            i.putExtra("name2",client.getDesc());
            i.putExtra("name3",client.getDesc2());
            this.context.startActivity(i);
        }
    }
}