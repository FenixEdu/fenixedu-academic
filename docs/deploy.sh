mysql -Dfenix < CorrectDataBaseForDeploy.sql
mysql -Dfenix < fixToCurriculum-step1.sql
mysql -Dfenix < fixToCurriculum-step2.sql > insertToCurriculum.sql
mysql -Dfenix < insertToCurriculum.sql
mysql -Dfenix < fixToCurriculum-step3.sql
mysql -Dfenix < fixToEvaluationMethod-step1.sql
mysql -Dfenix < fixToEvaluationMethod-step2.sql > insertEvaluationMethod.sql
mysql -Dfenix < insertEvaluationMethod.sql