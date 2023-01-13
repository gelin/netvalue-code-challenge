# Senior Spring Boot Coding Challenge for NetValue

This is a code sample for a coding challenge for NetValue Ltd.
To a senior Spring Boot developer position.

This is Spring Boot application with REST API plus some documented ideas.

## REST API

### Public endpoints

#### GET /version

Returns application version and database schema version.

### Admin endpoints

#### GET /charging-sessions

Returns the list of charging sessions.

Query params: 
* `from` — date to select sessions from (inclusive)
* `till` — date to select sessions till (inclusive)

Result: list of JSON objects

#### POST /charge-points/${charge point id}/connectors/

Adds a new connector to an existing charge point.

Body: empty

Result: JSON object of the new created charge point.

### Customer endpoints

#### POST /charging-sessions/

Starts a charging session.

TBD

#### POST /charging-sessions/${charging session id}/end

Ends the charging session.

TBD

## Entity model

TBD

## Date converter

TBD


