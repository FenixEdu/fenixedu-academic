alter table FILE add column KEY_UNIT int(11); 
alter table FILE add column KEY_UPLOADER int (11);
alter table FILE add index(`KEY_UNIT`);
alter table FILE add index(`KEY_UPLOADER`);
alter table FILE add column DESCRIPTION LONGTEXT;
