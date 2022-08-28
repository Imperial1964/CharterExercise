package com.retailer.purchaseapp.service;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.retailer.purchaseapp.model.PointsSummary;
import com.retailer.purchaseapp.model.PurchaseTransaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
public class RewardPointService {
    public static final int CENTS_IN_DOLLAR = 100;

    //alternately, if the business could want more thresholds dynamically, a different structure could be used.
    private final int firstThreshold;
    private final int secondThreshold;
    private final int firstThresholdMultiplier;
    private final int secondThresholdMultiplier;

    public RewardPointService(@Value("${purchaseapp.rewards.firstThreshold:50}") int firstThreshold,
                              @Value("${purchaseapp.rewards.secondThreshold:100}") int secondThreshold,
                              @Value("${purchaseapp.rewards.firstThresholdMultiplier:1}") int firstThresholdMultiplier,
                              @Value("${purchaseapp.rewards.secondThresholdMultiplier:2}") int secondThresholdMultiplier) {
        this.firstThreshold = firstThreshold;
        this.secondThreshold = secondThreshold;
        this.firstThresholdMultiplier = firstThresholdMultiplier;
        this.secondThresholdMultiplier = secondThresholdMultiplier;
    }

    public List<PointsSummary> buildRewardsSummaries(List<PurchaseTransaction> transactions) {
        List<PointsSummary> list = new ArrayList<>();

        Multimap<Integer, PurchaseTransaction> transactionsByCustomerId = Multimaps.index(transactions, PurchaseTransaction::getCustomerId);
        for (Integer key : transactionsByCustomerId.keySet()) {
            list.add(buildRewardsSummaryForSingleCustomer(key, transactionsByCustomerId.get(key)));
        }
        return list;
    }

    private PointsSummary buildRewardsSummaryForSingleCustomer(Integer customer, Collection<PurchaseTransaction> transactions) {
        Multimap<String, PurchaseTransaction> purchasesByMonth = Multimaps.index(transactions, PurchaseTransaction::getYearMonth);
        PointsSummary pointsSummary = new PointsSummary(customer);

        for (String key : purchasesByMonth.keySet()) {
            Collection<PurchaseTransaction> monthlyTransactions = purchasesByMonth.get(key);
            int monthlySum = monthlyTransactions.stream().mapToInt(p -> calculate(p.getTotalBeforeTaxInCents())).sum();
            pointsSummary.getPointsByMonth().put(key, monthlySum);
            pointsSummary.setTotalPoints(monthlySum + pointsSummary.getTotalPoints());
        }

        return pointsSummary;
    }

    private Integer calculate(int purchaseAmountInCents) {
        int points = 0;
        int purchaseAmountInDollars = purchaseAmountInCents / CENTS_IN_DOLLAR;

        if (purchaseAmountInDollars > firstThreshold) {
            if (purchaseAmountInDollars < secondThreshold) {
                points += (purchaseAmountInDollars - firstThreshold) * firstThresholdMultiplier;
            } else {
                points += (secondThreshold - firstThreshold) * firstThresholdMultiplier;
            }
        }
        if (purchaseAmountInDollars > secondThreshold) {
            points += (purchaseAmountInDollars - secondThreshold) * secondThresholdMultiplier;
        }

        return points;
    }
}
