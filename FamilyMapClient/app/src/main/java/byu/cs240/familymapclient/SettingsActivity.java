package byu.cs240.familymapclient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.os.Bundle;
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
            }
        });

        SwitchCompat familyTreeSwitch = findViewById(R.id.familyTreeSwitch);
        familyTreeSwitch.setChecked(DataCache.getInstance().isFamilyTreeLinesEnabled());
        familyTreeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DataCache.getInstance().setFamilyTreeLinesEnabled(isChecked);
            }
        });

        SwitchCompat spouseSwitch = findViewById(R.id.spouseSwitch);
        spouseSwitch.setChecked(DataCache.getInstance().isSpouseLinesEnabled());
        spouseSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DataCache.getInstance().setSpouseLinesEnabled(isChecked);
            }
        });

        SwitchCompat fatherFilterSwitch = findViewById(R.id.fatherFilterSwitch);
        fatherFilterSwitch.setChecked(DataCache.getInstance().isFatherSideVisible());
        fatherFilterSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DataCache.getInstance().setFatherSideVisible(isChecked);
            }
        });

        SwitchCompat motherFilterSwitch = findViewById(R.id.motherFilterSwitch);
        motherFilterSwitch.setChecked(DataCache.getInstance().isMotherSideVisible());
        motherFilterSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DataCache.getInstance().setMotherSideVisible(isChecked);
            }
        });

        SwitchCompat maleFilterSwitch = findViewById(R.id.maleFilterSwitch);
        maleFilterSwitch.setChecked(DataCache.getInstance().isMaleEventsVisible());
        maleFilterSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DataCache.getInstance().setMaleEventsVisible(isChecked);
            }
        });

        SwitchCompat femaleFilterSwitch = findViewById(R.id.femaleFilterSwitch);
        femaleFilterSwitch.setChecked(DataCache.getInstance().isFemaleEventsVisible());
        femaleFilterSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DataCache.getInstance().setFemaleEventsVisible(isChecked);
            }
        });
    }
}