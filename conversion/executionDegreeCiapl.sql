truncate ciapl.EXECUTION_DEGREE;
	insert into ciapl.EXECUTION_DEGREE (ID_INTERNAL, ACADEMIC_YEAR, KEY_DEGREE)
	select  codigoInterno , "2002/2003", chaveLicenciatura
	from fenix.licenciatura_execucao;
