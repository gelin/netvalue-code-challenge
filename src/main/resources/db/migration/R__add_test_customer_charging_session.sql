MERGE INTO charging_session (id, connector_id, rfid_tag_id)
VALUES ('test_session_1', 'test_connector_1', 'test_tag')
;
MERGE INTO charging_session_event (id, session_id, time, type, meter_value)
VALUES ( 'test_session_1_1', 'test_session_1', TIMESTAMP WITH TIME ZONE '2023-01-16 15:42:00Z', 'START', 1234 ),
       ( 'test_session_1_2', 'test_session_1', TIMESTAMP WITH TIME ZONE '2023-01-16 16:02:00Z', 'END', 2345 )
;
MERGE INTO charging_session (id, connector_id, rfid_tag_id)
    VALUES ('test_session_2', 'test_connector_1', 'test_tag')
;
MERGE INTO charging_session_event (id, session_id, time, type, meter_value)
    VALUES ( 'test_session_2_1', 'test_session_2', TIMESTAMP WITH TIME ZONE '2023-01-17 16:16:00Z', 'START', 2345 ),
           ( 'test_session_2_2', 'test_session_2', TIMESTAMP WITH TIME ZONE '2023-01-17 16:30:00Z', 'END', 3456 )
;
