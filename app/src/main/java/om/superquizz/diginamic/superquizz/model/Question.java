package om.superquizz.diginamic.superquizz.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Question implements Parcelable {
    private int id;
    private String intitule;
    private String[] propositions;
    private int goodAnswer;
    private boolean isAnswered;
    private boolean isSucceeded;
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
        isAnswered = in.readByte() != 0;
        isSucceeded = in.readByte() != 0;
    }

    // Getters and setters

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
    public void setId(int id) {this.id = id;}
    public void setBonneReponse(int indexQuestion) {
        this.goodAnswer = indexQuestion;
    }
    public int getId() {return id;}
    public boolean getIsAnswered() {return isAnswered;}
    public boolean setIsAnswered(boolean isAnswered) {return this.isAnswered = isAnswered;}
    public boolean getIsSucceeded() {return isSucceeded;}
    public boolean setIsSucceeded(boolean isSucceeded) {return this.isSucceeded = isSucceeded;}

    // Parcelable implementation

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
        dest.writeByte((byte) (isAnswered ? 1 : 0));
        dest.writeByte((byte) (isSucceeded ? 1 : 0));
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
}
