package om.superquizz.diginamic.superquizz.dao;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import java.util.ArrayList;

import om.superquizz.diginamic.superquizz.model.Question;

public class QuestionMemDao implements QuestionDao, Parcelable {

    private List<Question> questions;

    public QuestionMemDao() {
        questions = new ArrayList<>();
    }

    public List<Question> findAll() {
        return questions;
    }
    public void save(Question question) {
        int size = questions.size();
        question.setId(size + 1);
        questions.add(question);
    }
    public void delete(Question question) {
        questions.remove(question);
    }

    // Parcelable implementation

    public int describeContents() {return 0;}

    public void writeToParcel(Parcel out, int flags) {
        out.writeTypedList(questions);
    }

    public static final Parcelable.Creator<QuestionMemDao> CREATOR
            = new Parcelable.Creator<QuestionMemDao>() {

        public QuestionMemDao createFromParcel(Parcel in) {
            return new QuestionMemDao(in);
        }

        public QuestionMemDao[] newArray(int size) {
            return new QuestionMemDao[size];
        }

    };

    private QuestionMemDao(Parcel in) {
        questions = new ArrayList<Question>();
        in.readTypedList(questions, Question.CREATOR);
    }
}
