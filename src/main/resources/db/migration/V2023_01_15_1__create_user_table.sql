CREATE TABLE IF NOT EXISTS "user" (
    id VARCHAR PRIMARY KEY,
    name VARCHAR NOT NULL,
    password VARCHAR,
    UNIQUE (name)
)
;
