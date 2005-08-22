select concat("create temporary table batatas select max(PERSON.DOCUMENT_ID_NUMBER) as max from PERSON, AUTHOR where PERSON.TYPE_ID_DOCUMENT = 'EXTERNAL' and PERSON.USERNAME not like 'author%';") as "";

select concat('update PERSON, batatas set PERSON.DOCUMENT_ID_NUMBER = (batatas.max + 1), PERSON.USERNAME = concat("e", (batatas.max + 2)),  batatas.max = batatas.max + 1 where PERSON.ID_INTERNAL = ', PERSON.ID_INTERNAL, ';') as "" from PERSON where PERSON.USERNAME like 'author%';
