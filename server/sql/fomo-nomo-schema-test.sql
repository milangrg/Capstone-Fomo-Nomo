drop database if exists fomo_nomo_test;
create database fomo_nomo_test;
use fomo_nomo_test;

create table `user`(
	user_id int primary key auto_increment,
	first_name varchar(50) not null,
    last_name varchar(50) not null,
    email varchar(250) not null,
    phone varchar(50) null,
    dob date null
);

create table location(
	location_id int primary key auto_increment,
	address varchar(250) not null,
    state varchar(2) not null,
    city varchar(50) not null,
    postal varchar(15) null,
    location_name varchar(50) null
);

create table `event`(
	event_id int primary key auto_increment,
	title varchar(50) not null,
    user_id int not null,
	`description` varchar(250) null,
    location_id int not null,
    event_type varchar(50) not null,
    `start` datetime not null,
    `end` datetime not null,
	constraint fk_event_user_id
		foreign key (user_id)
        references `user`(user_id),
	constraint fk_event_location_id
		foreign key (location_id)
        references location(location_id)
);

create table invitation(
	invitation_id int primary key auto_increment,
    event_id int not null,
    user_id int not null,
    `status` varchar(20) not null,
	constraint fk_invitation_event_id
		foreign key (event_id)
        references `event`(event_id),
	constraint fk_invitation_user_id
		foreign key (user_id)
        references `user`(user_id)
);

delimiter //
create procedure set_known_good_state()
begin

delete from invitation;
delete from `event`;
delete from location;
delete from `user`;
alter table `user` auto_increment = 1;
    
insert into `user` 
	(first_name, last_name, email, phone, dob)
values
	('Alex', 'Carter', 'acarter1234@emailspot.com', '555-555-1234', '1995-08-02'),
    ('Marjorie', 'Pierce', 'margiep54@emailspot.com', '555-555-2216', '1954-02-19'),
    ('Russell', 'Wood', 'russ_woodz@emailspot.com', '555-555-9901', '1975-12-22'),
    ('Jimmy', 'Fisher', 'gonefishin889@yahoo2.com', '555-555-7895', '1989-05-10'),
    ('Sarah', 'Crawford', 's_crawford_31@aolmailbox.com', '555-555-4989', '1991-04-15'),
    ('Maria', 'Donahoe', 'md0nah0e2000@geemail.org', '555-555-1515', '2000-08-19'),
    ('Jack', 'Green', 'gr33nmachin399@emailer.com', '', NULL),
    ('Diane', 'Green', 'd_green8@college.edu', '', NULL),
    ('Viola', 'Reynolds', 'vr11_@yahoo2.com', '555-555-9999', '1989-05-19'),
    ('William', 'Quimby', 'quimby_will10@university.edu', '', '2001-10-31');

insert into location 
	(location_id, address, state, city, postal, location_name)
values
    (1, '3300 Riverfront Walk', 'NY', 'Buffalo', '14202', 'Riverside Restaurant'),
    (2, '1020 Sunshine Blvd', 'NY', 'Rochester', '14602', ''),
    (3, '720 Party Lane', 'NY', 'Syracuse', '13201', 'Party Central'),
    (4, '2500 Festival Road', 'NY', 'Albany', '12203', 'Festival Grounds'),
    (5, '4900 Street Ave', 'NY', 'Ithaca', '14850', 'The Lounge'),
    (6, '8800 Spa Street', 'NY', 'Utica', '13502', 'The GymSpa'),
    (7, '1550 Golf Path', 'NY', 'Saratoga Springs', '12866', 'The Golf Emporium'),
    (8, '5000 Big Hills', 'NY', 'New York', '10001', ''),
    (9, '2210 Mystery Blvd', 'NY', 'Poughkeepsie', '12601', 'Mystery Escape Room'),
    (10, '1460 Victory Lane', 'NY', 'Niagara Falls', '14301', 'That big waterfall'),
    (11, '3600 Twilight Ave', 'NY', 'Binghamton', '13901', 'The Cinema'),
    (12, '7700 Avenue Street', 'NJ', 'Newark', '07101', '');

insert into `event` 
	(event_id, user_id, title, `description`, location_id, event_type, `start`, `end`)
values
	(1, 4, 'Going Away Party', 'I am moving somewhere else, so come see me off!', 3, 'Social', '2024-05-17 20:00:00', '2024-05-17 23:59:00'),
    (2, 1, 'Appointment with Dr. Jones', '', 12, 'Appointment', '2024-05-30 11:30:00', '2024-05-30 13:00:00'),
    (3, 9, 'Work Happy Hour', 'Meet for happy hour drinks and food', 1, 'Work', '2024-05-21 17:00:00', '2024-05-21 20:00:00'),
    (4, 1, 'Spa/Gym Day', '', 6, 'Personal', '2024-05-15 09:30:00', '2024-05-15 22:00:00'),
    (5, 1, 'Golf Outing', 'It is time for our annual golf tourament!', 7, 'Social', '2024-05-11 08:00:00', '2024-05-11 12:00:00'),
    (6, 5, 'Escape Room Party', 'The new escape room opened up...we should check it out!', 9, 'Social', '2024-05-13 18:00:00', '2024-05-13 20:00:00');

insert into invitation 
	(invitation_id, event_id, user_id, `status`)
values
	(1, 1, 1, 'Accepted'),
    (2, 1, 2, 'Declined'),
    (3, 1, 3, 'Pending'),
    (4, 1, 5, 'Accepted'),
    (5, 1, 6, 'Accepted'),
    (6, 1, 10, 'Accepted'),
    (7, 3, 1, 'Pending'),
    (8, 3, 4, 'Accepted'),
    (9, 5, 7, 'Pending'),
    (10, 5, 8, 'Accepted'),
    (11, 5, 3, 'Declined'),
    (12, 6, 1, 'Declined');    

end //
-- 4. Change the statement terminator back to the original.
delimiter ;