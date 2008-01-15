update CONTENT SET OJB_CONCRETE_CLASS='net.sourceforge.fenixedu.domain.Section' WHERE OJB_CONCRETE_CLASS='net.sourceforge.fenixedu.domain.contents.TempSection';

alter table CONTENT ADD COLUMN KEY_INITIAL_SECTION int(11);
alter table CONTENT add index (KEY_INITIAL_SECTION);
