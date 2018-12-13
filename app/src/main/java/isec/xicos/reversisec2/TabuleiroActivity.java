package isec.xicos.reversisec2;

import android.media.Image;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import isec.xicos.reversisec2.Reversi.Celula;
import isec.xicos.reversisec2.Reversi.ReversicoXi;

import static android.widget.Toast.makeText;

public class TabuleiroActivity extends AppCompatActivity {

    ReversicoXi reversi;

    TextView tv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv1 = findViewById(R.id.tv1);

        findViewById(R.id.btnNovoJogo).setOnClickListener(listener -> {
            reversi = new ReversicoXi();
            tv1.setText("Jogador: 1");
            actualizaTabuleiro( reversi.getCampo() );
        });
        findViewById(R.id.btnJogada).setOnClickListener(listener -> {
            tv1.setText("Jogador: " + reversi.jogadaAIvsAI() );
            actualizaTabuleiro( reversi.getCampo() );
        });
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
        ImageView iv = (ImageView) view;

        String nome = getResources().getResourceEntryName(iv.getId());
        int x = Character.getNumericValue(nome.charAt(1));
        int y = Character.getNumericValue(nome.charAt(3));

        Toast.makeText(getApplicationContext(),
                "" + x + " " + y ,
                Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putSerializable("jogoAtual", reversi);
        super.onSaveInstanceState(outState);

        // guardar tudo
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        reversi = (ReversicoXi) savedInstanceState.getSerializable("jogoAtual");

        //reescrever tudo
    }
}
