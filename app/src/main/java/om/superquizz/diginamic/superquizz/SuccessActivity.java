package om.superquizz.diginamic.superquizz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import om.superquizz.diginamic.superquizz.database.QuestionDatabase;
import om.superquizz.diginamic.superquizz.model.Question;

public class SuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        Question q = getIntent().getParcelableExtra("Question");
        q.setIsAnswered(true);
        q.setIsSucceeded(true);
        QuestionDatabase.getInstance(this).editQuestion(q);
    }
}
