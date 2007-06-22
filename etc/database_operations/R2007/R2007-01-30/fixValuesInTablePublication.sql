alter table PUBLICATION_DATA drop column TITLE_OLD;
alter table PUBLICATION_DATA drop column OBSERVATION_OLD;

update PUBLICATION_DATA set COUNTRY = 'Estados Unidos da America' where COUNTRY='USA' or COUNTRY='EUA';
update PUBLICATION_DATA set COUNTRY = 'Reino Unido' where COUNTRY='UK' or COUNTRY='U.K.' or COUNTRY='United Kingdom';
update PUBLICATION_DATA set COUNTRY = 'Alemanha' where COUNTRY='Germany';
update PUBLICATION_DATA set COUNTRY = 'Holanda' where COUNTRY='Netherlands' or COUNTRY='The Netherlands'or COUNTRY='Holland';
update PUBLICATION_DATA set COUNTRY = 'Franca' where COUNTRY='França' or COUNTRY='France';
update PUBLICATION_DATA set COUNTRY = 'Canada' where COUNTRY='Canadá';
update PUBLICATION_DATA set COUNTRY = 'Italia' where COUNTRY='Itália';
update PUBLICATION_DATA set COUNTRY = 'Irlanda' where COUNTRY='Ireland';
update PUBLICATION_DATA set COUNTRY = 'Belgica' where COUNTRY='Bélgica';
update PUBLICATION_DATA set COUNTRY = 'Croacia' where COUNTRY='Croácia';
update PUBLICATION_DATA set COUNTRY = 'Suica' where COUNTRY='Suiça'or COUNTRY='Switzerland';
update PUBLICATION_DATA set COUNTRY = 'Eslovaquia' where COUNTRY='Slovakia';
--England??


update PUBLICATION_DATA set OBSERVATION = EDITOR where ID_INTERNAL=20490;
update PUBLICATION_DATA set EDITOR=NULL where ID_INTERNAL=20490;

update PUBLICATION_DATA set OBSERVATION = concat(OBSERVATION, " ",EDITOR) where ID_INTERNAL=23003;
update PUBLICATION_DATA set EDITOR=NULL where ID_INTERNAL=23003;

update PUBLICATION_DATA set MONTH="Julho", YEAR="1994", MONTH_END="Outubro", YEAR_END="1995" where ID_INTERNAL=18065;
update PUBLICATION_DATA set YEAR="1991", YEAR_END="2000" where ID_INTERNAL=21123;

alter table PARTY change column NAME NAME varchar(255) NOT NULL

