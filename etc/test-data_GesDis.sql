

#
# Data for table 'SITE'
#
#---------
DELETE FROM SITE;
INSERT INTO SITE VALUES (1, '24', 7, 'http://www.ist.utl.pt','joao@ist.utl.pt',null,null,'gesdis-web.css');
INSERT INTO SITE VALUES (2, '25', null, null, null, null, null,'gesdis-web.css');
INSERT INTO SITE VALUES (3, '26', null, null, null, null, null,'gesdis-web.css');
INSERT INTO SITE VALUES (4, '27', null, null, null, null, null,'gesdis-web.css');
INSERT INTO SITE VALUES (5, '28', null, null, null, null, null,'gesdis-web.css');
INSERT INTO SITE VALUES (6, '29', null, null, null, null, null,'gesdis-web.css');
INSERT INTO SITE VALUES (7, '30', null, null, null, null, null,'gesdis-web.css');
INSERT INTO SITE VALUES (8, '31', null, null, null, null, null,'gesdis-web.css');
INSERT INTO SITE VALUES (9, '32', null, null, null, null, null,'gesdis-web.css');
INSERT INTO SITE VALUES (10,'33', null, null, null, null, null,'gesdis-web.css');




#
# Data for table 'SECTION'
#
#---------
DELETE FROM SECTION;

INSERT INTO SECTION VALUES (3, 'SubSeccao1dePO',0,4,6,'2003-01-23');
INSERT INTO SECTION VALUES (4, 'SubSeccao2dePO',1,4,6,'2003-01-25');
INSERT INTO SECTION VALUES (5, 'SubSubSeccao1dePO',0,4,3,'2003-01-26');
INSERT INTO SECTION VALUES (6, 'Seccao1dePO',0,4,null,'2003-01-21');
INSERT INTO SECTION VALUES (7, 'Seccao1deTFCI',0,1,null,'2003-01-22');

#
# Data for table 'ITEM'
#
#---------
DELETE FROM ITEM;
INSERT INTO ITEM VALUES (1,'Item1dePO',0,'item1 da seccao1dePO',1,6);
INSERT INTO ITEM VALUES (2,'Item1deTFCI',0,'item1 da seccao1deTFCI',1,7);
INSERT INTO ITEM VALUES (3,'Item2deTFCI',1,'item2 da seccao1deTFCI',0,7);
#
# Data for table 'CURRICULUM'
#
#---------
DELETE FROM CURRICULUM;
INSERT INTO CURRICULUM VALUES (2, '25', 'bla','bla','bla');
INSERT INTO CURRICULUM VALUES (1, '24','bla','bla','bla');

#
# Data for table 'TEACHER'
#
#---------
DELETE FROM TEACHER;
INSERT INTO TEACHER VALUES (1, 1,1);
INSERT INTO TEACHER VALUES (2, 2,2);
INSERT INTO TEACHER VALUES (3, 3,3);
INSERT INTO TEACHER VALUES (4, 4,4);
INSERT INTO TEACHER VALUES (5, 5,5);
INSERT INTO TEACHER VALUES (6, 6,6);
INSERT INTO TEACHER VALUES (7, 8,8);



#
# Data for table 'PROFESSORSHIPS'
#
#---------
DELETE FROM PROFESSORSHIPS;
INSERT INTO PROFESSORSHIPS VALUES (1,1, 24);
INSERT INTO PROFESSORSHIPS VALUES (2,1, 25);
INSERT INTO PROFESSORSHIPS VALUES (3,1, 26);
INSERT INTO PROFESSORSHIPS VALUES (4,2, 24);
#INSERT INTO PROFESSORSHIPS VALUES (5,3, 24);

#
# Data for table 'RESPONSIBLEFOR'
#
#---------
DELETE FROM RESPONSIBLEFOR;
INSERT INTO RESPONSIBLEFOR VALUES (1,1, 24);
INSERT INTO RESPONSIBLEFOR VALUES (2,1, 25);
INSERT INTO RESPONSIBLEFOR VALUES (3,1, 26);

#
# Data for table 'ANNOUNCEMENT'
#
#---------
DELETE FROM ANNOUNCEMENT;
INSERT INTO ANNOUNCEMENT VALUES (1, 'announcement1deTFCI', '2003-01-21 13:20:00', '2003-01-21 0:0:00', 'information1', 1);
INSERT INTO ANNOUNCEMENT VALUES (2, 'announcement2deTFCI', '2003-01-22 13:20:00', '2003-01-22 0:0:00', 'information2', 1);
INSERT INTO ANNOUNCEMENT VALUES (3, 'announcement1dePO', '2003-01-23 13:20:01', '2003-01-23 0:00:00', 'information3', 4);
INSERT INTO ANNOUNCEMENT VALUES (4, 'announcement2dePO', '2003-01-21 13:20:02', '2003-01-25 0:00:00', 'information4', 4);
INSERT INTO ANNOUNCEMENT VALUES (5, 'announcement3dePO', '2003-01-21 13:20:03', '2003-01-24 0:00:00', 'information5', 4);

#
# Data for table 'BIBLIOGRAPHIC_REFERENCE'
#
#---------
DELETE FROM BIBLIOGRAPHIC_REFERENCE;
INSERT  INTO  BIBLIOGRAPHIC_REFERENCE VALUES  (1, 'xpto', 'pedro', 'ref', '2002', 0, 24) ;
INSERT  INTO  BIBLIOGRAPHIC_REFERENCE VALUES  (2, 'so', 'am', 'ref2', '2004', 0, 25) ;
INSERT  INTO  BIBLIOGRAPHIC_REFERENCE VALUES  (3, 'as', 'rs', 'ref5', '2040', 0, 24) ;
INSERT  INTO  BIBLIOGRAPHIC_REFERENCE VALUES  (4, 'ep', 'rs', 'ref5', '2040', 1, 24) ;


#
# Data for table 'EVALUATION'
#
#---------
DELETE FROM EVALUATION;
INSERT INTO EVALUATION VALUES (2, '25', 'bla');
INSERT INTO EVALUATION VALUES (1, '24','bla');