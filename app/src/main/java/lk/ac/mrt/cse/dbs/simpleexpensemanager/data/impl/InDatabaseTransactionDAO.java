package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.style.TtsSpan;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.dataBaseAccess.MySqliteHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by Subhash on 12/4/2015.
 */
public class InDatabaseTransactionDAO implements TransactionDAO {

    private String[] allColumns = { MySqliteHelper.COLUMN_DATE,
            MySqliteHelper.COLUMN_ACCNO,MySqliteHelper.COLUMN_EXPETYPE,MySqliteHelper.COLUMN_AMOUNT };

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
        database = dbHelper.getReadableDatabase();
        List<Transaction> transactions = new ArrayList<Transaction>();

        Cursor cursor = database.query(MySqliteHelper.TABLE_TRANSACTION,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Transaction transaction = cursorToTransaction(cursor);
            transactions.add(transaction);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return transactions;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> transactions = getAllTransactionLogs();
        int size = transactions.size();

        if (size <= limit) {
            return transactions;
        }

        return transactions.subList(size - limit, size);
    }

    private Transaction cursorToTransaction(Cursor cursor) {
        Transaction transaction = new Transaction();
        //transaction.setDate(cursor.getString(0));
        transaction.setAccountNo(cursor.getString(1));
        transaction.setExpenseType(ExpenseType.valueOf(cursor.getString(2)));
        transaction.setAmount(cursor.getDouble(3));
        return transaction;
    }
}
