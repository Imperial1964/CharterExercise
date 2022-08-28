package com.retailer.purchaseapp.endpoint;

import com.retailer.purchaseapp.model.PointsSummary;
import com.retailer.purchaseapp.model.PurchaseTransaction;
import com.retailer.purchaseapp.service.RewardPointService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RewardPointController_UT {
    public static final int CUSTOMER_ID = 123;
    @Mock
    private RewardPointService rewardPointService;
    @InjectMocks
    private RewardPointController rewardPointController;

    @Test
    public void testGetRewardSummaries() {
        List<PurchaseTransaction> transactions = new ArrayList<>();
        PurchaseTransaction transaction = new PurchaseTransaction(CUSTOMER_ID);
        transactions.add(transaction);

        ArrayList<PointsSummary> summaries = new ArrayList<>();
        summaries.add(new PointsSummary(CUSTOMER_ID));
        when(rewardPointService.buildRewardsSummaries(transactions)).thenReturn(summaries);

        List<PointsSummary> response = rewardPointController.getRewardSummaries(transactions);

        assertThat(response.get(0).getCustomerId(), is(CUSTOMER_ID));
    }
}
