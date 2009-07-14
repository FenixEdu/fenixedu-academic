set autocommit = 0;
begin;

select @xpto:=null;
select @xpto:=FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_ID from FF$DOMAIN_CLASS_INFO where FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.functionalities.ExecutionPath';
update EXECUTION_PATH set OID = (@xpto << 32) + ID_INTERNAL;
select @xpto:=null;
select @xpto:=FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_ID from FF$DOMAIN_CLASS_INFO where FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.AssemblySite';
update CONTENT set OID = (@xpto << 32) + ID_INTERNAL where OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.AssemblySite';
select @xpto:=null;
select @xpto:=FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_ID from FF$DOMAIN_CLASS_INFO where FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.DegreeSite';
update CONTENT set OID = (@xpto << 32) + ID_INTERNAL where OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.DegreeSite';
select @xpto:=null;
select @xpto:=FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_ID from FF$DOMAIN_CLASS_INFO where FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.DepartmentSite';
update CONTENT set OID = (@xpto << 32) + ID_INTERNAL where OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.DepartmentSite';
select @xpto:=null;
select @xpto:=FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_ID from FF$DOMAIN_CLASS_INFO where FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.ExecutionCourseSite';
update CONTENT set OID = (@xpto << 32) + ID_INTERNAL where OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.ExecutionCourseSite';
select @xpto:=null;
select @xpto:=FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_ID from FF$DOMAIN_CLASS_INFO where FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.InstitutionSite';
update CONTENT set OID = (@xpto << 32) + ID_INTERNAL where OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.InstitutionSite';
select @xpto:=null;
select @xpto:=FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_ID from FF$DOMAIN_CLASS_INFO where FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.Item';
update CONTENT set OID = (@xpto << 32) + ID_INTERNAL where OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.Item';
select @xpto:=null;
select @xpto:=FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_ID from FF$DOMAIN_CLASS_INFO where FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.ManagementCouncilSite';
update CONTENT set OID = (@xpto << 32) + ID_INTERNAL where OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.ManagementCouncilSite';
select @xpto:=null;
select @xpto:=FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_ID from FF$DOMAIN_CLASS_INFO where FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.PedagogicalCouncilSite';
update CONTENT set OID = (@xpto << 32) + ID_INTERNAL where OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.PedagogicalCouncilSite';
select @xpto:=null;
select @xpto:=FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_ID from FF$DOMAIN_CLASS_INFO where FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.ResearchUnitSite';
update CONTENT set OID = (@xpto << 32) + ID_INTERNAL where OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.ResearchUnitSite';
select @xpto:=null;
select @xpto:=FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_ID from FF$DOMAIN_CLASS_INFO where FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.ScientificAreaSite';
update CONTENT set OID = (@xpto << 32) + ID_INTERNAL where OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.ScientificAreaSite';
select @xpto:=null;
select @xpto:=FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_ID from FF$DOMAIN_CLASS_INFO where FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.ScientificCouncilSite';
update CONTENT set OID = (@xpto << 32) + ID_INTERNAL where OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.ScientificCouncilSite';
select @xpto:=null;
select @xpto:=FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_ID from FF$DOMAIN_CLASS_INFO where FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.Section';
update CONTENT set OID = (@xpto << 32) + ID_INTERNAL where OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.Section';
select @xpto:=null;
select @xpto:=FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_ID from FF$DOMAIN_CLASS_INFO where FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.Site';
update CONTENT set OID = (@xpto << 32) + ID_INTERNAL where OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.Site';
select @xpto:=null;
select @xpto:=FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_ID from FF$DOMAIN_CLASS_INFO where FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.StudentsSite';
update CONTENT set OID = (@xpto << 32) + ID_INTERNAL where OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.StudentsSite';
select @xpto:=null;
select @xpto:=FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_ID from FF$DOMAIN_CLASS_INFO where FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.TutorSite';
update CONTENT set OID = (@xpto << 32) + ID_INTERNAL where OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.TutorSite';
select @xpto:=null;
select @xpto:=FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_ID from FF$DOMAIN_CLASS_INFO where FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.UnitSite';
update CONTENT set OID = (@xpto << 32) + ID_INTERNAL where OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.UnitSite';
select @xpto:=null;
select @xpto:=FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_ID from FF$DOMAIN_CLASS_INFO where FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.contents.Attachment';
update CONTENT set OID = (@xpto << 32) + ID_INTERNAL where OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.contents.Attachment';
select @xpto:=null;
select @xpto:=FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_ID from FF$DOMAIN_CLASS_INFO where FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.contents.Container';
update CONTENT set OID = (@xpto << 32) + ID_INTERNAL where OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.contents.Container';
select @xpto:=null;
select @xpto:=FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_ID from FF$DOMAIN_CLASS_INFO where FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.contents.Content';
update CONTENT set OID = (@xpto << 32) + ID_INTERNAL, CREATION_DATE = CREATION_DATE where OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.contents.Content';
select @xpto:=null;
select @xpto:=FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_ID from FF$DOMAIN_CLASS_INFO where FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.contents.DateOrderedNode';
update NODE set OID = (@xpto << 32) + ID_INTERNAL where OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.contents.DateOrderedNode';
select @xpto:=null;
select @xpto:=FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_ID from FF$DOMAIN_CLASS_INFO where FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.contents.Element';
update CONTENT set OID = (@xpto << 32) + ID_INTERNAL where OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.contents.Element';
select @xpto:=null;
select @xpto:=FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_ID from FF$DOMAIN_CLASS_INFO where FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.contents.ExplicitOrderNode';
update NODE set OID = (@xpto << 32) + ID_INTERNAL where OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.contents.ExplicitOrderNode';
select @xpto:=null;
select @xpto:=FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_ID from FF$DOMAIN_CLASS_INFO where FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.contents.FunctionalityCall';
update CONTENT set OID = (@xpto << 32) + ID_INTERNAL where OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.contents.FunctionalityCall';
select @xpto:=null;
select @xpto:=FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_ID from FF$DOMAIN_CLASS_INFO where FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.contents.MetaDomainObjectPortal';
update CONTENT set OID = (@xpto << 32) + ID_INTERNAL where OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.contents.MetaDomainObjectPortal';
select @xpto:=null;
select @xpto:=FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_ID from FF$DOMAIN_CLASS_INFO where FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.contents.Node';
update NODE set OID = (@xpto << 32) + ID_INTERNAL where OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.contents.Node';
select @xpto:=null;
select @xpto:=FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_ID from FF$DOMAIN_CLASS_INFO where FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.contents.Portal';
update CONTENT set OID = (@xpto << 32) + ID_INTERNAL where OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.contents.Portal';
select @xpto:=null;
select @xpto:=FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_ID from FF$DOMAIN_CLASS_INFO where FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.functionalities.AvailabilityPolicy';
update AVAILABILITY_POLICY set OID = (@xpto << 32) + ID_INTERNAL where OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.functionalities.AvailabilityPolicy';
select @xpto:=null;
select @xpto:=FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_ID from FF$DOMAIN_CLASS_INFO where FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.functionalities.ExpressionGroupAvailability';
update AVAILABILITY_POLICY set OID = (@xpto << 32) + ID_INTERNAL where OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.functionalities.ExpressionGroupAvailability';
select @xpto:=null;
select @xpto:=FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_ID from FF$DOMAIN_CLASS_INFO where FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.functionalities.Functionality';
update CONTENT set OID = (@xpto << 32) + ID_INTERNAL where OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.functionalities.Functionality';
select @xpto:=null;
select @xpto:=FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_ID from FF$DOMAIN_CLASS_INFO where FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.functionalities.GroupAvailability';
update AVAILABILITY_POLICY set OID = (@xpto << 32) + ID_INTERNAL where OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.functionalities.GroupAvailability';
select @xpto:=null;
select @xpto:=FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_ID from FF$DOMAIN_CLASS_INFO where FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.functionalities.Module';
update CONTENT set OID = (@xpto << 32) + ID_INTERNAL where OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.functionalities.Module';
select @xpto:=null;
select @xpto:=FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_ID from FF$DOMAIN_CLASS_INFO where FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.homepage.Homepage';
update CONTENT set OID = (@xpto << 32) + ID_INTERNAL where OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.homepage.Homepage';
select @xpto:=null;
select @xpto:=FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_ID from FF$DOMAIN_CLASS_INFO where FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.messaging.Announcement';
update CONTENT set OID = (@xpto << 32) + ID_INTERNAL, LAST_MODIFICATION = LAST_MODIFICATION, PUBLICATION_BEGIN = PUBLICATION_BEGIN, PUBLICATION_END = PUBLICATION_END, REFERED_SUBJECT_BEGIN = REFERED_SUBJECT_BEGIN, REFERED_SUBJECT_END = REFERED_SUBJECT_END where OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.messaging.Announcement';
select @xpto:=null;
select @xpto:=FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_ID from FF$DOMAIN_CLASS_INFO where FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard';
update CONTENT set OID = (@xpto << 32) + ID_INTERNAL where OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard';
select @xpto:=null;
select @xpto:=FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_ID from FF$DOMAIN_CLASS_INFO where FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.messaging.AnnouncementNode';
update NODE set OID = (@xpto << 32) + ID_INTERNAL where OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.messaging.AnnouncementNode';
select @xpto:=null;
select @xpto:=FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_ID from FF$DOMAIN_CLASS_INFO where FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.messaging.ConversationMessage';
update CONTENT set OID = (@xpto << 32) + ID_INTERNAL where OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.messaging.ConversationMessage';
select @xpto:=null;
select @xpto:=FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_ID from FF$DOMAIN_CLASS_INFO where FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.messaging.ConversationThread';
update CONTENT set OID = (@xpto << 32) + ID_INTERNAL where OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.messaging.ConversationThread';
select @xpto:=null;
select @xpto:=FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_ID from FF$DOMAIN_CLASS_INFO where FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.messaging.ExecutionCourseAnnouncementBoard';
update CONTENT set OID = (@xpto << 32) + ID_INTERNAL where OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.messaging.ExecutionCourseAnnouncementBoard';
select @xpto:=null;
select @xpto:=FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_ID from FF$DOMAIN_CLASS_INFO where FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.messaging.ExecutionCourseForum';
update CONTENT set OID = (@xpto << 32) + ID_INTERNAL where OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.messaging.ExecutionCourseForum';
select @xpto:=null;
select @xpto:=FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_ID from FF$DOMAIN_CLASS_INFO where FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.messaging.Forum';
update CONTENT set OID = (@xpto << 32) + ID_INTERNAL where OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.messaging.Forum';
select @xpto:=null;
select @xpto:=FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_ID from FF$DOMAIN_CLASS_INFO where FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.messaging.PartyAnnouncementBoard';
update CONTENT set OID = (@xpto << 32) + ID_INTERNAL where OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.messaging.PartyAnnouncementBoard';
select @xpto:=null;
select @xpto:=FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_ID from FF$DOMAIN_CLASS_INFO where FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.messaging.UnitAnnouncementBoard';
update CONTENT set OID = (@xpto << 32) + ID_INTERNAL where OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.messaging.UnitAnnouncementBoard';
select @xpto:=null;
select @xpto:=FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_ID from FF$DOMAIN_CLASS_INFO where FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.thesis.ThesisSite';
update CONTENT set OID = (@xpto << 32) + ID_INTERNAL where OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.thesis.ThesisSite';
update CONTENT as t1, EXECUTION_COURSE as t2 set t1.OID_EXECUTION_COURSE = t2.OID where t2.ID_INTERNAL = t1.KEY_EXECUTION_COURSE;
update CONTENT as t1, THESIS as t2 set t1.OID_THESIS = t2.OID where t2.ID_INTERNAL = t1.KEY_THESIS;
update CONTENT as t1, CONTENT as t2 set t1.OID_FUNCTIONALITY = t2.OID where t2.ID_INTERNAL = t1.KEY_FUNCTIONALITY;
update EXECUTION_PATH as t1, CONTENT as t2 set t1.OID_FUNCTIONALITY = t2.OID where t2.ID_INTERNAL = t1.KEY_FUNCTIONALITY;
update EXECUTION_PATH as t1, ROOT_DOMAIN_OBJECT as t2 set t1.OID_ROOT_DOMAIN_OBJECT = t2.OID where t2.ID_INTERNAL = t1.KEY_ROOT_DOMAIN_OBJECT;
update CONTENT as t1, EXECUTION_PATH as t2 set t1.OID_EXECUTION_PATH_VALUE = t2.OID where t2.ID_INTERNAL = t1.KEY_EXECUTION_PATH_VALUE;
update ANNOUNCEMENT_BOARD_BOOKMARK as t1, PARTY as t2 set t1.OID_PERSON = t2.OID where t2.ID_INTERNAL = t1.KEY_PERSON;
update ANNOUNCEMENT_BOARD_BOOKMARK as t1, CONTENT as t2 set t1.OID_ANNOUNCEMENT_BOARD = t2.OID where t2.ID_INTERNAL = t1.KEY_ANNOUNCEMENT_BOARD;
update CONTENT as t1, FILE as t2 set t1.OID_LOGO = t2.OID where t2.ID_INTERNAL = t1.KEY_LOGO;
update UNIT_SITE_MANAGERS as t1, PARTY as t2 set t1.OID_PERSON = t2.OID where t2.ID_INTERNAL = t1.KEY_PERSON;
update UNIT_SITE_MANAGERS as t1, CONTENT as t2 set t1.OID_UNIT_SITE = t2.OID where t2.ID_INTERNAL = t1.KEY_UNIT_SITE;
update CONTENT as t1, PARTY as t2 set t1.OID_UNIT = t2.OID where t2.ID_INTERNAL = t1.KEY_UNIT;
update CONTENT as t1, EXECUTION_COURSE as t2 set t1.OID_SITE_EXECUTION_COURSE = t2.OID where t2.ID_INTERNAL = t1.KEY_SITE_EXECUTION_COURSE;
update CONTENT as t1, FILE as t2 set t1.OID_FILE = t2.OID where t2.ID_INTERNAL = t1.KEY_FILE;
update AVAILABILITY_POLICY as t1, CONTENT as t2 set t1.OID_CONTENT = t2.OID where t2.ID_INTERNAL = t1.KEY_CONTENT;
update AVAILABILITY_POLICY as t1, ROOT_DOMAIN_OBJECT as t2 set t1.OID_ROOT_DOMAIN_OBJECT = t2.OID where t2.ID_INTERNAL = t1.KEY_ROOT_DOMAIN_OBJECT;
update CONTENT as t1, DEGREE as t2 set t1.OID_DEGREE = t2.OID where t2.ID_INTERNAL = t1.KEY_DEGREE;
update NODE as t1, CONTENT as t2 set t1.OID_CHILD = t2.OID where t2.ID_INTERNAL = t1.KEY_CHILD;
update NODE as t1, CONTENT as t2 set t1.OID_PARENT = t2.OID where t2.ID_INTERNAL = t1.KEY_PARENT;
update NODE as t1, ROOT_DOMAIN_OBJECT as t2 set t1.OID_ROOT_DOMAIN_OBJECT = t2.OID where t2.ID_INTERNAL = t1.KEY_ROOT_DOMAIN_OBJECT;
update CONTENT as t1, PARTY as t2 set t1.OID_PERSON = t2.OID where t2.ID_INTERNAL = t1.KEY_PERSON;
update ANNOUNCEMENT_CATEGORY_ANNOUNCEMENT as t1, ANNOUNCEMENT_CATEGORY as t2 set t1.OID_ANNOUNCEMENT_CATEGORY = t2.OID where t2.ID_INTERNAL = t1.KEY_ANNOUNCEMENT_CATEGORY;
update ANNOUNCEMENT_CATEGORY_ANNOUNCEMENT as t1, CONTENT as t2 set t1.OID_ANNOUNCEMENT = t2.OID where t2.ID_INTERNAL = t1.KEY_ANNOUNCEMENT;
update CONTENT as t1, RESOURCE as t2 set t1.OID_CAMPUS = t2.OID, t1.LAST_MODIFICATION = t1.LAST_MODIFICATION, t1.PUBLICATION_BEGIN = t1.PUBLICATION_BEGIN, t1.PUBLICATION_END = t1.PUBLICATION_END, t1.REFERED_SUBJECT_BEGIN = t1.REFERED_SUBJECT_BEGIN, t1.REFERED_SUBJECT_END = t1.REFERED_SUBJECT_END where t2.ID_INTERNAL = t1.KEY_CAMPUS;
update CONTENT as t1, ROOT_DOMAIN_OBJECT as t2 set t1.OID_PORTAL_ROOT_DOMAIN_OBJECT = t2.OID where t2.ID_INTERNAL = t1.KEY_PORTAL_ROOT_DOMAIN_OBJECT;
update CONTENT as t1, CONTENT as t2 set t1.OID_INITIAL_CONTENT = t2.OID where t2.ID_INTERNAL = t1.KEY_INITIAL_CONTENT;
update CONTENT as t1, PARTY as t2 set t1.OID_PARTY = t2.OID where t2.ID_INTERNAL = t1.KEY_PARTY;
update CONTENT as t1, ROOT_DOMAIN_OBJECT as t2 set t1.OID_MODULE_ROOT_DOMAIN_OBJECT = t2.OID where t2.ID_INTERNAL = t1.KEY_MODULE_ROOT_DOMAIN_OBJECT;
update CONTENT as t1, META_DOMAIN_OBJECT as t2 set t1.OID_META_DOMAIN_OBJECT = t2.OID where t2.ID_INTERNAL = t1.KEY_META_DOMAIN_OBJECT;
update CONTENT as t1, CONTENT as t2 set t1.OID_PORTAL = t2.OID, t1.CREATION_DATE = t1.CREATION_DATE where t2.ID_INTERNAL = t1.KEY_PORTAL;
update CONTENT as t1, AVAILABILITY_POLICY as t2 set t1.OID_AVAILABILITY_POLICY = t2.OID, t1.CREATION_DATE = t1.CREATION_DATE where t2.ID_INTERNAL = t1.KEY_AVAILABILITY_POLICY;
update CONTENT as t1, ROOT_DOMAIN_OBJECT as t2 set t1.OID_ROOT_DOMAIN_OBJECT = t2.OID, t1.CREATION_DATE = t1.CREATION_DATE where t2.ID_INTERNAL = t1.KEY_ROOT_DOMAIN_OBJECT;
update CONTENT as t1, PARTY as t2 set t1.OID_CREATOR = t2.OID, t1.CREATION_DATE = t1.CREATION_DATE where t2.ID_INTERNAL = t1.KEY_CREATOR;

