-- -----------------------------
-- Data for table 'SEMINARY'
-- -----------------------------
delete from SEMINARY;
insert into SEMINARY values (1, 'Desenvolvimento Sustentavel','Ano lectivo de 2003 / 2004',1);

-- -----------------------------
-- Data for table 'SEMINARY_MODALITY'
-- -----------------------------
delete from SEMINARY_MODALITY;
insert into SEMINARY_MODALITY values (1, 'Completa','O aluno frequenta os três Seminários com cerca de 50 horas lectivas e elabora um trabalho de grupo de estudo dum caso, que será discutido e avaliado. Esta modalidade poderá ser oferecida como conteúdo possível para uma disciplina de licenciatura ou de programa de pós-graduação, desde que tal tenha as autorizações devidas no respectivo âmbito. Para todos os efeitos formais tudo se passará como se o aluno tivesse frequentado a disciplina original.');
insert into SEMINARY_MODALITY values (2, 'Seminário com Trabalho','O aluno frequenta um seminário com cerca de 15 a 17 horas lectivas e elabora um trabalho de grupo de estudo dum caso, que será discutido e avaliado. Esta modalidade poderá ser oferecida como conteúdo possível de um Seminário de pós-graduação ou de parte de uma disciplina de licenciatura ou programa de pós-graduação, desde que tal tenha as autorizações devidas no respectivo âmbito. De novo, tudo se passará formalmente como no contexto da disciplina ou Seminário originais.');
insert into SEMINARY_MODALITY values (3, 'Seminário','O aluno frequentará a parte lectiva de um seminário, como alternativa a um dos Seminários de Humanidades de Engenharia Civil, contando para o portfolio de Engenharia Informática, ou para efeitos semelhantes noutros eventuais cursos.');

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
insert into SEMINARY_CURRICULARCOURSE values (1,1,3842,2); 
insert into SEMINARY_CURRICULARCOURSE values (2,1,3841,2);
insert into SEMINARY_CURRICULARCOURSE values (3,1,3815,2);
insert into SEMINARY_CURRICULARCOURSE values (4,1,3769,2);
insert into SEMINARY_CURRICULARCOURSE values (5,1,3528,2);
insert into SEMINARY_CURRICULARCOURSE values (6,1,3418,2);
insert into SEMINARY_CURRICULARCOURSE values (8,1,2939,1);
insert into SEMINARY_CURRICULARCOURSE values (9,1,2859,1);
insert into SEMINARY_CURRICULARCOURSE values (11,1,3078,1);
insert into SEMINARY_CURRICULARCOURSE values (15,1,3631,3);
insert into SEMINARY_CURRICULARCOURSE values (16,1,3080,2);
insert into SEMINARY_CURRICULARCOURSE values (17,1,2673,1);
insert into SEMINARY_CURRICULARCOURSE values (18,1,3476,1);
insert into SEMINARY_CURRICULARCOURSE values (20,1,3376,1);
insert into SEMINARY_CURRICULARCOURSE values (22,1,4464,2);
insert into SEMINARY_CURRICULARCOURSE values (23,1,4489,1);
insert into SEMINARY_CURRICULARCOURSE values (24,1,4489,1);
insert into SEMINARY_CURRICULARCOURSE values (25,1,4749,2);
insert into SEMINARY_CURRICULARCOURSE values (29,1,4672,1);

-- -----------------------------
-- Data for table 'EQUIVALENCY_THEME'
-- -----------------------------
delete from EQUIVALENCY_THEME;
insert into EQUIVALENCY_THEME values (1,1);
insert into EQUIVALENCY_THEME values (2,1);
insert into EQUIVALENCY_THEME values (3,1);
insert into EQUIVALENCY_THEME values (1,2);
insert into EQUIVALENCY_THEME values (2,2);
insert into EQUIVALENCY_THEME values (3,2);
insert into EQUIVALENCY_THEME values (1,3);
insert into EQUIVALENCY_THEME values (2,3);
insert into EQUIVALENCY_THEME values (3,3);
insert into EQUIVALENCY_THEME values (2,4);
insert into EQUIVALENCY_THEME values (2,5);
insert into EQUIVALENCY_THEME values (1,6);
insert into EQUIVALENCY_THEME values (2,6);
insert into EQUIVALENCY_THEME values (3,6);
insert into EQUIVALENCY_THEME values (1,8);
insert into EQUIVALENCY_THEME values (2,8);
insert into EQUIVALENCY_THEME values (3,8);
insert into EQUIVALENCY_THEME values (1,9);
insert into EQUIVALENCY_THEME values (2,9);
insert into EQUIVALENCY_THEME values (3,9);
insert into EQUIVALENCY_THEME values (1,11);
insert into EQUIVALENCY_THEME values (2,11);
insert into EQUIVALENCY_THEME values (3,11);
insert into EQUIVALENCY_THEME values (1,15);
insert into EQUIVALENCY_THEME values (2,15);
insert into EQUIVALENCY_THEME values (3,15);
insert into EQUIVALENCY_THEME values (1,16);
insert into EQUIVALENCY_THEME values (2,16);
insert into EQUIVALENCY_THEME values (3,16);
insert into EQUIVALENCY_THEME values (1,17);
insert into EQUIVALENCY_THEME values (2,17);
insert into EQUIVALENCY_THEME values (3,17);
insert into EQUIVALENCY_THEME values (1,18);
insert into EQUIVALENCY_THEME values (2,18);
insert into EQUIVALENCY_THEME values (3,18);
insert into EQUIVALENCY_THEME values (1,20);
insert into EQUIVALENCY_THEME values (2,20);
insert into EQUIVALENCY_THEME values (3,20);
insert into EQUIVALENCY_THEME values (1,25);
insert into EQUIVALENCY_THEME values (3,25);
insert into EQUIVALENCY_THEME values (1,29);
insert into EQUIVALENCY_THEME values (2,29);
insert into EQUIVALENCY_THEME values (3,29);


#47676 - ambiente
#47629 - aero


