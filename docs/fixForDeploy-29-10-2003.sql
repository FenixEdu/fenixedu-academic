#select id_internal as key_execution_course,key_teacher,1 from execution_degree;

select CONCAT(
		"insert into COORDINATOR (key_execution_degree,key_teacher,responsible) values (",
         id_internal,
         ","
         ,key_teacher,
          ",1);" ) as "" from EXECUTION_DEGREE; 
              