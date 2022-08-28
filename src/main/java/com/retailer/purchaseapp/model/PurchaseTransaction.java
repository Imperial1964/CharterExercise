package com.retailer.purchaseapp.model;

import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Date;

@Data
public class PurchaseTransaction {
    private final int customerId;
    @NonNull
    private Date timestamp;
    private int totalBeforeTaxInCents;//amount in cents, not dollars for accuracy in calculations.

//There would likely be other fields here like a list of individual lineitems like on a receipt, sales tax, total including tax, etc.
// when stored in a database, but those do not factor into the calculations this example.

    public PurchaseTransaction(int customerId) {
        this.customerId = customerId;
    }

    public String getYearMonth() {
        LocalDate localDate = timestamp.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return YearMonth.from(localDate).toString();
    }
}
