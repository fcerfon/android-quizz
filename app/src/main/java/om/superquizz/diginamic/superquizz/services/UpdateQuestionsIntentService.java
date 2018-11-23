package om.superquizz.diginamic.superquizz.services;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import om.superquizz.diginamic.superquizz.api.APIClient;
import om.superquizz.diginamic.superquizz.database.QuestionDatabase;
import om.superquizz.diginamic.superquizz.model.Question;

public class UpdateQuestionsIntentService extends IntentService implements APIClient.APIResult<List<Question>> {

    APIClient client;
    QuestionDatabase db;
    List<Question> localQuestions;

    private void initialize() {
        client = APIClient.getInstance();
        db = QuestionDatabase.getInstance(this);
    }

    public UpdateQuestionsIntentService(String name) {
        super(name);
        initialize();
    }

    public UpdateQuestionsIntentService() {
        super("UpdateQuestions");
        initialize();
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        while (true) {
            client.getQuestions(this);
            SystemClock.sleep(3000);
        }
    }

    private Question getQuestionById(List<Question> questions, int id) {
        for (Question q : questions) {
            if (q.getId() == id) {
                return q;
            }
        }
        return null;
    }

    void addQuestionToDb(Question q) {
        db.addQuestion(q);
    }

    void updateQuestionIfNeeded(Question serverQuestion, Question localQuestion) {
        if (!serverQuestion.getIntitule().equals(localQuestion.getIntitule()) ||
                !serverQuestion.getFirstAnswer().equals(localQuestion.getFirstAnswer()) ||
                !serverQuestion.getSecondAnswer().equals(localQuestion.getSecondAnswer()) ||
                !serverQuestion.getThirdAnswer().equals(localQuestion.getThirdAnswer()) ||
                !serverQuestion.getFourthAnswer().equals(localQuestion.getFourthAnswer()) ||
                serverQuestion.getGoodAnswer() != (localQuestion.getGoodAnswer()))
        {
            db.deleteQuestion(localQuestion);
            db.addQuestion(localQuestion);
        }
    }

    void deleteQuestionInDb(Question q) {
        db.deleteQuestion(q);
    }

    @Override
    // todo : There surely is a better answer
    public void onSuccess(List<Question> serverQuestions) throws IOException {
        localQuestions = db.getAllQuestions();

        for (Question q : localQuestions) {
            Question serverQuestion = getQuestionById(serverQuestions, q.getId());
            if (serverQuestion == null) {
                deleteQuestionInDb(q);
            }
            else {
                updateQuestionIfNeeded(serverQuestion, q);
            }
        }
        for (Question q : serverQuestions) {
            Question localQuestion = getQuestionById(localQuestions, q.getId());
            if (localQuestion == null) {
                addQuestionToDb(q);
            }
        }
    }

    @Override
    public void onFailure(IOException e) {
        Log.e("updateQuestion", "Error while loading questions\n" + e.getMessage());
    }
}
