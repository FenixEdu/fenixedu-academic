-- -----------------------------
-- Data for table 'DEPARTMENT'
-- (ID_INTERNAL, NAME, CODE)
-- -----------------------------
delete from DEPARTMENT;
insert into DEPARTMENT values (1, 'Departamento de Engenharia Quimica', 'DEQ');

-- -----------------------------
-- Data for table 'DEPARTMENT_COURSE'
-- -----------------------------
-- (ID_INTERNAL, CODE, NAME, KEY_DEPARTMENT)
delete from DEPARTMENT_COURSE;
insert into DEPARTMENT_COURSE values (1, 'Disciplina Departamento', 'DD', 1);

-- -----------------------------
-- Data for table 'BRANCH'
-- (ID_INTERNAL, BRANCH_CODE, BRANCH_NAME)
-- -----------------------------
delete from BRANCH;
insert into BRANCH values (1, '', '');

-- -----------------------------
-- Data for table 'DEGREE'
-- (ID_INTERNAL, CODE, NAME, TYPE_DEGREE)
-- -----------------------------
delete from DEGREE;
insert into DEGREE values (1, 'LEQ', 'Licenciatura em Engenharia Quimica', 1);

-- -----------------------------
-- Data for table 'DEGREE_CURRICULAR_PLAN'
-- (ID_INTERNAL, NAME, KEY_DEGREE, STATE, INITIAL_DATE, END_DATE)
-- -----------------------------
delete from DEGREE_CURRICULAR_PLAN;
insert into DEGREE_CURRICULAR_PLAN values (1, 'LEQ-2003', 1, 1, '0000-00-00', '0000-00-00');

-- -----------------------------
-- Data for table 'STUDENT_CURRICULAR_PLAN'
-- (ID_INTERNAL, KEY_STUDENT, KEY_DEGREE_CURRICULAR_PLAN, CURRENT_STATE, START_DATE, KEY_BRANCH)
-- -----------------------------
delete from STUDENT_CURRICULAR_PLAN;
insert into STUDENT_CURRICULAR_PLAN values (1, 6, 1, 1, '0000-00-00', 1);

