package om.superquizz.diginamic.superquizz.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDatabase extends SQLiteOpenHelper {

    private final static String DATABASE_NAME = "userDatabase";
    private final static int DATABASE_VERSION = 1;


    private static final String DROP_TABLE_QUERY = "DROP TABLE IF EXISTS ";

    static private UserDatabase dbInstance;

    private UserDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onUpgrade(SQLiteDatabase db) {
        super.onConfigure(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
