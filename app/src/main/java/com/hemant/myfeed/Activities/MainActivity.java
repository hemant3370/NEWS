package com.hemant.myfeed.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;


import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.hemant.myfeed.R;
import com.hemant.myfeed.Util.Utils;
import com.hemant.myfeed.fragments.BlankFragment;
import com.hemant.myfeed.fragments.MainFragment;
import com.hemant.myfeed.model.Topic;
import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.mikepenz.aboutlibraries.ui.LibsFragment;
import com.mikepenz.aboutlibraries.ui.LibsSupportFragment;
import com.zplesac.connectionbuddy.ConnectionBuddy;
import com.zplesac.connectionbuddy.cache.ConnectionBuddyCache;
import com.zplesac.connectionbuddy.interfaces.ConnectivityChangeListener;
import com.zplesac.connectionbuddy.models.ConnectivityEvent;
import com.zplesac.connectionbuddy.models.ConnectivityState;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.saeid.fabloading.LoadingView;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        BlankFragment.OnFragmentInteractionListener,
        MainFragment.OnFragmentInteractionListener,
        ConnectivityChangeListener{
    android.support.v4.app.FragmentTransaction fragmentTransaction;
    LoadingView mLoadingView;

    Dialog progressDialog;
    private final Handler mHandler = new Handler();
    private final Runnable mUpdateUI = new Runnable() {
        public void run() {
            mLoadingView.performClick();
            mHandler.postDelayed(mUpdateUI, 2004);
        }
    };
    MainFragment mainFragment = new MainFragment();
    String url = "http://www.majorgeeks.com/news/rss/news.xml";
    @Bind(R.id.nav_view)
    NavigationView navigationView;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
     @OnClick(R.id.fab)
     public void fabClick(){
        showHomeFragment();
    }
    @Bind(R.id.drawer_layout)
    DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .cordinatorlayout);
        if(savedInstanceState != null){
            ConnectionBuddyCache.clearLastNetworkState(this);
        }
        ButterKnife.bind(this);
        ConnectionBuddy.getInstance().registerForConnectivityEvents(this, this);
        progressDialog = new Dialog(this);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        progressDialog.getWindow().requestFeature(Window.FEATURE_PROGRESS);
        progressDialog.getWindow().requestFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        progressDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        progressDialog.show();
        Animation rotation = AnimationUtils.loadAnimation(this, R.anim.flipy);
        rotation.setFillAfter(true);
        progressDialog.setContentView(R.layout.loadingview);
        mLoadingView = ButterKnife.findById(progressDialog, R.id.loading_view);
        mLoadingView.addAnimation(Color.parseColor("#FFD200"), Utils.marvel_1,
                LoadingView.FROM_LEFT);
        mLoadingView.addAnimation(Color.parseColor("#2F5DA9"),Utils. marvel_2,
                LoadingView.FROM_TOP);
        mLoadingView.addAnimation(Color.parseColor("#FF4218"),Utils. marvel_3,
                LoadingView.FROM_RIGHT);
        mLoadingView.addAnimation(Color.parseColor("#C7E7FB"),Utils. marvel_4,
                LoadingView.FROM_BOTTOM);
        mLoadingView.startAnimation(rotation);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mLoadingView.setElevation(12);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mLoadingView.setTranslationZ(8);
        }
        mHandler.post(mUpdateUI);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        if(!isOnline()){
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Message is deleted", Snackbar.LENGTH_INDEFINITE)
                    .setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                        }
                    });

            snackbar.show();
            mLoadingView.pauseAnimation();
            mHandler.removeCallbacks(mUpdateUI);
            progressDialog.dismiss();

        }
        Firebase myFirebaseRef = new Firebase("https://knowfeed.firebaseio.com/");

        myFirebaseRef.child("links").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    HashMap<String, String> dummy = new HashMap<>();
                    for (DataSnapshot topicSnapshot : postSnapshot.getChildren()) {
                        dummy.put(topicSnapshot.getKey(), (String) topicSnapshot.getValue());
                    }
                    Utils.links.add(dummy);
                }

                Utils.TOPICs.add(new Topic(R.drawable.world, "World ", "http://www.faroo.com/api?q=world&start=1&length=9&l=en&src=news&f=rss", R.color.purple, Utils.links.get(5)));
                Utils.TOPICs.add(new Topic(R.drawable.sports, "Sports", "http://www.faroo.com/api?q=sports&start=1&length=9&l=en&src=news&f=rss", R.color.saffron, Utils.links.get(4)));
                Utils.TOPICs.add(new Topic(R.drawable.science, "Science", "http://www.faroo.com/api?q=&science=1&length=9&l=en&src=news&f=rss", R.color.green, Utils.links.get(3)));
                Utils.TOPICs.add(new Topic(R.drawable.politics, "Politics", "http://www.faroo.com/api?q=politics&start=1&length=9&l=en&src=news&f=rss", R.color.colorAccent, Utils.links.get(2)));
                Utils.TOPICs.add(new Topic(R.drawable.entertainment, "Entertainment", "http://www.faroo.com/api?q=entertainment&start=1&length=9&l=en&src=news&f=rss", R.color.orange, Utils.links.get(0)));
                Utils.TOPICs.add(new Topic(R.drawable.healthormedical, "Health", "http://www.faroo.com/api?q=health&start=1&length=9&l=en&src=news&f=rss", R.color.saffron, Utils.links.get(1)));
