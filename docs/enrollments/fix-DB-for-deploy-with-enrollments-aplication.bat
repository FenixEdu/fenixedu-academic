call mysql ciapl < alter_table_restriction.sql
call ant -f ..\..\build_enrollmentDataMigration.xml migrate-all-ileec-data
call mysql ciapl < update_curricular_course_set_mandatory=1.sql | mysql ciapl
call mysql ciapl < update_curricular_course_set_mandatory=0.sql | mysql ciapl
call mysql ciapl < update_curricular_course_set_type=1.sql | mysql ciapl
call mysql ciapl < update_curricular_course_set_type=2.sql | mysql ciapl
call mysql ciapl < update_curricular_course_set_type=4.sql | mysql ciapl
call mysql ciapl < update_credits_and_precedences.sql
call mysql ciapl < delete_from_enrolment_evaluation.sql > temp.sql
call mysql ciapl < update_enrolment_set_state=1.sql | mysql ciapl
call mysql ciapl < update_enrolment_set_state=2.sql | mysql ciapl
call mysql ciapl < temp.sql
call del temp.sql
