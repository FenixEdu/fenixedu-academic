select concat('update UNIT set TYPE = "ACADEMIC_UNIT" where UNIT.ID_INTERNAL = ',
	UNIT.ID_INTERNAL, ';')
as "1" from UNIT where NAME = 'Unidades Académicas';