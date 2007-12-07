alter table FORUM change column READERS_GROUP READERS_GROUP longtext;
alter table FORUM change column WRITERS_GROUP WRITERS_GROUP longtext;
alter table FORUM change column ADMIN_GROUP ADMIN_GROUP longtext;

alter table FORUM change NAME NAME longtext;
alter table FORUM change DESCRIPTION DESCRIPTION longtext;

UPDATE FORUM SET NAME=CONCAT('pt', LENGTH(NAME), ':',NAME);
UPDATE FORUM SET DESCRIPTION=CONCAT('pt', LENGTH(DESCRIPTION), ':',DESCRIPTION);
