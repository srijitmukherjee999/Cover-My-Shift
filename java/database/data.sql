BEGIN TRANSACTION;

INSERT INTO users (username, password_hash, full_name, role, active) VALUES
('jdoe', 'hash1', 'John Doe', 'employee', TRUE),
('asmith', 'hash2', 'Alice Smith', 'manager', TRUE),
('mjones', 'hash3', 'Michael Jones', 'employee', FALSE),
('klee', 'hash4', 'Karen Lee', 'admin', TRUE),
('pwhite', 'hash5', 'Paul White', 'employee', TRUE);

INSERT INTO shift (assigned, start_date_time, duration, status, emergency, coverer, description) VALUES
((SELECT user_id FROM users WHERE username = 'jdoe'), '2024-08-05 08:00:00', 8, 1, FALSE, NULL, 'Regular shift'),
((SELECT user_id FROM users WHERE username = 'asmith'), '2024-08-06 09:00:00', 6, 2, TRUE, 1, 'Emergency shift covered by John Doe'),
((SELECT user_id FROM users WHERE username = 'mjones'), '2024-08-07 10:00:00', 5, 1, FALSE, NULL, 'Regular shift'),
((SELECT user_id FROM users WHERE username = 'klee'), '2024-08-08 07:00:00', 9, 3, FALSE, NULL, 'Overtime shift'),
((SELECT user_id FROM users WHERE username = 'pwhite'), '2024-08-09 08:00:00', 8, 1, TRUE, 2, 'Emergency shift covered by Alice Smith');

INSERT INTO hours (employee, week_year, hours_worked) VALUES
((SELECT user_id FROM users WHERE username = 'jdoe'), 202432, 40),
((SELECT user_id FROM users WHERE username = 'asmith'), 202432, 35),
((SELECT user_id FROM users WHERE username = 'mjones'), 202432, 0),
((SELECT user_id FROM users WHERE username = 'klee'), 202432, 45),
((SELECT user_id FROM users WHERE username = 'pwhite'), 202432, 38);

INSERT INTO vacation (employee, start_date, end_date, status) VALUES
((SELECT user_id FROM users WHERE username = 'jdoe'), '2024-08-01', '2024-08-05', 1),
((SELECT user_id FROM users WHERE username = 'asmith'), '2024-08-10', '2024-08-20', 2),
((SELECT user_id FROM users WHERE username = 'mjones'), '2024-08-15', '2024-08-25', 1),
((SELECT user_id FROM users WHERE username = 'klee'), '2024-09-01', '2024-09-10', 3),
((SELECT user_id FROM users WHERE username = 'pwhite'), '2024-08-05', '2024-08-15', 2);

INSERT INTO users_shift (shift_id, coverer_id) VALUES
((SELECT user_id FROM users WHERE username = 'asmith'), (SELECT shift_id FROM shift WHERE assigned = (SELECT user_id FROM users WHERE username = 'jdoe'))),
((SELECT user_id FROM users WHERE username = 'pwhite'), (SELECT shift_id FROM shift WHERE assigned = (SELECT user_id FROM users WHERE username = 'asmith')));

COMMIT;
