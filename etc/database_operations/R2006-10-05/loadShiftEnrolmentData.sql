update SHIFT_ENROLMENT set SHIFT_ENROLMENT.CREATED_ON = '2006-09-02 14:00:00';

drop temporary table XPTO;

create temporary table XPTO
select INVOCATION_DATE as CREATED_ON,
    substring(SERVICE_ARGUMENTS, 1, locate('[', SERVICE_ARGUMENTS) - 3) as R,
    substring(SERVICE_ARGUMENTS, locate('[', SERVICE_ARGUMENTS) + 1, length(SERVICE_ARGUMENTS) - locate('[', SERVICE_ARGUMENTS) - 3) as S
from SERVICE_LOG where SERVICE_LOG.SERVICE_NAME = 'EnrollStudentInShifts'
    and SERVICE_ARGUMENTS not like 'net.sourceforge.fenixedu.domain.student.Registration(%'
order by INVOCATION_DATE;

update SHIFT_ENROLMENT, XPTO set SHIFT_ENROLMENT.CREATED_ON = XPTO.CREATED_ON
where XPTO.S = SHIFT_ENROLMENT.KEY_SHIFT and XPTO.R = SHIFT_ENROLMENT.KEY_REGISTRATION;

drop temporary table XPTO;

create temporary table XPTO
select INVOCATION_DATE as CREATED_ON,
    substring(SERVICE_ARGUMENTS, locate('(', SERVICE_ARGUMENTS) + 1, locate(')', SERVICE_ARGUMENTS) - locate('(', SERVICE_ARGUMENTS) - 1) as R,
    substring(SERVICE_ARGUMENTS, locate(')', SERVICE_ARGUMENTS) + 2, length(SERVICE_ARGUMENTS) - locate(')', SERVICE_ARGUMENTS) - 3) as S
from SERVICE_LOG where SERVICE_LOG.SERVICE_NAME = 'EnrollStudentInShifts'
    and SERVICE_ARGUMENTS like 'net.sourceforge.fenixedu.domain.student.Registration(%'
order by INVOCATION_DATE;

update SHIFT_ENROLMENT, XPTO set SHIFT_ENROLMENT.CREATED_ON = XPTO.CREATED_ON
where XPTO.S = SHIFT_ENROLMENT.KEY_SHIFT and XPTO.R = SHIFT_ENROLMENT.KEY_REGISTRATION;
