create table ROOT_DOMAIN_OBJECT (
	ID_INTERNAL int(11) NOT NULL auto_increment,
	PRIMARY KEY  (ID_INTERNAL)
) TYPE=InnoDB;

insert into ROOT_DOMAIN_OBJECT values (1);

alter table ROLE add column KEY_ROOT_DOMAIN_OBJECT int(11) not null default 1;