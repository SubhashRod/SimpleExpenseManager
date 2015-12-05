package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.dataBaseAccess.DBHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * Created by Subhash on 12/4/2015.
 */
public class InDatabaseAccountDAO implements AccountDAO {

    private DBHelper dbHelper;

    public InDatabaseAccountDAO(DBHelper dbHelper){
        this.dbHelper = dbHelper;
    }

    @Override
    public List<String> getAccountNumbersList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<String> accNos = new ArrayList<String>();
        String selectQuery = "SELECT " + DBHelper.COLUMN_ID + " FROM " + DBHelper.TABLE_ACCOUNT;

        Log.e(DBHelper.LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                accNos.add(c.getString(c.getColumnIndex(DBHelper.COLUMN_ID)));
            } while (c.moveToNext());
        }

        db.close();

        return accNos;
    }

    @Override
    public List<Account> getAccountsList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Account> accounts = new ArrayList<Account>();
        String selectQuery = "SELECT  * FROM " + DBHelper.TABLE_ACCOUNT;

        Log.e(DBHelper.LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                Account account = new Account();
                account.setAccountNo(c.getString((c.getColumnIndex(DBHelper.COLUMN_ID))));
                account.setBankName((c.getString(c.getColumnIndex(DBHelper.COLUMN_NAME))));
                account.setAccountHolderName(c.getString(c.getColumnIndex(DBHelper.COLUMN_HOLDER)));
                account.setBalance(c.getDouble(c.getColumnIndex(DBHelper.COLUMN_BALANCE)));

                accounts.add(account);
            } while (c.moveToNext());
        }

        db.close();
        return accounts;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + DBHelper.TABLE_ACCOUNT + " WHERE "
                + DBHelper.COLUMN_ID + " = (?)";

        Log.e(DBHelper.LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, new String[]{accountNo});
        if (c != null) {
            c.moveToFirst();

            Account account = new Account();
            account.setAccountNo(c.getString((c.getColumnIndex(DBHelper.COLUMN_ID))));
            account.setBankName((c.getString(c.getColumnIndex(DBHelper.COLUMN_NAME))));
            account.setAccountHolderName(c.getString(c.getColumnIndex(DBHelper.COLUMN_HOLDER)));
            account.setBalance(c.getDouble(c.getColumnIndex(DBHelper.COLUMN_BALANCE)));
            db.close();

            return account;
        }
        db.close();
        String msg = "Account " + accountNo + " is invalid.";
        throw new InvalidAccountException(msg);
    }

    @Override
    public void addAccount(Account account) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_ID, account.getAccountNo());
        values.put(DBHelper.COLUMN_NAME, account.getBankName());
        values.put(DBHelper.COLUMN_HOLDER, account.getAccountHolderName());
        values.put(DBHelper.COLUMN_BALANCE, account.getBalance());

        db.insert(DBHelper.TABLE_ACCOUNT, null, values);
        db.close();

    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int success = db.delete(DBHelper.TABLE_ACCOUNT, DBHelper.COLUMN_ID + " = ?",
                new String[]{accountNo});
        db.close();
        if(success == 0){
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {

        Account account = getAccount(accountNo);

        if (account == null) {
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        switch (expenseType) {
            case EXPENSE:
                double balance1 = account.getBalance() - amount;
                ContentValues contentValues1 = new ContentValues();
                contentValues1.put(DBHelper.COLUMN_BALANCE, balance1);

                db.update(DBHelper.TABLE_ACCOUNT, contentValues1, DBHelper.COLUMN_ID + " = ?", new String[]{accountNo});
                db.close();
                break;

            case INCOME:
                double balance2 = account.getBalance() + amount;
                ContentValues contentValues2 = new ContentValues();
                contentValues2.put(DBHelper.COLUMN_BALANCE, balance2);

                db.update(DBHelper.TABLE_ACCOUNT, contentValues2, DBHelper.COLUMN_ID + " = ?", new String[]{accountNo});
                db.close();
                break;
        }



    }
}
