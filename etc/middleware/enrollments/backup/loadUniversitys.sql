-- **************************************************
-- *** ATENTION! ************************************
-- *** The contents of this file is not currently ***
-- *** being used. **********************************
-- **************************************************

SELECT CONCAT('UPDATE UNIVERSITY SET NAME = "', REPLACE(REPLACE(MWU.UNIVERSITYNAME,"'","\\'"),'"','\\"'), '", CODE = "', MWU.UNIVERSITYCODE, '";') AS "as"
FROM MW_UNIVERSITY MWU LEFT JOIN UNIVERSITY U ON MWU.UNIVERSITYCODE = U.CODE AND MWU.UNIVERSITYNAME = U.NAME WHERE U.ID_INTERNAL IS NULL;
