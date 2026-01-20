# kafka-pattern
This repository serves as a practical guide and portfolio for mastering Apache Kafka. It covers essential patterns required to build resilient, scalable, and data-consistent distributed systems.

## 📋 Table of Contents
1. [Resiliency & Error Handling](#1-resiliency--error-handling)
2. [Data Integrity & Consistency](#2-data-integrity--consistency)
3. [Distributed Architecture Patterns](#3-distributed-architecture-patterns)
4. [Stream Processing & Optimization](#4-stream-processing--optimization)

---

## 1. Resiliency & Error Handling

### Dead Letter Topic (DLT)
* **Concept:** Move "poison pill" messages (unparseable or invalid data) to a separate topic instead of blocking the consumer partition.
* **Implementation Goal:** Create a consumer that catches `DeserializationException` and routes the raw payload to a `.DLT` topic.

### Retry Topics (Non-Blocking Retries)
* **Concept:** For transient failures (e.g., a 3rd party API timeout), move the message to a series of back-off topics (e.g., `retry-5m`, `retry-15m`) to allow other messages to be processed.
* **Implementation Goal:** Use Spring Kafka's `@RetryableTopic` or manual routing logic to implement exponential back-off.



---

## 2. Data Integrity & Consistency

### Idempotent Producer
* **Concept:** Ensures that messages are written exactly once to the broker, even if the producer retries due to network issues.
* **Implementation Goal:** Configure a Producer with `enable.idempotence=true` and verify the `sequence_number` behavior in Kafka logs.

### Idempotent Consumer (Message Deduplication)
* **Concept:** Designing the business logic (or using a DB unique constraint) so that processing the same message multiple times has no side effects.
* **Implementation Goal:** Build a WebFlux service that checks a Redis cache for an `eventId` before persisting data to a database.

### Transactional Outbox
* **Concept:** Atomically update a database and publish a message to Kafka. This prevents "dual-write" failures where the DB updates but the message is never sent.
* **Implementation Goal:** Use a relational DB table as an "Outbox" and a separate process (or Debezium) to stream those records to Kafka.

---

## 3. Distributed Architecture Patterns

### Saga Pattern (Choreography)
* **Concept:** Manage distributed transactions across microservices. Each service performs a local transaction and publishes an event to trigger the next step or a rollback (compensation).
* **Implementation Goal:** Simulate an Order-Payment-Inventory flow where a failure in "Inventory" triggers a "Refund Payment" event.



### CQRS (Command Query Responsibility Segregation)
* **Concept:** Separate the "write" side (Kafka events) from the "read" side (a projection in Elasticsearch or Redis).
* **Implementation Goal:** A "Command" service writes events to Kafka; a "Query" service consumes those events to update a materialized view in MongoDB.

### Event Sourcing
* **Concept:** Store the state of a system as a sequence of immutable events rather than just the current snapshot.
* **Implementation Goal:** Reconstruct a user's current "Account Balance" by replaying all deposit/withdrawal events from a Kafka topic.

---

## 4. Stream Processing & Optimization

### Claim Check Pattern
* **Concept:** Handle large payloads (e.g., >1MB) by storing the data in external storage (S3/Azure Blob) and sending only the reference/URL via Kafka.
* **Implementation Goal:** Build a producer that uploads a dummy file to S3 and puts the S3 URI into a Kafka message.

### Windowing & Aggregation
* **Concept:** Perform calculations over a moving window of time (e.g., "Average sensor temperature over the last 60 seconds").
* **Implementation Goal:** Use **Kafka Streams** or **Project Reactor Kafka** to calculate real-time rolling averages.



### Change Data Capture (CDC)
* **Concept:** Automatically stream database row-level changes into Kafka topics.
* **Implementation Goal:** Set up a Debezium connector for MySQL/PostgreSQL and observe events flowing into Kafka upon every `INSERT` or `UPDATE`.

---

## 🛠 Tech Stack Used
* **Language:** Java 17/21
* **Framework:** Spring Boot & Spring WebFlux
* **Library:** Project Reactor (Reactor Kafka)
* **Broker:** Apache Kafka (Confluent or Redpanda)
* **Database:** PostgreSQL / Redis (for idempotency)
