select concat('UPDATE QUALIFICATION SET date = \'', year, '-01-01\' WHERE id_internal = ', id_internal, ';')  AS ''
from QUALIFICATION;
