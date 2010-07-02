--The YEAR_STUDENT_SPECIAL_SEASON_CODE table is the gluer for this data, working
--  as referral for all entities needed for the the SpecialSeasonCode semantics.
--Thus by dropping this table and SPECIAL_SEASON_CODE, all data related to this logics
--  shall be erased without breaking the 'harmonious' balance of our data model.

DELETE FROM YEAR_STUDENT_SPECIAL_SEASON_CODE;
DROP TABLE YEAR_STUDENT_SPECIAL_SEASON_CODE;

DELETE FROM SPECIAL_SEASON_CODE;
DROP TABLE SPECIAL_SEASON_CODE;