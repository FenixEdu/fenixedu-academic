----------------------------------
-- Table structure for PUBLICATION
----------------------------------
drop table if exists PUBLICATION;
create table PUBLICATION (
   ID_INTERNAL int(11) not null auto_increment,
   CLASS_NAME varchar(250) not null,
   CONFERENCE varchar(100) not null,
   JOURNAL varchar(100) not null,
   LOCATION varchar(100) not null,
   MONTH int(2) not null,
   NUMBER int(10) not null,
   PAGES varchar(10) not null,
   PUBLISHER varchar(100) not null,
   SERIES varchar(100) not null,   
   TITLE varchar(100) not null,
   TRNUMBER int(5) not null,
   UNIVERSITY varchar(100) not null,
   VOLUME int(5) not null,
   YEAR int(4) not null,
   primary key (ID_INTERNAL))
   type=InnoDB;