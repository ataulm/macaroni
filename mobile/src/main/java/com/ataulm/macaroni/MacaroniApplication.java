package com.ataulm.macaroni;

import android.app.Application;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import javax.crypto.Mac;

public class MacaroniApplication extends Application {

    public static void log(String message) {
        Log.d("!!!", message);
    }

}
