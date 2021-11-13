package byu.cs240.familymapclient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.widget.Toast;

import applogic.DataCache;
import model.Person;

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
    }

    @Override
    public void notifyDone(boolean isSuccess) {
        String arg1;
        String arg2;
        if (isSuccess) {
            Person user = DataCache.getInstance().getPerson();
            arg1 = user.getFirstName();
            arg2 = user.getLastName();
        } else {
            arg1 = "Error:";
            arg2 = "Login/Register Attempt Failed";
        }

        Toast.makeText(
                this,
                getString(R.string.login_register_result, arg1, arg2),
                Toast.LENGTH_SHORT)
                .show();
    }
}