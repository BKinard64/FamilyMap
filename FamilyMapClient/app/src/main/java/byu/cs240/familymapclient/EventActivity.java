package byu.cs240.familymapclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import applogic.DataCache;
import model.Event;

public class EventActivity extends AppCompatActivity {

    public static final String EVENT_KEY = "EventID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.event_fragment_container);

        Intent intent = getIntent();
        String eventID = intent.getStringExtra(EVENT_KEY);

        MapFragment mapFragment = createMapFragment(eventID);

        if (currentFragment == null) {
            fragmentManager.beginTransaction().add(R.id.event_fragment_container, mapFragment).commit();
        } else {
            fragmentManager.beginTransaction().replace(R.id.event_fragment_container, mapFragment).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        return true;
    }

    private MapFragment createMapFragment(String eventID) {
        MapFragment mapFragment = new MapFragment(this);

        Bundle arguments = new Bundle();
        arguments.putString(MapFragment.FROM_EVENT_KEY, eventID);
        mapFragment.setArguments(arguments);

        return mapFragment;
    }
}