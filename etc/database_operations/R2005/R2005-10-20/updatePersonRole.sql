#Objectivo: apagar de PERSON_ROLE todas as entradas que tenham ligações quebradas

delete from PERSON_ROLE where KEY_ROLE not in (select ID_INTERNAL from ROLE);
delete from PERSON_ROLE where KEY_PERSON not in (select ID_INTERNAL from PERSON);

#Objectivo: criar o role PERSON_ROLE para todas as pessoas que já tenham um role e que não tenham o role de pessoa
select concat('insert into PERSON_ROLE set KEY_ROLE=1,KEY_PERSON=',P.ID_INTERNAl,';') as "" FROM PERSON P WHERE P.ID_INTERNAL NOT IN (SELECT KEY_PERSON FROM PERSON_ROLE WHERE KEY_ROLE = '1') AND P.ID_INTERNAL IN (SELECT KEY_PERSON FROM PERSON_ROLE);



