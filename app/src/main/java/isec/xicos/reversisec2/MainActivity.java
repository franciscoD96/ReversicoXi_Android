package isec.xicos.reversisec2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import isec.xicos.reversisec2.GameActivities.AIvsAI_Activity;
import isec.xicos.reversisec2.GameActivities.PvsAI_Activity;
import isec.xicos.reversisec2.GameActivities.PvsP_Activity;
import isec.xicos.reversisec2.GameActivities.RemoteGame_Activity;
import isec.xicos.reversisec2.UserProfile.UserProfileActivity;

public class MainActivity extends AppCompatActivity {

    LinearLayout LL_AIvsAI, LLjogarvsAI, LLjogarvsAmigo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //  __ AI vs AI
        LL_AIvsAI = findViewById(R.id.LL_AIvsAI);
        findViewById(R.id.btnAIvsAI).setOnClickListener(listener -> {
            if (LL_AIvsAI.getVisibility() == View.GONE)
                findViewById(R.id.LL_AIvsAI).setVisibility(View.VISIBLE);
            else
                findViewById(R.id.LL_AIvsAI).setVisibility(View.GONE);
        });
        findViewById(R.id.startAIvsAIgame).setOnClickListener(listener -> {
            Intent i = new Intent(this, AIvsAI_Activity.class).
                    putExtra("AI1", ((RadioButton) findViewById(((RadioGroup) findViewById(R.id.RG_AIvsAI_AIl)).getCheckedRadioButtonId())).getText()).
                    putExtra("AI2", ((RadioButton) findViewById(((RadioGroup) findViewById(R.id.RG_AIvsAI_AI2)).getCheckedRadioButtonId())).getText());
            startActivity(i);
        });

        //  __ P vs AI
        LLjogarvsAI = findViewById(R.id.LLjogarvsAI);
        findViewById(R.id.btnJogarvsAI).setOnClickListener(listener -> {
            if (LLjogarvsAI.getVisibility() == View.GONE)
                findViewById(R.id.LLjogarvsAI).setVisibility(View.VISIBLE);
            else
                findViewById(R.id.LLjogarvsAI).setVisibility(View.GONE);
        });
        findViewById(R.id.startPvsAIgame).setOnClickListener(listener -> {
            String jogarComo = (String)((RadioButton) findViewById(((RadioGroup) findViewById(R.id.RG_PvsAI_numPlayer)).getCheckedRadioButtonId())).getText();
            int playerN = 0;
            if(jogarComo.equals(getString(R.string.whites)))
                playerN = 1;
            else if (jogarComo.equals(getString(R.string.blacks)))
                playerN = 2;

            Intent i = new Intent(this, PvsAI_Activity.class).
                    putExtra("NivelAI", ((RadioButton) findViewById(((RadioGroup) findViewById(R.id.RG_PvsAI_AIlvl)).getCheckedRadioButtonId())).getText()).
                    putExtra("Player", playerN);
            startActivity(i);
        });

        //  __ P vs P
        LLjogarvsAmigo = findViewById(R.id.LLjogarvsAmigo);
        findViewById(R.id.btnJogarvsAmigo).setOnClickListener(listener -> {
            if (LLjogarvsAmigo.getVisibility() == View.GONE)
                findViewById(R.id.LLjogarvsAmigo).setVisibility(View.VISIBLE);
            else
                findViewById(R.id.LLjogarvsAmigo).setVisibility(View.GONE);
        });
        findViewById(R.id.startPvsPamigo).setOnClickListener(listener -> {
            String jogarComo = (String)((RadioButton) findViewById(((RadioGroup) findViewById(R.id.RG_PvsP_Residente)).getCheckedRadioButtonId())).getText();
            int playerN = 0;
            if(jogarComo.equals(getString(R.string.whites)))
                playerN = 1;
            else if (jogarComo.equals(getString(R.string.blacks)))
                playerN = 2;

            Intent i = new Intent(this, PvsP_Activity.class).
                    putExtra("JogadorResidente", playerN);
            startActivity(i);
        });

        //  __ Go to Perfil
        findViewById(R.id.btnVerPerfil).setOnClickListener(listener -> {
            Intent i = new Intent(this, UserProfileActivity.class);
            startActivity(i);
        });

        findViewById(R.id.btn_networkPlay).setOnClickListener(listener -> {
            Intent i = new Intent(this, RemoteGame_Activity.class);
            startActivity(i);
        });
    }
}
