package com.example.android.downloadapp;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Android on 6/3/2017.
 */

public class MyIntentService extends IntentService {
    public static final String EXTRA_RECEIVER = "receiver";
    public static final String ACTION_FETCH_IMG = "fetch_img";

    public static final String EXTRA_URL = "extra_url";
    public static final String EXTRA_OUT_IMG = "img";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        boolean isCurrentThread = Looper.getMainLooper() == Looper.myLooper();
        Log.d("TAG", "this is" + (isCurrentThread ? "" : " not ") + "the main thread");
        if(intent != null){
            final String action = intent.getAction();
            ResultReceiver receiver = (ResultReceiver) intent.getParcelableExtra(EXTRA_RECEIVER);
            Bundle bundle = new Bundle();
            try {
                URL url = new URL(intent.getStringExtra(EXTRA_URL));
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                if (ACTION_FETCH_IMG.equals(action)) {
                    Bitmap img = downloadImage(input);
                    bundle.putParcelable(EXTRA_OUT_IMG, img);
                    receiver.send(0, bundle);
                }
                input.close();
            }catch (IOException e){
                e.printStackTrace();

            }

        }
    }
    private Bitmap downloadImage(InputStream inputStream){
        return BitmapFactory.decodeStream(inputStream);
    }

}
