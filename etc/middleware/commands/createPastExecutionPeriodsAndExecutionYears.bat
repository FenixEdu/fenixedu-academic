mysql ciapl < etc\middleware\allEnrollmentsMigration\createTableMWDistinctEnrollmentYearAndSemester.sql
mysql ciapl < etc\middleware\allEnrollmentsMigration\createPastExecutionYears.sql | mysql ciapl
mysql ciapl < etc\middleware\allEnrollmentsMigration\createPastExecutionPeriods.sql | mysql ciapl