//             TOPICs.add(new Topic(R.drawable.health, "http://www.jokesareawesome.com/rss/latest/25/", R.color.green, links.get(6),"Reuters", "Guardian", "BBC", "CNN", "NY Times","Hindu"));
//             TOPICs.add(new Topic(R.drawable.yalantis, "YALANTIS", R.color.purple,links.get(0),"Reuters", "Guardian", "BBC", "CNN", "NY Times","Hindu"));
                mLoadingView.pauseAnimation();
                mHandler.removeCallbacks(mUpdateUI);
                progressDialog.dismiss();
                showHomeFragment();
            }
            @Override
            public void onCancelled(FirebaseError error) {
            }
        });
    }
    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (mainFragment.isVisible()) {
                this.finish();
            } else {
                showHomeFragment();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
           showHomeFragment();

        } else if (id == R.id.nav_gallary) {
          showFragment(BlankFragment.newInstance(url));
        }else if (id == R.id.nav_about) {
            LibsSupportFragment fragment = new LibsBuilder()
                    .withVersionShown(false)
                    .withLicenseShown(false)
                    .withLibraryModification("aboutlibraries", Libs.LibraryFields.LIBRARY_NAME, "_AboutLibraries")
                    .supportFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame, fragment).commit();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
public void showFragment(Fragment fragmentToSet){
    fragmentTransaction = getSupportFragmentManager().beginTransaction();
    fragmentTransaction.replace(R.id.frame, fragmentToSet);
    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
            android.R.anim.fade_in, android.R.anim.fade_out);
    fragmentTransaction.commit();
}
    public void setUrl(String mUrl){
        showFragment(BlankFragment.newInstance(mUrl)
        );
    }
public void showHomeFragment(){
    if (mainFragment == null){
        mainFragment = new MainFragment();
    }
    toolbar.setTitle("NEWS");
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, mainFragment).addToBackStack("mainFragnment");
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.commit();
    }

    @Override
    public void onConnectionChange(ConnectivityEvent event) {
        if(event.getState() == ConnectivityState.CONNECTED){

            // device has active internet connection
        }
        else{
            // there is no active internet connection on this device
            final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                    .cordinatorlayout);
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Message is deleted", Snackbar.LENGTH_INDEFINITE)
                    .setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                        }
                    });

            snackbar.show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        ConnectionBuddy.getInstance().unregisterFromConnectivityEvents(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ConnectionBuddy.getInstance().registerForConnectivityEvents(this, this);
    }
}
