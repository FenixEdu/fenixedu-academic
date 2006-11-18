mysqladmin -uroot drop isa
mysqladmin -uroot create isa
mysql -uroot isa < ~/tmp/fenixprepatch.sql
mysql -f -uroot isa < /tmp/fixIsaDb.sql
mysql -f -uroot isa < /tmp/autoSB.sql
ant -f build_dataBaseUtils.xml prepare-full-run-file -Dfrom=2005-12-27
cd etc
./run isa root
cd ..
mysql -f -uroot isa < /tmp/roles.sql
mysql -f -uroot isa < etc/units.sql