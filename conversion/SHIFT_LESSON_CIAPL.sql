truncate ciapl.SHIFT_LESSON;

insert into ciapl.SHIFT_LESSON (KEY_SHIFT, KEY_LESSON)
select chaveTurno, chaveAula
from fenix.turno_aula;

