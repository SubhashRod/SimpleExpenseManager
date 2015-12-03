package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.dataBaseAccess.MySqliteHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by Subhash on 12/4/2015.
 */
public class InDatabaseTransactionDAO implements TransactionDAO {

    private SQLiteDatabase database;
    private MySqliteHelper dbHelper;

    public InDatabaseTransactionDAO(Context context){
        dbHelper = new MySqliteHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        Transaction transaction = new Transaction(date, accountNo, expenseType, amount);
        ContentValues values = new ContentValues();
        values.put(MySqliteHelper.COLUMN_DATE, transaction.getDate().toString());
        values.put(MySqliteHelper.COLUMN_ACCNO, transaction.getAccountNo());
        values.put(MySqliteHelper.COLUMN_EXPETYPE, transaction.getExpenseType().toString());
        values.put(MySqliteHelper.COLUMN_AMOUNT, transaction.getAmount());

        database.insert(MySqliteHelper.TABLE_TRANSACTION, null, values);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        return null;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        return null;
    }
}
