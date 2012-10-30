delete from FF$DOMAIN_CLASS_INFO where DOMAIN_CLASS_ID in (1542,1546,1547,1548);
update FF$DOMAIN_CLASS_INFO set DOMAIN_CLASS_NAME = "net.sourceforge.fenixedu.domain.inquiries.InquiryStudentCycleAnswer" where DOMAIN_CLASS_ID = 1545;
update INQUIRY_ANSWER set OJB_CONCRETE_CLASS = "net.sourceforge.fenixedu.domain.inquiries.InquiryStudentCycleAnswer" where OJB_CONCRETE_CLASS like "%InquiryStudent1rstCycleAnswer%";

alter table `INQUIRY_ANSWER` add `OID_PHD_PROCESS` bigint unsigned;
alter table `PROCESS` add `OID_INQUIRY_STUDENT_CYCLE_ANSWER` bigint unsigned;