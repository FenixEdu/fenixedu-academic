#-----------------------------
# Data for table 'BRANCH'
#-----------------------------
DELETE FROM BRANCH;
INSERT INTO BRANCH values (1, 'IA', 'Inteligencia Artificial');
INSERT INTO BRANCH values (2, 'II', 'Informatica Industrial');

#-----------------------------
# Data for table 'CURRICULAR_COURSE_BRANCH'
#-----------------------------
DELETE FROM CURRICULAR_COURSE_BRANCH;
INSERT INTO CURRICULAR_COURSE_BRANCH values (1, 1, 14);
INSERT INTO CURRICULAR_COURSE_BRANCH values (2, 2, 14);

#-----------------------------
# Data for table 'STUDENT_CURRICULAR_PLAN_BRANCH'
#-----------------------------
DELETE FROM STUDENT_CURRICULAR_PLAN_BRANCH;
INSERT INTO STUDENT_CURRICULAR_PLAN_BRANCH values (1, 1, 1);
INSERT INTO STUDENT_CURRICULAR_PLAN_BRANCH values (2, 2, 1);

#-----------------------------
# Data for table 'CURRICULAR_YEAR'
#-----------------------------
DELETE FROM CURRICULAR_YEAR;
INSERT INTO CURRICULAR_YEAR values (1, 1);
INSERT INTO CURRICULAR_YEAR values (2, 2);
INSERT INTO CURRICULAR_YEAR values (3, 3);
INSERT INTO CURRICULAR_YEAR values (4, 4);
INSERT INTO CURRICULAR_YEAR values (5, 5);

#-----------------------------
# Data for table 'CURRICULAR_SEMESTER'
#-----------------------------
DELETE FROM CURRICULAR_SEMESTER;
INSERT INTO CURRICULAR_SEMESTER values (1, 1, 1);
INSERT INTO CURRICULAR_SEMESTER values (2, 1, 2);

#-----------------------------
# Data for table 'CURRICULAR_COURSE_CURRICULAR_SEMESTER'
#-----------------------------
DELETE FROM CURRICULAR_COURSE_CURRICULAR_SEMESTER;
INSERT INTO CURRICULAR_COURSE_CURRICULAR_SEMESTER values (1, 1, 14);
INSERT INTO CURRICULAR_COURSE_CURRICULAR_SEMESTER values (2, 1, 15);

INSERT INTO CURRICULAR_COURSE_CURRICULAR_SEMESTER values (3, 1, 16);
INSERT INTO CURRICULAR_COURSE_CURRICULAR_SEMESTER values (4, 1, 17);
INSERT INTO CURRICULAR_COURSE_CURRICULAR_SEMESTER values (5, 1, 18);
INSERT INTO CURRICULAR_COURSE_CURRICULAR_SEMESTER values (6, 2, 19);
INSERT INTO CURRICULAR_COURSE_CURRICULAR_SEMESTER values (7, 2, 20);
INSERT INTO CURRICULAR_COURSE_CURRICULAR_SEMESTER values (8, 2, 21);
INSERT INTO CURRICULAR_COURSE_CURRICULAR_SEMESTER values (9, 2, 22);
INSERT INTO CURRICULAR_COURSE_CURRICULAR_SEMESTER values (10, 2, 23);

#-----------------------------
# Data for table 'EQUIVALENCE'
#-----------------------------
DELETE FROM EQUIVALENCE;
INSERT INTO EQUIVALENCE VALUES (1, 2, 1, 1);

#------------------------------------------------------------------------------------------------------------------

#-----------------------------
# Data for table 'ENROLMENT'
#-----------------------------
DELETE FROM ENROLMENT;
INSERT INTO ENROLMENT VALUES (1, 3, 14);
INSERT INTO ENROLMENT VALUES (2, 1, 14);

#-----------------------------
# Data for table 'STUDENT_CURRICULAR_PLAN'
#-----------------------------
DELETE FROM STUDENT_CURRICULAR_PLAN;
INSERT INTO STUDENT_CURRICULAR_PLAN VALUES (1, 1, 1, 1, '2002-12-21');
INSERT INTO STUDENT_CURRICULAR_PLAN VALUES (2, 1, 1, 2, '2002-10-21');
INSERT INTO STUDENT_CURRICULAR_PLAN VALUES (3, 5, 1, 1, '2002-10-21');

#-----------------------------
# Data for table 'DEPARTMENT'
#-----------------------------
DELETE FROM DEPARTMENT;
INSERT INTO DEPARTMENT VALUES (1, 'dep1', 'd1');
INSERT INTO DEPARTMENT VALUES (2, 'dep10', 'd10');

