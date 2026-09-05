package com.example.ordersaga.api;

import com.example.ordersaga.domain.*;
import org.apache.camel.ProducerTemplate;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final ProducerTemplate producer;
    private final OrderRepository orders;
    public OrderController(ProducerTemplate producer, OrderRepository orders) { this.producer = producer; this.orders = orders; }

    @PostMapping
    public ResponseEntity<?> place(@RequestBody OrderRequest request) {
        try {
            producer.requestBody("direct:placeOrder", request);
            return ResponseEntity.status(HttpStatus.CREATED).body(orders.findById(request.orderId()).orElseThrow());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(request.orderId(), "Order failed and compensations were requested", e.getMessage()));
        }
    }
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderEntity> get(@PathVariable String orderId) { return ResponseEntity.of(orders.findById(orderId)); }
    public record ErrorResponse(String orderId, String message, String cause) { }
}