-- -----------------------------
-- Data for table 'CURRICULAR_COURSE'
-- (ID_INTERNAL, KEY_DEPARTMENT_COURSE, KEY_DEGREE_CURRICULAR_PLAN, CREDITS, THEORETICAL_HOURS, PRATICAL_HOURS, THEO_PRAT_HOURS, LAB_HOURS, NAME, CODE, TYPE)
-- -----------------------------
-- PRIMEIRO ANO, PRIMEIRO SEMESTRE:
delete from CURRICULAR_COURSE;
insert into CURRICULAR_COURSE values (1, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "INTRODUÇÃO À QUÍMICA-FÍSICA", "D3", 1);
insert into CURRICULAR_COURSE values (2, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "ANÁLISE MATEMÁTICA I", "PY", 1);
insert into CURRICULAR_COURSE values (3, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "ÁLGEBRA LINEAR", "QN", 1);
insert into CURRICULAR_COURSE values (4, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "COMPUTAÇÃO E PROGRAMAÇÃO", "AZ9", 1);
insert into CURRICULAR_COURSE values (5, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "INTRODUÇÃO À LIGAÇÃO QUÍMICA", "GU", 1);
insert into CURRICULAR_COURSE values (6, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "LABORATÓRIO DE QUÍMICA GERAL I", "AGU", 1);
-- PRIMEIRO ANO, SEGUNDO SEMESTRE:
insert into CURRICULAR_COURSE values (7, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "QUÍMICA ORGÂNICA I", "HU", 1);
insert into CURRICULAR_COURSE values (8, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "MECÂNICA E ONDAS", "AZH", 1);
insert into CURRICULAR_COURSE values (9, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "ANÁLISE MATEMÁTICA II", "P5", 1);
insert into CURRICULAR_COURSE values (10, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "QUÍMICA DAS SOLUÇÕES AQUOSAS", "AGW", 1);
insert into CURRICULAR_COURSE values (11, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "LABORATÓRIO DE QUÍMICA GERAL II", "AGX", 1);
insert into CURRICULAR_COURSE values (12, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "PRINCÍPIOS BÁSICOS DE ENGENHARIA DE PROCESSOS", "AH2", 1);
-- SEGUNDO ANO, PRIMEIRO SEMESTRE:
insert into CURRICULAR_COURSE values (13, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "ANÁLISE MATEMÁTICA III", "UN", 1);
insert into CURRICULAR_COURSE values (14, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "TERMODINÂMICA QUÍMICA", "LP", 1);
insert into CURRICULAR_COURSE values (15, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "PROBABILIDADES E ESTATÍSTICA", "SF", 1);
insert into CURRICULAR_COURSE values (16, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "LABORATÓRIO DE QUÍMICA ORGÂNICA", "AIF", 1);
insert into CURRICULAR_COURSE values (17, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "QUÍMICA ORGÂNICA II", "AJM", 1);
insert into CURRICULAR_COURSE values (18, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "ELECTROMAGNETISMO E ÓPTICA", "AZI", 1);
-- SEGUNDO ANO, SEGUNDO SEMESTRE:
insert into CURRICULAR_COURSE values (19, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "FENÓMENOS DE TRANSFERÊNCIA I", "C4", 1);
insert into CURRICULAR_COURSE values (20, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "ANÁLISE MATEMÁTICA IV", "U8", 1);
insert into CURRICULAR_COURSE values (21, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "QUÍMICA FÍSICA", "XW", 1);
insert into CURRICULAR_COURSE values (22, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "TERMODINÂMICA DE ENGENHARIA QUÍMICA", "AII", 1);
insert into CURRICULAR_COURSE values (23, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "PROCESSOS DE ENGENHARIA QUÍMICA I", "AIK", 1);
insert into CURRICULAR_COURSE values (24, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "LABORATÓRIO DE ENGENHARIA QUÍMICA I", "AJL", 1);
-- TERCEIRO ANO, PRIMEIRO SEMESTRE:
insert into CURRICULAR_COURSE values (25, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "ANÁLISE QUÍMICA", "AN", 1);
insert into CURRICULAR_COURSE values (26, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "FENÓMENOS DE TRANSFERÊNCIA II", "C5", 1);
insert into CURRICULAR_COURSE values (27, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "ANÁLISE E SIMULAÇÃO NUMÉRICA", "AZ7", 1);
insert into CURRICULAR_COURSE values (28, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "PROCESSOS DE ENGENHARIA QUÍMICA II", "AL6", 1);
insert into CURRICULAR_COURSE values (29, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "LABORATÓRIO DE ENGENHARIA QUÍMICA II", "AL7", 1);
insert into CURRICULAR_COURSE values (30, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "MATERIAIS E CORROSÃO", "AMD", 1);
-- TERCEIRO ANO, SEGUNDO SEMESTRE:
insert into CURRICULAR_COURSE values (31, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "OPERAÇÕES EM SISTEMAS MULTIFÁSICOS", "AL8", 1);
insert into CURRICULAR_COURSE values (32, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "LABORATÓRIO DE ENGENHARIA QUÍMICA III", "AL9", 1);
insert into CURRICULAR_COURSE values (33, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "ENGENHARIA DAS REACÇÕES I", "AME", 1);
insert into CURRICULAR_COURSE values (34, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "FENÓMENOS DE TRANSFERÊNCIA III", "AMG", 1);
insert into CURRICULAR_COURSE values (35, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "PROCESSOS DE SEPARAÇÃO I", "AMH", 1);
insert into CURRICULAR_COURSE values (36, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "FUNDAMENTOS DE GESTÃO", "AX2", 1);
-- QUARTO ANO, PRIMEIRO SEMESTRE:
insert into CURRICULAR_COURSE values (37, 1, 1, 4.0, 3.0, 2.0, 0.0, 2.0, "OPTIMIZAÇÃO DE PROCESSOS", "7W", 1);
insert into CURRICULAR_COURSE values (38, 1, 1, 4.0, 3.0, 0.0, 2.0, 2.0, "CONTROLO E INSTRUMENTAÇÃO DE PROCESSOS", "APK", 1);
insert into CURRICULAR_COURSE values (39, 1, 1, 4.0, 3.0, 1.0, 2.0, 2.0, "LABORATÓRIOS DE ENGENHARIA QUÍMICA IV", "APL", 1);
insert into CURRICULAR_COURSE values (40, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "ENGENHARIA DAS REACÇÕES II", "AR7", 1);
insert into CURRICULAR_COURSE values (41, 1, 1, 4.0, 3.0, 0.0, 0.0, 2.0, "PROCESSOS DE SEPARAÇÃO II", "AR8", 1);
insert into CURRICULAR_COURSE values (42, 1, 1, 4.0, 3.0, 0.0, 2.0, 0.0, "OPÇÃO I", "", 2);
-- QUARTO ANO, SEGUNDO SEMESTRE:
insert into CURRICULAR_COURSE values (43, 1, 1, 4.0, 3.0, 0.0, 2.0, 0.0, "ENGENHARIA QUÍMICA INTEGRADA I", "APM", 1);
insert into CURRICULAR_COURSE values (44, 1, 1, 4.0, 3.0, 0.0, 0.0, 0.0, "SÍNTESE E INTEGRAÇÃO DE PROCESSOS", "APO", 1);
insert into CURRICULAR_COURSE values (45, 1, 1, 4.0, 3.0, 0.0, 3.0, 0.0, "LABORATÓRIOS DE ENGENHARIA QUÍMICA V", "APP", 1);
insert into CURRICULAR_COURSE values (46, 1, 1, 4.0, 3.0, 0.0, 2.0, 0.0, "TECNOLOGIA AMBIENTAL", "APQ", 1);
insert into CURRICULAR_COURSE values (47, 1, 1, 4.0, 3.0, 0.0, 2.0, 0.0, "DIMENSIONAMENTO E OPTIMIZAÇÃO DE EQUIPAMENTOS E UTILIDADES", "APN", 1);
insert into CURRICULAR_COURSE values (48, 1, 1, 4.0, 3.0, 0.0, 2.0, 0.0, "OPÇÃO II", "", 2);
-- QUINTO ANO, PRIMEIRO SEMESTRE:
insert into CURRICULAR_COURSE values (49, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "PROJECTO DE INDÚSTRIAS QUÍMICAS", "AB3", 1);
insert into CURRICULAR_COURSE values (50, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "ENGENHARIA QUÍMICA INTEGRADA II", "AV6", 1);
insert into CURRICULAR_COURSE values (51, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "OPÇÃO III", "", 2);
-- QUINTO ANO, SEGUNDO SEMESTRE:
insert into CURRICULAR_COURSE values (52, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "ESTÁGIO", "AV2", 1);
-- QUARTO ANO, PRIMEIRO SEMESTRE, OPÇÕES:
insert into CURRICULAR_COURSE values (53, 1, 1, 4.0, 3.0, 2.0, 0.0, 0.0, "RISCOS NATURAIS E TECNOLÓGICOS", "AH0", 1);
insert into CURRICULAR_COURSE values (54, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "GESTÃO PELA QUALIDADE TOTAL", "ALE", 1);
insert into CURRICULAR_COURSE values (55, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "ANÁLISES INDUSTRIAIS E CONTROLO", "AV7", 1);
insert into CURRICULAR_COURSE values (56, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "LIMITES DA CIÊNCIA", "AXM", 1);
insert into CURRICULAR_COURSE values (57, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "BIOTECNOLOGIA", "AB6", 1);
insert into CURRICULAR_COURSE values (58, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "QUÍMICA INDUSTRIAL", "AOZ", 1);
insert into CURRICULAR_COURSE values (59, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "ESTIMATIVA DE PROPRIEDADES PARA ENGENHARIA DE PROCESSOS", "", 1);
-- QUARTO ANO, SEGUNDO SEMESTRE, OPÇÕES:
insert into CURRICULAR_COURSE values (60, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "INVESTIGAÇÃO OPERACIONAL", "EA", 1);
insert into CURRICULAR_COURSE values (61, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "PREVISÃO DE PROPRIEDADES", "ZC", 1);
insert into CURRICULAR_COURSE values (62, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "TRATAMENTO DE EFLUENTES GASOSOS", "AEV", 1);
insert into CURRICULAR_COURSE values (63, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "ESTUDOS DE CIÊNCIA:ARTE,TECNOLOGIA E SOCIEDADE", "AP9", 1);
insert into CURRICULAR_COURSE values (64, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "GESTÃO, TRATAMENTO E VALORIZAÇÃO DE RESÍDUOS", "APR", 1);
insert into CURRICULAR_COURSE values (65, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "SUPERVISÃO E DIAGNÓSTICO DE PROCESSOS", "APU", 1);
insert into CURRICULAR_COURSE values (66, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "GESTÃO DA PRODUÇÃO E DAS OPERAÇÕES", "APW", 1);
insert into CURRICULAR_COURSE values (67, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "SISTEMAS DE GESTÃO AMBIENTAL", "AV8", 1);
insert into CURRICULAR_COURSE values (68, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "MODELAÇÃO E OPTIMIZAÇÃO DE SISTEMAS DINÂMICOS", "AX3", 1);
insert into CURRICULAR_COURSE values (69, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "CIÊNCIA E TECNOLOGIA DE POLÍMEROS", "AXN", 1);
-- QUINTO ANO, PRIMEIRO SEMESTRE, OPÇÕES:
insert into CURRICULAR_COURSE values (70, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "TECNOLOGIA ALIMENTAR", "AI", 1);
insert into CURRICULAR_COURSE values (71, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "REFINAÇÃO DE PETRÓLEOS E PETROQUÍMICA", "IF", 1);
insert into CURRICULAR_COURSE values (72, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "MODELOS MULTICRITÉRIO DE APOIO À DECISÃO", "AJA", 1);
insert into CURRICULAR_COURSE values (73, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "SEGURANÇA E HIGIENE INDUSTRIAL", "APB", 1);
insert into CURRICULAR_COURSE values (74, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "CONTROLO DE QUALIDADE", "APS", 1);
insert into CURRICULAR_COURSE values (75, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "CONTROLO AVANÇADO DE PROCESSOS", "APT", 1);
insert into CURRICULAR_COURSE values (76, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "CARACTERÍSTICAS E TRATAMENTO DE ÁGUAS", "AET", 1);
insert into CURRICULAR_COURSE values (77, 1, 1, 4.0, 0.0, 0.0, 0.0, 0.0, "ENGENHARIA DE SUPERFÍCIES", "APY", 1);

