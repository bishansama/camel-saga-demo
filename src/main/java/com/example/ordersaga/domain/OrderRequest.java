package com.example.ordersaga.domain;

import java.math.BigDecimal;

public record OrderRequest(String orderId, String productId, Integer quantity, BigDecimal amount, String customerId) { }
