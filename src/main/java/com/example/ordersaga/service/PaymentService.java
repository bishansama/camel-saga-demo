package com.example.ordersaga.service;

import com.example.ordersaga.domain.*;
import org.springframework.stereotype.Service;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/** Replace with an idempotent payment-provider integration in production. */
@Service
public class PaymentService {
    private final Set<String> charges = ConcurrentHashMap.newKeySet();
    private final OrderService orders;
    public PaymentService(OrderService orders) { this.orders = orders; }
    public void charge(OrderRequest order) { charges.add(order.orderId()); orders.updateStatus(order, OrderStatus.PAYMENT_SUCCESS); }
    public void refund(OrderRequest order) { charges.remove(order.orderId()); }
    public boolean isCharged(String orderId) { return charges.contains(orderId); }
}
