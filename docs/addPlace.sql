drop table if exists CAMPUS;
create table CAMPUS(
	ID_INTERNAL int(11) not null auto_increment,
	NAME varchar(50) not null,
	primary key (ID_INTERNAL),
	unique U1 (NAME)
) type=InnoDB;

insert CAMPUS values(1, "Alameda");
insert CAMPUS values(2, "TagusPark");


alter table EXECUTION_DEGREE ADD KEY_CAMPUS int(11) not null;
alter table EXECUTION_DEGREE DROP INDEX U1;
alter table EXECUTION_DEGREE ADD UNIQUE U1 (ACADEMIC_YEAR,KEY_DEGREE_CURRICULAR_PLAN, KEY_CAMPUS);
update EXECUTION_DEGREE set KEY_CAMPUS = 1;
update EXECUTION_DEGREE set KEY_CAMPUS = 2 where id_internal in (19, 22, 77, 78, 97);




