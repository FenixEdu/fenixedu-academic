truncate ciapl.CURRICULAR_COURSE_EXECUTION_COURSE;
insert into ciapl.CURRICULAR_COURSE_EXECUTION_COURSE (KEY_CURRICULAR_COURSE, KEY_EXECUTION_COURSE)
select  cc.ID_INTERNAL , ec.ID_INTERNAL
from ciapl.EXECUTION_COURSE ec,

		   ciapl.CURRICULAR_COURSE cc,
		  fenix.disciplina_curricular_disciplina_execucao	dcde
where  dcde.chaveDisciplinaExecucao = ec.ID_INTERNAL and 
			dcde.chaveDisciplinaCurricular = cc.ID_INTERNAL;
