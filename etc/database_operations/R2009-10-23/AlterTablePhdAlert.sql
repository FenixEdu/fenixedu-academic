alter table `PHD_ALERT` add column `SHARED` tinyint(1) null;
alter table `PHD_ALERT` add column `USER_DEFINED` tinyint(1) null;

update PHD_ALERT set SHARED='0' where SHARED IS NULL and OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.phd.alert.PhdCustomAlert';
update PHD_ALERT set USER_DEFINED='1' where USER_DEFINED IS NULL and OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.phd.alert.PhdCustomAlert';
