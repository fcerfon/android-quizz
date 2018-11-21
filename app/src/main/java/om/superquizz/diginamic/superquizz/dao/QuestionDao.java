package om.superquizz.diginamic.superquizz.dao;

import java.util.List;
import om.superquizz.diginamic.superquizz.model.Question;

public interface QuestionDao {
    List<Question> findAll();
    void save(Question question);
    void delete(Question question);
}
