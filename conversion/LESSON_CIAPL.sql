truncate ciapl.LESSON;

insert into ciapl.LESSON (ID_INTERNAL, 													
WEEKDAY, 													
START_TIME, 													
END_TIME, 													
KEY_ROOM, 													
KEY_EXECUTION_COURSE, 													
TYPE)
select codigoInterno, 
				case diaSemana
								when 'sabado' then 7
								when 'segunda' then 2
								when 'terça' then 3
								when 'quarta' then 4
								when 'quinta' then 5
								when 'sexta' then 6
								else  1 end,
				concat_ws(":",horaInicio,minutosInicio,"00"),
				concat_ws(":",horaFim,minutosFim,"00"),
				chaveSala,
				chaveDisciplinaExecucao,
				case tipo when 'T' then 1
									when 'P' then 2
									when 'TP' then 3
									when 'L' then 4
									else 5 end

				
from fenix.aula;


