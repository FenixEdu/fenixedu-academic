#------------------------------
# Table structure for SENT_SMS
#------------------------------
drop table if exists SENT_SMS;
create table SENT_SMS(
   ID_INTERNAL integer(11) not null auto_increment,
   ACK_OPT_LOCK int(11),
   KEY_PERSON integer(11) not null,
   DESTINATION_NUMBER integer(11) not null,
   SEND_DATE datetime NOT NULL default '0000-00-00 00:00:00',
   DELIVERY_DATE datetime NOT NULL default '0000-00-00 00:00:00',
   DELIVERY_TYPE integer(11) not null default '0',
   primary key (ID_INTERNAL)) type=InnoDB;