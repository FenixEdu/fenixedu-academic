echo off
cls
echo .START PART 2
echo .

echo ************CORRECTING THE MIGRATION.
echo .

echo Updating Docentes with New Teacher IDs...
mysql -u root -D fenix < c:\fenix\fenixSB\etc\middleware\grantMigration\updateTableMigracaoDocenteWithNewTeacherID.sql | mysql fenix
echo                                          Done

echo Inserting Roles to New Teachers (Beforing actualy adding them)...
mysql -u root -D fenix < c:\fenix\fenixSB\etc\middleware\grantMigration\insertMissingRolesForNewTeachers.sql | mysql fenix
echo                                                                  Done

echo Inserting new Teachers...
mysql -u root -D fenix < c:\fenix\fenixSB\etc\middleware\grantMigration\insertNewDocentesIntoTableTeacher.sql | mysql fenix
echo                          Done

echo Updating Docentes with New Teacher IDs (again!)...
mysql -u root -D fenix < c:\fenix\fenixSB\etc\middleware\grantMigration\updateTableMigracaoDocenteWithNewTeacherID.sql | mysql fenix
echo                                                   Done

echo Updating teacher IDs in teacher related tables...
mysql -u root -D fenix < c:\fenix\fenixSB\etc\middleware\grantMigration\updateNewTeacherIDInTablesWithKeyTeacher.sql | mysql fenix
echo                                                  Done

echo Inserting Grant Owner roles...
mysql -u root -D fenix < c:\fenix\fenixSB\etc\middleware\grantMigration\insertMissingRolesForNewGrantOwners.sql | mysql fenix
echo                               Done

echo DELETE OJB_HL_SEQ...
mysql -u root -D fenix < c:\fenix\fenixSB\etc\middleware\grantMigration\deleteOJB_HL_SEQ.sql
echo                     Done

echo .
echo *****************Grant Migration 2004 by Pica-Barbosa - PART 3 SUCCESSFULLY COMPLETED
echo .

echo Removing migration data...
mysql -u root -D fenix < c:\fenix\fenixSB\etc\middleware\grantMigration\cleanAllGrantMigration.sql | mysql fenix
echo                           Done

echo .
echo *****************ALL MIGRATION TASKS OVER! HALT :)
echo *****************Grant Migration 2004 by Pica-Barbosa - DONE!