#O id_internal do ROLE para Grant Owner é o [19]
select concat('insert into PERSON_ROLE values (null,19,',g.key_person,',1);') as "" from grant_owner g;
