truncate ciapl.ROOM;
insert into ciapl.ROOM (ID_INTERNAL, NAME, BUILDING, FLOOR, TYPE, NORMAL_CAPACITY, EXAM_CAPACITY)
select codigoInterno, 
			  nome, 
				edificio, 
				piso,
				case tipo
						when 'Anfiteatro' then 1
						when 'Laboratório' then 3
						when 'Plana' then 2
						else 2 end,
				capacidadeNormal, capacidadeExame
from fenix.sala;
