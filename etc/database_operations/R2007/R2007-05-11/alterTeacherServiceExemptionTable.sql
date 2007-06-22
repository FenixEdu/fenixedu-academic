rename table TEACHER_SERVICE_EXEMPTION to SERVICE_EXEMPTION;
alter table SERVICE_EXEMPTION add column `OJB_CONCRETE_CLASS` varchar(255) NOT NULL default '';
alter table SERVICE_EXEMPTION change column KEY_TEACHER KEY_TEACHER int(11) default NULL;
alter table SERVICE_EXEMPTION add column KEY_EMPLOYEE int(11) default NULL;
update SERVICE_EXEMPTION set OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.teacher.TeacherServiceExemption';