----------------------------
-- Table structure for CREDIT_LINE
-- Types : Sabática, Outro, Dispensa de Serviço Docente, Cargos de Gestão
----------------------------
drop table if exists CREDIT_LINE;
create table CREDIT_LINE (
   ID_INTERNAL int(11) not null auto_increment,
   KEY_TEACHER int(11) not null,
   CREDITS float not null,
	 CREDIT_LINE_TYPE int(11) not null,
   EXPLANATION varchar(250) not null,
   START_DATE date not null,
   END_DATE date not null,
   primary key (ID_INTERNAL)
)type=InnoDB;
