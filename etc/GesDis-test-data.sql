

#
# Data for table 'SITE'
#

DELETE FROM SITE;
INSERT INTO SITE VALUES (1, '24', null);
INSERT INTO SITE VALUES (2, '25', null);
INSERT INTO SITE VALUES (3, '26', null);
INSERT INTO SITE VALUES (4, '27', null);
INSERT INTO SITE VALUES (5, '28', null);
INSERT INTO SITE VALUES (6, '29', null);
INSERT INTO SITE VALUES (7, '30', null);
INSERT INTO SITE VALUES (8, '31', null);
INSERT INTO SITE VALUES (9, '32', null);
INSERT INTO SITE VALUES (10, '33', null);




#
# Data for table 'SECTION'
#

DELETE FROM SECTION;
INSERT INTO SECTION VALUES (1, 'Seccao1dePO',0,4,null,'2003-01-21');

#INSERT INTO SECTION VALUES (2, 'SubSeccao1dePO',0,4,1,'2003-01-23');
#INSERT INTO SECTION VALUES (3, 'SubSeccao2dePO',1,4,1,'2003-01-25');
#INSERT INTO SECTION VALUES (4, 'SubSubSeccao1dePO',0,4,3,'2003-01-26');

#INSERT INTO SECTION VALUES (5, 'Seccao1deAC',0,7,0,'2003-02-11');
#INSERT INTO SECTION VALUES (6, 'SubSeccao1deAC',0,7,5,'2003-02-13');
#INSERT INTO SECTION VALUES (7, 'SubSeccao2deAC',1,7,5,'2003-02-15');
#INSERT INTO SECTION VALUES (8, 'SubSubSeccao1deAC',0,7,7,'2003-02-16');

#
# Data for table 'ITEM'
#

#DELETE FROM ITEM;
#INSERT INTO ITEM VALUES (1, 'Item1', 0,'item1 da seccao1dePO',1,1);
#INSERT INTO ITEM VALUES (2, 'Item2', 1,'item2 da seccao1dePO',1,1);
#INSERT INTO ITEM VALUES (3, 'Item3', 2,'item3 da seccao1dePO',0,1);

#INSERT INTO ITEM VALUES (4, 'Item1', 0,'item1 da seccao1deAC',1,5);
#INSERT INTO ITEM VALUES (5, 'Item2', 1,'item2 da seccao1deAC',0,5);
#INSERT INTO ITEM VALUES (6, 'Item3', 2,'item3 da seccao1deAC',0,5);

#
# Data for table 'CURRICULUM'
#

DELETE FROM CURRICULUM;
INSERT INTO CURRICULUM VALUES (1, '24', 'bla','bla','bla');
INSERT INTO CURRICULUM VALUES (2, '25','bla','bla','bla');

#
# Data for table 'TEACHER'
#

DELETE FROM TEACHER;
INSERT INTO TEACHER VALUES (1, 1);
INSERT INTO TEACHER VALUES (2, 2);
INSERT INTO TEACHER VALUES (3, 3);
INSERT INTO TEACHER VALUES (4, 4);
INSERT INTO TEACHER VALUES (5, 5);

#
# Data for table 'professorships'
#

DELETE FROM professorships;
INSERT INTO professorships VALUES (1, 24);
INSERT INTO professorships VALUES (1, 25);
INSERT INTO professorships VALUES (1, 26);

#
# Data for table 'responsablefor'
#

DELETE FROM responsablefor;
INSERT INTO responsablefor VALUES (1, 25);
INSERT INTO responsablefor VALUES (1, 26);

#
# Data for table 'announcement'
#

DELETE FROM announcement;
INSERT INTO announcement VALUES (1, 'announcement1', '2003-01-21', '2003-01-21', 'information1', 1);


