DELETE FROM "user" WHERE id = 'default_admin'
;
INSERT INTO "user" (id, name, password)
VALUES ('default_admin', 'admin', '$2a$12$GnOwYNKEh6lo2DOtRPNiwO3ieeKr7Y1ldI5SaJErrWyPDmvHX6e36')
;
