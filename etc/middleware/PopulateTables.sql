LOAD DATA INFILE '@load.data.infile.root@alunos.txt' into table mw_ALUNO_temp;
LOAD DATA INFILE '@load.data.infile.root@pessoa.txt' into table mw_PESSOA IGNORE 1 LINES;
LOAD DATA INFILE '@load.data.infile.root@nomedis.txt' into table mw_COURSE;
LOAD DATA INFILE '@load.data.infile.root@curram.txt' into table mw_BRANCH;
LOAD DATA INFILE '@load.data.infile.root@medias.txt' into table mw_AVERAGE;
LOAD DATA INFILE '@load.data.infile.root@curriculo.txt' into table mw_ENROLMENT_temp;
LOAD DATA INFILE '@load.data.infile.root@nomedis.txt' into table mw_CURRICULAR_COURSE;
LOAD DATA INFILE '@load.data.infile.root@disciplinas.txt' into table mw_CURRICULAR_COURSE_SCOPE;
LOAD DATA INFILE '@load.data.infile.root@student-class-distribution.txt' into table mw_STUDENT_CLASS IGNORE 1 LINES;

delete from mw_DEGREE_TRANSLATION;
insert into mw_DEGREE_TRANSLATION values (01,1),(02,2),(03,3),(05,5),(06,6)
,(07,7),(08,8),(09,9),(10,10),(19,19),(11,11),(12,12),(13,13),(14,14)
,(15,15),(16,16),(17,17),(18,18),(20,20),(21,21),(22,22),(23,23),(24,51);

-- Temporary Delete's
delete from mw_ALUNO_temp where number = 42980;
delete from mw_ALUNO_temp where number = 51335;
