-- alter table METADATA add visibility bit not null default "1";
-- alter table XML_DOCUMENTS add visibility bit not null default "1";

select CONCAT(
		"insert into COORDINATOR (key_execution_degree,key_teacher,responsible) values (",
         id_internal,
         ","
         ,key_teacher,
          ",1);" ) as "" from EXECUTION_DEGREE; 
