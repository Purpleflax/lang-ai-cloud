# Lang AI Cloud Backend

Spring Boot backend service that manages controlled access to a locally hosted GPU for the language training AI frontend. This project exposes a REST API for monitoring GPU availability, requesting GPU sessions, and releasing them when clients are done.

## Getting Started

### Requirements

* Java 17+
* Maven 3.9+

### Run the application

```bash
mvn spring-boot:run
```

The API will be available at `http://localhost:8080`.

### Useful endpoints

* `GET /api/gpu/status` &mdash; health-style overview of current GPU availability and active sessions.
* `GET /api/gpu/sessions` &mdash; list all active GPU sessions managed by the service.
* `POST /api/gpu/sessions` &mdash; request a new GPU session (requires JSON payload with `requestedBy` and optional `durationMinutes`).
* `DELETE /api/gpu/sessions/{sessionId}` &mdash; release an existing GPU session.

### Configuration

You can configure concurrency and default session duration in `application.properties`:

```properties
gpu.max-concurrent-sessions=1
gpu.default-session-minutes=30
```

These properties help ensure the GPU cannot be over-subscribed, and allow the backend to enforce sensible defaults when the frontend omits a duration.

## Testing

```bash
mvn test
```
