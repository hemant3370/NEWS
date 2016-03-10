package com.hemant.myfeed.Activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.einmalfel.earl.Item;
import com.hemant.myfeed.AppClass;
import com.hemant.myfeed.R;
import com.hemant.myfeed.Util.Utils;
import com.hemant.myfeed.views.ZoomOutPageTransformer;
import com.hemant.myfeed.webview.AdvancedWebView;
import com.zplesac.connectionbuddy.ConnectionBuddy;
import com.zplesac.connectionbuddy.cache.ConnectionBuddyCache;
import com.zplesac.connectionbuddy.interfaces.ConnectivityChangeListener;
import com.zplesac.connectionbuddy.models.ConnectivityEvent;
import com.zplesac.connectionbuddy.models.ConnectivityState;

import java.util.ArrayList;

import io.saeid.fabloading.LoadingView;
import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;

public class WebActivity extends AppCompatActivity implements MaterialTabListener,
        ConnectivityChangeListener{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    MaterialTabHost tabHost;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    ArrayList<Item> RssItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        ConnectionBuddy.getInstance().registerForConnectivityEvents(this, this);
        if(savedInstanceState != null){
            ConnectionBuddyCache.clearLastNetworkState(this);
        }
         RssItems = AppClass.getsInstance().getRssitemforwebview();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                finish();
            }
        });
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        tabHost = (MaterialTabHost) this.findViewById(R.id.materialTabHost);
        // Set up the ViewPager with the sections adapter.
        int position = getIntent().getIntExtra("position",0);
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
//        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabHost.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            tabHost.addTab(
                    tabHost.newTab()
                            .setText("  "+ String.valueOf(i+1) + "  ")
                            .setTabListener(this)
            );
        }
        mViewPager.setCurrentItem(position);
        toolbar.setTitle("Full Article");
    }

    @Override
    public void onTabSelected(MaterialTab tab) {
        // when the tab is clicked the pager swipe content to the tab position
        mViewPager.setCurrentItem(tab.getPosition());

    }

    @Override
    public void onTabReselected(MaterialTab tab) {

    }

    @Override
    public void onTabUnselected(MaterialTab tab) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_web, menu);
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

    @Override
    public void onConnectionChange(ConnectivityEvent event) {
        if(event.getState() == ConnectivityState.CONNECTED){

            // device has active internet connection
        }
        else{
            // there is no active internet connection on this device
            final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                    .main_content);
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


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements AdvancedWebView.Listener {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        LoadingView mLoadingView;



        private final Handler mHandler = new Handler();
        private final Runnable mUpdateUI = new Runnable() {
            public void run() {


                mLoadingView.performClick();
                mHandler.postDelayed(mUpdateUI, 1001);
            }
        };
        private static final String ARG_SECTION_NUMBER = "section_number";
        private AdvancedWebView webView;
        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(String sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putString(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_web2, container, false);
             webView = (AdvancedWebView) rootView.findViewById(R.id.tabwebview);
            webView.getSettings().setBuiltInZoomControls(true);
            webView.getSettings().setDefaultFontSize(20);
//            webView.setWebViewClient(new WebViewClient());
            final Dialog progressDialog = new Dialog(getActivity());
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            progressDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            progressDialog.show();
            Animation rotation = AnimationUtils.loadAnimation(getActivity(), R.anim.flipy);
            rotation.setFillAfter(true);
            progressDialog.setContentView(R.layout.loadingview);
            mLoadingView = (LoadingView) progressDialog.findViewById( R.id.loading_view);
            mLoadingView.addAnimation(Color.parseColor("#FFD200"), Utils.marvel_1,
                    LoadingView.FROM_LEFT);
            mLoadingView.addAnimation(Color.parseColor("#2F5DA9"), Utils.marvel_2,
                    LoadingView.FROM_TOP);
            mLoadingView.addAnimation(Color.parseColor("#FF4218"), Utils.marvel_3,
                    LoadingView.FROM_RIGHT);
            mLoadingView.addAnimation(Color.parseColor("#C7E7FB"), Utils.marvel_4,
                    LoadingView.FROM_BOTTOM);
            mLoadingView.startAnimation(rotation);
            mHandler.post(mUpdateUI);
            webView.setWebChromeClient(new WebChromeClient(){
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    super.onProgressChanged(view, newProgress);
                    if(newProgress > 90){
                        progressDialog.dismiss();
                        mHandler.removeCallbacks(mUpdateUI);
                    }

                }
            });
            webView.setListener(getActivity(),this);
            webView.setThirdPartyCookiesEnabled(false);
            webView.loadUrl(getArguments().getString(ARG_SECTION_NUMBER));
            webView.setCookiesEnabled(false);

            return rootView;
        }

        @Override
        public void onPageStarted(String url, Bitmap favicon) {

        }

        @Override
        public void onPageFinished(String url) {

        }

        @Override
        public void onPageError(int errorCode, String description, String failingUrl) {

        }

        @Override
        public void onDownloadRequested(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {

        }

        @Override
        public void onExternalPageRequest(String url) {

        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(RssItems.get(position).getLink());
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return RssItems.size();
        }

//        @Override
//        public CharSequence getPageTitle(int position) {
//            switch (position) {
//                case 0:
//                    return RssItems.get(position).getTitle();
//                case 1:
//                    return RssItems.get(position).getTitle();
//                case 2:
//                    return RssItems.get(position).getTitle();
//            }
//            return null;
//        }
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
