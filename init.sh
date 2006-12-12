mysqladmin -uroot drop $1
mysqladmin -uroot create $1
mysql -uroot $1 < etc/dml.sql
mysql -uroot $1 < etc/ojb.sql
mysql -uroot $1 < etc/fenix-base.sql
ant -f build_dataBaseUtils.xml generate-fenix-sql
mysql -uroot $1 < etc/fenix.sql
mysql -uroot $1 < etc/functionalities.sql
mysql -uroot $1 < etc/units.sql
ant -f build_dataBaseUtils.xml initialize-database
echo "update IDENTIFICATION, LOGIN_ALIAS set IDENTIFICATION.PASSWORD = md5('pass') where IDENTIFICATION.ID_INTERNAL = LOGIN_ALIAS.KEY_LOGIN and LOGIN_ALIAS.ALIAS = 'admin';" > .tmp.init
mysql -uroot $1 < .tmp.init
rm -rf .tmp.init
