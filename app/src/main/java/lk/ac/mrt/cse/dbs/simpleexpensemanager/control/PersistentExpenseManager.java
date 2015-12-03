package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import java.sql.SQLException;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InDatabaseAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InDatabaseTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InMemoryAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InMemoryTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.ui.MainActivity;

/**
 * Created by Subhash on 12/3/2015.
 */
public class PersistentExpenseManager extends ExpenseManager {

    private Context context;

    public PersistentExpenseManager(Context context) {
        this.context = context;
        try {
            setup();
        } catch (ExpenseManagerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setup() throws ExpenseManagerException {

        TransactionDAO inDatabaseTransactionDAO = new InDatabaseTransactionDAO(context);
        setTransactionsDAO(inDatabaseTransactionDAO);


        AccountDAO inDatabaseAccountDAO = new InDatabaseAccountDAO(context);
        setAccountsDAO(inDatabaseAccountDAO);

        // dummy data
        Account dummyAcct1 = new Account("12345A", "Yoda Bank", "Anakin Skywalker", 10000.0);
        Account dummyAcct2 = new Account("78945Z", "Clone BC", "Obi-Wan Kenobi", 80000.0);
        getAccountsDAO().addAccount(dummyAcct1);
        getAccountsDAO().addAccount(dummyAcct2);

    }
}
