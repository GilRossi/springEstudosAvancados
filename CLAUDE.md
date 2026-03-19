# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Spring Boot 3.5.3 study project (Java 21) exploring SOLID principles, design patterns (Strategy, Observer, Template Method, Builder, Factory), dependency injection, async multithreading, and Apache Kafka event-driven architecture. Domain: order management (Pedidos).

## Build & Run Commands

```bash
# Build (skip tests)
./mvnw clean package -DskipTests

# Run all tests
./mvnw test

# Run single test class
./mvnw test -Dtest=PedidoServiceImplTest

# Run single test method
./mvnw test -Dtest=PedidoServiceImplTest#criarPedido_deveSalvarERetornarResponse

# Run application (requires Kafka via docker-compose)
docker-compose up -d && ./mvnw spring-boot:run
```

## Architecture

**Request flow:** Controller → Service (interface) → Repository (JPA) + KafkaProducer → KafkaConsumer → Service (status update) → Event Publisher → Observers

### Design Patterns Applied

- **Strategy:** `ProcessamentoStrategy` interface with `AltoValor`/`BaixoValor` implementations, selected at runtime via `ProcessamentoStrategyFactory` based on order value (threshold: R$1000)
- **Observer:** `PedidoStatusAlteradoEvent` published via Spring `ApplicationEventPublisher`; `LoggingPedidoObserver` and `NotificacaoPedidoObserver` react via `@EventListener`
- **Template Method:** `ProcessamentoService` (abstract) defines algorithm skeleton (`validar` → `executarProcessamento` → `finalizarProcessamento`); `ProcessamentoPadraoService` implements the processing step using the Strategy pattern
- **Builder:** Lombok `@Builder` on `Pedido`, `PedidoResponse`, `ErroResponse`
- **Factory:** `ProcessamentoStrategyFactory` selects correct strategy based on order characteristics

### Key Layers

- **DTOs split into request/response** — never return JPA entities from controllers
- **Mapper** (`PedidoMapper`) — centralizes Entity↔DTO conversion
- **Exception handling** — `GlobalExceptionHandler` with `@RestControllerAdvice` maps exceptions to standardized `ErroResponse`
- **Status lifecycle** — `StatusPedido` enum with `podeTransicionarPara()` enforcing valid transitions: PENDENTE → PROCESSANDO → CONCLUIDO/CANCELADO

### Test Profile

Tests use `@ActiveProfiles("test")` which excludes Kafka auto-configuration. Kafka services are mocked with `@MockitoBean` in integration tests.

## Infrastructure

Kafka runs via Docker Compose (Confluent images): Zookeeper on port 2181, Kafka broker on port 29092 (host) with 3 partitions.

## Language

Codebase uses Portuguese for domain naming. Maintain this convention.