commit;



begin;

create temporary table _XPTO_
   select CURRICULUM_LINE_LOG.ID_INTERNAL
   from CURRICULUM_LINE_LOG left join REGISTRATION on REGISTRATION.ID_INTERNAL = CURRICULUM_LINE_LOG.KEY_STUDENT
   where CURRICULUM_LINE_LOG.KEY_STUDENT is not null and REGISTRATION.ID_INTERNAL is null;
   
delete from CURRICULUM_LINE_LOG where CURRICULUM_LINE_LOG.ID_INTERNAL in (select * from _XPTO_);

drop temporary table _XPTO_;

commit;



begin;

create temporary table _XPTO_
   select TEST_QUESTION.ID_INTERNAL
   from TEST_QUESTION left join TEST on TEST.ID_INTERNAL = TEST_QUESTION.KEY_TEST
   where TEST_QUESTION.KEY_TEST is not null and TEST.ID_INTERNAL is null;
   
delete from TEST_QUESTION where TEST_QUESTION.ID_INTERNAL in (select * from _XPTO_);

drop temporary table _XPTO_;

commit;



begin;

create temporary table _XPTO_
   select EXECUTION_DEGREE.ID_INTERNAL
   from EXECUTION_DEGREE left join OCCUPATION_PERIOD on OCCUPATION_PERIOD.ID_INTERNAL = EXECUTION_DEGREE.KEY_PERIOD_EXAMS_SPECIAL_SEASON
   where EXECUTION_DEGREE.KEY_PERIOD_EXAMS_SPECIAL_SEASON is not null and OCCUPATION_PERIOD.ID_INTERNAL is null;

