alter table seminary add enrollment_begin_time time;
alter table seminary add enrollment_begin_date date;
alter table seminary add enrollment_end_time time;
alter table seminary add enrollment_end_date date;
alter table seminary add has_theme int;
alter table seminary add has_case_study int;
alter table seminary_modality drop key name;

update seminary set
	has_theme = 1,
	has_case_study = 1,
	enrollment_begin_date= '2003-9-1',
	enrollment_begin_time = '0:0:0',
	enrollment_end_date = '2003-9-2',
	enrollment_end_time = '23:59:59'
	where id_internal=1;


#select * from teacher,person,role,person_role WHERE
#role.id_internal = 20 AND
#person_role.key_role = role.id_internal AND
#person_role.key_person = person.id_internal AND
#teacher.key_person = person.id_internal




insert into seminary(id_internal,has_theme,has_case_study,name,description,allowed_candidacies_per_student,enrollment_begin_time,enrollment_begin_date,enrollment_end_time,enrollment_end_date,ACK_OPT_LOCK) 
             values (2,0,0,'Inovação','Seminários Sobre Inovação',1,'0:0:0','2003-1-31','23:59','2003-2-23',1);
insert into seminary_modality(id_internal,name,description,ACK_OPT_LOCK)  values(4,'Completa','O aluno frequenta os Seminários com cerca de 45 horas lectivas e elabora um trabalho de grupo de estudo dum caso, que será discutido e avaliado. Esta modalidade poderá ser oferecida como conteúdo possível para uma disciplina de licenciatura ou de programa de pós-graduação, desde que tal tenha as autorizações devidas no respectivo âmbito. Para todos os efeitos formais tudo se passará como se o aluno tivesse frequentado a disciplina original.',1);
insert into seminary_modality(id_internal,name,description,ACK_OPT_LOCK)  values(5,'Seminário','O aluno frequentará um conjunto de pelo menos 7 sessões lectivas, como alternativa a um dos Seminários   de Humanidades de Engenharia Civil, contando para o portfolio de Engenharia Informática, ou para efeitos semelhantes noutros eventuais cursos.',1);

insert into SEMINARY_CURRICULARCOURSE values (36,2,2819,4,1);
insert into SEMINARY_CURRICULARCOURSE values (37,2,10994,4,1);
insert into SEMINARY_CURRICULARCOURSE values (38,2,3806,4,1);
insert into SEMINARY_CURRICULARCOURSE values (39,2,3338,4,1);
insert into SEMINARY_CURRICULARCOURSE values (40,2,4924,4,1);
insert into SEMINARY_CURRICULARCOURSE values (41,2,4920,5,1);
insert into SEMINARY_CURRICULARCOURSE values (42,2,3633,5,1);
insert into SEMINARY_CURRICULARCOURSE values (43,2,3395,4,1);
insert into SEMINARY_CURRICULARCOURSE values (44,2,4918,4,1);
insert into SEMINARY_CURRICULARCOURSE values (45,2,2951,4,1);
insert into SEMINARY_CURRICULARCOURSE values (46,2,2800,4,1);
insert into SEMINARY_CURRICULARCOURSE values (47,2,2804,4,1);
insert into SEMINARY_CURRICULARCOURSE values (48,2,4747,5,1);

insert into person_role values (47000,20,1761,1);
insert into person_role values (47001,20,934,1);
insert into person_role values (47002,20,291,1);
insert into person_role values (47003,20,418,1);