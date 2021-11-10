package byu.cs240.familymapclient;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private boolean isServerHostSpecified;
    private boolean isServerPortSpecified;
    private boolean isUsernameSpecified;
    private boolean isPasswordSpecified;
    private boolean isFirstNameSpecified;
    private boolean isLastNameSpecified;
    private boolean isEmailSpecified;
    private boolean isGenderSpecified;

    public LoginFragment() {
        isServerHostSpecified = false;
        isServerPortSpecified = false;
        isUsernameSpecified = false;
        isPasswordSpecified = false;
        isFirstNameSpecified = false;
        isLastNameSpecified = false;
        isEmailSpecified = false;
        isGenderSpecified = false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        // Initialize buttons to be disabled
        Button loginButton = view.findViewById(R.id.loginButton);
        Button registerButton = view.findViewById(R.id.registerButton);
        loginButton.setEnabled(false);
        registerButton.setEnabled(false);

        // Add Listeners on EditText Fields to determine when buttons should be enabled/disabled
        EditText serverHostField = view.findViewById(R.id.serverHostField);
        serverHostField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                isServerHostSpecified = s.length() > 0;
                loginButton.setEnabled(areFieldsSpecified(false));
                registerButton.setEnabled(areFieldsSpecified(true));
            }
        });
        EditText serverPortField = view.findViewById(R.id.serverPortField);
        serverPortField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                isServerPortSpecified = s.length() > 0;
                loginButton.setEnabled(areFieldsSpecified(false));
                registerButton.setEnabled(areFieldsSpecified(true));
            }
        });
        EditText usernameField = view.findViewById(R.id.usernameField);
        usernameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                isUsernameSpecified = s.length() > 0;
                loginButton.setEnabled(areFieldsSpecified(false));
                registerButton.setEnabled(areFieldsSpecified(true));
            }
        });
        EditText passwordField = view.findViewById(R.id.passwordField);
        passwordField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                isPasswordSpecified = s.length() > 0;
                loginButton.setEnabled(areFieldsSpecified(false));
                registerButton.setEnabled(areFieldsSpecified(true));
            }
        });
        EditText firstNameField = view.findViewById(R.id.firstNameField);
        firstNameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                isFirstNameSpecified = s.length() > 0;
                registerButton.setEnabled(areFieldsSpecified(true));
            }
        });
        EditText lastNameField = view.findViewById(R.id.lastNameField);
        lastNameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                isLastNameSpecified = s.length() > 0;
                registerButton.setEnabled(areFieldsSpecified(true));
            }
        });
        EditText emailField = view.findViewById(R.id.emailField);
        emailField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                isEmailSpecified = s.length() > 0;
                registerButton.setEnabled(areFieldsSpecified(true));
            }
        });

        return view;
    }

    private boolean areFieldsSpecified(boolean isRegisterAttempt) {
        boolean fieldsSpecified = isServerHostSpecified && isServerPortSpecified &&
                                    isUsernameSpecified && isPasswordSpecified;
        if (isRegisterAttempt) {
            fieldsSpecified = fieldsSpecified && isFirstNameSpecified && isLastNameSpecified &&
                                isEmailSpecified && isGenderSpecified;
        }
        return fieldsSpecified;
    }
}