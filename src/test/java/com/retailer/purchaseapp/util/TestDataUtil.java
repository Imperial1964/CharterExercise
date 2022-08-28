package com.retailer.purchaseapp.util;

import com.retailer.purchaseapp.model.PurchaseTransaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestDataUtil {
    public static List<PurchaseTransaction> createOneTransactionForOneCustomer(int customerId, int purchaseTotal) {
        List<PurchaseTransaction> transactions = new ArrayList<>();
        PurchaseTransaction transaction = new PurchaseTransaction(customerId);
        transaction.setTimestamp(new Date());
        transaction.setTotalBeforeTaxInCents(purchaseTotal);
        transactions.add(transaction);
        return transactions;
    }
}
