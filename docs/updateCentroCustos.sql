-- Alteração do Centro de Custo de "Secção Autónoma de Engenharia Naval" de 3002 para 3201
-- ANTES: 3002;SEC. AUTONOMA DE ENGENHARIA NAVAL ; ; 
-- DEPOIS: 3201;SECCAO AUTONOMA DE ENGENHARIA NAVAL   ; SECCAO AUTONOMA DE ENGENHARIA NAVAL ;  

select concat('update ass_FUNCIONARIO_HISTORICO set data_fim = "2004-03-11" where chaveCCLocalTrabalho = 73 and chaveFuncionario =', chaveFuncionario,';') as ""
from ass_funcionario_historico fh inner join ass_funcionario f on
f.codigoInterno = fh.chaveFuncionario
where fh.chaveCCLocalTrabalho in (73,163)
group by fh.chaveFuncionario
having count(1) >= 2;
  
 update DEPARTMENT set code = '3201' where code = '3002';

-- Alteração do Centro de Custo de "Secção Autónoma de Economia e Gestão " de 3002 para 3201
-- ANTES: 3001;SEC. AUTONOMA DE ECONOMIA E GESTAO ; ; 
-- DEPOIS: 2901;DEPARTAMENTO DE ENGENHARIA E GESTAO   ;DEP.ENGENHARIA E GESTAO-PRESIDENCIA   ; 

select concat('update ass_FUNCIONARIO_HISTORICO set data_fim = "2004-03-11" where chaveCCLocalTrabalho = 63 and chaveFuncionario =', chaveFuncionario,';') as ""
from ass_funcionario_historico fh inner join ass_funcionario f on f.codigoInterno = fh.chaveFuncionario
where chaveCCLocalTrabalho in (63,157)
group by chaveFuncionario
having count(1) >= 2;
  
  update DEPARTMENT set code = '3201', name = 'DEPARTAMENTO DE ENGENHARIA E GESTAO' where code = '3002';