-- -----------------------------
-- Data for table 'CURRICULAR_COURSE_SCOPE'
-- (ID_INTERNAL, KEY_CURRICULAR_SEMESTER, KEY_CURRICULAR_COURSE, KEY_BRANCH, THEORETICAL_HOURS, PRATICAL_HOURS, THEO_PRAT_HOURS, LAB_HOURS, SCOPE_TYPE)
-- -----------------------------
delete from CURRICULAR_COURSE_SCOPE;
insert into CURRICULAR_COURSE_SCOPE  values (1, 1, 1, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (2, 1, 2, 1, 0.0, 0.0, 0.0, 0.0, 2);
insert into CURRICULAR_COURSE_SCOPE  values (3, 1, 3, 1, 0.0, 0.0, 0.0, 0.0, 2);
insert into CURRICULAR_COURSE_SCOPE  values (4, 1, 4, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (5, 1, 5, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (6, 1, 6, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (7, 2, 7, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (8, 2, 8, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (9, 2, 9, 1, 0.0, 0.0, 0.0, 0.0, 2);
insert into CURRICULAR_COURSE_SCOPE  values (10, 2, 10, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (11, 2, 11, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (12, 2, 12, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (13, 3, 13, 1, 0.0, 0.0, 0.0, 0.0, 2);
insert into CURRICULAR_COURSE_SCOPE  values (14, 3, 14, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (15, 3, 15, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (16, 3, 16, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (17, 3, 17, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (18, 3, 18, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (19, 4, 19, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (20, 4, 20, 1, 0.0, 0.0, 0.0, 0.0, 2);
insert into CURRICULAR_COURSE_SCOPE  values (21, 4, 21, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (22, 4, 22, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (23, 4, 23, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (24, 4, 24, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (25, 5, 25, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (26, 5, 26, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (27, 5, 27, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (28, 5, 28, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (29, 5, 29, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (30, 5, 30, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (31, 6, 31, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (32, 6, 32, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (33, 6, 33, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (34, 6, 34, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (35, 6, 35, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (36, 6, 36, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (37, 7, 37, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (38, 7, 38, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (39, 7, 39, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (40, 7, 40, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (41, 7, 41, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (42, 7, 42, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (43, 8, 43, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (44, 8, 44, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (45, 8, 45, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (46, 8, 46, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (47, 8, 47, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (48, 8, 48, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (49, 9, 49, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (50, 9, 50, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (51, 9, 51, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (52, 10, 52, 1, 0.0, 0.0, 0.0, 0.0, 0);

-- bianuais matemáticas
insert into CURRICULAR_COURSE_SCOPE  values (53, 2, 2, 1, 0.0, 0.0, 0.0, 0.0, 2);
insert into CURRICULAR_COURSE_SCOPE  values (54, 2, 3, 1, 0.0, 0.0, 0.0, 0.0, 2);
insert into CURRICULAR_COURSE_SCOPE  values (55, 1, 9, 1, 0.0, 0.0, 0.0, 0.0, 2);
insert into CURRICULAR_COURSE_SCOPE  values (56, 4, 13, 1, 0.0, 0.0, 0.0, 0.0, 2);
insert into CURRICULAR_COURSE_SCOPE  values (57, 3, 20, 1, 0.0, 0.0, 0.0, 0.0, 2);

-- bianuais quimica
insert into CURRICULAR_COURSE_SCOPE  values (58, 2, 1, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (59, 1, 10, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (60, 1, 7, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (61, 4, 17, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (62, 4, 14, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (63, 3, 22, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (64, 3, 19, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (65, 6, 26, 1, 0.0, 0.0, 0.0, 0.0, 0);
insert into CURRICULAR_COURSE_SCOPE  values (66, 5, 34, 1, 0.0, 0.0, 0.0, 0.0, 0);

-- -----------------------------
-- Data for table 'POSSIBLE_CURRICULAR_COURSE_FOR_OPTIONAL_CURRICULAR_COURSE'
-- (ID_INTERNAL, KEY_POSSIBLE_CURRICULAR_COURSE, KEY_OPTIONAL_CURRICULAR_COURSE)
-- -----------------------------
delete from POSSIBLE_CURRICULAR_COURSE_FOR_OPTIONAL_CURRICULAR_COURSE;
insert into POSSIBLE_CURRICULAR_COURSE_FOR_OPTIONAL_CURRICULAR_COURSE values (1, 53, 42);
insert into POSSIBLE_CURRICULAR_COURSE_FOR_OPTIONAL_CURRICULAR_COURSE values (2, 54, 42);
insert into POSSIBLE_CURRICULAR_COURSE_FOR_OPTIONAL_CURRICULAR_COURSE values (3, 55, 42);
insert into POSSIBLE_CURRICULAR_COURSE_FOR_OPTIONAL_CURRICULAR_COURSE values (4, 56, 42);
insert into POSSIBLE_CURRICULAR_COURSE_FOR_OPTIONAL_CURRICULAR_COURSE values (5, 57, 42);
insert into POSSIBLE_CURRICULAR_COURSE_FOR_OPTIONAL_CURRICULAR_COURSE values (6, 58, 42);
insert into POSSIBLE_CURRICULAR_COURSE_FOR_OPTIONAL_CURRICULAR_COURSE values (7, 59, 42);

insert into POSSIBLE_CURRICULAR_COURSE_FOR_OPTIONAL_CURRICULAR_COURSE values (8, 60, 48);
insert into POSSIBLE_CURRICULAR_COURSE_FOR_OPTIONAL_CURRICULAR_COURSE values (9, 61, 48);
insert into POSSIBLE_CURRICULAR_COURSE_FOR_OPTIONAL_CURRICULAR_COURSE values (10, 62, 48);
insert into POSSIBLE_CURRICULAR_COURSE_FOR_OPTIONAL_CURRICULAR_COURSE values (11, 63, 48);
insert into POSSIBLE_CURRICULAR_COURSE_FOR_OPTIONAL_CURRICULAR_COURSE values (12, 64, 48);
insert into POSSIBLE_CURRICULAR_COURSE_FOR_OPTIONAL_CURRICULAR_COURSE values (13, 65, 48);
insert into POSSIBLE_CURRICULAR_COURSE_FOR_OPTIONAL_CURRICULAR_COURSE values (14, 66, 48);
insert into POSSIBLE_CURRICULAR_COURSE_FOR_OPTIONAL_CURRICULAR_COURSE values (15, 67, 48);
insert into POSSIBLE_CURRICULAR_COURSE_FOR_OPTIONAL_CURRICULAR_COURSE values (16, 68, 48);
insert into POSSIBLE_CURRICULAR_COURSE_FOR_OPTIONAL_CURRICULAR_COURSE values (17, 69, 48);

insert into POSSIBLE_CURRICULAR_COURSE_FOR_OPTIONAL_CURRICULAR_COURSE values (18, 70, 51);
insert into POSSIBLE_CURRICULAR_COURSE_FOR_OPTIONAL_CURRICULAR_COURSE values (19, 71, 51);
insert into POSSIBLE_CURRICULAR_COURSE_FOR_OPTIONAL_CURRICULAR_COURSE values (20, 72, 51);
insert into POSSIBLE_CURRICULAR_COURSE_FOR_OPTIONAL_CURRICULAR_COURSE values (21, 73, 51);
insert into POSSIBLE_CURRICULAR_COURSE_FOR_OPTIONAL_CURRICULAR_COURSE values (22, 74, 51);
insert into POSSIBLE_CURRICULAR_COURSE_FOR_OPTIONAL_CURRICULAR_COURSE values (23, 75, 51);
insert into POSSIBLE_CURRICULAR_COURSE_FOR_OPTIONAL_CURRICULAR_COURSE values (24, 76, 51);
insert into POSSIBLE_CURRICULAR_COURSE_FOR_OPTIONAL_CURRICULAR_COURSE values (25, 77, 51);

-- -----------------------------
-- Data for table 'ENROLMENT'
-- (ID_INTERNAL, KEY_STUDENT_CURRICULAR_PLAN, KEY_CURRICULAR_COURSE, KEY_EXECUTION_PERIOD, STATE)
-- -----------------------------
delete from ENROLMENT;
insert into ENROLMENT values (1, 1, 1, 1, 1, 'Dominio.Enrolment', null);
insert into ENROLMENT values (2, 1, 2, 1, 1, 'Dominio.Enrolment', null);
insert into ENROLMENT values (3, 1, 3, 1, 1, 'Dominio.Enrolment', null);
insert into ENROLMENT values (4, 1, 4, 1, 1, 'Dominio.Enrolment', null);
insert into ENROLMENT values (5, 1, 5, 1, 1, 'Dominio.Enrolment', null);
insert into ENROLMENT values (6, 1, 6, 1, 1, 'Dominio.Enrolment', null);

insert into ENROLMENT values (7, 1, 7, 1, 1, 'Dominio.Enrolment', null);
insert into ENROLMENT values (8, 1, 8, 1, 1, 'Dominio.Enrolment', null);
insert into ENROLMENT values (9, 1, 9, 1, 1, 'Dominio.Enrolment', null);
insert into ENROLMENT values (10, 1, 10, 1, 1, 'Dominio.Enrolment', null);
insert into ENROLMENT values (11, 1, 11, 1, 1, 'Dominio.Enrolment', null);
insert into ENROLMENT values (12, 1, 12, 1, 1, 'Dominio.Enrolment', null);

insert into ENROLMENT values (13, 1, 13, 1, 1, 'Dominio.Enrolment', null);
insert into ENROLMENT values (14, 1, 14, 1, 1, 'Dominio.Enrolment', null);
insert into ENROLMENT values (15, 1, 15, 1, 1, 'Dominio.Enrolment', null);
insert into ENROLMENT values (16, 1, 16, 1, 1, 'Dominio.Enrolment', null);
insert into ENROLMENT values (17, 1, 17, 1, 1, 'Dominio.Enrolment', null);
insert into ENROLMENT values (18, 1, 18, 1, 1, 'Dominio.Enrolment', null);

insert into ENROLMENT values (19, 1, 19, 1, 1, 'Dominio.Enrolment', null);
insert into ENROLMENT values (20, 1, 20, 1, 1, 'Dominio.Enrolment', null);
insert into ENROLMENT values (21, 1, 21, 1, 1, 'Dominio.Enrolment', null);
insert into ENROLMENT values (22, 1, 22, 1, 1, 'Dominio.Enrolment', null);
insert into ENROLMENT values (23, 1, 23, 1, 1, 'Dominio.Enrolment', null);
insert into ENROLMENT values (24, 1, 24, 1, 1, 'Dominio.Enrolment', null);

insert into ENROLMENT values (25, 1, 25, 1, 1, 'Dominio.Enrolment', null);
insert into ENROLMENT values (26, 1, 26, 1, 1, 'Dominio.Enrolment', null);
insert into ENROLMENT values (27, 1, 27, 1, 1, 'Dominio.Enrolment', null);
insert into ENROLMENT values (28, 1, 28, 1, 1, 'Dominio.Enrolment', null);
insert into ENROLMENT values (29, 1, 29, 1, 1, 'Dominio.Enrolment', null);
insert into ENROLMENT values (30, 1, 30, 1, 1, 'Dominio.Enrolment', null);

insert into ENROLMENT values (31, 1, 31, 1, 1, 'Dominio.Enrolment', null);
insert into ENROLMENT values (32, 1, 32, 1, 1, 'Dominio.Enrolment', null);
insert into ENROLMENT values (33, 1, 33, 1, 1, 'Dominio.Enrolment', null);
insert into ENROLMENT values (34, 1, 34, 1, 1, 'Dominio.Enrolment', null);
insert into ENROLMENT values (35, 1, 35, 1, 1, 'Dominio.Enrolment', null);
insert into ENROLMENT values (36, 1, 36, 1, 1, 'Dominio.Enrolment', null);

-- insert into ENROLMENT values (37, 1, 37, 1, 1, 'Dominio.Enrolment', null);
-- insert into ENROLMENT values (38, 1, 38, 1, 1, 'Dominio.Enrolment', null);
-- insert into ENROLMENT values (39, 1, 39, 1, 1, 'Dominio.Enrolment', null);
-- insert into ENROLMENT values (40, 1, 40, 1, 1, 'Dominio.Enrolment', null);
-- insert into ENROLMENT values (41, 1, 41, 1, 1, 'Dominio.Enrolment', null);
-- insert into ENROLMENT values (42, 1, 42, 1, 1, 'Dominio.Enrolment', null);

-- insert into ENROLMENT values (43, 1, 43, 1, 1, 'Dominio.Enrolment', null);
-- insert into ENROLMENT values (44, 1, 44, 1, 1, 'Dominio.Enrolment', null);
-- insert into ENROLMENT values (45, 1, 45, 1, 1, 'Dominio.Enrolment', null);
-- insert into ENROLMENT values (46, 1, 46, 1, 1, 'Dominio.Enrolment', null);
-- insert into ENROLMENT values (47, 1, 47, 1, 1, 'Dominio.Enrolment', null);
-- insert into ENROLMENT values (48, 1, 48, 1, 1, 'Dominio.Enrolment', null);

-- -----------------------------
-- Data for table 'PRECEDENCE'
-- -----------------------------
-- (ID_INTERNAL, KEY_CURRICULAR_COURSE, SCOPE_TO_APPLY)
delete from PRECEDENCE;

-- precedencias do tipo Dominio.RestrictionCurricularCourseDone
insert into PRECEDENCE values (1, 14, 'SP');
insert into PRECEDENCE values (2, 17, 'SP');
insert into PRECEDENCE values (3, 22, 'SP');
insert into PRECEDENCE values (4, 19, 'SP');
insert into PRECEDENCE values (5, 21, 'SP');
insert into PRECEDENCE values (6, 26, 'SP');
insert into PRECEDENCE values (7, 25, 'SP');
insert into PRECEDENCE values (8, 34, 'SP');
insert into PRECEDENCE values (9, 24, 'SP');
insert into PRECEDENCE values (10, 9, 'SP');
insert into PRECEDENCE values (11, 13, 'SP');
insert into PRECEDENCE values (12, 20, 'SP');

-- precedencias do tipo Dominio.RestrictionDoneOrAlreadyEnrolledInCurricularCourse
insert into PRECEDENCE values (13, 10, 'SP');
insert into PRECEDENCE values (14, 12, 'SP');
insert into PRECEDENCE values (15, 7, 'SP');
insert into PRECEDENCE values (16, 14, 'SP');
insert into PRECEDENCE values (17, 26, 'SP');
insert into PRECEDENCE values (18, 25, 'SP');
insert into PRECEDENCE values (19, 34, 'SP');

-- -----------------------------
-- Data for table 'RESTRICTION'
-- -----------------------------
-- (ID_INTERNAL, CLASS_NAME, KEY_PRECEDENCE, KEY_CURRICULAR_COURSE, NUMBER_OF_CURRICULAR_COURSE_DONE)
delete from RESTRICTION;
insert into RESTRICTION values (1, 'Dominio.RestrictionCurricularCourseDone', 1, 1, 0);
insert into RESTRICTION values (2, 'Dominio.RestrictionCurricularCourseDone', 2, 7, 0);
insert into RESTRICTION values (3, 'Dominio.RestrictionCurricularCourseDone', 3, 14, 0);
insert into RESTRICTION values (4, 'Dominio.RestrictionCurricularCourseDone', 4, 3, 0);
insert into RESTRICTION values (5, 'Dominio.RestrictionCurricularCourseDone', 4, 9, 0);
insert into RESTRICTION values (6, 'Dominio.RestrictionCurricularCourseDone', 5, 3, 0);
insert into RESTRICTION values (7, 'Dominio.RestrictionCurricularCourseDone', 6, 19, 0);
insert into RESTRICTION values (8, 'Dominio.RestrictionCurricularCourseDone', 7, 10, 0);
insert into RESTRICTION values (9, 'Dominio.RestrictionCurricularCourseDone', 8, 19, 0);
insert into RESTRICTION values (10, 'Dominio.RestrictionCurricularCourseDone', 9, 11, 0);
insert into RESTRICTION values (11, 'Dominio.RestrictionCurricularCourseDone', 10, 2, 0);
insert into RESTRICTION values (12, 'Dominio.RestrictionCurricularCourseDone', 11, 9, 0);
insert into RESTRICTION values (13, 'Dominio.RestrictionCurricularCourseDone', 12, 13, 0);

insert into RESTRICTION values (14, 'Dominio.RestrictionDoneOrAlreadyEnrolledInCurricularCourse', 13, 1, 0);
insert into RESTRICTION values (15, 'Dominio.RestrictionDoneOrAlreadyEnrolledInCurricularCourse', 14, 1, 0);
insert into RESTRICTION values (16, 'Dominio.RestrictionDoneOrAlreadyEnrolledInCurricularCourse', 15, 1, 0);
insert into RESTRICTION values (17, 'Dominio.RestrictionDoneOrAlreadyEnrolledInCurricularCourse', 15, 5, 0);
insert into RESTRICTION values (18, 'Dominio.RestrictionDoneOrAlreadyEnrolledInCurricularCourse', 16, 9, 0);
insert into RESTRICTION values (19, 'Dominio.RestrictionDoneOrAlreadyEnrolledInCurricularCourse', 17, 13, 0);
insert into RESTRICTION values (20, 'Dominio.RestrictionDoneOrAlreadyEnrolledInCurricularCourse', 18, 15, 0);
insert into RESTRICTION values (21, 'Dominio.RestrictionDoneOrAlreadyEnrolledInCurricularCourse', 19, 20, 0);
insert into RESTRICTION values (22, 'Dominio.RestrictionDoneOrAlreadyEnrolledInCurricularCourse', 19, 26, 0);

-- -----------------------------
-- Data for table 'DEGREE_ENROLMENT_INFO'
-- (ID_INTERNAL, KEY_DEGREE_CURRICULAR_PLAN, DEGREE_DURATION, MINIMAL_YEAR_FOR_OPTIONAL_COURSES, END_DATE) 
-- -----------------------------
delete from DEGREE_CURRICULAR_PLAN_ENROLMENT_INFO;
insert into DEGREE_CURRICULAR_PLAN_ENROLMENT_INFO values (1, 1, 5, 3);

-- -----------------------------
-- Data for table 'STUDENT_GROUP_INFO'
-- (ID_INTERNAL, STUDENT_TYPE, MIN_COURSES_TO_ENROL, MAX_COURSES_TO_ENROL, MAX_NAC_TO_ENROL) 
-- -----------------------------
delete from STUDENT_GROUP_INFO;
insert into STUDENT_GROUP_INFO values (1, 1, 3, 7, 10);


-- Isto e para sair
-- -----------------------------
-- Data for table 'ENROLMENT_PERIOD'
-- (ID_INTERNAL, KEY_DEGREE_CURRICULAR_PLAN, KEY_EXECUTION_PERIOD, START_DATE, END_DATE) 
-- -----------------------------
delete from ENROLMENT_PERIOD;
insert into ENROLMENT_PERIOD values (1, 1, 1, SYSDATE(), '2010-01-10');


delete from EXECUTION_PERIOD;
insert into EXECUTION_PERIOD values (1, '1º Semestre', 1, 'A', 1);
insert into EXECUTION_PERIOD values (2, '2º Semestre', 1, 'NO', 2);
insert into EXECUTION_PERIOD values (3, '1º Semestre', 2, 'NO', 1);
insert into EXECUTION_PERIOD values (4, '2º Semestre', 2, 'NO', 2);
insert into EXECUTION_PERIOD values (5, '1º Semestre', 3, 'NO', 1);
insert into EXECUTION_PERIOD values (6, '2º Semestre', 3, 'NO', 2);
insert into EXECUTION_PERIOD values (7, '1º Semestre', 4, 'NO', 1);
insert into EXECUTION_PERIOD values (8, '2º Semestre', 4, 'NO', 2);
insert into EXECUTION_PERIOD values (9, '1º Semestre', 5, 'NO', 1);
insert into EXECUTION_PERIOD values (10, '2º Semestre', 5, 'NO', 2);
insert into EXECUTION_PERIOD values (11, '1º Semestre', 6, 'NO', 1);
insert into EXECUTION_PERIOD values (12, '2º Semestre', 6, 'NO', 2);
insert into EXECUTION_PERIOD values (13, '1º Semestre', 7, 'NO', 1);
insert into EXECUTION_PERIOD values (14, '2º Semestre', 7, 'NO', 2);
insert into EXECUTION_PERIOD values (15, '1º Semestre', 8, 'NO', 1);
insert into EXECUTION_PERIOD values (16, '2º Semestre', 8, 'NO', 2);

delete from EXECUTION_YEAR;
insert into EXECUTION_YEAR values (1, '2002/2003','A');
insert into EXECUTION_YEAR values (2, '2003/2004','NO');
insert into EXECUTION_YEAR values (3, '2005/2006','NO');
insert into EXECUTION_YEAR values (4, '2006/2007','NO');
insert into EXECUTION_YEAR values (5, '2007/2008','NO');
insert into EXECUTION_YEAR values (6, '2008/2009','NO');
insert into EXECUTION_YEAR values (7, '2009/2010','NO');
insert into EXECUTION_YEAR values (8, '2010/2011','NO');

-- -----------------------------
-- Data for table 'CURRICULAR_YEAR'
-- (ID_INTERNAL, YEAR)
-- -----------------------------
delete from CURRICULAR_YEAR;
insert into CURRICULAR_YEAR values (1, 1);
insert into CURRICULAR_YEAR values (2, 2);
insert into CURRICULAR_YEAR values (3, 3);
insert into CURRICULAR_YEAR values (4, 4);
insert into CURRICULAR_YEAR values (5, 5);

-- -----------------------------
-- Data for table 'CURRICULAR_SEMESTER'
-- (ID_INTERNAL, KEY_CURRICULAR_YEAR, SEMESTER)
-- -----------------------------
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

-- para sair até aqui
