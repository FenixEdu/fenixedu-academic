-------------------------------
-- Data for table 'PERSON'
-------------------------------
INSERT INTO PERSON VALUES (18, 000000000, 'Lisboa', '2003-07-26', '2003-07-26','Administrador',
                           '2002-07-26', 'Nome do Pai', 'Nome da Mae', 'Portuguesa',
                           'Freguesia', 'Concelho', 'Distrito', 'Morada', 'localidade',
                           '1700-200', 'l200', 'frequesia morada', 'concelho morada',
                           'distrito morada', '000000000', '000000000', 'admin@fenix','http',
                           '0000000000', 'Profissao', 'admin',MD5("pass"), 63,
                           '0000000000', 1, 1, 1);

-----------------------------
-- Data for table 'ROLE'
-----------------------------
insert into ROLE values (18, 18, "/admin", "/index.do", "portal.admin");

-------------------------------
-- Data for table 'PERSON_ROLE'
-------------------------------
insert into PERSON_ROLE (ID_INTERNAL, KEY_ROLE, KEY_PERSON) values (35,18,18);
