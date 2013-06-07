alter table `TEST_SCOPE` add `OID_EXECUTION_COURSE` bigint unsigned, add index (OID_EXECUTION_COURSE);
alter table `EXECUTION_COURSE` add `OID_TEST_SCOPE` bigint unsigned, add index (OID_TEST_SCOPE);

set @xpto = (SELECT DOMAIN_CLASS_ID FROM FF$DOMAIN_CLASS_INFO where DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.ExecutionCourse') << 32;
update `TEST_SCOPE` SET `OID_EXECUTION_COURSE` = @xpto + `KEY_CLASS`;

update `EXECUTION_COURSE` join `TEST_SCOPE` on `OID_EXECUTION_COURSE` = `EXECUTION_COURSE`.OID set `OID_TEST_SCOPE` = `TEST_SCOPE`.OID;
