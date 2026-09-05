package com.example.ordersaga;

import com.example.ordersaga.domain.*;
import com.example.ordersaga.service.*;
import org.apache.camel.ProducerTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class OrderSagaIntegrationTest {
    @Autowired ProducerTemplate producer;
    @Autowired OrderRepository orders;
    @Autowired InventoryService inventory;
    @Autowired PaymentService payment;
    @Autowired ShippingService shipping;

    @Test void completes_when_every_step_succeeds() {
        var order = new OrderRequest("ok-1", "SKU-1", 1, new BigDecimal("19.99"), "customer-1");
        producer.requestBody("direct:placeOrder", order);
        assertThat(orders.findById("ok-1")).get().extracting(OrderEntity::getStatus).isEqualTo(OrderStatus.COMPLETED);
        assertThat(inventory.isReserved("ok-1")).isTrue();
        assertThat(payment.isCharged("ok-1")).isTrue();
        assertThat(shipping.isShipped("ok-1")).isTrue();
    }

    @Test void compensates_payment_and_inventory_when_shipping_fails() {
        var order = new OrderRequest("fail-1", "FAIL-SKU", 1, new BigDecimal("19.99"), "customer-1");
        assertThatThrownBy(() -> producer.requestBody("direct:placeOrder", order)).isNotNull();
        assertThat(orders.findById("fail-1")).get().extracting(OrderEntity::getStatus).isEqualTo(OrderStatus.FAILED);
        assertThat(inventory.isReserved("fail-1")).isFalse();
        assertThat(payment.isCharged("fail-1")).isFalse();
        assertThat(shipping.isShipped("fail-1")).isFalse();
    }
}