#-----------------------------
# Data for table 'DEPARTMENT_COURSE'
#-----------------------------
DELETE FROM DEPARTMENT_COURSE;
INSERT INTO DEPARTMENT_COURSE VALUES (1, 'Engenharia da Programacao', 'ep', 1);
INSERT INTO DEPARTMENT_COURSE VALUES (2, 'Nao sei', 'mvt', 1);

#-----------------------------
# Data for table 'DEGREE_CURRICULAR_PLAN'
#-----------------------------
DELETE FROM DEGREE_CURRICULAR_PLAN;
INSERT INTO DEGREE_CURRICULAR_PLAN VALUES (1, 'plano1', 8, 1, '0000-00-00', '0000-00-00');
INSERT INTO DEGREE_CURRICULAR_PLAN VALUES (2, 'plano2', 9, 1, '0000-00-00', '0000-00-00');

#-----------------------------
# Data for table 'DEGREE'
#-----------------------------
DELETE FROM DEGREE;
INSERT INTO DEGREE VALUES (8,'LEIC','Licenciatura de Engenharia Informatica e de Computadores',1);
INSERT INTO DEGREE VALUES (9,'LEEC','Licenciatura de Engenharia Electrotecnica e de Computadores',2);
INSERT INTO DEGREE VALUES (10,'MIC','Mestrado em Informatica e Computadores',2);

#-----------------------------
# Data for table 'CURRICULAR_COURSE'
#-----------------------------
DELETE FROM CURRICULAR_COURSE;
#INSERT INTO CURRICULAR_COURSE VALUES (14,1,1,0,0,0,0,0,2,1,'Trabalho Final de Curso I','TFCI');
#INSERT INTO CURRICULAR_COURSE VALUES (15,1,2,0,0,0,0,0,5,2,'Trabalho Final de Curso II','TFCII');
#INSERT INTO CURRICULAR_COURSE VALUES (16,2,1,0,0,0,0,0,1,1,'Introducao a Programacao','IP');
#INSERT INTO CURRICULAR_COURSE VALUES (17,2,2,0,0,0,0,0,2,1,'Programacao com Objectos','PO');
#INSERT INTO CURRICULAR_COURSE VALUES (18,1,1,0,0,0,0,0,3,1,'Redes de Computadores I','RCI');
#INSERT INTO CURRICULAR_COURSE VALUES (19,1,2,0,0,0,0,0,4,1,'Engenharia da Programacao','EP');
#INSERT INTO CURRICULAR_COURSE VALUES (20,2,1,1,0,0,0,0,1,2,'Arquitecturas de Computadores','AC');
#INSERT INTO CURRICULAR_COURSE VALUES (21,2,2,0,0,0,0,0,2,2,'Compiladores','COMP');
#INSERT INTO CURRICULAR_COURSE VALUES (22,1,2,0,0,0,0,0,3,2,'Redes de Computadores II','RCII');
#INSERT INTO CURRICULAR_COURSE VALUES (23,2,1,0,0,0,0,0,4,2,'Aprendizagem','APR');

INSERT INTO CURRICULAR_COURSE VALUES (14,1,1,0,0,0,0,0,'Trabalho Final de Curso I','TFCI');
INSERT INTO CURRICULAR_COURSE VALUES (15,1,2,0,0,0,0,0,'Trabalho Final de Curso II','TFCII');
INSERT INTO CURRICULAR_COURSE VALUES (16,2,1,0,0,0,0,0,'Introducao a Programacao','IP');
INSERT INTO CURRICULAR_COURSE VALUES (17,2,2,0,0,0,0,0,'Programacao com Objectos','PO');
INSERT INTO CURRICULAR_COURSE VALUES (18,1,1,0,0,0,0,0,'Redes de Computadores I','RCI');
INSERT INTO CURRICULAR_COURSE VALUES (19,1,2,0,0,0,0,0,'Engenharia da Programacao','EP');
INSERT INTO CURRICULAR_COURSE VALUES (20,2,1,1,0,0,0,0,'Arquitecturas de Computadores','AC');
INSERT INTO CURRICULAR_COURSE VALUES (21,2,2,0,0,0,0,0,'Compiladores','COMP');
INSERT INTO CURRICULAR_COURSE VALUES (22,1,2,0,0,0,0,0,'Redes de Computadores II','RCII');
INSERT INTO CURRICULAR_COURSE VALUES (23,2,1,0,0,0,0,0,'Aprendizagem','APR');
