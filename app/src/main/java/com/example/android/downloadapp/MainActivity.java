package com.example.android.downloadapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import static com.example.android.downloadapp.MyIntentService.ACTION_FETCH_IMG;
import static com.example.android.downloadapp.MyIntentService.EXTRA_OUT_IMG;
import static com.example.android.downloadapp.MyIntentService.EXTRA_RECEIVER;
import static com.example.android.downloadapp.MyIntentService.EXTRA_URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView imgIntent;
    private MyResultReceiver myResultReceiver;
    private static final String URL_IMG = "https://www.osadl.org/fileadmin/dam/images/tux-288.png";
    Button dlBut;
    Button displayBT;
    boolean pick = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dlBut = (Button)findViewById(R.id.dlButton);
        dlBut.setOnClickListener(this);
        displayBT = (Button) findViewById(R.id.displayBT);
        displayBT.setOnClickListener(this);
        imgIntent = (ImageView) findViewById(R.id.imageView);
        myResultReceiver = new MyResultReceiver(this, new Handler(Looper.getMainLooper()));

    }
    public void onImageDownloaded(Bitmap bmp){

        imgIntent.setImageBitmap(bmp);
    }
    public void loadImgClick(){
        startService(new Intent(getBaseContext(), MyIntentService.class)
                .setAction(ACTION_FETCH_IMG)
                .putExtra(EXTRA_URL, URL_IMG)
                .putExtra(EXTRA_RECEIVER, myResultReceiver));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dlButton:
                loadImgClick();

                break;
            case R.id.displayBT:
                imgIntent.setImageResource(R.drawable.theimageee);

               // Toast.makeText(this, , Toast.LENGTH_SHORT).show();
                break;
            default:
                return;
        }
    }
}
class MyResultReceiver extends ResultReceiver {


    /**
     * Create a new ResultReceive to receive results.  Your
     * {@link #onReceiveResult} method will be called from the thread running
     * <var>handler</var> if given, or from an arbitrary thread if null.
     *
     * @param handler
     */
        private final MainActivity mainActivity;


    public MyResultReceiver(MainActivity mainActivity, Handler handler) {
        super(handler);
        this.mainActivity = mainActivity;


    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        super.onReceiveResult(resultCode, resultData);
        //Log.d("TAG", "onReceiveResult: ");
        if(resultData.containsKey(EXTRA_OUT_IMG))
            mainActivity.onImageDownloaded((Bitmap)resultData.getParcelable(EXTRA_OUT_IMG));

    }
}

