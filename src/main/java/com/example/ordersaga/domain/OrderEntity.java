package com.example.ordersaga.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity @Table(name = "orders")
public class OrderEntity {
    @Id private String orderId;
    private String customerId;
    private String productId;
    private Integer quantity;
    private BigDecimal amount;
    @Enumerated(EnumType.STRING) private OrderStatus status;
    protected OrderEntity() { }
    public OrderEntity(OrderRequest r, OrderStatus status) { orderId=r.orderId(); customerId=r.customerId(); productId=r.productId(); quantity=r.quantity(); amount=r.amount(); this.status=status; }
    public String getOrderId() { return orderId; }
    public String getCustomerId() { return customerId; }
    public String getProductId() { return productId; }
    public Integer getQuantity() { return quantity; }
    public BigDecimal getAmount() { return amount; }
    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }
}
