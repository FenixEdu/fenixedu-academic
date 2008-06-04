alter table CONTENT change column KEY_FILE_ITEM KEY_FILE INTEGER(11);
alter table CONTENT add index(KEY_FILE);

update FILE set OJB_CONCRETE_CLASS='net.sourceforge.fenixedu.domain.FileContent' WHERE OJB_CONCRETE_CLASS='net.sourceforge.fenixedu.domain.FileItem';