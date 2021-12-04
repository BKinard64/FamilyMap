package byu.cs240.familymapclient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class EventActivity extends AppCompatActivity {

    public static final String EVENT_KEY = "EventID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
    }
}