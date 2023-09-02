package com.example.savelot;

import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.IntentSender;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Telephony;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.CredentialPickerConfig;
import com.google.android.gms.auth.api.credentials.CredentialRequest;
import com.google.android.gms.auth.api.credentials.CredentialRequestResult;
import com.google.android.gms.auth.api.credentials.Credentials;
import com.google.android.gms.auth.api.credentials.CredentialsClient;
import com.google.android.gms.auth.api.credentials.CredentialsOptions;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.auth.api.credentials.IdentityProviders;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.w3c.dom.Text;

import java.security.Permissions;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    // creating variables for text view on below line for displaying sender name, body of the message and the date on which message is received.
    private TextView numberTV, bodyTV, dateTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // initializing variables for text view on below line.
        numberTV = findViewById(R.id.idTVNumber);
        bodyTV = findViewById(R.id.idTVSMSBody);
        dateTV = findViewById(R.id.idTVSMSDate);
        Log.d("MainActivity", "The sending number:"+"123");
        // creating a variable for content resolver on below line.
        ContentResolver cr = getApplicationContext().getContentResolver();
        ActivityResultLauncher<String> launcher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                // Permission is granted. Continue the action or workflow in your
                // app.
                // creating a variable for cursor on below line and setting uri as sms uri.
                Cursor c = cr.query(Telephony.Sms.CONTENT_URI, null, null, null, null);
                // on below line checking if the cursor is not null.
                if (c != null) {
                    // on below line moving to the first position.
                    if (c.moveToFirst()) {
                        // on below line extracting the sms data from the cursor.
                        String smsDate = c.getString(c.getColumnIndexOrThrow(Telephony.Sms.DATE));
                        // on below line extracting the sender name/ number from cursor. .
                        String number = c.getString(c.getColumnIndexOrThrow(Telephony.Sms.ADDRESS));
                        System.out.println("The sending number: " + number);
                        Log.d("MainActivity", "The sending number:"+number);
                        // on below line extracting the body of the sms from the cursor.
                        String body = c.getString(c.getColumnIndexOrThrow(Telephony.Sms.BODY));
                        // on below line converting date in date format.
                        Date dateFormat = new Date(Long.valueOf(smsDate));
                        // on below line moving cursor to next position.
                        c.moveToNext();
                        // on below line setting data from the string to our text views.
                        numberTV.setText("Sender is : " + number);
                        bodyTV.setText("Body is : " + body);
                        dateTV.setText(dateFormat.toString());
                    }
                    // on below line closing the cursor.
                    c.close();
                } else {
                    // on below line displaying toast message if there is no sms present in the device.
                    Toast.makeText(this, "No message to show!", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Explain to the user that the feature is unavailable because the
                // feature requires a permission that the user has denied. At the
                // same time, respect the user's decision. Don't link to system
                // settings in an effort to convince the user to change their
                // decision.
                Toast.makeText(this, "SMS Permission is required", Toast.LENGTH_SHORT).show();
            }
        });
        launcher.launch(Manifest.permission.READ_SMS);

    }
}