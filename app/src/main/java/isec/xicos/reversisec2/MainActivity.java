package isec.xicos.reversisec2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import isec.xicos.reversisec2.GameActivities.AIvsAI_Activity;
import isec.xicos.reversisec2.GameActivities.PvsAI_Activity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnAIvsAI).setOnClickListener(listener -> {
            Intent i = new Intent(this, AIvsAI_Activity.class).
                    putExtra("btnClicked", "AIvsAI");
            startActivity(i);
        });
        findViewById(R.id.btnJogarvsAI).setOnClickListener(listener -> {
            Intent i = new Intent(this, PvsAI_Activity.class);
            startActivity(i);
        });
    }
}
