-- Because a manual insert was made, this correction
--  is needed to allow future creations of StudentStatute
--  entities

DELETE FROM OJB_HL_SEQ WHERE TABLENAME='SEQ_STUDENT_STATUTE';