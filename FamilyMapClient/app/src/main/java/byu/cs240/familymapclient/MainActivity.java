package byu.cs240.familymapclient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.widget.Toast;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

public class MainActivity extends AppCompatActivity implements LoginFragment.Listener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.fragment_container);

        if (currentFragment == null) {
            LoginFragment fragment = new LoginFragment();
            fragment.registerListener(this);
            fragmentManager.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }

        Iconify.with(new FontAwesomeModule());
    }

    @Override
    public void notifyDone(String message) {
        // If the login/register attempt was successful
        if (message == null) {
            // Replace Login Fragment with Map Fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            MapFragment mapFragment = new MapFragment();
            fragmentManager.beginTransaction().replace(R.id.fragment_container, mapFragment).commit();
        } else {
            // Report the error
            Toast.makeText(
                    this,
                    getString(R.string.login_register_result, message),
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }
}