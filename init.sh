mysqladmin -uroot -p<password> drop $1
mysqladmin -uroot -p<password> create $1

ant clean-all
ant -f build_dataBaseUtils.xml pre-init-data-structure

wget --no-check-certificate https://fenix-ashes.ist.utl.pt/fenixWiki/FenixSetup\?action=AttachFile\&do=get\&target=content-structure.sql
mv FenixSetup\?action=AttachFile\&do=get\&target=content-structure.sql content-structure.sql
mysql -uroot -p<password> -f $1 < content-structure.sql 
rm -rf content-structure.sql

ant -f build_dataBaseUtils.xml initialize-database
echo "update IDENTIFICATION, LOGIN_ALIAS set IDENTIFICATION.PASSWORD = md5('pass') where IDENTIFICATION.OID = LOGIN_ALIAS.OID_LOGIN and LOGIN_ALIAS.ALIAS = 'admin';" > .tmp.init
mysql -uroot -p<password> $1 < .tmp.init
rm -rf .tmp.init
