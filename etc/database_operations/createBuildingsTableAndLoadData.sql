select concat('update ROOM set ROOM.KEY_BUILDING= ', BUILDING.ID_INTERNAL, ', ACK_OPT_LOCK=ACK_OPT_LOCK + 1 where ROOM.BUILDING= "', BUILDING.NAME,'";') as "" from BUILDING;

alter table ROOM add index (KEY_BUILDING);
