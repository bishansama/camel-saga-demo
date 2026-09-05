package com.example.ordersaga.service;

import com.example.ordersaga.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final OrderRepository orders;
    public OrderService(OrderRepository orders) { this.orders = orders; }
    @Transactional public void create(OrderRequest request) { orders.save(new OrderEntity(request, OrderStatus.CREATED)); }
    @Transactional public void updateStatus(OrderRequest request, OrderStatus status) { orders.findById(request.orderId()).ifPresent(o -> o.setStatus(status)); }
    @Transactional public void complete(OrderRequest request) { updateStatus(request, OrderStatus.COMPLETED); }
    @Transactional public void fail(OrderRequest request) { updateStatus(request, OrderStatus.FAILED); }
    @Transactional public void cancel(OrderRequest request) { fail(request); }
}
