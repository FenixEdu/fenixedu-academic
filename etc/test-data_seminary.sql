-- -----------------------------
-- Data for table 'SEMINARY'
-- -----------------------------
delete from SEMINARY;
insert into SEMINARY values (1, 'Desenvolvimento Sustentavel','Ano lectivo de 2003 / 2004');
insert into SEMINARY values (2, 'Clonagem humana a altas temperaturas','Ano 2042, orador: Gonçalo Mengel Luiz');

-- -----------------------------
-- Data for table 'SEMINARY_MODALITY'
-- -----------------------------
delete from SEMINARY_MODALITY;
insert into SEMINARY_MODALITY values (1, 'Completa','O Aluno esta tramado');
insert into SEMINARY_MODALITY values (2, 'Seminario com trabalho','O Aluno esta quase tramado');
insert into SEMINARY_MODALITY values (3, 'Seminario','O Aluno nao esta esta tramado');

-- -----------------------------
-- Data for table 'SEMINARY_THEME'
-- -----------------------------
delete from SEMINARY_THEME;
insert into SEMINARY_THEME values (1, 'O Mar','Pescas e essas coisas',1);
insert into SEMINARY_THEME values (2, 'O Sistema Produtivo','Coisa que nao existe em Portugal',1);
insert into SEMINARY_THEME values (3, 'O Territorio','Ordenacao do Territorio',1);

-- -----------------------------
-- Data for table 'SEMINARY_CASESTUDY'
-- -----------------------------
delete from SEMINARY_CASESTUDY;
insert into SEMINARY_CASESTUDY values (1, 'Politicas locais de mobilidade','comparacao entre Evora e Montpellier','T.1',1);
insert into SEMINARY_CASESTUDY values (2, 'As promessas da tecnologia','o automovel com pilha de combustivel','T.2',1);
insert into SEMINARY_CASESTUDY values (3, 'Sistemas inteligentes de transportes',NULL,'T.3',1);

-- -----------------------------
-- Data for table 'SEMINARY_CANDIDACY'
-- -----------------------------
delete from SEMINARY_CANDIDACY;
insert into SEMINARY_CANDIDACY values (1,1,1,1,1,1);
insert into SEMINARY_CANDIDACY values (2,1,2,1,1,2);
insert into SEMINARY_CANDIDACY values (3,2,3,1,1,3);

-- -----------------------------
-- Data for table 'SEMINARYCANDIDACY_SEMINARYCASESTUDY'
-- -----------------------------
delete from SEMINARYCANDIDACY_SEMINARYCASESTUDY;
insert into SEMINARYCANDIDACY_SEMINARYCASESTUDY values (1,1,1);
insert into SEMINARYCANDIDACY_SEMINARYCASESTUDY values (1,2,2);
insert into SEMINARYCANDIDACY_SEMINARYCASESTUDY values (1,3,3);
insert into SEMINARYCANDIDACY_SEMINARYCASESTUDY values (2,1,1);
insert into SEMINARYCANDIDACY_SEMINARYCASESTUDY values (2,2,2);
insert into SEMINARYCANDIDACY_SEMINARYCASESTUDY values (2,3,3);

-- -----------------------------
-- Data for table 'SEMINARY_CURRICULARCOURSE'
-- -----------------------------
delete from SEMINARY_CURRICULARCOURSE;
insert into SEMINARY_CURRICULARCOURSE values (1,1,1);
insert into SEMINARY_CURRICULARCOURSE values (1,1,2);
insert into SEMINARY_CURRICULARCOURSE values (1,1,3);
insert into SEMINARY_CURRICULARCOURSE values (1,2,1);
insert into SEMINARY_CURRICULARCOURSE values (1,2,2);
insert into SEMINARY_CURRICULARCOURSE values (2,1,1);
