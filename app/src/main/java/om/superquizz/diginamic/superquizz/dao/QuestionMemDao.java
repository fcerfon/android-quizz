package om.superquizz.diginamic.superquizz.dao;

import java.util.List;
import java.util.ArrayList;

import om.superquizz.diginamic.superquizz.model.Question;

public class QuestionMemDao implements QuestionDao {

    private List<Question> questions;

    public QuestionMemDao() {
        questions = new ArrayList<>();
    }

    public List<Question> findAll() {
        return questions;
    }
    public void save(Question question) {
        questions.add(question);
    }
    public void delete(Question question) {
        questions.remove(question);
    }
}
