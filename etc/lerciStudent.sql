--
-- Data for table 'PERSON'
--
DELETE FROM PERSON;
INSERT INTO PERSON VALUES (1, 10, 'Lisboa', "2002-10-12", "2005-11-1",'Estudante',
                           "1979-5-12", 'Manuel Fiado', 'Maria Fiado', 'Portuguesa',
                           'Freguesia Fiado', 'Concelho Fiado', 'Distrito Fiado', 'Morada Fiado', 'localidade Fiado',
                           '1700-200', 'l200', 'frequesia morada Sousa', 'concelho morada Fiado',
                           'distrito morada Fiado', '10', '965463210', 'fiado@ist.pt','www.fiado.pt',
                           '218894433', 'Profissao', 'stdnt',MD5("pass"), 63,
                           '918763321', 2, 1, 1);      

--
-- Data for table 'STUDENT'
--
DELETE FROM STUDENT;
INSERT INTO STUDENT VALUES (1, 600, 1, 567, 1, 1);

DELETE FROM STUDENT_GROUP_INFO;
INSERT INTO STUDENT_GROUP_INFO VALUES(1,1,1,1,1);

--
-- Data for table 'ROLE'
--
delete from ROLE;
insert into ROLE (ID_INTERNAL, ROLE_TYPE, PORTAL_SUB_APPLICATION, PAGE, PAGE_NAME_PROPERTY) values (1,1,'/person','/index.do','portal.person');
insert into ROLE (ID_INTERNAL, ROLE_TYPE, PORTAL_SUB_APPLICATION, PAGE, PAGE_NAME_PROPERTY) values (2,2,'/student','/index.do','portal.student');
insert into ROLE (ID_INTERNAL, ROLE_TYPE, PORTAL_SUB_APPLICATION, PAGE, PAGE_NAME_PROPERTY) values (3,3,'/teacher','/index.do','portal.teacher');
insert into ROLE (ID_INTERNAL, ROLE_TYPE, PORTAL_SUB_APPLICATION, PAGE, PAGE_NAME_PROPERTY) values (4,4,'/sop','/paginaPrincipal.jsp','portal.sop');
insert into ROLE (ID_INTERNAL, ROLE_TYPE, PORTAL_SUB_APPLICATION, PAGE, PAGE_NAME_PROPERTY) values (5,5,'/candidato','/index.do','portal.candidate');
insert into ROLE (ID_INTERNAL, ROLE_TYPE, PORTAL_SUB_APPLICATION, PAGE, PAGE_NAME_PROPERTY) values (6,6,'/posGraduacao','/index.do','portal.masterDegree');
insert into ROLE (ID_INTERNAL, ROLE_TYPE, PORTAL_SUB_APPLICATION, PAGE, PAGE_NAME_PROPERTY) values (7,7,'/treasury','/index.do','portal.treasury');
insert into ROLE (ID_INTERNAL, ROLE_TYPE, PORTAL_SUB_APPLICATION, PAGE, PAGE_NAME_PROPERTY) values (8,8,'/coordinator','/index.do','portal.coordinator');


--
-- Data for table 'PERSON_ROLE'
--
delete from PERSON_ROLE;
insert into PERSON_ROLE (ID_INTERNAL, KEY_ROLE, KEY_PERSON) values (1,1,1);
insert into PERSON_ROLE (ID_INTERNAL, KEY_ROLE, KEY_PERSON) values (2,2,1);

                           
