LOAD DATA INFILE 'E:\\Joao Luz\\alunos.txt' into table ciapl.mw_ALUNO_temp;
LOAD DATA INFILE 'E:\\Joao Luz\\pessoa.txt' into table ciapl.mw_PESSOA IGNORE 1 LINES;
LOAD DATA INFILE 'E:\\Joao Luz\\curram.txt' into table ciapl.mw_BRANCH;
LOAD DATA INFILE 'E:\\Joao Luz\\medias.txt' into table ciapl.mw_AVERAGE;
LOAD DATA INFILE 'E:\\Joao Luz\\curriculo.txt' into table ciapl.mw_ENROLMENT_temp;
LOAD DATA INFILE 'E:\\Joao Luz\\nomedis.txt' into table ciapl.mw_CURRICULAR_COURSE;
LOAD DATA INFILE 'E:\\Joao Luz\\disciplinas.txt' into table ciapl.mw_CURRICULAR_COURSE_SCOPE;

-- Temporary Delete's
delete from mw_ALUNO_temp where number = 42980;
delete from mw_ALUNO_temp where number = 51335;
