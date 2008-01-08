create table PROTOCOL_RESPONSIBLE_FUNCTION (
	KEY_PROTOCOL int(11) not null, 
	KEY_FUNCTION int(11) not null, 
	primary key (KEY_PROTOCOL, KEY_FUNCTION), 
	key(KEY_PROTOCOL), 
	key(KEY_FUNCTION)
) type=InnoDB;
