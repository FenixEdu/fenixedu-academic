LOAD DATA INFILE 'E:\\Joao Luz\\alunos.txt' into table ciapl.mw_ALUNO_temp;
LOAD DATA INFILE 'E:\\Joao Luz\\pessoa.txt' into table ciapl.mw_PESSOA IGNORE 1 LINES;
LOAD DATA INFILE 'E:\\Joao Luz\\nomedis.txt' into table ciapl.mw_COURSE;
LOAD DATA INFILE 'E:\\Joao Luz\\curram.txt' into table ciapl.mw_BRANCH;
LOAD DATA INFILE 'E:\\Joao Luz\\medias.txt' into table ciapl.mw_AVERAGE;