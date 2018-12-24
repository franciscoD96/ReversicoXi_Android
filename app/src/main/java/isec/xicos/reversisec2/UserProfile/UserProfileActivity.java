package isec.xicos.reversisec2.UserProfile;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.os.PersistableBundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import isec.xicos.reversisec2.R;


class UserDetails implements Serializable {
    private String username = "";
    private int jogosGanhos = 0;
    private int jogosPerdidos = 0;

    public UserDetails(String u) { username = u; }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getUsername() {
        return username;
    }

    public int getJogosGanhos() {
        return jogosGanhos;
    }
    public void setJogosGanhos(int jogosGanhos) {
        this.jogosGanhos = jogosGanhos;
    }

    public int getJogosPerdidos() {
        return jogosPerdidos;
    }
    public void setJogosPerdidos(int jogosPerdidos) {
        this.jogosPerdidos = jogosPerdidos;
    }
}



public class UserProfileActivity extends AppCompatActivity {

    public static final String userFile = "reversiUser.serialized";
    private UserDetails userDetails;
    File internalDirectory; // FileWriter is meant for writing streams of characters. For writing streams of raw bytes, consider using a FileOutputStream.

    TextView debug;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        userDetails = new UserDetails(getString(R.string.username));

        internalDirectory = getFilesDir();
        File[] listFiles = internalDirectory.listFiles();

        File f = new File("" + getBaseContext().getFilesDir().getPath().toString() + File.pathSeparator + userFile);
        if ( f.exists() ) {
            getUserFile(); // updates userDetails
            updateTextView();
        } else {
            if (!setUserFile()) throw new RuntimeException();
        }

        findViewById(R.id.btn_changeUsername).setOnClickListener(listener -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle(R.string.newUsername);
            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT); // type for normal text
            builder.setView(input);
            builder.setPositiveButton("OK", (dialog, which) -> {
                userDetails.setUsername( input.getText().toString() );
                setUserFile();
                updateTextView();
            });
            builder.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel());
            builder.show();
        });

        findViewById(R.id.btn_takeNewPhoto).setOnClickListener(listener -> { });

        updateTextView();


        //----------------------- debug ------------------------------
        debug = findViewById(R.id.debugTV);
        for (File fi : internalDirectory.listFiles())
            debug.setText("" + debug.getText() + "\n" + fi.getName());
        //------------------------------------------------------------
    }

    private void updateTextView () {
        ((TextView) findViewById(R.id.tv_Username)).
                setText(userDetails.getUsername());
        ((TextView) findViewById(R.id.tv_jogosGanhos)).
                setText("" + userDetails.getJogosGanhos());
        ((TextView) findViewById(R.id.tv_jogosPerdidos)).
                setText("" + userDetails.getJogosPerdidos());
    }

    // --- file utils ---
    public boolean setUserFile(){
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("" + getBaseContext().getFilesDir().getPath().toString() + File.pathSeparator + userFile/*userFile*/);//openFileOutput(userFile, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(userDetails);
            fileOutputStream.getFD().sync(); // boas práticas
            return true;
        } catch (FileNotFoundException e) { e.printStackTrace(); return false; }
          catch (IOException e) { e.printStackTrace(); return false; }
    }

    public boolean getUserFile() {
        try {
            FileInputStream fileInputStream = new FileInputStream("" + getBaseContext().getFilesDir().getPath().toString() + File.pathSeparator + userFile);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            userDetails = (UserDetails) objectInputStream.readObject();
            return true;
        } catch (FileNotFoundException e) {e.printStackTrace();}
        catch (IOException e) { e.printStackTrace();}
        finally { return false; }

    }
}
