package com.evoke.central.evokecentral;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.evoke.central.evokecentral.api.Client3;
import com.evoke.central.evokecentral.api.Service3;
import com.evoke.central.evokecentral.model.Show;
import com.evoke.central.evokecentral.model.ShowResponse2;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Main4Activity extends BaseActivity {

    private RecyclerView recyclerView;
    private Shows3Adapter adapter;
    private List<Show> showList;
    private Show show;
    ProgressDialog pd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);


        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                refreshItems();
            }

            private void refreshItems() {
                initViews2();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        initViews();
        initCollapsingToolbar();

    }

    @Override
    int getContentViewId() {
        return R.layout.activity_main4;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.navigation_cinemas;

    }


    public Activity getActivity(){
        Context context = this;
        while (context instanceof ContextWrapper){
            if (context instanceof Activity){
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }


    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name4));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        navigationView.postDelayed(() -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_today) {
                startActivity(new Intent(this, Main2Activity.class));
            } else if (itemId == R.id.navigation_popular) {
                startActivity(new Intent(this, Main3Activity.class));
            } else if (itemId == R.id.navigation_cinemas) {
                startActivity(new Intent(this, Main4Activity.class));
            }
            finish();
        }, 300);
        return true;
    }


    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }



    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


    private void initViews(){
        pd = new ProgressDialog(this);
        pd.setMessage("Fetching Shows...");
        pd.setCancelable(false);
        pd.show();

        try {
            Glide.with(this).load(R.drawable.cover3).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        showList = new ArrayList<>();
        adapter = new Shows3Adapter(this, showList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        loadJSON();
    }


    private void initViews2(){
        pd = new ProgressDialog(this);
        pd.setMessage("Fetching Shows...");
        pd.setCancelable(false);
        pd.show();

        try {
            Glide.with(this).load(R.drawable.cover3).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        showList = new ArrayList<>();
        adapter = new Shows3Adapter(this, showList);

        recyclerView.setAdapter(adapter);

        loadJSON();
    }


    private void loadJSON(){
        try{

            Client3 Client = new Client3();
            Service3 apiService =
                    Client.getClient().create(Service3.class);
            Call<ShowResponse2> call = apiService.getShows();
            call.enqueue(new Callback<ShowResponse2>() {
                @Override
                public void onResponse(Call<ShowResponse2> call, Response<ShowResponse2> response) {
                    List<Show> items = response.body().getShows();
                    recyclerView.setAdapter(new Shows3Adapter(getApplicationContext(), items));
                    recyclerView.smoothScrollToPosition(0);
                    pd.hide();
                    Toast.makeText(Main4Activity.this, "Fetch completed...", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ShowResponse2> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                    Toast.makeText(Main4Activity.this, "Error Fetching Data!", Toast.LENGTH_SHORT).show();
                    pd.hide();

                }
            });
        }catch (Exception e){
            Log.d("Error", e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main3, menu);//Menu Resource, Menu
        if(menu instanceof MenuBuilder){
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                startActivity(new Intent(Main4Activity.this, AccountActivity.class));
                return true;
            case R.id.item2:
                startActivity(new Intent(Main4Activity.this, Main2Activity.class));
                return true;
            case R.id.item3:
                startActivity(new Intent(Main4Activity.this, Main3Activity.class));
                return true;
            case R.id.item4:
                startActivity(new Intent(Main4Activity.this, ReminderActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}






