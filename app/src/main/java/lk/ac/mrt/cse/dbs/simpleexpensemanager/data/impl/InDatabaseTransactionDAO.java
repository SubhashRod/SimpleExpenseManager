package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.dataBaseAccess.DBHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by Subhash on 12/4/2015.
 */
public class InDatabaseTransactionDAO implements TransactionDAO {

    private DBHelper dbHelper;

    public InDatabaseTransactionDAO(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_ACCNO, accountNo);
        values.put(DBHelper.COLUMN_DATE, getDateTime(date));
        values.put(DBHelper.COLUMN_EXPETYPE, expenseType.toString());
        values.put(DBHelper.COLUMN_AMOUNT, amount);

        db.insert(DBHelper.TABLE_TRANSACTION, null, values);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        List<Transaction> transactions = new ArrayList<Transaction>();
        String selectQuery = "SELECT  * FROM " + DBHelper.TABLE_TRANSACTION;

        Log.e(DBHelper.LOG, selectQuery);

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                Transaction transaction = new Transaction();

                Date date = null;
                try {
                    date = getDateTime(c.getString(c.getColumnIndex(DBHelper.COLUMN_DATE)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                transaction.setAccountNo(c.getString((c.getColumnIndex(DBHelper.COLUMN_ACCNO))));
                transaction.setDate(date);
                transaction.setExpenseType(ExpenseType.valueOf(c.getString(c.getColumnIndex(DBHelper.COLUMN_EXPETYPE))));
                transaction.setAmount(c.getDouble(c.getColumnIndex(DBHelper.COLUMN_AMOUNT)));

                // adding to todo list
                transactions.add(transaction);
            } while (c.moveToNext());
        }

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

    private String getDateTime(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(date);
    }

    private Date getDateTime(String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());


        Date date1 = format.parse(date);
        return date1;

    }

}
