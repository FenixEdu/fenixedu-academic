-- -----------------------------
-- Data for table 'SEMINARY'
-- -----------------------------
delete from SEMINARY;
insert into SEMINARY values (1, 'Desenvolvimento Sustentavel','Ano lectivo de 2003 / 2004',1);
insert into SEMINARY values (2, 'Clonagem humana a altas temperaturas','Ano 2042, orador: Gonçalo Mengel Luiz',3);
insert into SEMINARY values (3, 'Pontes e Túneis','Ano 1940, orador: Hintze Ribeiro',2);

-- -----------------------------
-- Data for table 'SEMINARY_MODALITY'
-- -----------------------------
delete from SEMINARY_MODALITY;
insert into SEMINARY_MODALITY values (1, 'Completa','O aluno frequenta os três Seminários com cerca de 50 horas lectivas e elabora um trabalho de grupo de estudo dum caso, que será discutido e avaliado. Esta modalidade poderá ser oferecida como conteúdo possível para uma disciplina de licenciatura ou de programa de pós-graduação, desde que tal tenha as autorizações devidas no respectivo âmbito. Para todos os efeitos formais tudo se passará como se o aluno tivesse frequentado a disciplina original.');
-- -----------------------------
-- Data for table 'SEMINARY_THEME'
-- -----------------------------
delete from SEMINARY_THEME;
insert into SEMINARY_THEME values (1, 'O Mar','M','');
insert into SEMINARY_THEME values (2, 'O Sistema Produtivo','SP','');
insert into SEMINARY_THEME values (3, 'O Territorio','T','');

-- -----------------------------
-- Data for table 'SEMINARY_CASESTUDY'
-- -----------------------------
delete from SEMINARY_CASESTUDY;
insert into SEMINARY_CASESTUDY values (101, 'A evolução das capturas e frota de pesca em Portugal – stocks e sustentabilidade',NULL,'M.1',1);
insert into SEMINARY_CASESTUDY values (102, 'Modelização de recursos piscícolas',NULL,'M.2',1);
insert into SEMINARY_CASESTUDY values (103, 'Aplicabilidade das auto-estradas marítimas no comércio externo português',NULL,'M.3',1);
insert into SEMINARY_CASESTUDY values (104, 'Perspectivas de desenvolvimento do transporte marítimo de curta distância em Portugal',NULL,'M.4',1);
insert into SEMINARY_CASESTUDY values (105, 'Marina de Lisboa – America’s Cup: avaliação do potencial',NULL,'M.5',1);
insert into SEMINARY_CASESTUDY values (106, 'Modelização sustentável do sector marítimo turístico',NULL,'M.6',1);
insert into SEMINARY_CASESTUDY values (107, 'Modelação e monitorização na gestão do ambiente marinho',NULL,'M.7',1);
insert into SEMINARY_CASESTUDY values (108, 'Eutrofização na costa portuguesa',NULL,'M.8',1);
insert into SEMINARY_CASESTUDY values (109, 'Estratégias de gestão da linha de costa portuguesa.',NULL,'M.9',1);
insert into SEMINARY_CASESTUDY values (110, 'Avaliação dos riscos de poluição por hidrocarbonetos nas costas portuguesas.',NULL,'M.10',1);
insert into SEMINARY_CASESTUDY values (111, 'Estratégia de combate à poluição por hidrocarbonetos.',NULL,'M.11',1);
insert into SEMINARY_CASESTUDY values (112, 'Business Inteligence na área do controlo ambiental e segurança marítima',NULL,'M.12',1);
insert into SEMINARY_CASESTUDY values (113, 'Tecnologias para inspecção de estruturas costeiras',NULL,'M.13',1);
insert into SEMINARY_CASESTUDY values (114, 'Tecnologias para estudo e exploração do mar profundo',NULL,'M.14',1);
insert into SEMINARY_CASESTUDY values (115, 'Barreiras e potencial da energia das ondas em Portugal',NULL,'M.15',1);
insert into SEMINARY_CASESTUDY values (4, 'Caso 1 Tema 2',NULL,'T.1',2);
insert into SEMINARY_CASESTUDY values (5, 'Caso 2 Tema 2',NULL,'T.2',2);
insert into SEMINARY_CASESTUDY values (6, 'Caso 3 Tema 2',NULL,'T.3',2);
insert into SEMINARY_CASESTUDY values (7, 'Caso 1 Tema 3',NULL,'T.1',3);
insert into SEMINARY_CASESTUDY values (8, 'Caso 2 Tema 3',NULL,'T.2',3);
insert into SEMINARY_CASESTUDY values (9, 'Caso 3 Tema 3',NULL,'T.3',3);


-- -----------------------------
-- Data for table 'SEMINARY_CURRICULARCOURSE'
-- -----------------------------
delete from SEMINARY_CURRICULARCOURSE;
insert into SEMINARY_CURRICULARCOURSE values (1,1,666,1);
insert into SEMINARY_CURRICULARCOURSE values (2,1,666,2);
insert into SEMINARY_CURRICULARCOURSE values (3,1,666,3);
insert into SEMINARY_CURRICULARCOURSE values (4,1,701,1);
insert into SEMINARY_CURRICULARCOURSE values (5,1,701,2);

-- -----------------------------
-- Data for table 'EQUIVALENCY_THEME'
-- -----------------------------
delete from EQUIVALENCY_THEME;
insert into EQUIVALENCY_THEME values (1,1);
insert into EQUIVALENCY_THEME values (1,2);
insert into EQUIVALENCY_THEME values (1,3);
insert into EQUIVALENCY_THEME values (2,1);
insert into EQUIVALENCY_THEME values (2,2);
insert into EQUIVALENCY_THEME values (2,3);
insert into EQUIVALENCY_THEME values (2,4);
insert into EQUIVALENCY_THEME values (2,5);
insert into EQUIVALENCY_THEME values (3,1);
insert into EQUIVALENCY_THEME values (3,3);