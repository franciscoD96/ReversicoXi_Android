package isec.xicos.reversisec2.UserProfile;

import android.content.DialogInterface;
import android.os.PersistableBundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import isec.xicos.reversisec2.R;

public class UserProfileActivity extends AppCompatActivity {

    static final String userData = "reversiUser";

    File internalDirectory;
    String username;

    TextView debug;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        internalDirectory = getFilesDir();

        File[] listFiles = internalDirectory.listFiles();
        if ( Arrays.asList(listFiles).contains(userData) ) {
            // load file
        } else {
            // create new
        }


        findViewById(R.id.btn_changeUsername).setOnClickListener(listener -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle(R.string.newUsername);
            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT); // type for normal text

            builder.setView(input);
            builder.setPositiveButton("OK", (dialog, which) -> {
                username = input.getText().toString();
                ((TextView) findViewById(R.id.tv_Username)).setText(username);
            });
            builder.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel());
            builder.show();

            Log.d("Lol", "" + username);


        });

        findViewById(R.id.btn_takeNewPhoto).setOnClickListener(listener -> {

        });


        //------------------------------------------------------------ debug ------------------------------------------------------------
        debug = findViewById(R.id.debugTV);
        for (File f : internalDirectory.listFiles())
            debug.setText("" + debug.getText() + "\n" + f.getName());


    }

}
