truncate ciapl.EXECUTION_COURSE;
# Perdemos a disciplinas execucao que não estão emparelhadas com curriculares.

#   select de.*

#	  from fenix.disciplina_execucao de LEFT JOIN fenix.disciplina_curricular_disciplina_execucao ec on de.codigoInterno = ec.chaveDisciplinaExecucao

#   where ec.chaveDisciplinaExecucao IS NULL order by de.nome



insert into ciapl.EXECUTION_COURSE (ID_INTERNAL,

							NAME, CODE, KEY_EXECUTION_DEGREE, THEORETICAL_HOURS, PRATICAL_HOURS, THEO_PRAT_HOURS, LAB_HOURS, SEMESTER)

select distinct de.codigoInterno, de.nome, de.sigla, CEILING(de.chaveLicenciaturaExecucao / 2), 

			dc.cargaHorariaTeorica, dc.cargaHorariaPratica, dc.cargaHorariaTeoricoPratica, dc.cargaHorariaLaboratorial, le.semestre

from fenix.disciplina_execucao de, 

		fenix.disciplina_curricular dc,

		fenix.disciplina_curricular_disciplina_execucao dcde,
		fenix.licenciatura_execucao le

where dcde.chaveDisciplinaCurricular = dc.codigoInterno and 

		dcde.chaveDisciplinaExecucao = de.codigoInterno and
		le.codigoInterno = de.chaveLicenciaturaExecucao ;



select count(1) as query

from fenix.disciplina_execucao de;

#		fenix.disciplina_curricular dc,

#		fenix.disciplina_curricular_disciplina_execucao dcde

#where dcde.chaveDisciplinaCurricular = dc.codigoInterno and 

#		dcde.chaveDisciplinaExecucao = de.codigoInterno;



#select count(1) as de

#from fenix.disciplina_execucao



#select count(1) as ligacoes

#from fenix.disciplina_curricular_disciplina_execucao;



#select count(1) as ciaplE

#from ciapl.EXECUTION_COURSE;



#select de.* from fenix.disciplina_execucao de LEFT JOIN ciapl.EXECUTION_COURSE ec on de.codigoInterno = ec.ID_INTERNAL

#where ec.ID_INTERNAL IS NULL order by de.nome;



#select de.* from fenix.disciplina_execucao de LEFT JOIN fenix.disciplina_curricular_disciplina_execucao ec on de.codigoInterno = ec.chaveDisciplinaExecucao

#where ec.chaveDisciplinaExecucao IS NULL order by de.nome;

