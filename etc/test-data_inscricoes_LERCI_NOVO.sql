#------------------------------------------------------------------------------------------------------------------
# DADOS ESPECIFICOS LERCI:
#------------------------------------------------------------------------------------------------------------------

#-----------------------------
# Data for table 'CURRICULAR_YEAR'
#-----------------------------
# (ID_INTERNAL, YEAR)
;
delete from CURRICULAR_YEAR;
insert into CURRICULAR_YEAR values (1, 1);
insert into CURRICULAR_YEAR values (2, 2);
insert into CURRICULAR_YEAR values (3, 3);
insert into CURRICULAR_YEAR values (4, 4);
insert into CURRICULAR_YEAR values (5, 5);

#-----------------------------
# Data for table 'CURRICULAR_SEMESTER'
#-----------------------------
# (ID_INTERNAL, KEY_CURRICULAR_YEAR, SEMESTER)
;
delete from CURRICULAR_SEMESTER;
insert into CURRICULAR_SEMESTER values (1, 1, 1);
insert into CURRICULAR_SEMESTER values (2, 1, 2);
insert into CURRICULAR_SEMESTER values (3, 2, 1);
insert into CURRICULAR_SEMESTER values (4, 2, 2);
insert into CURRICULAR_SEMESTER values (5, 3, 1);
insert into CURRICULAR_SEMESTER values (6, 3, 2);
insert into CURRICULAR_SEMESTER values (7, 4, 1);
insert into CURRICULAR_SEMESTER values (8, 4, 2);
insert into CURRICULAR_SEMESTER values (9, 5, 1);
insert into CURRICULAR_SEMESTER values (10, 5, 2);

#-----------------------------
# Data for table 'DEPARTMENT'
#-----------------------------
# (ID_INTERNAL, NAME, CODE)
;
delete from DEPARTMENT;
insert into DEPARTMENT values (1, 'Departamento de Engenharia Informática', 'DEI');

#-----------------------------
# Data for table 'DEPARTMENT_COURSE'
#-----------------------------
# NOTAS:
# FALTA COLOCAR A INFORMAÇÃO DAS DISCIPLINAS DEPARTAMENTAIS CORESPONDENTES ÁS DISCIPLINAS CURRICULARES ABAIXO.
# (ID_INTERNAL, CODE, NAME, KEY_DEPARTMENT)
;
delete from DEPARTMENT_COURSE;
insert into DEPARTMENT_COURSE values (1, 'Disciplina Departamento', 'DD', 1);

#-----------------------------
# Data for table 'BRANCH'
#-----------------------------
# (ID_INTERNAL, BRANCH_CODE, BRANCH_NAME)
;
delete from BRANCH;
insert into BRANCH values (1, '', '');
insert into BRANCH values (2, 'AAGR', 'ÁREA DE ARQUITECTURA E GESTÃO DE REDES');
insert into BRANCH values (3, 'AAS', 'ÁREA DE APLICAÇÕES E SERVIÇOS');

#-----------------------------
# Data for table 'DEGREE'
#-----------------------------
# (ID_INTERNAL, CODE, NAME, TYPE_DEGREE)
;
delete from DEGREE;
insert into DEGREE values (1, 'LERCI', 'Licenciatura em Engenharia de Redes de Comunicação e de Informação', 1);

#-----------------------------
# Data for table 'DEGREE_CURRICULAR_PLAN'
#-----------------------------
# NOTAS:
# VERIFICAR OS VALORES DAS DATAS PARA OS CAMPOS 'INITIAL_YEAR' E 'END_YEAR'.
# (ID_INTERNAL, NAME, KEY_DEGREE, STATE, INITIAL_DATE, END_DATE)
;
delete from DEGREE_CURRICULAR_PLAN;
insert into DEGREE_CURRICULAR_PLAN values (1, 'Licenciatura em Engenharia de Redes de Comunicação e de Informação', 1, 1, '0000-00-00', '0000-00-00');

#-----------------------------
# Data for table 'STUDENT_CURRICULAR_PLAN'
#-----------------------------
# NOTAS:
# VERIFICAR O VALOR DA DATA PARA O CAMPO 'START_DATE'.
# VERIFICAR O VALOR DA CHAVE PARA ALUNO.
# (ID_INTERNAL, KEY_STUDENT, KEY_DEGREE_CURRICULAR_PLAN, CURRENT_STATE, START_DATE, KEY_BRANCH)
;
delete from STUDENT_CURRICULAR_PLAN;
insert into STUDENT_CURRICULAR_PLAN values (1, 1, 1, 1, '0000-00-00', 2);

