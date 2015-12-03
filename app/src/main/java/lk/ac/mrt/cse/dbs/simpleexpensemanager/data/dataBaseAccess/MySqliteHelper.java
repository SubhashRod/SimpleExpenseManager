package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.dataBaseAccess;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Subhash on 12/3/2015.
 */
public class MySqliteHelper extends SQLiteOpenHelper {

    public static final String TABLE_ACCOUNT = "Accounts";
    public static final String COLUMN_ID = "accountNo";
    public static final String COLUMN_NAME = "bankName";
    public static final String COLUMN_HOLDER = "accountHolderName";
    public static final String COLUMN_BALANCE = "balance";

    public static final String TABLE_TRANSACTION = "Transactions";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_ACCNO = "accountNo";
    public static final String COLUMN_EXPETYPE = "expenseType";
    public static final String COLUMN_AMOUNT = "amount";

    private static final String DATABASE_NAME = "130517U.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE1 = "create table "
            + TABLE_ACCOUNT + "(" + COLUMN_ID
            + " text primary key, " + COLUMN_NAME
            + " text not null, "+ COLUMN_HOLDER + "text, "+ COLUMN_BALANCE + "double);";

    private static final String DATABASE_CREATE2 = "create table "
            + TABLE_TRANSACTION + "(" + COLUMN_DATE
            + " date, " + COLUMN_ACCNO
            + " text primary key, "+ COLUMN_EXPETYPE + "text, "+ COLUMN_AMOUNT + "double);";

    public MySqliteHelper(Context context) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE1);
        db.execSQL(DATABASE_CREATE2);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySqliteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTION);

        onCreate(db);
    }
}
