package isec.xicos.reversisec2.GameActivities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import isec.xicos.reversisec2.R;
import isec.xicos.reversisec2.Reversi.Celula;
import isec.xicos.reversisec2.Reversi.Coord;
import isec.xicos.reversisec2.Reversi.ReversicoXi;


public class PvsAI_Activity extends AppCompatActivity {

    ReversicoXi reversi;
    int cntJogadasInvalidas = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_vs_ai);

        ((LinearLayout) findViewById(R.id.LLtabuleiro)).setMinimumHeight(( findViewById(R.id.LLlinha1).getHeight() ));

        if (savedInstanceState != null)
            reversi = (ReversicoXi) savedInstanceState.getSerializable("reversi");
        else {
            Intent received = getIntent();
            String nivelAI = received.getStringExtra("NivelAI");
            int playerN = received.getIntExtra("Player", 0); if(playerN == 0) throw new IndexOutOfBoundsException();

            reversi = new ReversicoXi(playerN);
        }

        actualizaVistaTabuleiro(reversi.getCampo());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("reversi", reversi);
    }

    public void onClickCampoJogo(View view) {

        Coord c = getCoordEscolhidas(view);

        Log.d("tag", "jogadaUser " + c.getX() + c.getY());
        for (Coord co : reversi.getPosJogaveis())
            Log.d("tag", "posJogaveis:" + co.getX() + co.getY());

        if ( reversi.checkIsJogadaValida(c) ) {
            cntJogadasInvalidas = 0; // só para não aparecer o toast de todas as vezes que o user se engana

            reversi.jogadaUser(c);
            actualizaVistaTabuleiro(reversi.getCampo());

            new CountDownTimer(Math.round(500 + (Math.random() * 1000)), 1000) {
                @Override
                public void onTick(long millisUntilFinished) {}

                @Override
                public void onFinish() {
                    reversi.jogadaDumbAI();
                    actualizaVistaTabuleiro(reversi.getCampo());
                }
            }.start();

        } else {
            cntJogadasInvalidas++;
            if (cntJogadasInvalidas > 2)
                Toast.makeText(this, "Jogada Inválida!", Toast.LENGTH_SHORT).show();
        }

    }

    // Modelo
    public Coord getCoordEscolhidas(View v) {
        ImageView iv = (ImageView) v;
        String nome = getResources().getResourceEntryName(iv.getId());
        return new Coord( Character.getNumericValue(nome.charAt(1)),
                          Character.getNumericValue(nome.charAt(3)));
    }
    public void actualizaVistaTabuleiro(List<List<Celula>> c) {
        String cel;
        ImageView iv;
        for (int x = 0; x < 8; x++)
            for (int y =0; y < 8; y++) {
                String res = c.get(y).get(x).getCelula();
                cel = "x" + x + "y" + y;
                iv = (ImageView) findViewById(getResources().getIdentifier(cel, "id", getPackageName()));
                if (res == "Jogavel")
                    iv.setImageResource(R.drawable.jogavel);
                if (res == "Branco")
                    iv.setImageResource(R.drawable.branca);
                if (res == "Preto")
                    iv.setImageResource(R.drawable.preta);
                if (res == "Vazio")
                    iv.setImageResource(R.drawable.vazia);
            }
    }
}