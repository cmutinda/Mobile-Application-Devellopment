package com.evoke.central.evokecentral;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class DetailActivity2 extends AppCompatActivity {

    TextView NameOfShow, Rating, Description;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail2);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageView = (ImageView) findViewById(R.id.thumbnail_image_header2);
        NameOfShow = (TextView) findViewById(R.id.name2);
        Rating = (TextView) findViewById(R.id.ratingofmovie2);
        Description = (TextView) findViewById(R.id.descriptionofmovie2);

        String showname = getIntent().getExtras().getString("name");
        String rating = getIntent().getExtras().getString("vote_average");
        String thumbnail = getIntent().getExtras().getString("poster_path");
        String description = getIntent().getExtras().getString("overview");

        NameOfShow.setText(showname);
        Rating.setText(rating);
        Description.setText(description);
        Glide.with(this)
                .load(thumbnail)
                .placeholder(R.drawable.load)
                .into(imageView);

        getSupportActionBar().setTitle("Show Details");

    }
}