#-----------------------------
# Data for table 'CURRICULAR_COURSE'
#-----------------------------
# NOTAS:
# VERIFICAR O VALOR DA CHAVE PARA DICIPLINA DEPARTAMENTO.
# VERIFICAR O VALOR DOS CAMPOS 'CREDITS' E 'CODE' PARA AS DISCIPLINAS DO SEGUNDO ANO PARA A FRENTE.
# (ID_INTERNAL, KEY_DEPARTMENT_COURSE, KEY_DEGREE_CURRICULAR_PLAN, CREDITS, THEORETICAL_HOURS, PRATICAL_HOURS, THEO_PRAT_HOURS, LAB_HOURS, NAME, CODE)

# PRIMEIRO ANO, PRIMEIRO SEMESTRE:
;
delete from CURRICULAR_COURSE;
insert into CURRICULAR_COURSE values (1, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "INTRODUÇÃO À PROGRAMAÇÃO", "IK");
insert into CURRICULAR_COURSE values (2, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "ANÁLISE MATEMÁTICA I", "PY");
insert into CURRICULAR_COURSE values (3, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "ÁLGEBRA LINEAR", "QN");
insert into CURRICULAR_COURSE values (4, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "SISTEMAS DIGITAIS", "TU");
insert into CURRICULAR_COURSE values (5, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "TEORIA DA COMPUTAÇÃO", "VI");
# PRIMEIRO ANO, SEGUNDO SEMESTRE:
;
insert into CURRICULAR_COURSE values (6, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "ALGORITMOS E ESTRUTURA DE DADOS", "01");
insert into CURRICULAR_COURSE values (7, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "ARQUITECTURA DE COMPUTADORES", "02");
insert into CURRICULAR_COURSE values (8, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "ANÁLISE MATEMÁTICA II", "P5");
insert into CURRICULAR_COURSE values (9, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "FÍSICA I - CURSO INFORMÁTICA", "A37");
insert into CURRICULAR_COURSE values (10, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "MATEMÁTICA COMPUTACIONAL", "AG7");
# SEGUNDO ANO, PRIMEIRO SEMESTRE:
;
insert into CURRICULAR_COURSE values (11, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "ANÁLISE MATEMÁTICA III", "");
insert into CURRICULAR_COURSE values (12, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "FÍSICA II", "");
insert into CURRICULAR_COURSE values (13, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "SISTEMAS OPERATIVOS", "");
insert into CURRICULAR_COURSE values (14, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "PROGRAMAÇÃO COM OBJECTOS", "");
insert into CURRICULAR_COURSE values (15, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "REDES DE COMPUTADORES I", "");
# SEGUNDO ANO, SEGUNDO SEMESTRE:
;
insert into CURRICULAR_COURSE values (16, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "ANÁLISE MATEMÁTICA IV", "");
insert into CURRICULAR_COURSE values (17, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "PROBABILIDADES E ESTATÍSTICA", "");
insert into CURRICULAR_COURSE values (18, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "COMPUTAÇÃO GRÁFICA", "");
insert into CURRICULAR_COURSE values (19, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "SINAIS E SISTEMAS", "");
insert into CURRICULAR_COURSE values (20, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "BASES DE DADOS", "");
# TERCEIRO ANO, PRIMEIRO SEMESTRE:
;
insert into CURRICULAR_COURSE values (21, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "ELECTRÓNICA I", "");
insert into CURRICULAR_COURSE values (22, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "FUNDAMENTOS DAS TELECOMUNICAÇÕES", "");
insert into CURRICULAR_COURSE values (23, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "SISTEMAS DISTRIBUÍDOS", "");
insert into CURRICULAR_COURSE values (24, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "REDES DE COMPUTADORES II", "");
insert into CURRICULAR_COURSE values (25, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "INTERFACES PESSOA-MÁQUINA", "");
insert into CURRICULAR_COURSE values (26, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "GESTÃO DE REDES E SISTEMAS DISTRIBUÍDOS", "");
# TERCEIRO ANO, SEGUNDO SEMESTRE:
;
insert into CURRICULAR_COURSE values (27, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "ELECTRÓNICA II", "");
insert into CURRICULAR_COURSE values (28, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "SISTEMAS EMBEBIDOS", "");
insert into CURRICULAR_COURSE values (29, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "PROPARAGÇÃO E ANTENAS", "");
insert into CURRICULAR_COURSE values (30, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "REDES COM INTEGRAÇÃO DE SERVIÇOS", "");
insert into CURRICULAR_COURSE values (31, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "INTELIGÊNCIA ARTIFICIAL", "");
insert into CURRICULAR_COURSE values (32, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "APLICAÇÕES EM REDES DE GRANDE ESCALA", "");
insert into CURRICULAR_COURSE values (33, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "COMPILADORES", "");
insert into CURRICULAR_COURSE values (34, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "MODELAÇÃO DE SISTEMAS DE INFORMAÇÃO", "");
# QUARTO ANO, PRIMEIRO SEMESTRE:
;
insert into CURRICULAR_COURSE values (35, 1, 1, 4.0, 3.0, 2.0, 0.0, 2.0, "SOFTWARE DE TELECOMUNICAÇÕES", "");
#insert into CURRICULAR_COURSE values (36, 1, 1, 4.0, 3.0, 2.0, 0.0, 2.0, "GESTÃO DE REDES E SISTEMAS DISTRIBUÍDOS", "");
;
insert into CURRICULAR_COURSE values (37, 1, 1, 4.0, 3.0, 0.0, 2.0, 2.0, "SEGURANÇA EM REDES", "");
insert into CURRICULAR_COURSE values (38, 1, 1, 4.0, 3.0, 1.0, 2.0, 2.0, "SISTEMAS DE TELECOMUNICAÇÕES", "");
insert into CURRICULAR_COURSE values (39, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "APLICAÇÕES PARA SISTEMAS EMBEBIDOS", "");
insert into CURRICULAR_COURSE values (40, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "ENGENHARIA DE SOFTWARE", "");
insert into CURRICULAR_COURSE values (41, 1, 1, 4.0, 3.0, 0.0, 2.0, 0.0, "GESTÃO DE PROJECTOS INFORMÁTICOS", "");
# QUARTO ANO, SEGUNDO SEMESTRE:
;
insert into CURRICULAR_COURSE values (42, 1, 1, 4.0, 3.0, 0.0, 2.0, 0.0, "REDES MÓVEIS E SEM FIOS", "");
insert into CURRICULAR_COURSE values (43, 1, 1, 4.0, 3.0, 0.0, 0.0, 0.0, "REDES DE ACESSO", "");
insert into CURRICULAR_COURSE values (44, 1, 1, 4.0, 3.0, 0.0, 3.0, 0.0, "COMUNICAÇÃO DE ÁUDIO E VÍDEO", "");
insert into CURRICULAR_COURSE values (45, 1, 1, 4.0, 3.0, 0.0, 2.0, 0.0, "PLANEAMENTO DE PROJECTO E REDES", "");
insert into CURRICULAR_COURSE values (46, 1, 1, 4.0, 3.0, 0.0, 2.0, 0.0, "COMPUTAÇÃO MÓVEL", "");
insert into CURRICULAR_COURSE values (47, 1, 1, 4.0, 3.0, 0.0, 2.0, 0.0, "PRODUÇÃO DE CONTEÚDOS MULTIMÉDIA", "");
insert into CURRICULAR_COURSE values (48, 1, 1, 4.0, 3.0, 0.0, 2.0, 0.0, "PROJECTO DE APLICAÇÕES E SERVIÇOS", "");
#insert into CURRICULAR_COURSE values (49, 1, 1, 4.0, 3.0, 0.0, 3.0, 0.0, "COMUNICAÇÃO DE ÁUDIO E VÍDEO", "");
;
insert into CURRICULAR_COURSE values (50, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "CARTEIRA PESSOAL", "");
# QUINTO ANO, PRIMEIRO SEMESTRE:
;
insert into CURRICULAR_COURSE values (51, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "OPÇÃO I", "");
insert into CURRICULAR_COURSE values (52, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "OPÇÃO II", "");
insert into CURRICULAR_COURSE values (53, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "TRABALHO FINAL DE CURSO I", "");
# QUINTO ANO, SEGUNDO SEMESTRE:
;
insert into CURRICULAR_COURSE values (54, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "ORAGANIZAÇÃO E GESTÃO DE EMPRESAS", "");
insert into CURRICULAR_COURSE values (55, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "OPÇÃO III", "");
insert into CURRICULAR_COURSE values (56, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "TRABALHO FINAL DE CURSO II", "");

