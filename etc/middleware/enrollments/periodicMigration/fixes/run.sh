# mysql fenix < general_enrolment_and_enrolmentEvaluation_updates.sql | mysql fenix
# mysql fenix < update_enrolment_set_state1.sql | mysql fenix
# mysql fenix < update_enrolment_set_state2.sql | mysql fenix
# mysql fenix < update_enrolment_set_state6.sql | mysql fenix

mysql fenix < general_enrolment_and_enrolmentEvaluation_updates.sql > temp1.sql
mysql fenix < temp1.sql
mysql fenix < update_enrolment_set_state1.sql > temp2.sql
mysql fenix < temp2.sql
mysql fenix < update_enrolment_set_state2.sql > temp3.sql
mysql fenix < temp3.sql
mysql fenix < update_enrolment_set_state6.sql > temp4.sql
mysql fenix < temp4.sql

mysql fenix < cleanOJB_HL_SEQ_Table.sql

# rm -rf temp1.sql
# rm -rf temp2.sql
# rm -rf temp3.sql
# rm -rf temp4.sql
