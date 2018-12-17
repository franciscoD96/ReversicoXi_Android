package isec.xicos.reversisec2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class EscolheDificuldadeAIActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escolhe_dificuldade_ai);

        Intent received = getIntent();
        String params = received.getStringExtra("btnClicked");

        if (params.equals("AIvsAI")) {
            // TODO: todo, todo, todotododododododododo
            //TODO:
        }
    }
}
