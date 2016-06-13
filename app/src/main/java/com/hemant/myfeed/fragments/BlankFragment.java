package com.hemant.myfeed.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.einmalfel.earl.EarlParser;
import com.einmalfel.earl.Feed;
import com.einmalfel.earl.Item;
import com.hemant.myfeed.AppClass;
import com.hemant.myfeed.Util.CustomItemClickListener;
import com.hemant.myfeed.R;
import com.hemant.myfeed.Util.RVAdapter;
import com.hemant.myfeed.Util.Utils;

import com.hemant.myfeed.Activities.WebActivity;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.saeid.fabloading.LoadingView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BlankFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BlankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlankFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    LoadingView mLoadingView;

    Dialog progressDialog;

    private final Handler mHandler = new Handler();
    private final Runnable mUpdateUI = new Runnable() {
        public void run() {


            mLoadingView.performClick();
            mHandler.postDelayed(mUpdateUI, 1001);
        }
    };
    @Bind(R.id.rv)
    RecyclerView rv;
    private ArrayList<Item> RssItems;
    // TODO: Rename and change types of parameters
    private String mParam1;


    private OnFragmentInteractionListener mListener;

    public BlankFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BlankFragment newInstance(String param1) {
        BlankFragment fragment = new BlankFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_blank, container, false);
        ButterKnife.bind(this, rootView);
         progressDialog = new Dialog(getActivity());
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        progressDialog.getWindow().requestFeature(Window.FEATURE_PROGRESS);
        progressDialog.getWindow().requestFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        progressDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.show();
        Animation rotation = AnimationUtils.loadAnimation(getActivity(), R.anim.flipy);
        rotation.setFillAfter(true);
        progressDialog.setContentView(R.layout.loadingview);
        mLoadingView = ButterKnife.findById(progressDialog, R.id.loading_view);
        mLoadingView.addAnimation(Color.parseColor("#FFD200"), Utils.marvel_1,
                LoadingView.FROM_LEFT);
        mLoadingView.addAnimation(Color.parseColor("#2F5DA9"), Utils.marvel_2,
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
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(false);
        RssItems = new ArrayList<>();
        new GetRssFeed().execute(mParam1);
//        new GetRssFeed().execute("http://feeds.reuters.com/reuters/INoddlyEnoughNews");
        return rootView;
    }
    private void initializeData(){

    }

    private void initializeAdapter(){
        RVAdapter adapter = new RVAdapter(getActivity(), RssItems, new CustomItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent webPager = new Intent(getActivity(), WebActivity.class);
                AppClass.getsInstance().setRssitemforwebview(RssItems);
                webPager.putExtra("position",position);
                startActivity(webPager);
            }
        });
        adapter.notifyDataSetChanged();
        if (adapter != null && rv != null)
        rv.setAdapter(adapter);
        if(progressDialog.isShowing())
        progressDialog.dismiss();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    private class GetRssFeed extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            try {
                InputStream inputStream = new URL(params[0]).openConnection().getInputStream();
                Feed feed = EarlParser.parseOrThrow(inputStream, 0);
                int i = 0;
                for (Item item : feed.getItems()) {
                    if (i < 8) {
                        RssItems.add(item);
                    }
                    else {
                        continue;
                    }
                    i++;
                }

            } catch (Exception e) {
                Log.v("Error Parsing Data", e + "");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            initializeData();
            initializeAdapter();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

}
