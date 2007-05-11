rename table TEACHER_LEGAL_REGIMEM to LEGAL_REGIMEN;
alter table LEGAL_REGIMEN add column `OJB_CONCRETE_CLASS` varchar(255) NOT NULL default '';
alter table LEGAL_REGIMEN change column KEY_TEACHER KEY_TEACHER int(11) default NULL;
alter table LEGAL_REGIMEN add column KEY_EMPLOYEE int(11) default NULL;
update LEGAL_REGIMEN set OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.teacher.TeacherLegalRegimen';