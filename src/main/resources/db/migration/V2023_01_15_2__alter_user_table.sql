-- assuming password authentication is performed somewhere else and we don't store passwords here
ALTER TABLE IF EXISTS "user"
DROP COLUMN IF EXISTS password
;
-- but we may need to verify user roles
ALTER TABLE IF EXISTS "user"
ADD COLUMN IF NOT EXISTS
roles VARCHAR ARRAY
;
