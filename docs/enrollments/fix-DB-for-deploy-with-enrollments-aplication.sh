mysql fenix < alter_table_restriction.sql
ant -f ../../build_enrollmentDataMigration.xml migrate-all-ileec-data
mysql fenix < update_curricular_course_set_mandatory1.sql | mysql fenix
mysql fenix < update_curricular_course_set_mandatory0.sql | mysql fenix
mysql fenix < update_curricular_course_set_type1.sql | mysql fenix
mysql fenix < update_curricular_course_set_type2.sql | mysql fenix
mysql fenix < update_curricular_course_set_type4.sql | mysql fenix
mysql fenix < update_credits_and_precedences.sql
mysql fenix < delete_from_enrolment_evaluation.sql > temp1.sql
mysql fenix < update_enrolment_set_state1.sql | mysql fenix
mysql fenix < update_enrolment_set_state2.sql | mysql fenix
mysql fenix < temp.sql
mysql fenix < enrollmentTableUpdates.sql > temp2.sql
rm -rf temp1.sql
rm -rf temp2.sql
