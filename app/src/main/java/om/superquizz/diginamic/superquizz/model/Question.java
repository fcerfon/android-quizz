package om.superquizz.diginamic.superquizz.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Question implements Parcelable {
    private int id;
    private String intitule;
    private String[] propositions;
    private int goodAnswer;
//    private TypeQuestion type;

    public Question(int id, String question, String firstAnswer, String secondAnswer,
                    String thirdAnswer, String fourthAnswer, int goodAnswer) {
        this.id = id;
        this.intitule = question;

        propositions = new String[4];
        propositions[0] = firstAnswer;
        propositions[1] = secondAnswer;
        propositions[2] = thirdAnswer;
        propositions[3] = fourthAnswer;

        this.goodAnswer = goodAnswer;
    }


    protected Question(Parcel in) {
        id = in.readInt();
        intitule = in.readString();
        propositions = in.createStringArray();
        goodAnswer = in.readInt();
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    public String getFirstAnswer() {return propositions[0];}
    public String getSecondAnswer() {return propositions[1];}
    public String getThirdAnswer() {return propositions[2];}
    public String getFourthAnswer() {return propositions[3];}
    public int getGoodAnswer() {return goodAnswer;}
    public String getGoodAnswerToString() { return propositions[goodAnswer]; };

    public String getIntitule() {
        return intitule;
    }

    public String[] getPropositions() {
        return propositions;
    }

    public void setBonneReponse(int indexQuestion) {
        this.goodAnswer = indexQuestion;
    }

    /*public TypeQuestion getType() {
        return type;
    }*/

    /*public void setType(TypeQuestion type) {
        this.type = type;
    }*/

    public int getId() {return id;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(intitule);
        dest.writeStringArray(propositions);
        dest.writeInt(goodAnswer);
    }
}
