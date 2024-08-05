BEGIN TRANSACTION;

INSERT INTO users (username,password_hash,role) VALUES ('user','$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC','ROLE_USER');
INSERT INTO users (username,password_hash,role) VALUES ('admin','$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC','ROLE_ADMIN');



INSERT INTO shift(shift_id, assigned, start_date_time, duration, status, emergency, coverer, description)
VALUES(1, 1, TIMESTAMP('2024-08-02 14:30:45'), 8, 1, false, 1, "Assigned");
INSERT INTO shift(shift_id, assigned, start_date_time, duration, status, emergency, coverer, description)
VALUES(2, 2, TIMESTAMP('2024-08-02 14:35:50'), 8, 3, true, 1, "Emergency");
INSERT INTO shift(shift_id, assigned, start_date_time, duration, status, emergency, coverer, description)
VALUES(3, 3, TIMESTAMP('2024-08-02 14:40:05'), 8, 2, false, 1, "Accepted");

INSERT INTO hours(hours_id, employee, week_year, hours_worked)
VALUES(1, 1, ,40);
INSERT INTO hours(hours_id, employee, week_year, hours_worked)
VALUES(2, 2, ,32);
INSERT INTO hours(hours_id, employee, week_year, hours_worked)
VALUES(3,3, ,48);

INSERT INTO vacation(vacation_id, employee, start_date, end_date, status)
VALUES(1, 1, , ,0);
INSERT INTO vacation(vacation_id, employee, start_date, end_date, status)
VALUES(2, 2, , ,1 );
INSERT INTO vacation(vacation_id, employee, start_date, end_date, status)
VALUES(3, 3, , ,2);

INSERT INTO user_shift(shift_id, coverer_id)
VALUES(2, );
INSERT INTO user_shift(shift_id, coverer_id)
VALUES();
INSERT INTO user_shift(shift_id, coverer_id)
VALUES();

COMMIT TRANSACTION;
