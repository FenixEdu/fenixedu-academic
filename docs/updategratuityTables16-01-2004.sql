alter table PAYMENT_PHASE add ACK_OPT_LOCK int(11) default NULL after DESCRIPTION;
alter table PAYMENT_PHASE change START_PAYMENT START_DATE date;
alter table PAYMENT_PHASE change END_PAYMENT END_DATE date;

