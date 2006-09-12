insert into SEMINARY values (7, 'Desenvolvimento Sustentável 2005/2006', 'Ano Lectivo de 2006 / 2007', 1, 1, '00:00:00', '2006-09-06', '23:59:59', '2006-09-29', 0, 0, 1);

insert into SEMINARY_CURRICULARCOURSE values (null, 7, 3840, 1, null, 1);
insert into SEMINARY_CURRICULARCOURSE values (null, 7, 3769, 1, null, 1);
insert into SEMINARY_CURRICULARCOURSE values (null, 7, 3528, 1, null, 1);
insert into SEMINARY_CURRICULARCOURSE values (null, 7, 3418, 1, null, 1);
insert into SEMINARY_CURRICULARCOURSE values (null, 7, 11983, 1, null, 1);
insert into SEMINARY_CURRICULARCOURSE values (null, 7, 2818, 1, null, 1);
insert into SEMINARY_CURRICULARCOURSE values (null, 7, 4920, 3, null, 1);
insert into SEMINARY_CURRICULARCOURSE values (null, 7, 4920, 1, null, 1);
insert into SEMINARY_CURRICULARCOURSE values (null, 7, 4920, 1, null, 1);
insert into SEMINARY_CURRICULARCOURSE values (null, 7, 4920, 1, null, 1);
insert into SEMINARY_CURRICULARCOURSE values (null, 7, 4920, 1, null, 1);
insert into SEMINARY_CURRICULARCOURSE values (null, 7, 4920, 1, null, 1);
insert into SEMINARY_CURRICULARCOURSE values (null, 7, 12018, 1, null, 1);
insert into SEMINARY_CURRICULARCOURSE values (null, 7, 12231, 1, null, 1);
insert into SEMINARY_CURRICULARCOURSE values (null, 7, 11959, 1, null, 1);
insert into SEMINARY_CURRICULARCOURSE values (null, 7, 4920, 1, null, 1);
insert into SEMINARY_CURRICULARCOURSE values (null, 7, 15113, 1, null, 1);
insert into SEMINARY_CURRICULARCOURSE values (null, 7, 15048, 1, null, 1);
insert into SEMINARY_CURRICULARCOURSE values (null, 7, 14672, 1, null, 1);
insert into SEMINARY_CURRICULARCOURSE values (null, 7, 15253, 1, null, 1);
insert into SEMINARY_CURRICULARCOURSE values (null, 7, 15588, 1, null, 1);


