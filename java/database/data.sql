BEGIN TRANSACTION;

INSERT INTO users (username, password_hash, full_name, role, active) VALUES
('jdoe', 'hash1', 'John Doe', 'employee', TRUE),
('asmith', 'hash2', 'Alice Smith', 'employee', TRUE),
('mjones', 'hash3', 'Michael Jones', 'employee', FALSE),
('klee', 'hash4', 'Karen Lee', 'admin', TRUE),
('pwhite', 'hash5', 'Paul White', 'employee', TRUE);

INSERT INTO shift (assigned, start_date_time, duration, status, emergency, coverer, description) VALUES
((SELECT user_id FROM users WHERE username = 'jdoe'), '2024-08-05 08:00:00', 8, 1, FALSE, (SELECT user_id FROM users WHERE username = 'jdoe'), 'Regular shift'),
((SELECT user_id FROM users WHERE username = 'asmith'), '2024-08-06 09:00:00', 6, 4, TRUE, (SELECT user_id FROM users WHERE username = 'jdoe'), 'Emergency shift covered by John Doe'),

((SELECT user_id FROM users WHERE username = 'mjones'), '2024-08-07 10:00:00', 5, 1, FALSE, NULL, 'Regular shift'),
((SELECT user_id FROM users WHERE username = 'klee'), '2024-08-08 07:00:00', 9, 3, FALSE, NULL, 'Overtime shift'),
((SELECT user_id FROM users WHERE username = 'pwhite'), '2024-08-09 08:00:00', 8, 3, TRUE, 2, 'Emergency shift covered by Alice Smith');
((SELECT user_id FROM users WHERE username = 'mjones'), '2024-08-07 10:00:00', 5, 1, FALSE, (SELECT user_id FROM users WHERE username = 'mjones'), 'Regular shift'),
((SELECT user_id FROM users WHERE username = 'klee'), '2024-08-08 07:00:00', 9, 3, FALSE, NULL, 'Uncovered overtime shift'),
((SELECT user_id FROM users WHERE username = 'pwhite'), '2024-08-09 08:00:00', 8, 3, TRUE, (SELECT user_id FROM users WHERE username = 'asmith'), 'Emergency shift covered by Alice Smith');
((SELECT user_id FROM users WHERE username = 'pwhite'), '2024-08-09 08:00:00', 8, 4, TRUE, (SELECT user_id FROM users WHERE username = 'asmith'), 'Emergency shift covered by Alice Smith');


((SELECT user_id FROM users WHERE username = 'mjones'), '2024-08-07 10:00:00', 5, 1, FALSE, (SELECT user_id FROM users WHERE username = 'mjones'), 'Regular shift'),
((SELECT user_id FROM users WHERE username = 'klee'), '2024-08-08 07:00:00', 9, 3, FALSE, NULL, 'Uncovered overtime shift'),
((SELECT user_id FROM users WHERE username = 'pwhite'), '2024-08-09 08:00:00', 8, 4, TRUE, (SELECT user_id FROM users WHERE username = 'asmith'), 'Emergency shift covered by Alice Smith');



INSERT INTO hours (employee, week_year, hours_worked) VALUES
((SELECT user_id FROM users WHERE username = 'jdoe'), 202432, 40),
((SELECT user_id FROM users WHERE username = 'asmith'), 202432, 35),
((SELECT user_id FROM users WHERE username = 'mjones'), 202432, 0),
((SELECT user_id FROM users WHERE username = 'klee'), 202432, 45),
((SELECT user_id FROM users WHERE username = 'pwhite'), 202432, 38);

INSERT INTO vacation (employee, start_date, end_date, status, description) VALUES
((SELECT user_id FROM users WHERE username = 'jdoe'), '2024-08-01', '2024-08-05', 1, 'Description1'),
((SELECT user_id FROM users WHERE username = 'asmith'), '2024-08-10', '2024-08-20', 2,'Description2'),
((SELECT user_id FROM users WHERE username = 'mjones'), '2024-08-15', '2024-08-25', 1,'Description3'),
((SELECT user_id FROM users WHERE username = 'klee'), '2024-09-01', '2024-09-10', 3,'Description4'),
((SELECT user_id FROM users WHERE username = 'pwhite'), '2024-08-05', '2024-08-15', 2,'Description5');

INSERT INTO cover_request (shift_id, coverer_id, status, message) VALUES
((SELECT shift_id FROM shift WHERE assigned = (SELECT user_id FROM users WHERE username = 'asmith')),(SELECT user_id FROM users WHERE username = 'jdoe'), 2, null),
((SELECT shift_id FROM shift WHERE assigned = (SELECT user_id FROM users WHERE username = 'klee')),(SELECT user_id FROM users WHERE username = 'asmith'), 1, null),
((SELECT shift_id FROM shift WHERE assigned = (SELECT user_id FROM users WHERE username = 'klee')),(SELECT user_id FROM users WHERE username = 'jdoe'), 3, 'No more working.'),
((SELECT shift_id FROM shift WHERE assigned = (SELECT user_id FROM users WHERE username = 'klee')),(SELECT user_id FROM users WHERE username = 'pwhite'), 1, null),
((SELECT shift_id FROM shift WHERE assigned = (SELECT user_id FROM users WHERE username = 'pwhite')),(SELECT user_id FROM users WHERE username = 'asmith'), 2, null);

COMMIT;
