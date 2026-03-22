INSERT INTO accounts (email, password_hash, role, created_at, active)
VALUES ('admin1@example.com', '$2a$10$S9g02YbTQhA2/MT5BS8qzu38CoLlg.ZDwdWNr7E9Ew6x.o.r1ucta', 'ADMIN', NOW(), true)
ON CONFLICT (email) DO NOTHING;
