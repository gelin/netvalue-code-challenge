MERGE INTO "user" (id, name, roles)
VALUES ('test_customer', 'customer', ARRAY['CUSTOMER'])
;
MERGE INTO charge_point (id, name, serial_number, owner_id)
VALUES ('test_charge_point', 'Test Charge Point', 'SOME SERIAL', 'test_customer')
;
MERGE INTO connector (id, charge_point_id, number)
VALUES ('test_connector_1', 'test_charge_point', '1')
;
MERGE INTO vehicle (id, name, registration_plate)
VALUES ( 'test_vehicle', 'Test Vehicle', 'TEST' )
;
MERGE INTO rfid_tag (id, name, number, owner_id, vehicle_id)
VALUES ( 'test_tag', 'Test Tag', 'NUMBER', 'test_customer', 'test_vehicle' )
;
