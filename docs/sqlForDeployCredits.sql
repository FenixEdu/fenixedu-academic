update ROLE set portal_sub_application = '/facultyAdmOffice' where id_internal = 13;

drop table if exists crd_MANAGEMENT_POSITION_CREDIT_LINE;
create table crd_MANAGEMENT_POSITION_CREDIT_LINE (
   ID_INTERNAL int(11) not null auto_increment,
   ACK_OPT_LOCK int(11),
   KEY_TEACHER int(11) not null,
   POSITION TEXT not null,
   START_DATE DATE not null,
   END_DATE DATE not null,
   primary key (ID_INTERNAL)
)type=InnoDB;   

drop table if exists crd_EXEMPTION_SITUATION_CREDIT_LINE;
create table crd_EXEMPTION_SITUATION_CREDIT_LINE(
   ID_INTERNAL int(11) not null auto_increment,
   ACK_OPT_LOCK int(11),
   KEY_TEACHER int(11) not null,
   TYPE int(11) not null,
   START_DATE DATE not null,
   END_DATE DATE not null,
   primary key (ID_INTERNAL)
)type=InnoDB;   

#