package com.hemant.news.Util;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.einmalfel.earl.Item;

import com.hemant.news.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;


public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PersonViewHolder> {
    Context mContext;
    private int lastPosition = -1;
    CustomItemClickListener listener;
    public static class PersonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.cv)
        CardView cv;
        @Bind(R.id.person_name)
        TextView personName;
        @Bind(R.id.person_age)
        WebView personAge;
        @Bind(R.id.person_photo)
        ImageView personPhoto;

        PersonViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            cv.setCardElevation(8);
        }

        @Override
        public void onClick(View v) {

        }
    }

    ArrayList<Item> RssItems;

    public RVAdapter(Context mContext, ArrayList<Item> RssItems, CustomItemClickListener listener) {

        try {
            this.RssItems = RssItems;
            this.mContext = mContext;
            this.listener = listener;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        final PersonViewHolder pvh = new PersonViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, pvh.getAdapterPosition());
            }
        });
        return pvh;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, int i) {
        personViewHolder.personName.setText(RssItems.get(i).getTitle());
        personViewHolder.personAge.getSettings().setJavaScriptEnabled(true);
        personViewHolder.personAge.setWebChromeClient(new WebChromeClient(){
        });
        personViewHolder.personAge.loadDataWithBaseURL(null, "<html>" + RssItems.get(i).getDescription() + "</html>", "text/html", "utf-8", null);
        if(RssItems.get(i).getImageLink() != null) {
            Picasso.with(mContext).load((RssItems.get(i).getImageLink())).into(personViewHolder.personPhoto);
        }
        else {

        Uri data = Uri.parse(RssItems.get(i).getLink());
        try {
            Picasso.with(mContext).load(new String(String.valueOf(new URL(data.getScheme(), data.getHost(), "/favicon.ico")))).into(personViewHolder.personPhoto);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        }
        setAnimation(personViewHolder.cv, i);
    }
    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
    @Override
    public int getItemCount() {
        return RssItems.size();
    }

}