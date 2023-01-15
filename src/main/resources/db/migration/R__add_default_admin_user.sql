DELETE FROM "user" WHERE id = 'default_admin'
;
INSERT INTO "user" (id, name, roles)
VALUES ('default_admin', 'admin', ARRAY['ADMIN'])
;
