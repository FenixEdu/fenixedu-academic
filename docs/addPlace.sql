-- ---------------------------------
--Table structure for DEGREE_INFO
-- ---------------------------------
drop table if exists CAMPUS;
create table CAMPUS(
	ID_INTERNAL int(11) not null auto_increment,
	KEY_DEGREE int(11) not null,
	NAME varchar(50) not null,
	primary key (ID_INTERNAL),
	unique U1 (NAME, KEY_DEGREE)
) type=InnoDB;

insert CAMPUS values(1, 1,"Alameda");
insert CAMPUS values(2, 2,"Alameda");
insert CAMPUS values(3, 3,"Alameda");
insert CAMPUS values(4, 4,"Alameda");
insert CAMPUS values(5, 5,"Alameda");
insert CAMPUS values(6, 6,"Alameda");
insert CAMPUS values(7, 7,"Alameda");
insert CAMPUS values(8, 8,"Alameda");
insert CAMPUS values(9, 9,"Alameda");
insert CAMPUS values(10, 10,"Alameda");
insert CAMPUS values(11, 11,"Alameda");
insert CAMPUS values(12, 12,"Alameda");
insert CAMPUS values(13, 13,"Alameda");
insert CAMPUS values(14, 14,"Alameda");
insert CAMPUS values(15, 15,"Alameda");
insert CAMPUS values(16, 16,"Alameda");
insert CAMPUS values(17, 17,"Alameda");
insert CAMPUS values(18, 18,"Alameda");
insert CAMPUS values(19, 19,"TagusPark");
insert CAMPUS values(20, 20,"Alameda");
insert CAMPUS values(21, 21,"Alameda");
insert CAMPUS values(22, 22,"TagusPark");
insert CAMPUS values(23, 23,"Alameda");
insert CAMPUS values(24, 24,"Alameda");
insert CAMPUS values(25, 25,"Alameda");
insert CAMPUS values(26, 26,"Alameda");
insert CAMPUS values(27, 27,"Alameda");
insert CAMPUS values(28, 28,"Alameda");
insert CAMPUS values(29, 29,"Alameda");
insert CAMPUS values(30, 30,"Alameda");
insert CAMPUS values(31, 31,"Alameda");
insert CAMPUS values(32, 32,"Alameda");
insert CAMPUS values(33, 33,"Alameda");
insert CAMPUS values(34, 34,"Alameda");
insert CAMPUS values(35, 35,"Alameda");
insert CAMPUS values(36, 36,"Alameda");
insert CAMPUS values(37, 37,"Alameda");
insert CAMPUS values(38, 38,"Alameda");
insert CAMPUS values(39, 39,"Alameda");
insert CAMPUS values(40, 40,"Alameda");
insert CAMPUS values(41, 41,"Alameda");
insert CAMPUS values(42, 42,"Alameda");
insert CAMPUS values(43, 43,"Alameda");
insert CAMPUS values(44, 44,"Alameda");
insert CAMPUS values(45, 45,"Alameda");
insert CAMPUS values(46, 46,"Alameda");
insert CAMPUS values(47, 47,"Alameda");
insert CAMPUS values(48, 48,"Alameda");
insert CAMPUS values(49, 49,"Alameda");
insert CAMPUS values(51, 51,"TagusPark");


