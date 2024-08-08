
BEGIN TRANSACTION;

DROP TABLE IF EXISTS users_shift, users, shift, hours, vacation CASCADE;

CREATE TABLE users (
	user_id serial NOT NULL,
	username varchar(50) NOT NULL UNIQUE,
	password_hash varchar(200) NOT NULL,
	full_name varchar(200) NOT NULL,
	role varchar(50) NOT NULL,
	active boolean,
	CONSTRAINT PK_user PRIMARY KEY (user_id)
);

CREATE TABLE shift (
	shift_id serial NOT NULL,
	assigned int NOT NULL,
	start_date_time TIMESTAMP NOT NULL,
	duration int NOT NULL,
    status int NOT NULL,
    emergency boolean NOT NULL,
    coverer int,
    description varchar(500),
	CONSTRAINT PK_shift PRIMARY KEY (shift_id),
	CONSTRAINT FK_shift_assigned FOREIGN KEY (assigned) REFERENCES users (user_id) ON DELETE CASCADE
);

CREATE TABLE hours(
	hours_id serial NOT NULL,
    employee int NOT NULL,
	week_year int NOT NULL,
    hours_worked int NOT NULL,
	CONSTRAINT PK_hours PRIMARY KEY (hours_id),
    CONSTRAINT FK_hours_employee FOREIGN KEY (employee) REFERENCES users (user_id) ON DELETE CASCADE
);

CREATE TABLE vacation(
    vacation_id serial NOT NULL,
    employee int NOT NULL,
    start_date date NOT NULL,
    end_date date NOT NULL,
    status int NOT NULL,
    description varchar(500) NOT NULL,
    CONSTRAINT PK_vacation PRIMARY KEY (vacation_id),
    CONSTRAINT FK_vacation_employee FOREIGN KEY (employee) REFERENCES users (user_id) ON DELETE CASCADE
);

CREATE TABLE users_shift (
	shift_id int NOT NULL,
	coverer_id int NOT NULL,
	CONSTRAINT PK_user_shift PRIMARY KEY (shift_id, coverer_id),
	CONSTRAINT FK_coverer_id FOREIGN KEY (coverer_id) REFERENCES users (user_id) ON DELETE CASCADE,
	CONSTRAINT FK_shift_id FOREIGN KEY (shift_id) REFERENCES shift (shift_id) ON DELETE CASCADE
);

COMMIT;
