
-- -----------------------------
-- Data for table 'SEMINARY'
-- -----------------------------
delete from SEMINARY;
insert into SEMINARY values (1, 'Desenvolvimento Sustentável','Ano lectivo de 2003 / 2004',1);

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
insert into SEMINARY_CASESTUDY values (201, 'Políticas locais de mobilidade – comparação entre Évora e Montpellier',NULL,'T.1',2);
insert into SEMINARY_CASESTUDY values (202, 'As promessas da tecnologia – o automóvel com pilha de combustível',NULL,'T.2',2);
insert into SEMINARY_CASESTUDY values (203, 'Sistemas inteligentes de transportes',NULL,'T.3',2);
insert into SEMINARY_CASESTUDY values (204, 'Sistemas de bilhética inteligente',NULL,'T.4',2);
insert into SEMINARY_CASESTUDY values (205, 'As TI’s como factor de inclusão social',NULL,'T.5',2);
insert into SEMINARY_CASESTUDY values (206, 'Estudo da relação entre a pressão humana e serviços de ecossistema em Portugal enquadrado pelo Millenium Ecosystem Assessment',NULL,'T.6',2);
insert into SEMINARY_CASESTUDY values (207, 'Avaliação do potencial de integração de energia solar fotovoltaica em edifícios em Portugal',NULL,'T.7',2);
insert into SEMINARY_CASESTUDY values (208, 'Edifícios inteligentes',NULL,'T.8',2);
insert into SEMINARY_CASESTUDY values (209, 'A pegada ecológica – a diversidade nacional: comparação entre o Minho e o Algarve',NULL,'T.9',2);
insert into SEMINARY_CASESTUDY values (210, 'O espaço urbano do estuário do Tejo – ligações entre as duas margens',NULL,'T.10',2);
insert into SEMINARY_CASESTUDY values (211, 'O mosaico urbano da Cova da Beira',NULL,'T.11',2);
insert into SEMINARY_CASESTUDY values (212, 'A inserção do porto de Setúbal na cidade',NULL,'T.12',2);
insert into SEMINARY_CASESTUDY values (213, 'Os recursos hídricos em Portugal. Exploração sustentável',NULL,'T.13',2);
insert into SEMINARY_CASESTUDY values (214, 'Indicadores de desertificação. Margem esquerda do Guadiana',NULL,'T.14',2);

insert into SEMINARY_CASESTUDY values (301, 'Estratégias de optimização dos processos de recuperação e reciclagem de metais ferrosos e não ferrosos provenientes de VFV (veículos em fim de vida), REEE (resíduos de equipamentos eléctricos e electrónicos) e outros resíduos fragmentados',NULL,'SP.1',3);
insert into SEMINARY_CASESTUDY values (302, 'Estratégias para o desenvolvimento da indústria fotovoltaica em Portugal',NULL,'SP.2',3);
insert into SEMINARY_CASESTUDY values (303, 'Estratégias de reciclagem para polímeros e materiais compósitos indiferenciados',NULL,'SP.3',3);
insert into SEMINARY_CASESTUDY values (304, 'Estratégias de desenvolvimentos da gestão de resíduos sólidos em Portugal – o contributo de uma Bolsa de Resíduos',NULL,'SP.4',3);
insert into SEMINARY_CASESTUDY values (305, 'Sistema de informação para apoio à gestão de VFV em Portugal',NULL,'SP.5',3);
insert into SEMINARY_CASESTUDY values (306, 'Potencialidades e limitações da separação mecânica de resíduos sólidos urbanos (RSU)',NULL,'SP.6',3);
insert into SEMINARY_CASESTUDY values (307, 'O sistema ponto verde – balanço.',NULL,'SP.7',3);
insert into SEMINARY_CASESTUDY values (308, 'O hidrogénio como vector energético',NULL,'SP.8',3);
insert into SEMINARY_CASESTUDY values (309, 'Certificados verdes. Perspectiva de impacto futuro.',NULL,'SP.9',3);
insert into SEMINARY_CASESTUDY values (310, 'Emissões na indústria cimenteira-perspectivas futuras',NULL,'SP.10',3);
insert into SEMINARY_CASESTUDY values (311, 'O ciclo da floresta em Portugal',NULL,'SP.11',3);
insert into SEMINARY_CASESTUDY values (312, 'Impacte socio-económico e ambiental da Fusão Nuclear',NULL,'SP.12',3);
insert into SEMINARY_CASESTUDY values (313, 'Tratamento de resíduos e produção de energia.',NULL,'SP.13',3);


-- -----------------------------
-- Data for table 'SEMINARY_CURRICULARCOURSE'
-- -----------------------------

