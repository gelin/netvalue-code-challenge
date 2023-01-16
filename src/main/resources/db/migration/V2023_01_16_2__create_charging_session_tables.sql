CREATE TABLE IF NOT EXISTS charging_session (
    id VARCHAR PRIMARY KEY,
    connector_id VARCHAR REFERENCES connector (id) ON UPDATE CASCADE ON DELETE SET NULL,
    rfid_tag_id VARCHAR REFERENCES rfid_tag (id) ON UPDATE CASCADE ON DELETE SET NULL
)
;
CREATE TABLE IF NOT EXISTS charging_session_event (
    id VARCHAR PRIMARY KEY,
    session_id VARCHAR NOT NULL REFERENCES charging_session (id) ON UPDATE CASCADE ON DELETE CASCADE,
    time TIMESTAMP WITH TIME ZONE NOT NULL,
    type VARCHAR NOT NULL,
    meter_value INTEGER,
    message VARCHAR
)
;
CREATE INDEX IF NOT EXISTS charging_session_event_time
ON charging_session_event (time)
;
