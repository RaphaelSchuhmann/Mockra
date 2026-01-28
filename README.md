# Mockra

Mockra is a config based API written in Java with Spring-Boot (4.0.1), which can be used to mock real APIs for Unit tests in the frontend. Endpoints are defined in a `config.yaml` file.


## Installation

Download .jar file from the releases in this repo.

Create a `config.yaml` file in any directory and execute Mockra using:
```bash
java -jar Mockra-X.X.X.jar
```

It will automatically get the config file in your current working directory.
    
## Features

- Configurable endpoints – Quickly set up mock API endpoints for testing.
- Dynamic responses – Return custom data, errors, or delays per endpoint.
- Error simulation – Test client behavior with simulated error responses.
- Hot reload support – Update endpoint configurations without restarting the server.
- Lightweight and fast – Minimal setup, runs locally for development or testing.
## Configuration

Mockra is configured entirely via a YAML file. The configuration defines the server and the mock endpoints, including request and response variants. Anything not documented here is not supported in V1.

### General Rules
- YAML is the single source of truth
- Confgiuration is loaded at startup and fully validated
- Invalid configuration causees startup failure.
- No defaults are assumed unless explicitly documented
- Invalid configuration on hot reload will not be used, fallback to latest valid configuration

### Root Structure
```yaml
server:
    port: 8089
endpoints:
    - ...
```
- `server` - Server Confgiuration (port, etc.)
- `endpoints` - List of mock endpoints

### Server Configuration
`port` (integer, required) - TCP port Mockra listens on.
- Missing or invalid port -> startup failure.

### Endpoint Configuration
Each endpoint defines a unique API route:
- `id` (string, required) - Unique endpoint identifier
- `method` (string, required) - HTTP method (`GET`, `POST`, etc.)
- `path` (string, required) - Exact request path (no wildcards, no path variables)
- `responses` (list, required) - Response variants for this endpoint

#### Rules
- `id` must be unique across all endpoitns
- `path` combination must be unique
- Path matching is exact(`/api/users` ≠ `/api/users/`).

### Request body
- Optional per endpiotn if the method supports a body.
- Structure is defined in YAML (`STRING`, `NUMBER`, `BOOLEAN`, `OBJECT`, `ARRAY`)

### Response variants
- Selected via 'X-Mockra-Variant' request header.
- Fields per variant:
  - `variant` (string, required) - Header match name
  - `status` (integer, required) - HTTP status code
  - `body` (object, optional) - JSON response body
  - `delayMs` (integer, optional) - Artificial response delay in milliseconds

#### Rules:
- Header value must match a variant exactly (case-sensitive).
- Missing or invalid variant -> `400 Bad Request`.
- Response are always JSON.
- `delayMs` blocks delivery for the specified duration.

### Generated Errors
Mockra always returns JSON for errors:
- Missing header -> 400
- Missing variant -> 400
- Unknown route -> 404

### Example:
```yaml
server:
  port: 8089
endpoints:
  - id: login
    method: POST
    path: /api/login
    request:
      username: STRING
      password: STRING
    responses:
      - variant: success
        status: 200
        body:
          token: "abc123"
      - variant: invalid-credentials
        status: 401
        body:
          error: "Invalid credentials"
        delayMs: 100

```
## Authors

- [@RaphaelSchuhmann](https://www.github.com/RaphaelSchuhmann)


## License

[MIT](https://choosealicense.com/licenses/mit/)

