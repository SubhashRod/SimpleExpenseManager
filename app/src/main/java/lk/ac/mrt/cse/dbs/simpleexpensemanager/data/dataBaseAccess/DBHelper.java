package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.dataBaseAccess;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Subhash on 12/4/2015.
 */
public class DBHelper extends SQLiteOpenHelper {

    // Logcat tag
    public static final String LOG = DBHelper.class.getName();

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "130517U";

    // Table Names
    public static final String TABLE_ACCOUNT = "accounts";
    public static final String TABLE_TRANSACTION = "transactions";

    // Account Table - column names
    public static final String COLUMN_ID = "accountNo";
    public static final String COLUMN_NAME = "bankName";
    public static final String COLUMN_HOLDER = "accountHolderName";
    public static final String COLUMN_BALANCE = "balance";

    // Transaction Table - column nmaes
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_ACCNO = "accountNo";
    public static final String COLUMN_EXPETYPE = "expenseType";
    public static final String COLUMN_AMOUNT = "amount";

    // Table Create Statements
    // Account table create statement
    private static final String CREATE_TABLE_ACCOUNT = "CREATE TABLE "
            + TABLE_ACCOUNT + "(" + COLUMN_ID + " TEXT PRIMARY KEY," + COLUMN_NAME
            + " TEXT," + COLUMN_HOLDER + " TEXT," + COLUMN_BALANCE
            + " DOUBLE" + ")";

    // Transaction table create statement
    private static final String CREATE_TABLE_TRANSACTION = "CREATE TABLE " + TABLE_TRANSACTION
            + "(" + COLUMN_ACCNO + " TEXT PRIMARY KEY," + COLUMN_DATE + " DATETIME,"
            + COLUMN_EXPETYPE + " TEXT," + COLUMN_AMOUNT + " DOUBLE" + ")";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TRANSACTION);
        db.execSQL(CREATE_TABLE_ACCOUNT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTION);

        onCreate(db);
    }
}