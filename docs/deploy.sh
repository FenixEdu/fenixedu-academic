mysql -Dfenix -p < fixForDeploy-29-10-2003.sql > temp.sql
mysql -Dfenix -p < temp.sql | mysql -Dfenix
