alter table ROLE add column `OJB_CONCRETE_CLASS` varchar(250) NOT NULL default '';

update ROLE set OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.Role';
update ROLE set OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.ResourceAllocationRole' where ROLE_TYPE like 'RESOURCE_ALLOCATION_MANAGER';

alter table ROLE add column SCHEDULES_ACCESS_GROUP text;
alter table ROLE add column SPACES_ACCESS_GROUP text;
alter table ROLE add column MATERIALS_ACCESS_GROUP text;
alter table ROLE add column VEHICLES_ACCESS_GROUP text;

select concat('update ROLE set SCHEDULES_ACCESS_GROUP = CONCAT(COALESCE(SCHEDULES_ACCESS_GROUP,""), " || person(', PERSON_ROLE.KEY_PERSON , ')") where ID_INTERNAL = ', ROLE.ID_INTERNAL , ';') as "" from PERSON_ROLE inner join ROLE on PERSON_ROLE.KEY_ROLE = ROLE.ID_INTERNAL where ROLE.ROLE_TYPE = 'RESOURCE_ALLOCATION_MANAGER';
select concat('update ROLE set SPACES_ACCESS_GROUP = CONCAT(COALESCE(SPACES_ACCESS_GROUP,""), " || person(', PERSON_ROLE.KEY_PERSON , ')") where ID_INTERNAL = ', ROLE.ID_INTERNAL , ';') as "" from PERSON_ROLE inner join ROLE on PERSON_ROLE.KEY_ROLE = ROLE.ID_INTERNAL where ROLE.ROLE_TYPE = 'RESOURCE_ALLOCATION_MANAGER';

insert into PERSISTENT_GROUP_MEMBERS (NAME, TYPE, KEY_ROOT_DOMAIN_OBJECT) values ('Grupo do GOP', 'SPACE_OCCUPATION', 1);

select concat('update RESOURCE set LESSON_OCCUPATIONS_ACCESS_GROUP = "persistentGroup($I(' , ID_INTERNAL , ',' , "'accessControl.PersistentGroupMembers')" , ')" where LESSON_OCCUPATIONS_ACCESS_GROUP like "role%" ;') as "" from PERSISTENT_GROUP_MEMBERS where NAME like 'Grupo do GOP' and TYPE like 'SPACE_OCCUPATION';
select concat('update RESOURCE set WRITTEN_EVALUATION_OCCUPATIONS_ACCESS_GROUP = "persistentGroup($I(' , ID_INTERNAL , ',' , "'accessControl.PersistentGroupMembers')" , ')" where WRITTEN_EVALUATION_OCCUPATIONS_ACCESS_GROUP like "role%" ;') as "" from PERSISTENT_GROUP_MEMBERS where NAME like 'Grupo do GOP' and TYPE like 'SPACE_OCCUPATION';
select concat('update RESOURCE set GENERIC_EVENT_OCCUPATIONS_ACCESS_GROUP = "persistentGroup($I(' , ID_INTERNAL , ',' , "'accessControl.PersistentGroupMembers')" , ')" where GENERIC_EVENT_OCCUPATIONS_ACCESS_GROUP like "role%" ;') as "" from PERSISTENT_GROUP_MEMBERS where NAME like 'Grupo do GOP' and TYPE like 'SPACE_OCCUPATION';

insert into PERSISTENT_GROUP_MEMBERS_PERSON (KEY_PERSON, KEY_PERSISTENT_GROUP_MEMBERS) select PERSON_ROLE.KEY_PERSON, 0 from PERSON_ROLE inner join ROLE on PERSON_ROLE.KEY_ROLE = ROLE.ID_INTERNAL where ROLE.ROLE_TYPE = 'RESOURCE_ALLOCATION_MANAGER';
update PERSISTENT_GROUP_MEMBERS_PERSON set KEY_PERSISTENT_GROUP_MEMBERS = (select ID_INTERNAL from PERSISTENT_GROUP_MEMBERS where NAME like 'Grupo do GOP' and TYPE like 'SPACE_OCCUPATION') where KEY_PERSISTENT_GROUP_MEMBERS = 0;
