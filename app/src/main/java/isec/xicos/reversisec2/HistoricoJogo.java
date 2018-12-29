package isec.xicos.reversisec2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import isec.xicos.reversisec2.Reversi.Campo;
import isec.xicos.reversisec2.Reversi.Celula;
import isec.xicos.reversisec2.Reversi.ReversicoXi;


public class HistoricoJogo extends AppCompatActivity {

    class Historico implements Serializable {
        List<Campo> historicoJogo;

        public Historico() {historicoJogo = new ArrayList<>();}
        public Historico(List<Campo> historicoJogo) {this.historicoJogo = historicoJogo;}
        public List<Campo> getHistoricoJogo() {return historicoJogo;}
        public void add(Campo campo) {historicoJogo.add(campo);}
        public Campo get(int i) {return historicoJogo.get(i);}
        public List<List<Celula>> getAsArrayofArrays(int i) {return historicoJogo.get(i).getCampo();}
        public int getSize() {return (historicoJogo == null) ? 0 : historicoJogo.size();}
    }

    Historico historicoJogo;
    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico_jogo);

        historicoJogo = new Historico();

        if (savedInstanceState != null) {
            historicoJogo = ((Historico)savedInstanceState.getSerializable("historico"));
            i = savedInstanceState.getInt("i");
        } else {
            historicoJogo = new Historico();
            i = 0;
        }

        findViewById(R.id.btn_prev).setOnClickListener(listener -> {
            if(i > 0) {
                i--;
                Campo campoAApresentar = historicoJogo.get(i);
                actualizaVistaTabuleiro(campoAApresentar.getCampo());
            }
        });
        findViewById(R.id.btn_next).setOnClickListener(listener -> {
            if(i < historicoJogo.getSize()) {
                i++;
                List<List<Celula>> campoAApresentar = historicoJogo.getAsArrayofArrays(i);
                actualizaVistaTabuleiro(campoAApresentar);
            }
        });
        findViewById(R.id.btn_VerJogosGuardados).setOnClickListener(listener -> {
            File internalDirectory = getFilesDir();
            File[] listFiles = internalDirectory.listFiles();
            List<String> nomesFicheiros = new ArrayList<>(); for (int i = 0; i < listFiles.length; i++) nomesFicheiros.add(listFiles[i].getName());
            AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle(R.string.chooseFile);
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice);
            if (nomesFicheiros != null)
                for (String i : nomesFicheiros)
                    if ( !i.equals("instant-run"))
                        if (!i.equals("reversiUser.serialized"))
                            arrayAdapter.add(i);
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setAdapter(arrayAdapter, (dialog, which) -> {
                if (carregaFicheiro(arrayAdapter.getItem(which)) == false) {
                    Toast.makeText(this, "Couldn't load the file!", Toast.LENGTH_SHORT);
                }
            });
            builder.show();
        });
    }

            @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        outState.putSerializable("historico", historicoJogo);
        outState.putInt("i", i);
    }

    private Boolean carregaFicheiro(String nomeFicheiro) {
        try {
            //File myDir = getDir( "jogosFeitos", MODE_PRIVATE);
            File file = new File(getFilesDir(), nomeFicheiro);
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            ((TextView)findViewById(R.id.tv_Topo_HistoricoJogo)).
                    setText( "" + ((String)objectInputStream.readObject()) +
                        " vs. " + ((String)objectInputStream.readObject()) );

            historicoJogo = new Historico();
            Object readObj;

            while((readObj = objectInputStream.readObject() ) instanceof Campo) {
                historicoJogo.add((Campo) readObj);
            }

            if (historicoJogo.getSize() > 0) {
                actualizaVistaTabuleiro( historicoJogo.getAsArrayofArrays(0) );
                return true;
            } else {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT);
                return false;
            }

        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void onClickCampoJogo(View view) {}

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


