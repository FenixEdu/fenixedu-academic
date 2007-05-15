CREATE TABLE INQUIRIES_STUDENT_EXECUTION_PERIOD (
    ID_INTERNAL int(11) not null auto_increment,
    KEY_ROOT_DOMAIN_OBJECT int(11) not null default 1,
    KEY_EXECUTION_PERIOD int(11) not null,
    KEY_STUDENT int(11) not null,
    DONT_WANT_TO_RESPOND tinyint(1) default 0,
    primary key (ID_INTERNAL),
    index (KEY_ROOT_DOMAIN_OBJECT),
    index (KEY_EXECUTION_PERIOD),
    index (KEY_STUDENT)
) Type=InnoDB;
