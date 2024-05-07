drop database if exists fomo_nomo;
create database fomo_nomo;
use fomo_nomo;

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
    host_id int not null,
	`description` varchar(250) null,
    location_id int not null,
    event_type varchar(50) not null,
    `start` datetime not null,
    `end` datetime not null,
	constraint fk_event_host_id
		foreign key (host_id)
        references `user`(user_id),
	constraint fk_event_location_id
		foreign key (location_id)
        references location(location_id)
);

create table invitation(
	invitation_id int primary key auto_increment,
    event_id int not null,
    guest_id int not null,
    `status` varchar(20) not null,
	constraint fk_invitation_event_id
		foreign key (event_id)
        references `event`(event_id),
	constraint fk_invitation_guest_id
		foreign key (guest_id)
        references `user`(user_id)
);
