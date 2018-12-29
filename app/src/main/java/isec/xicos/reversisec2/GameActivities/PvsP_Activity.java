package isec.xicos.reversisec2.GameActivities;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import isec.xicos.reversisec2.R;
import isec.xicos.reversisec2.Reversi.Celula;
import isec.xicos.reversisec2.Reversi.Coord;
import isec.xicos.reversisec2.Reversi.ReversicoXi;


public class PvsP_Activity extends AppCompatActivity {

    ReversicoXi reversi;
    int cntJogadasInvalidas = 0;
    boolean lockAcesso = false;
    List<Integer> pontos;
    String tv1 = "", tv2 = "";

    Boolean u1PJo = false, u2PJo = false, u1PVe = false, u2PVe = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_vs_p);


        if (savedInstanceState != null) {
            reversi = (ReversicoXi) savedInstanceState.getSerializable("reversi");
            tv1 = savedInstanceState.getString("tv1");
            pontos = savedInstanceState.getIntegerArrayList("pontos");
        } else {
            Intent received = getIntent();
            int playerN = received.getIntExtra("JogadorResidente", 0); if(playerN == 0) throw new IndexOutOfBoundsException();
            if (playerN == 1)
                tv1 = "(" + getText(R.string.localPlayer) + ") " + getText(R.string.whites) + ": \n"
                    + "(" + getString(R.string.GuestPlayer) + ") " + getText(R.string.blacks) + ": ";
            else
                tv1 = "(" + getString(R.string.GuestPlayer) + ") " + getText(R.string.whites) + ": \n"
                    + "(" + getText(R.string.localPlayer) + ") " + getText(R.string.blacks) + ": ";

            reversi = new ReversicoXi(playerN, "Player 1", "Player 2", this.getApplicationContext());
            pontos = new ArrayList<Integer>() {{ add(2); add(2); }};
        }

        tv2 = "" + pontos.get(0) + " pontos\n" + pontos.get(1) + " pontos";

        findViewById(R.id.btn_PassarJogada).setOnClickListener(listener -> {
            int jogadorInvocador = reversi.getJogadorAtual();
            String str = reversi.passarJogadaPVP();
            Toast.makeText(this, str, Toast.LENGTH_SHORT).show();

            if (jogadorInvocador == 1) {
                if(str.equals("Ta-Da!")) {
                    findViewById(R.id.btn_PassarJogada).setVisibility(View.INVISIBLE);
                    u1PJo = true;
                    //findViewById(R.id.btn_PassarJogada).setVisibility(View.VISIBLE);
                }
            }
            if (jogadorInvocador == 2) {
                if(str.equals("Ta-Da!")) {
                    findViewById(R.id.btn_PassarJogada).setVisibility(View.INVISIBLE);
                    u2PJo = true;
                    //findViewById(R.id.btn_PassarJogada).setVisibility(View.VISIBLE);
                }
            }
            actualizaVistaTabuleiro(reversi.getCampo());



        });

        findViewById(R.id.btn_JogarOutraVez).setOnClickListener(listener -> {
            reversi.jogarOutraVez();
            Toast.makeText(this, "Ta-Da!", Toast.LENGTH_SHORT).show();
            //findViewById(R.id.btn_JogarOutraVez).setVisibility(View.INVISIBLE);
            int jogadorInvocador = reversi.getJogadorAtual();
            if (jogadorInvocador == 1) {
                findViewById(R.id.btn_JogarOutraVez).setVisibility(View.INVISIBLE);
                u1PVe = true;
                reversi.JogarOutraVezPvP();
            }
            if (jogadorInvocador == 2) {
                findViewById(R.id.btn_JogarOutraVez).setVisibility(View.INVISIBLE);
                u2PVe = true;
                reversi.JogarOutraVezPvP();
            }
            actualizaVistaTabuleiro(reversi.getCampo());
        });


        ((TextView) findViewById(R.id.tv_PvsAI1)).setText(tv1);
        ((TextView) findViewById(R.id.tv_PvsAI2)).setText(tv2);
        actualizaVistaTabuleiro(reversi.getCampo());

    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("reversi", reversi);
        outState.putString("tv1", tv1);
        outState.putIntegerArrayList("pontos", (ArrayList<Integer>)pontos);
    }

    public void onClickCampoJogo(View view) {

        Coord c = getCoordEscolhidas(view);

        if(!lockAcesso)
            if ( reversi.checkIsJogadaValida(c) ) {
                lockAcesso = true;
                cntJogadasInvalidas = 0; // só para não aparecer o toast de todas as vezes que o user se engana

                pontos = reversi.jogadaPvsP(c);

                actualizaVistaTabuleiro(reversi.getCampo());

                new CountDownTimer( (Math.round(1000 + (Math.random() * 1000))) , 400) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        ((TextView) findViewById(R.id.tv_PvsAI2)).setText("" + pontos.get(0) + " pontos\n" + pontos.get(1) + " pontos");
                        findViewById(R.id.btn_PassarJogada).setEnabled(false);
                        findViewById(R.id.btn_JogarOutraVez).setEnabled(false);

                    }

                    @Override
                    public void onFinish() {
                        actualizaVistaTabuleiro(reversi.getCampo());
                        findViewById(R.id.btn_PassarJogada).setEnabled(true);
                        findViewById(R.id.btn_JogarOutraVez).setEnabled(true);
                        if(pontos.size() == 2)
                            lockAcesso = false;
                        else
                            endGame();


                        //TODO nota: o jogador so pode jogar na vez a seguir, e uma feature ( manual )
                        if (reversi.getContadorDeJogadas() > 9) {//TODO passar para 9
                            findViewById(R.id.LL_Cartas).setVisibility(View.VISIBLE);//ta certo

                            if (reversi.getJogadorAtual() == 1) {
                                if (u1PJo == false)
                                    findViewById(R.id.btn_PassarJogada).setVisibility(View.VISIBLE);
                                else
                                    findViewById(R.id.btn_PassarJogada).setVisibility(View.INVISIBLE);
                                if (u1PVe == false)
                                    findViewById(R.id.btn_JogarOutraVez).setVisibility(View.VISIBLE);
                                else
                                    findViewById(R.id.btn_JogarOutraVez).setVisibility(View.INVISIBLE);
                            }

                            if (reversi.getJogadorAtual() == 2) {
                                if (u2PJo == false)
                                    findViewById(R.id.btn_PassarJogada).setVisibility(View.VISIBLE);
                                else
                                    findViewById(R.id.btn_PassarJogada).setVisibility(View.INVISIBLE);
                                if (u2PVe == false)
                                    findViewById(R.id.btn_JogarOutraVez).setVisibility(View.VISIBLE);
                                else
                                    findViewById(R.id.btn_JogarOutraVez).setVisibility(View.INVISIBLE);
                            }
                        }
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

        if (pontos.get(0) != pontos.get(1))
            if (pontos.get(0) > pontos.get(1))
                builder.setTitle("" + getString(R.string.gameEnded) + "\n" + getString(R.string.whites) + " " + getString(R.string.won) );
            else
                builder.setTitle("" + getString(R.string.gameEnded) + "\n" + getString(R.string.blacks) + " " + getString(R.string.won) );
        else
            builder.setTitle("" + getString(R.string.gameEnded) + "\n" + getString(R.string.itsADraw) );

        builder.setPositiveButton("OK", (dialog, which) -> {

        });
        builder.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel());
        builder.show();


        //
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
