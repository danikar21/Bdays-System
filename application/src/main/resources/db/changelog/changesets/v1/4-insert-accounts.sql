INSERT INTO accounts (email, password_hash, role, created_at, active, team_id)
VALUES
('adminTest@example.com', '$2a$10$IT4RJ3SeBznUCsvRX72t0OgoB0kgDtHRix/dFSNPFuukXbh04N2Zm', 'ADMIN', NOW(), true, 5),
('userTest@example.com', '$2a$10$yROCT.If7n4HiJwlmb5AIe5Vz4fH2m3bupLPtFwZ7a9S3k9.1PEh.', 'USER', NOW(), true, 1)
ON CONFLICT (email) DO NOTHING;

INSERT INTO accounts (email, password_hash, role, created_at, active)
VALUES
('guestTest@example.com', '$2a$10$9RxkWc0b.rnzT4x7hFWxWeZcAMjnStHVxu2ECLvEwywdY9KbhuWNi', 'GUEST', NOW(), true)
ON CONFLICT (email) DO NOTHING;


INSERT INTO accounts (email, password_hash, role, first_name, patronymic, last_name, birth_date, position, team_id, created_at)
VALUES
('ivanIvanov@example.com', '$2a$10$o3lwwetS1hlX5ZjmUfQineFMIay2OAZN/O.RTGLcmiE2QxmgEs1B2', 'USER', 'Иван', 'Иванович', 'Иванов', '2003-03-15', ' Разработчик', 1, NOW()),
('petrPetrov@example.com', '$2a$10$o3lwwetS1hlX5ZjmUfQineFMIay2OAZN/O.RTGLcmiE2QxmgEs1B2', 'USER', 'Петр', 'Петрович', 'Петров', '2000-03-28', 'Разработчик', 1, NOW()),
('stepanStepanov@example.com', '$2a$10$o3lwwetS1hlX5ZjmUfQineFMIay2OAZN/O.RTGLcmiE2QxmgEs1B2', 'USER', 'Степан', 'Степанович', 'Степанов', '2005-03-20', 'Разработчик', 1, NOW()),

('viktoriaViktorova@example.com', '$2a$10$o3lwwetS1hlX5ZjmUfQineFMIay2OAZN/O.RTGLcmiE2QxmgEs1B2', 'USER', 'Виктория', 'Викторовна', 'Викторова', '2001-04-10', 'Маркетолог', 2, NOW()),
('alexandraAlexandrova@example.com', '$2a$10$o3lwwetS1hlX5ZjmUfQineFMIay2OAZN/O.RTGLcmiE2QxmgEs1B2', 'USER', 'Александра', 'Александровна', 'Александрова', '2003-04-25', 'Маркетолог', 2, NOW()),

('konstantinKonstantinov@example.com', '$2a$10$o3lwwetS1hlX5ZjmUfQineFMIay2OAZN/O.RTGLcmiE2QxmgEs1B2', 'USER', 'Константин', 'Константинович', 'Константинов', '1996-05-05', 'Специалист по информационной безопасности', 3, NOW()),
('polinaPavlova@example.com', '$2a$10$o3lwwetS1hlX5ZjmUfQineFMIay2OAZN/O.RTGLcmiE2QxmgEs1B2', 'USER', 'Полина', 'Павловна', 'Павлова', '1999-05-18', 'Специалист по информационной безопасности', 3, NOW())
ON CONFLICT (email) DO NOTHING;
