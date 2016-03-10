package com.hemant.myfeed.Util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;

import java.io.IOException;
import java.net.URL;

/**
 * Created by anuraggupta on 31/01/16.
 */
public class BitmapLoader extends AsyncTask<URL, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(URL... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {

                return BitmapFactory.decodeStream(urls[0].openConnection().getInputStream());
            } catch (IOException e) {
                return null;
            }
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(Bitmap result) {


            }
        }

