call mysql -f ciapl < fixDbForDeploy1-31-01-2004.sql > temp1.sql
call mysql -f ciapl < fixDbForDeploy2-31-01-2004.sql
call mysql -f ciapl < temp1.sql
call mysql -f ciapl < fixDbForDeploy3-31-01-2004.sql > temp2.sql
call mysql -f ciapl < temp2.sql
call mysql -f ciapl < fixDbForDeploy4-31-01-2004.sql
call mysql -f ciapl < fixDbForDeploy5-31-01-2004.sql > temp3.sql
call mysql -f ciapl < temp3.sql
REM call mysql -f ciapl < updateQualificationBeforeScript.sql
REM call mysql -f ciapl < updateQualificationScript1.sql | mysql ciapl
REM call mysql -f ciapl < updateQualificationScript2.sql | mysql ciapl
REM call mysql -f ciapl < updateQualification.AfterScript.sql

call del temp1.sql
call del temp2.sql
call del temp3.sql

call mysql -f ciapl < fixDbForDeploy6-31-01-2004.sql
