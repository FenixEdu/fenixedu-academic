#!/bin/bash
#
# This script should be use to detect tables
# in the database that are using MyISAM engine 
# instead of InnoDB.
#
# Usage: ./detectMyISAMTables.sh <DATABASE_NAME> <USER>
# Output: list of tables in tablesWithMyISAM.txt file
#
# For more information please email 
# fenix-dev@mlists.ist.utl.pt or 
# paulo.abrantes@ist.utl.pt
#
# ********ATTENTION*********** 
# The tables LAST_TX_PROCESSED and TRANSACTION_STATISTICS
# __MUST STAY__ in MyISAM. Modifying these two tables to
# InnoDB will break the application.
# ****************************

USER=$2
DATABASE=$1

mysql -u $USER -D $DATABASE -e 'show tables' | grep "[A-Z]" | sed -e 's/\(.*\)/show create table \1;/' | grep -v Tables_in_$DATABASE > showTables.sql
mysql -u $USER -D $DATABASE -f < showTables.sql > createTables.sql
grep MyISAM createTables.sql | awk '{print $1}' > tablesWithMyISAM.txt
rm showTables.sql createTables.sql

echo "Tables for analysis can be found in tablesWithMyISAM.txt"
