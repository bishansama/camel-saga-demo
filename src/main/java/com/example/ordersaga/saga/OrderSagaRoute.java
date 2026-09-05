package com.example.ordersaga.saga;

import com.example.ordersaga.domain.OrderStatus;
import com.example.ordersaga.service.*;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.saga.SagaPropagation;
import org.springframework.stereotype.Component;

@Component
public class OrderSagaRoute extends RouteBuilder {
    @Override
    public void configure() {
        errorHandler(defaultErrorHandler().maximumRedeliveries(2).redeliveryDelay(100));

        from("direct:placeOrder").routeId("order-saga")
            .saga().propagation(SagaPropagation.REQUIRED)
            .to("direct:saveOrder")
            .to("direct:reserveInventory")
            .to("direct:chargePayment")
            .to("direct:createShipment")
            .to("direct:completeOrder");

        from("direct:saveOrder")
            .saga().propagation(SagaPropagation.MANDATORY).option("order", body()).compensation("direct:cancelOrder")
            .bean(OrderService.class, "create");

        from("direct:reserveInventory")
            .saga().propagation(SagaPropagation.MANDATORY).option("order", body()).compensation("direct:releaseInventory")
            .bean(InventoryService.class, "reserve");

        from("direct:chargePayment")
            .saga().propagation(SagaPropagation.MANDATORY).option("order", body()).compensation("direct:refundPayment")
            .bean(PaymentService.class, "charge");

        from("direct:createShipment")
            .saga().propagation(SagaPropagation.MANDATORY).option("order", body()).compensation("direct:cancelShipment")
            .bean(ShippingService.class, "createShipment");

        from("direct:completeOrder").bean(OrderService.class, "complete");
        from("direct:releaseInventory").setBody(header("order")).bean(InventoryService.class, "release");
        from("direct:refundPayment").setBody(header("order")).bean(PaymentService.class, "refund");
        from("direct:cancelShipment").setBody(header("order")).bean(ShippingService.class, "cancelShipment");
        from("direct:cancelOrder").setBody(header("order")).bean(OrderService.class, "cancel");
    }
}
