INSERT INTO accounts (email, password_hash, role, created_at, active)
VALUES
('adminTest@example.com', '$2a$10$S9g02YbTQhA2/MT5BS8qzu38CoLlg.ZDwdWNr7E9Ew6x.o.r1ucta', 'ADMIN', NOW(), true),
('userTest@example.com', '$2a$10$UQgp.YhhKVf98lnBz.9oVOKMm411DhPUI9NU6Iw0HObd0pK0RGRii', 'USER', NOW(), true),
('guestTest@example.com', '$2a$10$PdHJN8736XCk/I/lSWuui.Na43hucXnn5sauVH0p7BS7FrY1L5shy', 'GUEST', NOW(), true)
ON CONFLICT (email) DO NOTHING;
