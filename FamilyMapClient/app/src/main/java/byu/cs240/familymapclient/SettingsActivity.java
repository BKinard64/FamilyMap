package byu.cs240.familymapclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;

import applogic.DataCache;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Write Event Handling for each of the switches
        SwitchCompat lifeStorySwitch = findViewById(R.id.lifeStorySwitch);
        lifeStorySwitch.setChecked(DataCache.getInstance().isLifeStoryLinesEnabled());
        lifeStorySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DataCache.getInstance().setLifeStoryLinesEnabled(isChecked);
                DataCache.getInstance().setFilterStatusChanged(true);
                DataCache.getInstance().setPersonEventPool();
            }
        });

        SwitchCompat familyTreeSwitch = findViewById(R.id.familyTreeSwitch);
        familyTreeSwitch.setChecked(DataCache.getInstance().isFamilyTreeLinesEnabled());
        familyTreeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DataCache.getInstance().setFamilyTreeLinesEnabled(isChecked);
                DataCache.getInstance().setFilterStatusChanged(true);
                DataCache.getInstance().setPersonEventPool();
            }
        });

        SwitchCompat spouseSwitch = findViewById(R.id.spouseSwitch);
        spouseSwitch.setChecked(DataCache.getInstance().isSpouseLinesEnabled());
        spouseSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DataCache.getInstance().setSpouseLinesEnabled(isChecked);
                DataCache.getInstance().setFilterStatusChanged(true);
                DataCache.getInstance().setPersonEventPool();
            }
        });

        SwitchCompat fatherFilterSwitch = findViewById(R.id.fatherFilterSwitch);
        fatherFilterSwitch.setChecked(DataCache.getInstance().isFatherSideVisible());
        fatherFilterSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DataCache.getInstance().setFatherSideVisible(isChecked);
                DataCache.getInstance().setFilterStatusChanged(true);
                DataCache.getInstance().setPersonEventPool();
            }
        });

        SwitchCompat motherFilterSwitch = findViewById(R.id.motherFilterSwitch);
        motherFilterSwitch.setChecked(DataCache.getInstance().isMotherSideVisible());
        motherFilterSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DataCache.getInstance().setMotherSideVisible(isChecked);
                DataCache.getInstance().setFilterStatusChanged(true);
                DataCache.getInstance().setPersonEventPool();
            }
        });

        SwitchCompat maleFilterSwitch = findViewById(R.id.maleFilterSwitch);
        maleFilterSwitch.setChecked(DataCache.getInstance().isMaleEventsVisible());
        maleFilterSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DataCache.getInstance().setMaleEventsVisible(isChecked);
                DataCache.getInstance().setFilterStatusChanged(true);
                DataCache.getInstance().setPersonEventPool();
            }
        });

        SwitchCompat femaleFilterSwitch = findViewById(R.id.femaleFilterSwitch);
        femaleFilterSwitch.setChecked(DataCache.getInstance().isFemaleEventsVisible());
        femaleFilterSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DataCache.getInstance().setFemaleEventsVisible(isChecked);
                DataCache.getInstance().setFilterStatusChanged(true);
                DataCache.getInstance().setPersonEventPool();
            }
        });
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
}