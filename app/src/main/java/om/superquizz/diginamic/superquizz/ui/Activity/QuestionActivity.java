package om.superquizz.diginamic.superquizz.ui.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;

import android.content.Intent;
import android.widget.ProgressBar;
import android.widget.TextView;

import om.superquizz.diginamic.superquizz.R;
import om.superquizz.diginamic.superquizz.helper.DelayTask;
import om.superquizz.diginamic.superquizz.model.Question;

public class QuestionActivity extends AppCompatActivity implements DelayTask.progressCallbackInterface {

    private Button goodAnswer;
    private Button[] falseAnswers;
    private ProgressBar pb;
    private Question q;
    private Boolean questionAnswered;

    private void initialize() {

        q = getIntent().getParcelableExtra("question");
        ((TextView)findViewById(R.id.intitule)).setText(q.getIntitule());

        String[] propositions = q.getPropositions();

        Button button1 = findViewById(R.id.answer1);
        Button button2 = findViewById(R.id.answer2);
        Button button3 = findViewById(R.id.answer3);
        Button button4 = findViewById(R.id.answer4);

        button1.setText(propositions[0]);
        button2.setText(propositions[1]);
        button3.setText(propositions[2]);
        button4.setText(propositions[3]);

        falseAnswers = new Button[3];

        switch (q.getGoodAnswer()) {
            case 0:
                goodAnswer = button1;
                falseAnswers[0] = button2;
                falseAnswers[1] = button3;
                falseAnswers[2] = button4;
                break;
            case 1:
                goodAnswer = button2;
                falseAnswers[0] = button1;
                falseAnswers[1] = button3;
                falseAnswers[2] = button4;
                break;
            case 2:
                goodAnswer = button3;
                falseAnswers[0] = button1;
                falseAnswers[1] = button2;
                falseAnswers[2] = button4;
                break;
            case 3:
                goodAnswer = button4;
                falseAnswers[0] = button1;
                falseAnswers[1] = button2;
                falseAnswers[2] = button3;
                break;
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
                questionAnswered = true;
                Intent intent = new Intent(QuestionActivity.this, SuccessActivity.class);
                intent.putExtra("Question", q);
                startActivity(intent);
            }
        });

        for (Button badAnswer : falseAnswers) {
            badAnswer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    questionAnswered = true;
                    Intent intent = new Intent(QuestionActivity.this, FailureActivity.class);
                    intent.putExtra("Question", q);
                    startActivity(intent);
                }
            });
        }

        pb = findViewById(R.id.progressBar);
        DelayTask task = new DelayTask(this, 60);
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
        if (!questionAnswered) {
            Intent intent = new Intent(this, FailureActivity.class);
            intent.putExtra("Question", q);
            startActivity(intent);
        }
    }
}
