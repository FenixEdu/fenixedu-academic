#migrcao_docente new_id_internal 0
#select concat('insert into person_role values (null,20,',t.key_person,',1);') as "" from teacher t inner join person_role p on p.key_person=t.key_person and p.key_role='20';
#Existem alguns docentes que nao tem role de docente!!

select concat('insert into PERSON_ROLE values (null,20,',t.chavePessoa,',1);') as "" from mwgrant_migracao_docente t where t.new_id_internal=0;
