call mysql ciapl < alter_table_restriction.sql
call ant -f ..\..\build_enrollmentDataMigration.xml migrate-all-ileec-data
call mysql ciapl < update_curricular_course_set_mandatory1.sql | mysql ciapl
call mysql ciapl < update_curricular_course_set_mandatory0.sql | mysql ciapl
call mysql ciapl < update_curricular_course_set_type1.sql | mysql ciapl
call mysql ciapl < update_curricular_course_set_type2.sql | mysql ciapl
call mysql ciapl < update_curricular_course_set_type4.sql | mysql ciapl
call mysql ciapl < update_credits_and_precedences.sql
call mysql ciapl < delete_from_enrolment_evaluation.sql > temp1.sql
call mysql ciapl < update_enrolment_set_state1.sql | mysql ciapl
call mysql ciapl < update_enrolment_set_state2.sql | mysql ciapl
call mysql ciapl < temp.sql
call mysql ciapl < enrollmentTableUpdates.sql > temp2.sql
call del temp1.sql
call del temp2.sql
