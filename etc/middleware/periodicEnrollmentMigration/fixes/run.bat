REM mysql ciapl < general_enrolment_and_enrolmentEvaluation_updates.sql | mysql ciapl
REM mysql ciapl < update_enrolment_set_state1.sql | mysql ciapl
REM mysql ciapl < update_enrolment_set_state2.sql | mysql ciapl
REM mysql ciapl < update_enrolment_set_state6.sql | mysql ciapl

REM mysql ciapl < general_enrolment_and_enrolmentEvaluation_updates.sql > temp1.sql
mysql ciapl < temp1.sql
mysql ciapl < update_enrolment_set_state1.sql > temp2.sql
mysql ciapl < temp2.sql
mysql ciapl < update_enrolment_set_state2.sql > temp3.sql
mysql ciapl < temp3.sql
mysql ciapl < update_enrolment_set_state6.sql > temp4.sql
mysql ciapl < temp4.sql

mysql ciapl < cleanOJB_HL_SEQ_Table.sql

REM del temp1.sql
REM del temp2.sql
REM del temp3.sql
REM del temp4.sql
