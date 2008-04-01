mysqladmin -uroot drop $1
mysqladmin -uroot create $1
mysql -uroot $1 < etc/dml.sql
mysql -uroot $1 < etc/ojb.sql
ant -f build_dataBaseUtils.xml generate-fenix-sql
mysql -uroot $1 < etc/fenix.sql
ant -f build_dataBaseUtils.xml initialize-database
echo "update IDENTIFICATION, LOGIN_ALIAS set IDENTIFICATION.PASSWORD = md5('pass') where IDENTIFICATION.ID_INTERNAL = LOGIN_ALIAS.KEY_LOGIN and LOGIN_ALIAS.ALIAS = 'admin';" > .tmp.init
mysql -uroot $1 < .tmp.init
rm -rf .tmp.init
cat <<EOF
You should now obtain a copy of an up-to-date dump of the 
content structure to conclude the initialization of the database.

After the download, run the command

  mysql -uroot $1 < content-structure.sql

EOF
