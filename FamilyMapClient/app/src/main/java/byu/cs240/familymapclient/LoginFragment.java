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
import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import applogic.DataCache;
import applogic.ServerProxy;
import model.Person;
import requests.LoginRequest;
import requests.RegisterRequest;
import results.FamilyEventsResult;
import results.FamilyResult;
import results.LoginResult;
import results.RegisterResult;
import results.Result;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private static final String SUCCESS_STATUS_KEY = "Success status of Task";
    private static final String ERROR_KEY = "Error message";

    private Listener listener;

    public interface Listener {
        void notifyDone(String message);
    }

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

    public void registerListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        // Create Listener for the Login Button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a handler for the LoginTask
                Handler uiThreadMessageHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        String result = null;
                        Bundle bundle = msg.getData();
                        if (!bundle.getBoolean(SUCCESS_STATUS_KEY)) {
                            result = bundle.getString(ERROR_KEY);
                        }

                        // Notify the MainActivity that the Login Fragment is done
                        if (listener != null) {
                            listener.notifyDone(result);
                        }
                    }
                };

                // Instantiate a LoginTask and log the user in on a background task
                LoginTask loginTask = new LoginTask(uiThreadMessageHandler,
                                               usernameField.getText().toString(),
                                               passwordField.getText().toString(),
                                               serverHostField.getText().toString(),
                                               serverPortField.getText().toString());
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.submit(loginTask);
            }
        });

        // Create Listener for the Register Button
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a handler for the RegisterTask
                Handler uiThreadMessageHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        String result = null;
                        Bundle bundle = msg.getData();
                        if (!bundle.getBoolean(SUCCESS_STATUS_KEY)) {
                            result = bundle.getString(ERROR_KEY);
                        }

                        // Notify the MainActivity that the Login Fragment is done
                        if (listener != null) {
                            listener.notifyDone(result);
                        }
                    }
                };

                // Instantiate a RegisterTask and register the user on a background task
                RegisterTask registerTask = new RegisterTask(uiThreadMessageHandler,
                                                             usernameField.getText().toString(),
                                                             passwordField.getText().toString(),
                                                             emailField.getText().toString(),
                                                             firstNameField.getText().toString(),
                                                             lastNameField.getText().toString(),
                                                             maleButton.isChecked() ? "m" : "f",
                                                             serverHostField.getText().toString(),
                                                             serverPortField.getText().toString());
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.submit(registerTask);
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

        public LoginTask(Handler handler, String username, String password, String serverHost,
                         String serverPort) {
            this.handler = handler;
            this.request = new LoginRequest(username, password);
            this.serverHost = serverHost;
            this.serverPort = serverPort;
        }

        @Override
        public void run() {
            ServerProxy serverProxy = new ServerProxy(serverHost, serverPort);

            // Send login request to the server
            LoginResult result = serverProxy.login(request);

            if (result.isSuccess()) {
                // If the request was successful store the auth token and personID in the DataCache
                DataCache.getInstance().setAuthToken(result.getAuthtoken());
                DataCache.getInstance().setPersonID(result.getPersonID());

                // Get the data for the logged-in user
                new GetDataTask(serverProxy).run();
            }

            // Send success status back to UI Thread
            sendMessage(result.isSuccess(), result.getMessage());
        }

        private void sendMessage(boolean isSuccess, String errorMessage) {
            Message message = Message.obtain();

            Bundle messageBundle = new Bundle();
            messageBundle.putBoolean(SUCCESS_STATUS_KEY, isSuccess);
            if (errorMessage != null) {
                messageBundle.putString(ERROR_KEY, errorMessage);
            }
            message.setData(messageBundle);

            handler.sendMessage(message);
        }
    }

    private static class RegisterTask implements Runnable {
        private final Handler handler;
        private final RegisterRequest request;
        private String serverHost;
        private String serverPort;

        public RegisterTask(Handler handler, String username, String password, String email,
                            String firstName, String lastName, String gender, String serverHost,
                            String serverPort) {
            this.handler = handler;
            this.request = new RegisterRequest(username, password, email, firstName, lastName,
                                               gender);
            this.serverHost = serverHost;
            this.serverPort = serverPort;
        }

        @Override
        public void run() {
            ServerProxy serverProxy = new ServerProxy(serverHost, serverPort);

            // Send register request to the server
            RegisterResult result = serverProxy.register(request);

            if (result.isSuccess()) {
                // If the request was successful store the auth token and personID in the DataCache
                DataCache.getInstance().setAuthToken(result.getAuthtoken());
                DataCache.getInstance().setPersonID(result.getPersonID());

                // Get the data for the newly registered user
                new GetDataTask(serverProxy).run();
            }

            sendMessage(result.isSuccess(), result.getMessage());
        }

        private void sendMessage(boolean isSuccess, String errorMessage) {
            Message message = Message.obtain();

            Bundle messageBundle = new Bundle();
            messageBundle.putBoolean(SUCCESS_STATUS_KEY, isSuccess);
            if (errorMessage != null) {
                messageBundle.putString(ERROR_KEY, errorMessage);
            }
            message.setData(messageBundle);

            handler.sendMessage(message);
        }
    }

    private static class GetDataTask implements Runnable {
        private final ServerProxy serverProxy;

        public GetDataTask(ServerProxy serverProxy) {
            this.serverProxy = serverProxy;
        }

        @Override
        public void run() {
            // Send person and event requests to the server
            FamilyResult famResult = serverProxy.getFamily();
            FamilyEventsResult famEventsResult = serverProxy.getFamilyEvents();

            // Store the family and family events data in the DataCache
            DataCache.getInstance().setPeople(famResult.getData());
            DataCache.getInstance().setEvents(famEventsResult.getData());
        }
    }
}