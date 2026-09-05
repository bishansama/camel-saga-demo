# Camel Order Saga

A runnable Spring Boot example of an **orchestrated Apache Camel Saga**. It demonstrates a successful order and a shipping failure that compensates payment, inventory, and the order record.

## Run

```bash
mvn spring-boot:run
```

Place a successful order:

```bash
curl -i -X POST http://localhost:8080/orders \
  -H 'Content-Type: application/json' \
  -d '{"orderId":"order-100","productId":"SKU-42","quantity":1,"amount":49.99,"customerId":"customer-7"}'
```

Trigger a shipping failure (the `FAIL-` prefix is deliberate for the demo):

```bash
curl -i -X POST http://localhost:8080/orders \
  -H 'Content-Type: application/json' \
  -d '{"orderId":"order-101","productId":"FAIL-SKU","quantity":1,"amount":49.99,"customerId":"customer-7"}'
```

The failed order returns HTTP 409. Confirm that its final status is `FAILED`:

```bash
curl http://localhost:8080/orders/order-101
```

## What to replace in production

The three downstream services keep demo state in memory. The example also uses Camel's `InMemorySagaService`, which is intentionally local-only and loses saga state on a process crash. Replace it with an LRA-backed or otherwise durable coordinator; use idempotent HTTP/gRPC or messaging clients; add an outbox/inbox for messages; and use a real retry/DLQ policy appropriate to each downstream dependency.

Run the verification suite with `mvn test`.
