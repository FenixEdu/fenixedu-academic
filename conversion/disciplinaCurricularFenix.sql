truncate  ciapl.CURRICULAR_COURSE;

#NOTA: Perdemos algumas disciplinas curriculares. Razão são os ramos...

#				UNIQUE (NAME, CODE, SEMESTER, CURRICULAR_YEAR, KEY_DEGREE_CURRICULAR_PLAN)

insert into ciapl.CURRICULAR_COURSE (ID_INTERNAL,

																	THEORETICAL_HOURS,

																	 PRATICAL_HOURS,

																	THEO_PRAT_HOURS,

																	LAB_HOURS,

																	CURRICULAR_YEAR,

																	NAME,

																	CODE,

																	SEMESTER,

																	KEY_DEGREE_CURRICULAR_PLAN  )

select dc.codigoInterno, dc.cargaHorariaTeorica, 
			dc.cargaHorariaPratica, 
			dc.cargaHorariaTeoricoPratica, 
			dc.cargaHorariaLaboratorial, 
			dc.anoCurricular, dc.nome, dc.codigo,	
			dc.semestre, dcp.ID_INTERNAL
from fenix.disciplina_curricular dc, 
			ciapl.DEGREE d, 
			ciapl.DEGREE_CURRICULAR_PLAN dcp, 
			fenix.licenciatura l
where d.ID_INTERNAL = dcp.KEY_DEGREE and
			d.ID_INTERNAL = l.codigoInterno and
			l.codigoInterno = dc.chaveLicenciatura;



#select count(1) from fenix.disciplina_curricular;

#select count(1) from ciapl.CURRICULAR_COURSE;

