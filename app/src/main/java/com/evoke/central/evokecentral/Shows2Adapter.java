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

public class Shows2Adapter extends RecyclerView.Adapter<Shows2Adapter.MyViewHolder>{

    private Context mContext;
    private List<Show> showList2;

    public Shows2Adapter(Context mContext2, List<Show> showList2) {
        this.mContext = mContext2;
        this.showList2 = showList2;
    }

    @Override
    public Shows2Adapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.show_card2, viewGroup, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final Shows2Adapter.MyViewHolder viewHolder, int i) {


        viewHolder.title.setText(showList2.get(i).getName());
        viewHolder.rating.setText(showList2.get(i).getRating());

        //load show cover using picasso

        Picasso.with(mContext)
                .load(showList2.get(i).getPosterPath())
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
        return showList2.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title, rating;
        public ImageView thumbnail, overflow;

        public MyViewHolder(View view2) {
            super(view2);
            title = (TextView) view2.findViewById(R.id.title2);
            rating = (TextView) view2.findViewById(R.id.rating2);
            thumbnail = (ImageView) view2.findViewById(R.id.thumbnail2);
            overflow = (ImageView) view2.findViewById(R.id.overflow2);

            //on item click

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION){

                        Show clickedDataItem = showList2.get(pos);
                        Intent intent = new Intent(mContext, DetailActivity2.class);
                        intent.putExtra("name", showList2.get(pos).getName());
                        intent.putExtra("vote_average", showList2.get(pos).getRating());
                        intent.putExtra("poster_path", showList2.get(pos).getPosterPath());
                        intent.putExtra("overview", showList2.get(pos).getDescription());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                        Toast.makeText(v.getContext(), "You clicked " + clickedDataItem.getName(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }



}
