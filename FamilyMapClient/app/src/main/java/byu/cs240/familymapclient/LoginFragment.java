package byu.cs240.familymapclient;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import applogic.DataCache;
import applogic.ServerProxy;
import requests.LoginRequest;
import requests.RegisterRequest;
import results.LoginResult;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private static final String LOGIN_SUCCESS_KEY = "Success status of Login Task";

    private boolean isServerHostSpecified;
    private boolean isServerPortSpecified;
    private boolean isUsernameSpecified;
    private boolean isPasswordSpecified;
    private boolean isFirstNameSpecified;
    private boolean isLastNameSpecified;
    private boolean isEmailSpecified;
    private boolean isGenderSpecified;
    private boolean isLoginTaskSuccessful;

    public LoginFragment() {
        isServerHostSpecified = false;
        isServerPortSpecified = false;
        isUsernameSpecified = false;
        isPasswordSpecified = false;
        isFirstNameSpecified = false;
        isLastNameSpecified = false;
        isEmailSpecified = false;
        isGenderSpecified = false;
        isLoginTaskSuccessful = false;
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

        // Add Listeners on EditText/RadioButton Fields to determine when buttons should be enabled
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
        RadioButton maleButton = view.findViewById(R.id.maleButton);
        maleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isGenderSpecified = true;
                registerButton.setEnabled(areFieldsSpecified(true));
            }
        });
        RadioButton femaleButton = view.findViewById(R.id.femaleButton);
        femaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isGenderSpecified = true;
                registerButton.setEnabled(areFieldsSpecified(true));
            }
        });

        // Create Listeners for the Login and Register Buttons
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginRequest request = new LoginRequest(usernameField.getText().toString(),
                                                        passwordField.getText().toString());

                Handler uiThreadMessageHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        Bundle bundle = msg.getData();
                        isLoginTaskSuccessful = bundle.getBoolean(LOGIN_SUCCESS_KEY);
                    }
                };

                LoginTask task = new LoginTask(uiThreadMessageHandler, request,
                                               serverHostField.getText().toString(),
                                               serverPortField.getText().toString());
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.submit(task);

                if (isLoginTaskSuccessful) {
                    // Get Data Task
                } else {
                    // Pop up Toast with error message
                }
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gender;
                if (maleButton.isChecked()) {
                    gender = "m";
                } else {
                    gender = "f";
                }
                RegisterRequest request = new RegisterRequest(usernameField.getText().toString(),
                                                              passwordField.getText().toString(),
                                                              emailField.getText().toString(),
                                                              firstNameField.getText().toString(),
                                                              lastNameField.getText().toString(),
                                                              gender);
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

    private static class LoginTask implements Runnable {
        private final Handler handler;
        private final LoginRequest request;
        private final String serverHost;
        private final String serverPort;

        public LoginTask(Handler handler, LoginRequest request, String serverHost, String serverPort) {
            this.handler = handler;
            this.request = request;
            this.serverHost = serverHost;
            this.serverPort = serverPort;
        }

        @Override
        public void run() {
            ServerProxy serverProxy = new ServerProxy(serverHost, serverPort);

            LoginResult result = serverProxy.login(request);

            if (result.isSuccess()) {
                DataCache.getInstance().setAuthToken(result.getAuthtoken());
            }

            sendMessage(result.isSuccess());
        }

        private void sendMessage(boolean isSuccess) {
            Message message = Message.obtain();

            Bundle messageBundle = new Bundle();
            messageBundle.putBoolean(LOGIN_SUCCESS_KEY, isSuccess);
            message.setData(messageBundle);

            handler.sendMessage(message);
        }
    }
}