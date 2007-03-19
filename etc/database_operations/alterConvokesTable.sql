update CONVOKE set ATTENDED_TO_CONVOKE=1, ACTIVE=1 where OJB_CONCRETE_CLASS='net.sourceforge.fenixedu.domain.vigilancy.Vigilancy';

update CONVOKE set OJB_CONCRETE_CLASS='net.sourceforge.fenixedu.domain.vigilancy.OwnCourseVigilancy' where OJB_CONCRETE_CLASS='net.sourceforge.fenixedu.domain.vigilancy.Vigilancy';

update CONVOKE set OJB_CONCRETE_CLASS='net.sourceforge.fenixedu.domain.vigilancy.OtherCourseVigilancy' where OJB_CONCRETE_CLASS='net.sourceforge.fenixedu.domain.vigilancy.VigilancyWithCredits';

 alter table CONVOKE add column STATUS;
 update CONVOKE set STATUS='ATTENDED' where ATTENDED_TO_CONVOKE=1;
 update CONVOKE set STATUS='NOT_ATTENDED' where ATTENDED_TO_CONVOKE=0;
 alter table CONVOKE drop column ATTENDED_TO_CONVOKE;
