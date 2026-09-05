package com.example.ordersaga.service;

import com.example.ordersaga.domain.*;
import org.springframework.stereotype.Service;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ShippingService {
    private final Set<String> shipments = ConcurrentHashMap.newKeySet();
    private final OrderService orders;
    public ShippingService(OrderService orders) { this.orders = orders; }
    public void createShipment(OrderRequest order) {
        if (order.productId().startsWith("FAIL-")) throw new IllegalStateException("Shipping unavailable");
        shipments.add(order.orderId()); orders.updateStatus(order, OrderStatus.SHIPPED);
    }
    public void cancelShipment(OrderRequest order) { shipments.remove(order.orderId()); }
    public boolean isShipped(String orderId) { return shipments.contains(orderId); }
}
