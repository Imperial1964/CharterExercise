package com.retailer.purchaseapp.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class PointsSummary {
    private final int customerId;
    private Map<String, Integer> pointsByMonth = new HashMap<>();
    private int totalPoints = 0;
}
