#-----------------------------
# Data for table 'MASTER_DEGREE_CANDIDATE'
#-----------------------------
DELETE FROM MASTER_DEGREE_CANDIDATE;
DELETE FROM MASTER_DEGREE_CANDIDATE;
INSERT INTO MASTER_DEGREE_CANDIDATE VALUES (1, 6, 10, 1, 1, 'Informatica', 'IST', 2000, 14.99, null, null, null, null);
INSERT INTO MASTER_DEGREE_CANDIDATE VALUES (2, 7, 10, 2, 2, 'Informatica 2', 'IST 2', 2001, 13.99, null, null, null, null);


#-----------------------------
# Data for table 'CANDIDATE_SITUATION'
#-----------------------------
DELETE FROM CANDIDATE_SITUATION;
DELETE FROM CANDIDATE_SITUATION;
INSERT INTO CANDIDATE_SITUATION VALUES (1, '2002-11-17', 'Nothing', 0, 1, 1);
INSERT INTO CANDIDATE_SITUATION VALUES (2, '2002-11-17', 'Nothing', 1, 2, 1);
INSERT INTO CANDIDATE_SITUATION VALUES (3, '2002-11-20', 'Nothing', 1, 1, 2);


#-----------------------------
# Data for table 'CONTRIBUTOR'
#-----------------------------
DELETE FROM CONTRIBUTOR;
DELETE FROM CONTRIBUTOR;
INSERT INTO CONTRIBUTOR values (1, 123, 'Nome1', 'Morada1');
INSERT INTO CONTRIBUTOR values (2, 456, 'Nome2', 'Morada2');

#-----------------------------
# Data for table 'GUIDE'
#-----------------------------
DELETE FROM GUIDE;
DELETE FROM GUIDE;
INSERT INTO GUIDE values (1, 1, 2003, 1, 6, 50.02, 'guia1', 1, 10, 1, '2003-4-4', 1, '2003-4-4');
INSERT INTO GUIDE values (2, 2, 2003, 1, 6, 25, 'guia2', 1, 10, 2, '2003-4-3', 1, '2003-4-4');
INSERT INTO GUIDE values (3, 1, 2002, 2, 6, 45, 'guia3', 1, 10, 1, '2003-4-1', 1, '2003-4-4');
INSERT INTO GUIDE values (4, 1, 2002, 1, 6, 66, 'guia1', 1, 10, 1, '2003-4-5', 2, '2003-4-4');
INSERT INTO GUIDE values (5, 3, 2003, 1, 6, 66, 'guia3', 1, 10, 1, '2003-4-5', 1, '2003-4-4');


#-----------------------------
# Data for table 'GUIDE_ENTRY'
#-----------------------------
DELETE FROM GUIDE_ENTRY;
DELETE FROM GUIDE_ENTRY;
INSERT INTO GUIDE_ENTRY values (1, 1, 1, 2, 'Conclusão', 10.02 , 1);
INSERT INTO GUIDE_ENTRY values (2, 1, 1, 6, 'Atraso no Pagamento', 15, 2);
INSERT INTO GUIDE_ENTRY values (3, 1, 1, 7, 'de vida', 10.00, 1);

INSERT INTO GUIDE_ENTRY values (4, 2, 1, 1, 'desc1', 1, 1);
INSERT INTO GUIDE_ENTRY values (5, 2, 1, 2, 'desc2', 12, 2);


INSERT INTO GUIDE_ENTRY values (6, 3, 2, 3, 'desc3', 33, 1);
INSERT INTO GUIDE_ENTRY values (7, 3, 2, 5, 'desc5', 4, 3);


INSERT INTO GUIDE_ENTRY values (8, 4, 2, 3, 'desc3', 33, 2);

INSERT INTO GUIDE_ENTRY values (9, 5, 2, 3, 'desc3', 33, 2);


#-----------------------------
# Data for table 'GUIDE_SITUATION'
#-----------------------------
DELETE FROM GUIDE_SITUATION;
DELETE FROM GUIDE_SITUATION;
INSERT INTO GUIDE_SITUATION values (1, 1, 1, '2003-3-12', 'nao pago', 0);
INSERT INTO GUIDE_SITUATION values (2, 1, 2, '2003-10-6', 'pago', 1);
INSERT INTO GUIDE_SITUATION values (3, 2, 3, '2003-5-4', 'anulado', 1);
INSERT INTO GUIDE_SITUATION values (4, 4, 3, '2003-5-4', 'anulado', 1);
INSERT INTO GUIDE_SITUATION values (5, 3, 1, '2003-5-4', 'Ainda nao foi pago', 1);
INSERT INTO GUIDE_SITUATION values (6, 5, 1, '2003-5-4', 'Ainda nao foi pago', 1);




#-----------------------------
# Data for table 'PRICE'
#-----------------------------
DELETE FROM PRICE;
DELETE FROM PRICE;
INSERT INTO PRICE values (1, 2, 1, 'Duração do Curso', '11.47');
INSERT INTO PRICE values (2, 1, 2, 'Doc2', '12.45');
INSERT INTO PRICE values (3, 1, 3, 'Doc3', '13.47');
INSERT INTO PRICE values (4, 2, 1, 'Matrícula', '75');
INSERT INTO PRICE values (5, 2, 1, 'Inscrição', '75');
INSERT INTO PRICE values (6, 2, 1, 'Matrícula e Inscrição', '75');
INSERT INTO PRICE values (7, 2, 1, 'Aproveitamento', '75');
INSERT INTO PRICE values (8, 2, 1, 'Aproveitamento de Disciplinas Extra Curricular', '75');
INSERT INTO PRICE values (9, 2, 4, 'Emolumentos de Candidatura', '100');
INSERT INTO PRICE values (10, 2, 4, 'Mestrado', '100');
INSERT INTO PRICE values (11, 2, 4, 'Especialização', '100');
INSERT INTO PRICE values (12, 2, 1, 'Fim parte escolar simples', '100');
INSERT INTO PRICE values (13, 2, 1, 'Fim parte escolar discriminada sem média', '100');
INSERT INTO PRICE values (14, 2, 1, 'Fim parte escolar discriminada com média', '100');
INSERT INTO PRICE values (15, 2, 5, 'Inscrições', '80');
INSERT INTO PRICE values (16, 2, 6, 'Multas', '45');
INSERT INTO PRICE values (17, 2, 7, 'Seguros', '100');
INSERT INTO PRICE values (18, 2, 1, 'Diploma', '100');
INSERT INTO PRICE values (43, 2, 11, 'Duração do curso', '15');



