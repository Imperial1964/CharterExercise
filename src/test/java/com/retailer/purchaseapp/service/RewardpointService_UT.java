package com.retailer.purchaseapp.service;

import com.retailer.purchaseapp.model.PointsSummary;
import com.retailer.purchaseapp.model.PurchaseTransaction;
import com.retailer.purchaseapp.util.TestDataUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.YearMonth;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = RewardPointService.class)
public class RewardpointService_UT {
    @Autowired
    private RewardPointService rewardPointService;

    @Test
    public void testCalculate_none() {
        List<PurchaseTransaction> transactions = new ArrayList<>();

        List<PointsSummary> response = rewardPointService.buildRewardsSummaries(transactions);

        assertThat(response.size(), is(0));
    }

    @Test
    public void testCalculate_outOfBounds() {
        int purchaseTotalInCents = -1;
        List<PurchaseTransaction> transactions = TestDataUtil.createOneTransactionForOneCustomer(123, purchaseTotalInCents);

        List<PointsSummary> response = rewardPointService.buildRewardsSummaries(transactions);

        PointsSummary firstCustomer = response.get(0);
        assertThat(firstCustomer.getPointsByMonth().keySet().iterator().next(), is(YearMonth.now().toString()));
        assertThat(firstCustomer.getPointsByMonth().values().iterator().next(), is(0));
        assertThat(firstCustomer.getTotalPoints(), is(0));
    }

    @Test
    public void testCalculate_multipleCustomers() {
        int firstCustomerId = 123;
        int secondCustomerId = 456;
        int firstPurchaseTotal = 8500;
        int secondPurchaseTotal = 9000;
        List<PurchaseTransaction> transactions = TestDataUtil.createOneTransactionForOneCustomer(firstCustomerId, firstPurchaseTotal);
        transactions.addAll(TestDataUtil.createOneTransactionForOneCustomer(secondCustomerId, secondPurchaseTotal));

        List<PointsSummary> response = rewardPointService.buildRewardsSummaries(transactions);

        assertThat(response.size(), is(2));
        PointsSummary firstCustomer = response.get(0);
        PointsSummary secondCustomer = response.get(1);
        assertThat(firstCustomer.getPointsByMonth().values().iterator().next(), is(35));
        assertThat(firstCustomer.getTotalPoints(), is(35));
        assertThat(secondCustomer.getPointsByMonth().values().iterator().next(), is(40));
        assertThat(secondCustomer.getTotalPoints(), is(40));
    }

    @Test
    public void testCalculate_singleCustomer_multipleTransaction_differentMonth() {
        int customerId = 123;
        int firstPurchaseTotal = 5500;
        int secondPurchaseTotal = 6500;
        List<PurchaseTransaction> transactions = TestDataUtil.createOneTransactionForOneCustomer(customerId, firstPurchaseTotal);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        transactions.get(0).setTimestamp(cal.getTime());
        transactions.addAll(TestDataUtil.createOneTransactionForOneCustomer(customerId, secondPurchaseTotal));

        List<PointsSummary> response = rewardPointService.buildRewardsSummaries(transactions);

        PointsSummary firstCustomer = response.get(0);
        assertThat(firstCustomer.getPointsByMonth().size(), is(2));
        Iterator<Integer> iterator = firstCustomer.getPointsByMonth().values().iterator();
        assertThat(iterator.next(), is(5));
        assertThat(iterator.next(), is(15));
        assertThat(firstCustomer.getTotalPoints(), is(20));
    }

    @Test
    public void testCalculate_singleCustomer_multipleTransaction_sameMonth() {
        int customerId = 123;
        int firstPurchaseTotal = 12000;
        int secondPurchaseTotal = 7500;
        List<PurchaseTransaction> transactions = TestDataUtil.createOneTransactionForOneCustomer(customerId, firstPurchaseTotal);
        transactions.addAll(TestDataUtil.createOneTransactionForOneCustomer(customerId, secondPurchaseTotal));

        List<PointsSummary> response = rewardPointService.buildRewardsSummaries(transactions);

        PointsSummary firstCustomer = response.get(0);
        assertThat(firstCustomer.getPointsByMonth().keySet().iterator().next(), is(YearMonth.now().toString()));
        assertThat(firstCustomer.getPointsByMonth().values().iterator().next(), is(115));
        assertThat(firstCustomer.getTotalPoints(), is(115));
    }

    @Test
    public void testCalculate_singleCustomer_singleTransaction_overSecondThreshold() {
        int purchaseTotalInCents = 12000;
        List<PurchaseTransaction> transactions = TestDataUtil.createOneTransactionForOneCustomer(123, purchaseTotalInCents);

        List<PointsSummary> response = rewardPointService.buildRewardsSummaries(transactions);

        PointsSummary firstCustomer = response.get(0);
        assertThat(firstCustomer.getPointsByMonth().keySet().iterator().next(), is(YearMonth.now().toString()));
        assertThat(firstCustomer.getPointsByMonth().values().iterator().next(), is(90));
        assertThat(firstCustomer.getTotalPoints(), is(90));
    }

    @Test
    public void testCalculate_singleCustomer_singleTransaction_atSecondThreshold() {
        int purchaseTotalInCents = 10000;
        List<PurchaseTransaction> transactions = TestDataUtil.createOneTransactionForOneCustomer(123, purchaseTotalInCents);

        List<PointsSummary> response = rewardPointService.buildRewardsSummaries(transactions);

        PointsSummary firstCustomer = response.get(0);
        assertThat(firstCustomer.getPointsByMonth().keySet().iterator().next(), is(YearMonth.now().toString()));
        assertThat(firstCustomer.getPointsByMonth().values().iterator().next(), is(50));
        assertThat(firstCustomer.getTotalPoints(), is(50));
    }

    @Test
    public void testCalculate_singleCustomer_singleTransaction_betweenFirstAndSecondThreshold() {
        int purchaseTotalInCents = 7500;
        List<PurchaseTransaction> transactions = TestDataUtil.createOneTransactionForOneCustomer(123, purchaseTotalInCents);

        List<PointsSummary> response = rewardPointService.buildRewardsSummaries(transactions);

        PointsSummary firstCustomer = response.get(0);
        assertThat(firstCustomer.getPointsByMonth().keySet().iterator().next(), is(YearMonth.now().toString()));
        assertThat(firstCustomer.getPointsByMonth().values().iterator().next(), is(25));
        assertThat(firstCustomer.getTotalPoints(), is(25));
    }

    @Test
    public void testCalculate_singleCustomer_singleTransaction_atFirstThreshold() {
        int purchaseTotalInCents = 5000;
        List<PurchaseTransaction> transactions = TestDataUtil.createOneTransactionForOneCustomer(123, purchaseTotalInCents);

        List<PointsSummary> response = rewardPointService.buildRewardsSummaries(transactions);

        PointsSummary firstCustomer = response.get(0);
        assertThat(firstCustomer.getPointsByMonth().keySet().iterator().next(), is(YearMonth.now().toString()));
        assertThat(firstCustomer.getPointsByMonth().values().iterator().next(), is(0));
        assertThat(firstCustomer.getTotalPoints(), is(0));
    }
}
