# Nao esquecer de correr ep2002-tables.sql para garantir o estado da BD

#-----------------------------
# Data for table 'ITEM'
#------------------------------
INSERT INTO ITEM VALUES (181,'1',0,'sou a primeira',1,181);
INSERT INTO ITEM VALUES (182,'2',1,'sou a segunda',0,181);
INSERT INTO ITEM VALUES (183,'1',0,'sou outra vez a primeira',0,186);
INSERT INTO ITEM VALUES (184,'2',1,'sou outra vez a primeira',0,186);
INSERT INTO ITEM VALUES (185,'3',2,'sou outra vez a primeira',0,186);
INSERT INTO ITEM VALUES (186,'4',3,'sou outra vez a primeira',0,186);

#------------------------------
# Data for table 'SECCAO'
#------------------------------
INSERT INTO SECCAO VALUES (181,'Topo1',0,0,111);
INSERT INTO SECCAO VALUES (182,'Topo2',1,0,111);
INSERT INTO SECCAO VALUES (183,'Topo1',0,0,112);
INSERT INTO SECCAO VALUES (184,'Sub1',0,181,111);
INSERT INTO SECCAO VALUES (185,'Sub2',1,181,111);
INSERT INTO SECCAO VALUES (186,'Sub1',0,184,111);

#------------------------------
# Data for table 'SITIO'
#------------------------------
INSERT INTO SITIO VALUES (111,'EP',4,1,'LEIC','DEI');
INSERT INTO SITIO VALUES (112,'PO',2,1,'LEIC','DEI');

