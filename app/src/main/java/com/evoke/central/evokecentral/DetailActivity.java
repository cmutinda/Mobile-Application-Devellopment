package com.evoke.central.evokecentral;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class DetailActivity extends AppCompatActivity {

    TextView NameOfShow, Rating, Description;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageView = (ImageView) findViewById(R.id.thumbnail_image_header);
        NameOfShow = (TextView) findViewById(R.id.name);
        Rating = (TextView) findViewById(R.id.ratingofmovie);
        Description = (TextView) findViewById(R.id.descriptionofmovie);

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
