echo off
cls
echo .START
echo .

echo Creating tables grant...
mysql -u root -D fenix < c:\fenix\fenixSB\etc\tables_grants.sql
echo                         Done

echo Creating tables mw_bolseiros...
mysql -u root -D fenix < c:\fenix\fenixSB\etc\middleware\grantMigration\createMWGrantTables.sql
echo                                Done

echo Loading tables mw_bolseiros...
mysql -u root -D fenix < c:\fenix\fenixSB\etc\middleware\grantMigration\loadMWGrantTables.sql
echo                               Done

echo .
echo **************CORRECTING ORIGINAL DATABASE.
echo .

echo Executing mailCorrectionsBD.sql...
mysql -u root -D fenix < c:\fenix\fenixSB\docs\grant\mailCorrectionsBD.sql
echo                                   Done

echo Executing teacherDuplicatePersonCorrections.sql...
mysql -u root -D fenix < c:\fenix\fenixSB\docs\grant\teacherDuplicatePersonCorrections.sql
echo                                                   Done

echo Executing correctDuplicateIDNumberEntriesInPessoa.sql...
mysql -u root -D fenix < c:\fenix\fenixSB\docs\grant\correctDuplicateIDNumberEntriesInPessoa.sql
echo                                                         Done

echo Executing deleteTeachersWithoutPerson.sql...
mysql -u root -D fenix < c:\fenix\fenixSB\docs\grant\deleteTeachersWithoutPerson.sql
echo                                             Done

echo Executing correctUniquesInOldTables.sql...
mysql -u root -D fenix < c:\fenix\fenixSB\docs\grant\correctUniquesInOldTables.sql
echo                                           Done

echo Executing correctRepeatedProjects.sql...
mysql -u root -D fenix < c:\fenix\fenixSB\docs\grant\correctRepeatedProjects.sql
echo                                         Done

echo Executing correctDuplicatedEntriesInPaymentEntityOriginalTable.sql...
mysql -u root -D fenix < c:\fenix\fenixSB\docs\grant\correctDuplicatedEntriesInPaymentEntityOriginalTable.sql
echo                                                                      Done

echo updateWrongTeacherInGrantProject.sql...
mysql -u root -D fenix < c:\fenix\fenixSB\docs\grant\updateWrongTeacherInGrantProject.sql
echo                                        Done

echo Creating table mwgrant_migracao_pessoa...
mysql -u root -D fenix < c:\fenix\fenixSB\etc\middleware\grantMigration\createLoadMWGrant_pessoa_auxiliar_table.sql
echo                                          Done

echo Creating table mwgrant_migracao_docente...
mysql -u root -D fenix < c:\fenix\fenixSB\etc\middleware\grantMigration\createLoadMWGrant_docente_auxiliar_table.sql
echo                                           Done

echo Erasing dead objects (lost references)...
mysql -u root -D fenix < c:\fenix\fenixSB\etc\middleware\grantMigration\eraseDeadObjects.sql | mysql fenix
echo                                          Done

echo .
echo *****************MIGRATING TABLES.
echo .

echo Migrating Bolseiro to Grant Owner...
mysql -u root -D fenix < c:\fenix\fenixSB\etc\middleware\grantMigration\migrateMWGrant_bolseiroToGrantOwner.sql | mysql fenix
echo                                     Done

echo Migrating Comparticipacao to Grant Part...
mysql -u root -D fenix < c:\fenix\fenixSB\etc\middleware\grantMigration\migrateMWGrant_comparticipacaoToGrantPart.sql | mysql fenix
echo                                           Done

echo Migrating Contrato to Grant Contract...
mysql -u root -D fenix < c:\fenix\fenixSB\etc\middleware\grantMigration\migrateMWGrant_contratoToGrantContract.sql | mysql fenix
echo                                        Done

echo Migrating Orientacao to Grant Orientation...
mysql -u root -D fenix < c:\fenix\fenixSB\etc\middleware\grantMigration\migrateMWGrant_orientacaoToGrantOrientationTeacher.sql | mysql fenix
echo                                             Done

echo Migrating Tipos de Bolsa to Grant Type...
mysql -u root -D fenix < c:\fenix\fenixSB\etc\middleware\grantMigration\migrateMWGrant_tipobolsaToGrantType.sql | mysql fenix
echo                                          Done

echo Migrating Subsidio to Grant Subsidy...
mysql -u root -D fenix < c:\fenix\fenixSB\etc\middleware\grantMigration\migrateMWGrant_subsidioToGrantSubsidy.sql | mysql fenix
echo                                       Done

echo Migrating Entidade Pagadora to Grant Payment Entity...
mysql -u root -D fenix < c:\fenix\fenixSB\etc\middleware\grantMigration\migrateMWGrant_entidadepagadoraToGrantPaymentEntity.sql | mysql fenix
echo                                                       Done

echo Creating New Roles for GrantOwner and GrantOwnerManager...
mysql -u root -D fenix < c:\fenix\fenixSB\etc\middleware\grantMigration\createFenixGrantsRoles.sql
echo                                                           Done

echo DELETE OJB_HL_SEQ...
mysql -u root -D fenix < c:\fenix\fenixSB\etc\middleware\grantMigration\deleteOJB_HL_SEQ.sql
echo                     Done

echo .
echo **** Grant Migration 2004 by Pica-Barbosa - PART 1 SUCCESSFULLY COMPLETED
echo **** NEXT PART: Java migration (ant -f buid_migration.xml migrate-bolseiros)