-- 
-- Casos que faltam averiguar:
-- 
-- SAEN 	Licenciatura em Eng.ª e Arquitectura Naval 	Complementos de Engenharia Naval 	Completa	
-- select DEGREE_MODULE.ID_INTERNAL, DEGREE_MODULE.NAME, DEGREE_CURRICULAR_PLAN.NAME from DEGREE_MODULE inner join DEGREE_CURRICULAR_PLAN on DEGREE_CURRICULAR_PLAN.ID_INTERNAL = DEGREE_MODULE.KEY_DEGREE_CURRICULAR_PLAN inner join DEGREE on DEGREE.ID_INTERNAL = DEGREE_CURRICULAR_PLAN.KEY_DEGREE where DEGREE.SIGLA like 'LEN-pB' and DEGREE_MODULE.NAME like '%Comp%';
-- 
-- DEI 	Licenciatura em Eng.ª Informática e de Computadores 	Portfólio Pessoal 	Seminário	
-- select DEGREE_MODULE.ID_INTERNAL, DEGREE_MODULE.NAME, DEGREE_CURRICULAR_PLAN.NAME from DEGREE_MODULE inner join DEGREE_CURRICULAR_PLAN on DEGREE_CURRICULAR_PLAN.ID_INTERNAL = DEGREE_MODULE.KEY_DEGREE_CURRICULAR_PLAN inner join DEGREE on DEGREE.ID_INTERNAL = DEGREE_CURRICULAR_PLAN.KEY_DEGREE where DEGREE.SIGLA like 'LEIC-pB' and DEGREE_MODULE.NAME like '%Port%Pes%';
-- 
-- DEEC 	Mestrado em Eng.ª Electrotécnica e de Computadores 	Desenvolvimento Sustentável e de Inovação 	Completa	
-- select DEGREE_MODULE.ID_INTERNAL, DEGREE_MODULE.NAME, DEGREE_CURRICULAR_PLAN.NAME from DEGREE_MODULE inner join DEGREE_CURRICULAR_PLAN on DEGREE_CURRICULAR_PLAN.ID_INTERNAL = DEGREE_MODULE.KEY_DEGREE_CURRICULAR_PLAN inner join DEGREE on DEGREE.ID_INTERNAL = DEGREE_CURRICULAR_PLAN.KEY_DEGREE where DEGREE.SIGLA like 'MEEC-pB' and DEGREE_MODULE.NAME like '%Des%Sus%Inov%';
-- 
-- DEMG 	Mestrado em Georrecursos 	Seminários 	Completa	
-- select DEGREE_MODULE.ID_INTERNAL, DEGREE_MODULE.NAME, DEGREE_CURRICULAR_PLAN.NAME from DEGREE_MODULE inner join DEGREE_CURRICULAR_PLAN on DEGREE_CURRICULAR_PLAN.ID_INTERNAL = DEGREE_MODULE.KEY_DEGREE_CURRICULAR_PLAN inner join DEGREE on DEGREE.ID_INTERNAL = DEGREE_CURRICULAR_PLAN.KEY_DEGREE where DEGREE.SIGLA like 'MG-pB' and DEGREE_MODULE.NAME like '%Sem%';
-- 
-- DEG 	Mestrado em Eng.ª e Gestão de Tecnologia 	Politicas de Ciência, Tecnologia e Inovação 	Completa	
-- select DEGREE_MODULE.ID_INTERNAL, DEGREE_MODULE.NAME, DEGREE_CURRICULAR_PLAN.NAME from DEGREE_MODULE inner join DEGREE_CURRICULAR_PLAN on DEGREE_CURRICULAR_PLAN.ID_INTERNAL = DEGREE_MODULE.KEY_DEGREE_CURRICULAR_PLAN inner join DEGREE on DEGREE.ID_INTERNAL = DEGREE_CURRICULAR_PLAN.KEY_DEGREE where DEGREE.SIGLA like 'MEGT-pB' and DEGREE_MODULE.NAME like '%Pol%Ci%Tec%In%';
-- 
-- DEG 	Mestrado em Eng.ª e Gestão de Tecnologia 	Seminário em Gestão de Tecnologia 	Completa	
-- select DEGREE_MODULE.ID_INTERNAL, DEGREE_MODULE.NAME, DEGREE_CURRICULAR_PLAN.NAME from DEGREE_MODULE inner join DEGREE_CURRICULAR_PLAN on DEGREE_CURRICULAR_PLAN.ID_INTERNAL = DEGREE_MODULE.KEY_DEGREE_CURRICULAR_PLAN inner join DEGREE on DEGREE.ID_INTERNAL = DEGREE_CURRICULAR_PLAN.KEY_DEGREE where DEGREE.SIGLA like 'MEGT-pB' and DEGREE_MODULE.NAME like '%Sem%';
-- 
-- DM 	Mestrado em Matemática Aplicada 	Tópicos Especiais 1 	Completa	
-- select DEGREE_MODULE.ID_INTERNAL, DEGREE_MODULE.NAME, DEGREE_CURRICULAR_PLAN.NAME from DEGREE_MODULE inner join DEGREE_CURRICULAR_PLAN on DEGREE_CURRICULAR_PLAN.ID_INTERNAL = DEGREE_MODULE.KEY_DEGREE_CURRICULAR_PLAN inner join DEGREE on DEGREE.ID_INTERNAL = DEGREE_CURRICULAR_PLAN.KEY_DEGREE where DEGREE.SIGLA like 'MMA-pB' and DEGREE_MODULE.NAME like '%T%Esp%%';
