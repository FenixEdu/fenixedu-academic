# Disciplinas execução não emparelhadas com curriculares... FENIX...
select de.*
from fenix.disciplina_execucao de LEFT JOIN fenix.disciplina_curricular_disciplina_execucao ec on de.codigoInterno = ec.chaveDisciplinaExecucao
where ec.chaveDisciplinaExecucao IS NULL order by de.nome;

# Lista de Salas a tratar (poderá haver mais mas estas já vão dar trabalho)
select *
from fenix.sala
where tipo = " ";


