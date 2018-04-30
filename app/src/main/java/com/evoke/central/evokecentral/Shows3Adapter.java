package com.evoke.central.evokecentral;

import android.content.Context;
import android.content.Intent;
import android.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.evoke.central.evokecentral.model.Show;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by Castrol on 10/23/2017.
 */

public class Shows3Adapter extends RecyclerView.Adapter<Shows3Adapter.MyViewHolder>{

    private Context mContext;
    private List<Show> showList;

    public Shows3Adapter(Context mContext, List<Show> showList) {
        this.mContext = mContext;
        this.showList = showList;
    }

    @Override
    public Shows3Adapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.show_card3, viewGroup, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final Shows3Adapter.MyViewHolder viewHolder, int i) {


        viewHolder.title.setText(showList.get(i).getTitle());
        viewHolder.rating.setText(showList.get(i).getRating());

        //load album cover using picasso

        Picasso.with(mContext)
                .load(showList.get(i).getPosterPath())
                .placeholder(R.drawable.load)
                .into(viewHolder.thumbnail);


        viewHolder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showPopupMenu(viewHolder.overflow);
            }
        });

    }

    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);

        try {
            Method method = popup.getMenu().getClass().getDeclaredMethod("setOptionalIconsVisible", boolean.class);
            method.setAccessible(true);
            method.invoke(popup.getMenu(), true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        popup.getMenuInflater().inflate(R.menu.menu_show, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_add_favourite:
                        Toast.makeText(mContext, "Added to favourites", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.action_show_next:
                        Toast.makeText(mContext, "Added to bucket-list", Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                }
                return false;
            }
        });
        popup.show();
    }



    @Override
    public int getItemCount() {
        return showList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title, rating;
        public ImageView thumbnail, overflow;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            rating = (TextView) view.findViewById(R.id.rating);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            overflow = (ImageView) view.findViewById(R.id.overflow);

            //on item click

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION){

                        Show clickedDataItem = showList.get(pos);
                        Intent intent = new Intent(mContext, ActivityDetail3.class);
                        intent.putExtra("name", showList.get(pos).getTitle());
                        intent.putExtra("vote_average", showList.get(pos).getRating());
                        intent.putExtra("poster_path", showList.get(pos).getPosterPath());
                        intent.putExtra("overview", showList.get(pos).getDescription());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                        Toast.makeText(v.getContext(), "You clicked " + clickedDataItem.getTitle(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

    public void clear() {
        showList.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Show> list) {
        showList.addAll(list);
        notifyDataSetChanged();
    }

}
