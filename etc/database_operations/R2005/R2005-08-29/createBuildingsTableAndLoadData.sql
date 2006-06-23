CREATE TABLE `BUILDING` (
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `ACK_OPT_LOCK` int(11) default NULL,
  `NAME` varchar(100) NOT NULL default '',
  PRIMARY KEY  (`ID_INTERNAL`),
  UNIQUE KEY `U1` (`NAME`)
) Type=InnoDB;

alter table ROOM add column KEY_BUILDING int(11) NOT NULL;

insert into BUILDING(ACK_OPT_LOCK, NAME)
	select 1, ROOM.BUILDING
		from ROOM
	group by ROOM.BUILDING
	order by ROOM.BUILDING;

select concat('update ROOM set ROOM.KEY_BUILDING= ', BUILDING.ID_INTERNAL, ', ACK_OPT_LOCK=ACK_OPT_LOCK + 1 where ROOM.BUILDING= "', BUILDING.NAME,'";') as "" from BUILDING;

alter table ROOM add index (KEY_BUILDING);