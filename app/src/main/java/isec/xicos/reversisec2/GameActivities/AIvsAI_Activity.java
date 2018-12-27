package isec.xicos.reversisec2.GameActivities;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import isec.xicos.reversisec2.R;
import isec.xicos.reversisec2.Reversi.Celula;
import isec.xicos.reversisec2.Reversi.ReversicoXi;

import static android.widget.Toast.makeText;

public class AIvsAI_Activity extends AppCompatActivity {

    ReversicoXi reversi;
    public static String AI1;
    public static String AI2;
    TextView tv;
    List<Integer> pontos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_vs_ai);

        if (savedInstanceState != null) {
            AI1 = savedInstanceState.getString("AI1");
            AI2 = savedInstanceState.getString("AI2");
            reversi = (ReversicoXi) savedInstanceState.getSerializable("reversi");
        } else {
            Intent i = getIntent();
            AI1 = i.getStringExtra("AI1");
            AI2 = i.getStringExtra("AI2");

            reversi = new ReversicoXi( ((Math.random() > 0.5) ? 1 : 2), getString(R.string.dumbAI), getString(R.string.smartAI), this.getApplicationContext());
        }

        actualizaTabuleiro( reversi.getCampo() );

        tv = findViewById(R.id.tv1); tv.setText("Brancas = " + AI1 + "\nPretas = " + AI2);
        findViewById(R.id.btnNovoJogo).setOnClickListener(listener -> {

            reversi = new ReversicoXi( ((Math.random() > 0.5) ? 1 : 2), getString(R.string.dumbAI), getString(R.string.smartAI), this.getApplicationContext());
            actualizaTabuleiro( reversi.getCampo() );
        });
        findViewById(R.id.btnJogada).setOnClickListener(listener -> {

            pontos = reversi.jogadaAIvsAI();
            actualizaTabuleiro(reversi.getCampo());

            if (pontos.size() != 2)
                endGame();
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("reversi", reversi);
        outState.putString("AI1", AI1);
        outState.putString("AI2", AI2);
    }
    private void endGame() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (pontos.get(0) != pontos.get(1)) // empate ?
            if (pontos.get(0) > pontos.get(1))
                builder.setTitle("" + getString(R.string.gameEnded) + "\n" + getString(R.string.whites) + " " + getString(R.string.won) );
            else
                builder.setTitle("" + getString(R.string.gameEnded) + "\n" + getString(R.string.blacks) + " " + getString(R.string.won) );
        else
            builder.setTitle("" + getString(R.string.gameEnded) + "\n" + getString(R.string.itsADraw) );
        builder.setPositiveButton("OK", (dialog, which) -> {});
    }

    public void actualizaTabuleiro(List<List<Celula>> c) {
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

    public void onClickCampoJogo(View view) {
        Toast.makeText(this, "Para jogar escolha outro modo de jogo", Toast.LENGTH_SHORT).show();
    }
}
