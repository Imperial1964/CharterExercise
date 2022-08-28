package com.retailer.purchaseapp.endpoint;

import com.retailer.purchaseapp.model.PointsSummary;
import com.retailer.purchaseapp.model.PurchaseTransaction;
import com.retailer.purchaseapp.service.RewardPointService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("rewards-summary")
public class RewardPointController {
    private final RewardPointService rewardPointService;

    public RewardPointController(RewardPointService rewardPointCalculator) {
        this.rewardPointService = rewardPointCalculator;
    }

    @GetMapping()
    public List<PointsSummary> getRewardSummaries(@RequestBody List<PurchaseTransaction> transactions) {
        return rewardPointService.buildRewardsSummaries(transactions);
    }
}