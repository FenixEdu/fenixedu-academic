truncate ciapl.CLASS_SHIFT;
insert into ciapl.CLASS_SHIFT (KEY_CLASS, KEY_SHIFT)
select chaveTurma, chaveTurno
from fenix.turma_turno;
