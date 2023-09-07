set FOREIGN_KEY_CHECKS = 0;

create table building (
id int not null auto_increment,
address varchar(100) not null,
levels tinyint not null,
entrances tinyint not null,
primary key (id)
);

create table `member` (
id int not null auto_increment,
fullname varchar(100) not null,
email varchar(100) default null,
resident_id int,
primary key (id),
constraint `fk_resident_id` foreign key (`resident_id`) references `resident` (`id`) on update restrict on delete restrict
);

create table resident (
id int not null auto_increment,
fullname varchar(100) not null,
entercar tinyint default 0,
apartment_id int not null,
primary key(id),
constraint `fk_apartment_id` foreign key (`apartment_id`) references `apartment` (`id`) on update restrict on delete restrict
);

create table apartment (
id int not null auto_increment,
number int not null,
rooms tinyint not null,
square float not null,
floor tinyint not null,
building_id int not null,
member_id int not null,
primary key(id),
constraint `fk_building_id` foreign key (`building_id`) references `building` (`id`) on update restrict on delete restrict,
constraint `fk_member_id` foreign key (`member_id`) references `member` (`id`) on update restrict on delete restrict
);

create table role (
id int not null auto_increment,
role_name varchar(45) not null,
primary key (id)
);

create table member_role (
member_id int not null,
role_id int not null,
primary key (member_id, role_id),
constraint `fk2_member_id` foreign key (`member_id`) references `member` (`id`) on update restrict on delete restrict,
constraint `fk_role_id` foreign key (`role_id`) references `role` (`id`) on update restrict on delete restrict
);