update EXECUTION_DEGREE, _XPTO_
set EXECUTION_DEGREE.KEY_PERIOD_EXAMS_SPECIAL_SEASON = null
where EXECUTION_DEGREE.ID_INTERNAL = _XPTO_.ID_INTERNAL;

drop temporary table _XPTO_;

commit;



begin;

create temporary table _XPTO_
   select EXPORT_GROUPING.ID_INTERNAL
   from EXPORT_GROUPING left join GROUPING on GROUPING.ID_INTERNAL = EXPORT_GROUPING.KEY_GROUPING
   where EXPORT_GROUPING.KEY_GROUPING is not null and GROUPING.ID_INTERNAL is null;
   
delete from EXPORT_GROUPING where EXPORT_GROUPING.ID_INTERNAL in (select * from _XPTO_);

drop temporary table _XPTO_;

commit;



begin;

create temporary table _XPTO_
   select CURRICULUM_MODULE.ID_INTERNAL
   from CURRICULUM_MODULE left join STUDENT_CURRICULAR_PLAN on STUDENT_CURRICULAR_PLAN.ID_INTERNAL = CURRICULUM_MODULE.KEY_STUDENT_CURRICULAR_PLAN
   where CURRICULUM_MODULE.KEY_STUDENT_CURRICULAR_PLAN is not null and STUDENT_CURRICULAR_PLAN.ID_INTERNAL is null;

update CURRICULUM_MODULE, _XPTO_
set CURRICULUM_MODULE.KEY_STUDENT_CURRICULAR_PLAN = null
where CURRICULUM_MODULE.ID_INTERNAL = _XPTO_.ID_INTERNAL;

drop temporary table _XPTO_;

commit;



begin;

create temporary table _XPTO_
   select GROUP_STUDENT.ID_INTERNAL
   from GROUP_STUDENT left join REGISTRATION on REGISTRATION.ID_INTERNAL = GROUP_STUDENT.KEY_STUDENT
   where GROUP_STUDENT.KEY_STUDENT is not null and REGISTRATION.ID_INTERNAL is null;
   
delete from GROUP_STUDENT where GROUP_STUDENT.ID_INTERNAL in (select * from _XPTO_);

drop temporary table _XPTO_;

commit;







