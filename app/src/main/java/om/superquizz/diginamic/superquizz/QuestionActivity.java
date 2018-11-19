package om.superquizz.diginamic.superquizz;

import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import android.content.Intent;

public class QuestionActivity extends AppCompatActivity {

    private Button goodAnswer;
    private Button[] falseAnswers;

    private void initialize() {
        goodAnswer = findViewById(R.id.answer2);
        falseAnswers = new Button[3];
        falseAnswers[0] = findViewById(R.id.answer1);
        falseAnswers[1] = findViewById(R.id.answer3);
        falseAnswers[2] = findViewById(R.id.answer4);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        initialize();

        goodAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuestionActivity.this, SuccessActivity.class);
                startActivity(intent);
            }
        });

        for (Button badAnswer : falseAnswers) {
            badAnswer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(QuestionActivity.this, FailureActivity.class);
                    startActivity(intent);
                }
            });
        }
    }
}
