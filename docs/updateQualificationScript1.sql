select concat('UPDATE QUALIFICATION SET year = 0 WHERE year is null and id_internal = ', id_internal, ';')  AS ''
from QUALIFICATION;