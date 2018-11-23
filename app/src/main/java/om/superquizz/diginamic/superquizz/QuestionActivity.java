package om.superquizz.diginamic.superquizz;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.content.Intent;
import android.widget.ProgressBar;
import android.widget.TextView;

import om.superquizz.diginamic.superquizz.helper.DelayTask;
import om.superquizz.diginamic.superquizz.model.Question;

public class QuestionActivity extends AppCompatActivity implements DelayTask.progressCallbackInterface {

    private Button goodAnswer;
    private Button[] falseAnswers;
    private ProgressBar pb;

    private void initialize() {

        Question q = getIntent().getParcelableExtra("question");
        ((TextView)findViewById(R.id.intitule)).setText(q.getIntitule());

        String[] propositions = q.getPropositions();
        ((Button)findViewById(R.id.answer1)).setText(propositions[0]);
        ((Button)findViewById(R.id.answer2)).setText(propositions[1]);
        ((Button)findViewById(R.id.answer3)).setText(propositions[2]);
        ((Button)findViewById(R.id.answer4)).setText(propositions[3]);

        falseAnswers = new Button[3];

        // todo factoriser
        if (q.getGoodAnswer() == 0) {
            goodAnswer = findViewById(R.id.answer1);
            falseAnswers[0] = findViewById(R.id.answer2);
            falseAnswers[1] = findViewById(R.id.answer3);
            falseAnswers[2] = findViewById(R.id.answer4);
        }
        if (q.getGoodAnswer() == 1) {
            goodAnswer = findViewById(R.id.answer2);
            falseAnswers[0] = findViewById(R.id.answer1);
            falseAnswers[1] = findViewById(R.id.answer3);
            falseAnswers[2] = findViewById(R.id.answer4);
        }
        if (q.getGoodAnswer() == 2) {
            goodAnswer = findViewById(R.id.answer3);
            falseAnswers[0] = findViewById(R.id.answer1);
            falseAnswers[1] = findViewById(R.id.answer2);
            falseAnswers[2] = findViewById(R.id.answer4);
        }
        if (q.getGoodAnswer() == 3) {
            goodAnswer = findViewById(R.id.answer4);
            falseAnswers[0] = findViewById(R.id.answer1);
            falseAnswers[1] = findViewById(R.id.answer2);
            falseAnswers[2] = findViewById(R.id.answer3);
        }
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

        pb = findViewById(R.id.progressBar);
        DelayTask task = new DelayTask(this);
        task.execute();
    }

    @Override
    public void onTaskStarted() {
        pb.setVisibility(ProgressBar.VISIBLE);
    }

    @Override
    public void onTaskProgress(int progressValue) {
        pb.setProgress(progressValue);
    }

    @Override
    public void onTaskCompleted() {
        Intent intent = new Intent(this, FailureActivity.class);
        startActivity(intent);
    }
}
