mysqladmin -uroot drop isa
mysqladmin -uroot create isa
mysql -uroot isa < etc/dml.sql
mysql -uroot isa < etc/ojb.sql
mysql -uroot isa < etc/fenix-base.sql
ant -f build_dataBaseUtils.xml generate-fenix-sql
mysql -uroot isa < etc/fenix.sql
mysql -uroot isa < etc/functionalities.sql
mysql -uroot isa < etc/units.sql
ant -f build_dataBaseUtils.xml initialize-database
echo "update IDENTIFICATION, LOGIN_ALIAS set IDENTIFICATION.PASSWORD = md5('pass') where IDENTIFICATION.ID_INTERNAL = LOGIN_ALIAS.KEY_LOGIN and LOGIN_ALIAS.ALIAS = 'admin';" > .tmp.init
mysql -uroot isa < .tmp.init
rm -rf .tmp.init