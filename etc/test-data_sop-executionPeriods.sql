
#-----------------------------
# Data for table 'EXECUTION_COURSE'
#-----------------------------
INSERT INTO EXECUTION_COURSE VALUES (34,'Arquitecturas de Computadores','AC',1,0,0,0, 1, 3,"");
INSERT INTO EXECUTION_COURSE VALUES (35,'Para apagar','PA',1,0,0,0, 1, 3,"");

#-----------------------------
# Data for table 'CURRICULAR_COURSE_EXECUTION_COURSE'
#-----------------------------
INSERT INTO CURRICULAR_COURSE_EXECUTION_COURSE VALUES (45,20,34);

#-----------------------------
# Data for table 'EXECUTION_DEGREE'
#-----------------------------
INSERT INTO EXECUTION_DEGREE VALUES (14, 2, 1, 7, 1, 1);

#-----------------------------
# Data for table 'CURRICULAR_COURSE_SCOPE'
#   (ID_INTERNAL, KEY_CURRICULAR_SEMESTER, KEY_CURRICULAR_COURSE, KEY_BRANCH) 
#-----------------------------
INSERT INTO CURRICULAR_COURSE_SCOPE values (8, 1, 20, 1,0,0,0,0, 0, 2, 1, 1);