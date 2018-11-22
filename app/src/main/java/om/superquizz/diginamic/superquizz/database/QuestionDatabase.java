package om.superquizz.diginamic.superquizz.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import om.superquizz.diginamic.superquizz.model.Question;

public class QuestionDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "questionDatabse";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String QUESTION_TABLE = "QCM";
    private static final String KEY_QUESTION_ID = "id";
    private static final String KEY_QUESTION_TITLE = "intitule";
    private static final String KEY_QUESTION_FIRST_ANSWER = "first_answer";
    private static final String KEY_QUESTION_SECOND_ANSWER = "second_answer";
    private static final String KEY_QUESTION_THIRD_ANSWER = "third_answer";
    private static final String KEY_QUESTION_FOURTH_ANSWER = "fourth_answer";
    private static final String KEY_QUESTION_GOOD_ANSWER = "good_answer";

    private static final String DROP_TABLE_QUERY = "DROP TABLE IF EXISTS ";

    static private QuestionDatabase dbInstance;

    // Singleton Pattern functions

    private QuestionDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // todo : comprendre le context (domaine de l'application, genre une interface)
    public static synchronized QuestionDatabase getInstance(Context context) {
        if (dbInstance == null) {
            dbInstance = new QuestionDatabase(context.getApplicationContext());
        }
        return dbInstance;
    }

    // Basic configuration functions

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL(DROP_TABLE_QUERY + QUESTION_TABLE);
            onCreate(db);
        }
    }

    // Query functions

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_QUESTION_TABLE = "CREATE TABLE " + QUESTION_TABLE +
                "(" +
                    KEY_QUESTION_ID + " INTEGER PRIMARY KEY," +
                    KEY_QUESTION_TITLE + " NOT NULL," +
                    KEY_QUESTION_FIRST_ANSWER + " NOT NULL," +
                    KEY_QUESTION_SECOND_ANSWER + " NOT NULL," +
                    KEY_QUESTION_THIRD_ANSWER + "," +
                    KEY_QUESTION_FOURTH_ANSWER + "," +
                    KEY_QUESTION_GOOD_ANSWER + " INTEGER NOT NULL" +
                ")";

        db.execSQL(CREATE_QUESTION_TABLE);
    }

    public void deleteQuestion(Question q) {
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = "id=" + q.getId();
        db.delete(QUESTION_TABLE, whereClause, null);
    }

    public List<Question> getAllQuestions() {

        List<Question> questions = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        String[] columns = new String[]{ KEY_QUESTION_ID, KEY_QUESTION_TITLE, KEY_QUESTION_FIRST_ANSWER, KEY_QUESTION_SECOND_ANSWER, KEY_QUESTION_THIRD_ANSWER, KEY_QUESTION_FOURTH_ANSWER, KEY_QUESTION_GOOD_ANSWER };
        Cursor c = db.query(QUESTION_TABLE, columns, null, null, null, null, null);

        c.moveToFirst();
        while (!c.isAfterLast()) {

            int id = c.getInt(c.getColumnIndex(KEY_QUESTION_ID));
            String intitule = c.getString(c.getColumnIndex(KEY_QUESTION_TITLE));
            String first_answer = c.getString(c.getColumnIndex(KEY_QUESTION_FIRST_ANSWER));
            String second_answer = c.getString(c.getColumnIndex(KEY_QUESTION_SECOND_ANSWER));
            String third_answer = c.getString(c.getColumnIndex(KEY_QUESTION_THIRD_ANSWER));
            String fourth_answer = c.getString(c.getColumnIndex(KEY_QUESTION_FOURTH_ANSWER));
            int good_answer = c.getInt(c.getColumnIndex(KEY_QUESTION_GOOD_ANSWER));

            questions.add(new Question(id, intitule, first_answer, second_answer, third_answer, fourth_answer, good_answer));

            c.moveToNext();
        }

        c.close();
        return questions;
    }

    public long addQuestion(Question q) {

        // variables initialization
        long questionId = -1;

        // database initialization
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();

        // start update process
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_QUESTION_TITLE, q.getIntitule());
            values.put(KEY_QUESTION_FIRST_ANSWER, q.getFirstAnswer());
            values.put(KEY_QUESTION_GOOD_ANSWER, q.getGoodAnswer());
            values.put(KEY_QUESTION_SECOND_ANSWER, q.getSecondAnswer());
            if (!q.getThirdAnswer().equals("")) {
                values.put(KEY_QUESTION_THIRD_ANSWER, q.getThirdAnswer());
            }
            if (!q.getFourthAnswer().equals("")) {
                values.put(KEY_QUESTION_FOURTH_ANSWER, q.getFourthAnswer());
            }

            questionId = db.insertOrThrow(QUESTION_TABLE, null, values);
            db.setTransactionSuccessful();

        } catch (Exception e) {
            Log.e("DATABASE_ERROR", "Error while trying to add a question");
        } finally {
            db.endTransaction();
        }

        return questionId;
    }
}
