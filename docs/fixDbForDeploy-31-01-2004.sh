mysql -f fenix < fixDbForDeploy1-31-01-2004.sql > temp1.sql
mysql -f fenix < fixDbForDeploy2-31-01-2004.sql
mysql -f fenix < temp1.sql
mysql -f fenix < fixDbForDeploy3-31-01-2004.sql > temp2.sql
mysql -f fenix < temp2.sql
mysql -f fenix < fixDbForDeploy4-31-01-2004.sql
mysql -f fenix < fixDbForDeploy5-31-01-2004.sql > temp3.sql
mysql -f fenix < temp3.sql
mysql -f fenix < updateQualificationBeforeScipt.sql
mysql -f fenix < updateQualificationScript1.sql | mysql fenix
mysql -f fenix < updateQualificationScript2.sql | mysql fenix
mysql -f fenix < updateQualificationAfterScript.sql




