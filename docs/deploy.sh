mysql -Dfenix < CorrectDataBaseForDeploy.sql
mysql -Dfenix < fixToCurriculum-step1.sql
mysql -Dfenix < fixToCurriculum-step2.sql 
mysql -Dfenix < insertToCurriculum.sql
mysql -Dfenix < fixToCurriculum-step3.sql
