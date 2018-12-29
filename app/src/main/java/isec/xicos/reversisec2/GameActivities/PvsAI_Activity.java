package isec.xicos.reversisec2.GameActivities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PersistableBundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import isec.xicos.reversisec2.R;
import isec.xicos.reversisec2.Reversi.Celula;
import isec.xicos.reversisec2.Reversi.Coord;
import isec.xicos.reversisec2.Reversi.ReversicoXi;



public class PvsAI_Activity extends AppCompatActivity {

    ReversicoXi reversi;
    String nivelAI;
    int cntJogadasInvalidas = 0;
    boolean lockAcesso = false;
    List<Integer> pontos;
    String tv1 = "", tv2 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_vs_ai);

        /* TODO. redimensionar o tabuleiro em portrait
            TODO. incluir temporizador
        LinearLayout.LayoutParams lp = ((LinearLayout.LayoutParams)((LinearLayout) findViewById(R.id.LLlinha1)).getLayoutParams());
        lp.width = ((LinearLayout) findViewById(R.id.LLtabuleiro)).getHeight();

        LinearLayout llTabuleiro = findViewById(R.id.LLtabuleiro);
        llTabuleiro.setLayoutParams(new LinearLayout.LayoutParams(llTabuleiro.getMeasuredHeight(), llTabuleiro.getMeasuredHeight()));*/

        if (savedInstanceState != null) {
            reversi = (ReversicoXi) savedInstanceState.getSerializable("reversi");
            nivelAI = savedInstanceState.getString("nivelAI");
            tv1 = savedInstanceState.getString("tv1");
            pontos = savedInstanceState.getIntegerArrayList("pontos");
        } else {
            Intent received = getIntent();
            int playerN = received.getIntExtra("Player", 0); if(playerN == 0) throw new IndexOutOfBoundsException();
            nivelAI = received.getStringExtra("NivelAI");
            if (playerN == 1)
                tv1 = "(" + getText(R.string.user) + ") " + getText(R.string.whites) + ": \n"
                    + "(" + nivelAI + ") " + getText(R.string.blacks) + ": ";
            else
                tv1 = "(" + nivelAI + ") " + getText(R.string.whites) + ": \n"
                    + "(" + getText(R.string.user) + ") " + getText(R.string.blacks) + ": ";

            reversi = new ReversicoXi(playerN, getString(R.string.dumbAI), getString(R.string.smartAI), this.getApplicationContext());
            pontos = new ArrayList<Integer>() {{ add(2); add(2); }};
        }

        tv2 = "" + pontos.get(0) + " pontos\n" + pontos.get(1) + " pontos";

        findViewById(R.id.btn_PassarJogada).setOnClickListener(listener -> {
            String str = reversi.passarJogada();
            Toast.makeText(this, str, Toast.LENGTH_SHORT).show();

            if(str.equals("Ta-Da!"))
                findViewById(R.id.btn_PassarJogada).setVisibility(View.INVISIBLE);

            pontos = (ArrayList)reversi.jogadaAIvsP(nivelAI);

            new CountDownTimer( (Math.round(1000 + (Math.random() * 1000))) , 300) {
                @Override
                public void onTick(long millisUntilFinished) {
                    ((TextView) findViewById(R.id.tv_PvsAI2)).setText("" + pontos.get(0) + " pontos\n" + pontos.get(1) + " pontos");
                    actualizaVistaTabuleiro(reversi.getCampo());

                }

                @Override
                public void onFinish() {
                    ((TextView) findViewById(R.id.tv_PvsAI2)).setText("" + pontos.get(0) + " pontos\n" + pontos.get(1) + " pontos");
                    actualizaVistaTabuleiro(reversi.getCampo());

                    if (pontos.size() == 2)
                        lockAcesso = false;
                    else
                        endGame();
                }
            }.start();
        });

        findViewById(R.id.btn_JogarOutraVez).setOnClickListener(listener -> {
            reversi.jogarOutraVez();
            Toast.makeText(this, "Ta-Da!", Toast.LENGTH_SHORT).show();
            findViewById(R.id.btn_JogarOutraVez).setVisibility(View.INVISIBLE);


        });

        ((TextView) findViewById(R.id.tv_PvsAI1)).setText(tv1);
        ((TextView) findViewById(R.id.tv_PvsAI2)).setText(tv2);
        actualizaVistaTabuleiro(reversi.getCampo());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("reversi", reversi);
        outState.putString("nivelAI", nivelAI);
        outState.putString("tv1", tv1);
        outState.putIntegerArrayList("pontos", (ArrayList<Integer>)pontos);
    }

    public void onClickCampoJogo(View view) {

        Coord c = getCoordEscolhidas(view);

        if(!lockAcesso)
            if ( reversi.checkIsJogadaValida(c) ) {
                lockAcesso = true;
                cntJogadasInvalidas = 0;

                pontos = reversi.jogadaPvsAI(c);
                if (pontos.size() == 2)
                    lockAcesso = false;
                else
                    endGame();

                new CountDownTimer( (Math.round(1000 + (Math.random() * 1000))) , 300) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        ((TextView) findViewById(R.id.tv_PvsAI2)).setText("" + pontos.get(0) + " pontos\n" + pontos.get(1) + " pontos");
                        actualizaVistaTabuleiro(reversi.getCampo());
                        findViewById(R.id.btn_PassarJogada).setEnabled(false);
                        findViewById(R.id.btn_JogarOutraVez).setEnabled(false);
                    }

                    @Override
                    public void onFinish() {
                        pontos = (ArrayList) reversi.jogadaAIvsP(nivelAI);
                        ((TextView) findViewById(R.id.tv_PvsAI2)).setText("" + pontos.get(0) + " pontos\n" + pontos.get(1) + " pontos");
                        actualizaVistaTabuleiro(reversi.getCampo());
                        findViewById(R.id.btn_PassarJogada).setEnabled(true);
                        findViewById(R.id.btn_JogarOutraVez).setEnabled(true);
                        if (reversi.getContadorDeJogadas() > 9)//TODO POR ISTO A 9
                            findViewById(R.id.LL_Cartas).setVisibility(View.VISIBLE);
                        if (pontos.size() == 2)
                            lockAcesso = false;
                        else
                            endGame();



                    }
                }.start();

            } else {
                cntJogadasInvalidas++;
                if (cntJogadasInvalidas > 2)
                    Toast.makeText(this, "Jogada Inválida!", Toast.LENGTH_SHORT).show();
            }

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
        builder.show();
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