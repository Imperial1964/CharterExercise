package com.retailer.purchaseapp.endpoint;

import com.retailer.purchaseapp.model.PointsSummary;
import com.retailer.purchaseapp.model.PurchaseTransaction;
import com.retailer.purchaseapp.service.RewardPointService;
import com.retailer.purchaseapp.util.TestDataUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.YearMonth;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {RewardPointController.class, RewardPointService.class})
public class RewardPointController_IT {
    @Autowired
    private RewardPointController rewardPointController;

    @Test
    public void testCalculate_singleCustomer_singleTransaction_overSecondThreshold() {
        int customerId = 123;
        int purchaseTotal = 12000;
        List<PurchaseTransaction> transactions = TestDataUtil.createOneTransactionForOneCustomer(customerId, purchaseTotal);

        List<PointsSummary> response = rewardPointController.getRewardSummaries(transactions);

        PointsSummary firstCustomer = response.get(0);
        assertThat(firstCustomer.getPointsByMonth().keySet().iterator().next(), is(YearMonth.now().toString()));
        assertThat(firstCustomer.getPointsByMonth().values().iterator().next(), is(90));
        assertThat(firstCustomer.getTotalPoints(), is(90));
    }
}
