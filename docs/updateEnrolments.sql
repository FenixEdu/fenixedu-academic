SELECT CONCAT('UPDATE ENROLMENT_EVALUATION SET STATE = 1 WHERE ID_INTERNAL = ', ID_INTERNAL, ';') AS ""
FROM ENROLMENT_EVALUATION WHERE GRADE IS NOT NULL AND STATE = 2;


DROP TABLE IF EXISTS XPTO;
CREATE TABLE XPTO
SELECT EE1.* FROM ENROLMENT_EVALUATION EE1
INNER JOIN ENROLMENT_EVALUATION EE2 ON
EE1.KEY_ENROLMENT = EE2.KEY_ENROLMENT
WHERE
EE1.GRADE IS NULL AND
EE2.GRADE IS NULL AND
EE1.ID_INTERNAL <> EE2.ID_INTERNAL;


DROP TABLE IF EXISTS XPTI;
CREATE TABLE XPTI
SELECT * FROM ENROLMENT_EVALUATION WHERE GRADE IS NULL GROUP BY KEY_ENROLMENT HAVING COUNT(*) > 1;


SELECT CONCAT('DELETE FROM ENROLMENT_EVALUATION WHERE ID_INTERNAL = ', EE.ID_INTERNAL, ';') AS ""
FROM ENROLMENT_EVALUATION EE
INNER JOIN XPTO X ON X.ID_INTERNAL = EE.ID_INTERNAL;

SELECT CONCAT('INSERT INTO ENROLMENT_EVALUATION VALUES (NULL, NULL, 1, NULL, NULL, NULL, 2, ', KEY_ENROLMENT, ', NULL, NULL, NULL, NULL, 1);') AS ""
FROM XPTI;

DROP TABLE IF EXISTS XPTO;
DROP TABLE IF EXISTS XPTI;

update enrolment_evaluation set key_enrolment = 25417 where id_internal = 487121;
delete from enrolment_evaluation where id_internal = 25430;
delete from enrolment where id_internal = 44018;
update enrolment set state = 1 where id_internal = 25417;

update enrolment_evaluation set key_enrolment = 26406 where id_internal = 530406;
delete from enrolment_evaluation where id_internal = 26419;
delete from enrolment where id_internal = 44020;
update enrolment set state = 6 where id_internal = 26406;

update enrolment_evaluation set key_enrolment = 28451 where id_internal = 487498;
delete from enrolment_evaluation where id_internal = 28464;
delete from enrolment where id_internal = 44033;
update enrolment set state = 1 where id_internal = 28451;

update enrolment_evaluation set key_enrolment = 28456 where id_internal = 487500;
delete from enrolment_evaluation where id_internal = 28469;
delete from enrolment where id_internal = 44034;
update enrolment set state = 1 where id_internal = 28456;

update enrolment_evaluation set key_enrolment = 28467 where id_internal = 487501;
delete from enrolment_evaluation where id_internal = 28480;
delete from enrolment where id_internal = 44035;
update enrolment set state = 2 where id_internal = 28467;

update enrolment_evaluation set key_enrolment = 28473 where id_internal = 487503;
delete from enrolment_evaluation where id_internal = 28486;
delete from enrolment where id_internal = 44036;
update enrolment set state = 2 where id_internal = 28473;

update enrolment_evaluation set key_enrolment = 28501 where id_internal = 530448;
delete from enrolment_evaluation where id_internal = 28514;
delete from enrolment where id_internal = 44037;
update enrolment set state = 6 where id_internal = 28501;

update enrolment_evaluation set key_enrolment = 28511 where id_internal = 487505;
delete from enrolment_evaluation where id_internal = 28524;
delete from enrolment where id_internal = 44038;
update enrolment set state = 2 where id_internal = 28511;

update enrolment_evaluation set key_enrolment = 28516 where id_internal = 487506;
delete from enrolment_evaluation where id_internal = 28529;
delete from enrolment where id_internal = 44039;
update enrolment set state = 1 where id_internal = 28516;

update enrolment_evaluation set key_enrolment = 29477 where id_internal = 487571;
delete from enrolment_evaluation where id_internal = 29490;
delete from enrolment where id_internal = 44040;
update enrolment set state = 2 where id_internal = 29477;

update enrolment_evaluation set key_enrolment = 34703 where id_internal = 530468;
delete from enrolment_evaluation where id_internal = 34716;
delete from enrolment where id_internal = 44054;
update enrolment set state = 6 where id_internal = 34703;

update enrolment_evaluation set key_enrolment = 34732 where id_internal = 530496;
delete from enrolment_evaluation where id_internal = 34745;
delete from enrolment where id_internal = 44059;
update enrolment set state = 6 where id_internal = 34732;

update enrolment_evaluation set key_enrolment = 34738 where id_internal = 530500;
delete from enrolment_evaluation where id_internal = 34751;
delete from enrolment where id_internal = 44060;
update enrolment set state = 6 where id_internal = 34738;

update enrolment_evaluation set key_enrolment = 34744 where id_internal = 530503;
delete from enrolment_evaluation where id_internal = 34757;
delete from enrolment where id_internal = 44061;
update enrolment set state = 6 where id_internal = 34744;

update enrolment_evaluation set key_enrolment = 34709 where id_internal = 489276;
delete from enrolment_evaluation where id_internal = 34722;
delete from enrolment where id_internal = 468315;
update enrolment set state = 1 where id_internal = 34709;

update enrolment_evaluation set key_enrolment = 34714 where id_internal = 489277;
delete from enrolment_evaluation where id_internal = 34727;
delete from enrolment where id_internal = 468316;
update enrolment set state = 1 where id_internal = 34714;

update enrolment_evaluation set key_enrolment = 34719 where id_internal = 489278;
delete from enrolment_evaluation where id_internal = 34732;
delete from enrolment where id_internal = 468317;
update enrolment set state = 1 where id_internal = 34719;

update enrolment_evaluation set key_enrolment = 34726 where id_internal = 489279;
delete from enrolment_evaluation where id_internal = 34739;
delete from enrolment where id_internal = 468318;
update enrolment set state = 1 where id_internal = 34726;
