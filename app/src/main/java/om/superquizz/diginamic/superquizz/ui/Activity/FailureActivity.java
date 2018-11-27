package om.superquizz.diginamic.superquizz.ui.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import om.superquizz.diginamic.superquizz.R;
import om.superquizz.diginamic.superquizz.database.QuestionDatabase;
import om.superquizz.diginamic.superquizz.model.Question;

public class FailureActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_failure);
        Question q = getIntent().getParcelableExtra("Question");
        q.setIsAnswered(true);
        q.setIsSucceeded(false);
        QuestionDatabase.getInstance(this).editQuestion(q);
    }
}