#-----------------------------
# Data for table 'CURRICULAR_COURSE_SCOPE'
#-----------------------------
;
delete from CURRICULAR_COURSE_SCOPE;
#(ID_INTERNAL, KEY_CURRICULAR_SEMESTER, KEY_CURRICULAR_COURSE, KEY_BRANCH)
;
insert into CURRICULAR_COURSE_SCOPE  values (1, 1, 1, 1, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (2, 1, 2, 1, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (3, 1, 3, 1, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (4, 1, 4, 1, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (5, 1, 5, 1, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (6, 2, 6, 1, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (7, 2, 7, 1, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (8, 2, 8, 1, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (9, 2, 9, 1, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (10, 2, 10, 1, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (11, 3, 11, 1, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (12, 3, 12, 1, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (13, 3, 13, 1, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (14, 3, 14, 1, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (15, 3, 15, 1, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (16, 4, 16, 1, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (17, 4, 17, 1, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (18, 4, 18, 1, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (19, 4, 19, 1, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (20, 4, 20, 1, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (21, 5, 21, 2, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (22, 5, 22, 2, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (23, 5, 23, 2, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (24, 5, 24, 2, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (25, 5, 23, 3, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (26, 5, 24, 3, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (27, 5, 25, 3, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (28, 5, 26, 3, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (29, 6, 27, 2, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (30, 6, 28, 2, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (31, 6, 29, 2, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (32, 6, 30, 2, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (33, 6, 31, 3, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (34, 6, 32, 3, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (35, 6, 33, 3, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (36, 6, 34, 3, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (37, 7, 35, 2, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (38, 7, 26, 2, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (39, 7, 37, 2, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (40, 7, 38, 2, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (41, 7, 37, 3, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (42, 7, 39, 3, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (43, 7, 40, 3, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (44, 7, 41, 3, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (45, 8, 42, 2, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (46, 8, 43, 2, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (47, 8, 44, 2, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (48, 8, 45, 2, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (49, 8, 46, 3, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (50, 8, 47, 3, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (51, 8, 48, 3, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (52, 8, 44, 3, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (53, 5, 50, 2, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (54, 6, 50, 2, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (55, 7, 50, 2, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (56, 8, 50, 2, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (57, 5, 50, 3, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (58, 6, 50, 3, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (59, 7, 50, 3, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (60, 8, 50, 3, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (61, 9, 51, 2, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (62, 9, 51, 3, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (63, 9, 52, 2, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (64, 9, 52, 3, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (65, 9, 53, 2, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (66, 9, 53, 3, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (67, 10, 54, 2, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (68, 10, 54, 3, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (69, 10, 55, 2, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (70, 10, 55, 3, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (71, 10, 56, 2, 0.0, 0.0, 0.0, 0.0);
insert into CURRICULAR_COURSE_SCOPE  values (72, 10, 56, 3, 0.0, 0.0, 0.0, 0.0);

#-----------------------------
# Data for table 'ENROLMENT'
#-----------------------------
# (ID_INTERNAL, KEY_STUDENT_CURRICULAR_PLAN, KEY_CURRICULAR_COURSE, KEY_EXECUTION_PERIOD, STATE)
;
delete from ENROLMENT;
insert into ENROLMENT VALUES (1, 1, 1, 1, 1);
insert into ENROLMENT VALUES (2, 1, 2, 1, 1);
insert into ENROLMENT VALUES (3, 1, 3, 1, 1);
insert into ENROLMENT VALUES (4, 1, 4, 1, 1);
insert into ENROLMENT VALUES (5, 1, 5, 1, 1);

insert into ENROLMENT VALUES (6, 1, 6, 1, 1);
insert into ENROLMENT VALUES (7, 1, 7, 1, 1);
insert into ENROLMENT VALUES (8, 1, 8, 1, 1);
insert into ENROLMENT VALUES (9, 1, 9, 1, 1);
insert into ENROLMENT VALUES (10, 1, 10, 1, 1);

insert into ENROLMENT VALUES (11, 1, 11, 1, 2);
insert into ENROLMENT VALUES (12, 1, 12, 1, 2);
insert into ENROLMENT VALUES (13, 1, 13, 1, 2);
insert into ENROLMENT VALUES (14, 1, 14, 1, 2);
