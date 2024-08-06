BEGIN TRANSACTION;

INSERT INTO users (username,password_hash,role) VALUES ('user','$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC','ROLE_USER');
INSERT INTO users (username,password_hash,role) VALUES ('admin','$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC','ROLE_ADMIN');

INSERT INTO users (username, password_hash, full_name, role, active) VALUES
('jdoe', 'hash1', 'John Doe', 'employee', TRUE),
('asmith', 'hash2', 'Alice Smith', 'manager', TRUE),
('mjones', 'hash3', 'Michael Jones', 'employee', FALSE),
('klee', 'hash4', 'Karen Lee', 'admin', TRUE),
('pwhite', 'hash5', 'Paul White', 'employee', TRUE);

INSERT INTO shift (assigned, start_date_time, duration, status, emergency, coverer, description) VALUES
(1, '2024-08-05 08:00:00', 8, 1, FALSE, NULL, 'Regular shift'),
(2, '2024-08-06 09:00:00', 6, 2, TRUE, 1, 'Emergency shift covered by John Doe'),
(3, '2024-08-07 10:00:00', 5, 1, FALSE, NULL, 'Regular shift'),
(4, '2024-08-08 07:00:00', 9, 3, FALSE, NULL, 'Overtime shift'),
(1, '2024-08-09 08:00:00', 8, 1, TRUE, 2, 'Emergency shift covered by Alice Smith');

INSERT INTO hours (employee, week_year, hours_worked) VALUES
(1, 202432, 40),
(2, 202432, 35),
(3, 202432, 0),
(4, 202432, 45),
(5, 202432, 38);

INSERT INTO vacation (employee, start_date, end_date, status) VALUES
(1, '2024-08-01', '2024-08-05', 1),
(2, '2024-08-10', '2024-08-20', 2),
(3, '2024-08-15', '2024-08-25', 1),
(4, '2024-09-01', '2024-09-10', 3),
(5, '2024-08-05', '2024-08-15', 2);

INSERT INTO users_shift (shift_id, coverer_id) VALUES
(2, 1),
(5, 2);

COMMIT;