insert into CURRICULAR_COURSE (id_internal,name, code, key_degree_curricular_plan) values
	(4918,"COMPLEMENTOS DE ENGENHARIA NAVAL II","A2J",113),
	(4919,"COMPLEMENTOS DE ENGENHARIA NAVAL I","A2I",113),
	(4920,"Seminários em Humanidades","##1",51),
	(4921,"Opção Livre","##2",48),
	(4922,"DESENVOLVIMENTO SUSTENTÁVEL","B1S",108),
	(4923,"DESENVOLVIMENTO SUSTENTÁVEL DO DEG","##3",88),
	(4924,"Opção","##4",91),
	(4925,"Desenvolvimento Sustentável e de Inovação","##5",119),
	(4926,"Opção (2º Semestre)","##6",127);

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
insert into SEMINARY_CURRICULARCOURSE values (25,1,4749,2);
insert into SEMINARY_CURRICULARCOURSE values (29,1,4672,1);

insert into SEMINARY_CURRICULARCOURSE values (7,1,4918,1);
insert into SEMINARY_CURRICULARCOURSE values (33,1,4919,1);
insert into SEMINARY_CURRICULARCOURSE values (10,1,4920,2);
insert into SEMINARY_CURRICULARCOURSE values (12,1,4921,1);
insert into SEMINARY_CURRICULARCOURSE values (13,1,4922,1);
insert into SEMINARY_CURRICULARCOURSE values (14,1,4923,1);
insert into SEMINARY_CURRICULARCOURSE values (19,1,4924,1);
insert into SEMINARY_CURRICULARCOURSE values (24,1,4925,1);
insert into SEMINARY_CURRICULARCOURSE values (26,1,4558,2);
insert into SEMINARY_CURRICULARCOURSE values (27,1,4552,1);
insert into SEMINARY_CURRICULARCOURSE values (28,1,4655,1);
insert into SEMINARY_CURRICULARCOURSE values (31,1,4658,1);
insert into SEMINARY_CURRICULARCOURSE values (32,1,4651,1);
insert into SEMINARY_CURRICULARCOURSE values (30,1,4926,1);



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
insert into EQUIVALENCY_THEME values (1,7);
insert into EQUIVALENCY_THEME values (2,7);
insert into EQUIVALENCY_THEME values (3,7);
insert into EQUIVALENCY_THEME values (1,33);
insert into EQUIVALENCY_THEME values (2,33);
insert into EQUIVALENCY_THEME values (3,33);
insert into EQUIVALENCY_THEME values (1,10);
insert into EQUIVALENCY_THEME values (2,10);
insert into EQUIVALENCY_THEME values (3,10);
insert into EQUIVALENCY_THEME values (1,12);
insert into EQUIVALENCY_THEME values (2,12);
insert into EQUIVALENCY_THEME values (3,12);
insert into EQUIVALENCY_THEME values (1,13);
insert into EQUIVALENCY_THEME values (2,13);
insert into EQUIVALENCY_THEME values (3,13);
insert into EQUIVALENCY_THEME values (1,14);
insert into EQUIVALENCY_THEME values (2,14);
insert into EQUIVALENCY_THEME values (3,14);
insert into EQUIVALENCY_THEME values (1,19);
insert into EQUIVALENCY_THEME values (2,19);
insert into EQUIVALENCY_THEME values (3,19);
insert into EQUIVALENCY_THEME values (1,24);
insert into EQUIVALENCY_THEME values (2,24);
insert into EQUIVALENCY_THEME values (3,24);
insert into EQUIVALENCY_THEME values (1,26);
insert into EQUIVALENCY_THEME values (2,26);
insert into EQUIVALENCY_THEME values (3,26);
insert into EQUIVALENCY_THEME values (1,27);
insert into EQUIVALENCY_THEME values (2,27);
insert into EQUIVALENCY_THEME values (3,27);
insert into EQUIVALENCY_THEME values (1,28);
insert into EQUIVALENCY_THEME values (2,28);
insert into EQUIVALENCY_THEME values (3,28);
insert into EQUIVALENCY_THEME values (1,31);
insert into EQUIVALENCY_THEME values (2,31);
insert into EQUIVALENCY_THEME values (3,31);
insert into EQUIVALENCY_THEME values (1,32);
insert into EQUIVALENCY_THEME values (2,32);
insert into EQUIVALENCY_THEME values (3,32);
insert into EQUIVALENCY_THEME values (1,30);
insert into EQUIVALENCY_THEME values (2,30);
insert into EQUIVALENCY_THEME values (3,30);


#47676 - ambiente
#47629 - aero
#49921 - quimica