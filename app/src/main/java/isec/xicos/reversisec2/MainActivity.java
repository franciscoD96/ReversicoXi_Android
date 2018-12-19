package isec.xicos.reversisec2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import isec.xicos.reversisec2.GameActivities.AIvsAI_Activity;
import isec.xicos.reversisec2.GameActivities.PvsAI_Activity;
import isec.xicos.reversisec2.GameActivities.PvsP_Activity;

public class MainActivity extends AppCompatActivity {

    LinearLayout LLjogarvsAI;
    LinearLayout LLjogarvsAmigo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
            if(jogarComo == "Brancas") playerN = 1;
            else if (jogarComo == "Pretas") playerN = 2;

            Intent i = new Intent(this, PvsAI_Activity.class).
                    putExtra("NivelAI", ((RadioButton) findViewById(((RadioGroup) findViewById(R.id.RG_PvsAI_AIlvl)).getCheckedRadioButtonId())).getText()).
                    putExtra("Player", playerN);
            startActivity(i);
        });

        LLjogarvsAmigo = findViewById(R.id.LLjogarvsAmigo);
        findViewById(R.id.btnJogarvsAmigo).setOnClickListener(listener -> {
            if (LLjogarvsAmigo.getVisibility() == View.GONE)
                findViewById(R.id.LLjogarvsAmigo).setVisibility(View.VISIBLE);
            else
                findViewById(R.id.LLjogarvsAmigo).setVisibility(View.GONE);
        });
        findViewById(R.id.startPvsPamigo).setOnClickListener(listener -> {
            String jogarComo = (String)((RadioButton) findViewById(((RadioGroup) findViewById(R.id.RG_PvsAI_numPlayer)).getCheckedRadioButtonId())).getText();
            int playerN = 0;
            if(jogarComo == "Brancas") playerN = 1;
            else if (jogarComo == "Pretas") playerN = 2;

            Intent i = new Intent(this, PvsP_Activity.class).
                    putExtra("JogadorResidente", playerN);
            startActivity(i);
        });


/*        findViewById(R.id.btnJogarvsAI).setOnClickListener(listener -> {
            Intent i = new Intent(this, EscolheDificuldadeAIActivity.class).
                    putExtra("btnClicked", "PvsAI");
            startActivity(i);
        });*/
    }
}
