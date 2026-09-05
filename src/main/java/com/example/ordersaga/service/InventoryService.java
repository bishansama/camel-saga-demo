package com.example.ordersaga.service;

import com.example.ordersaga.domain.*;
import org.springframework.stereotype.Service;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/** Replace this stateful demo with HTTP/gRPC calls to the inventory service in production. */
@Service
public class InventoryService {
    private final Set<String> reservations = ConcurrentHashMap.newKeySet();
    private final OrderService orders;
    public InventoryService(OrderService orders) { this.orders = orders; }
    public void reserve(OrderRequest order) { reservations.add(order.orderId()); orders.updateStatus(order, OrderStatus.INVENTORY_RESERVED); }
    public void release(OrderRequest order) { reservations.remove(order.orderId()); }
    public boolean isReserved(String orderId) { return reservations.contains(orderId); }
}
