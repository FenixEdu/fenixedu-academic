-- 2006-06-29
--
-- All fields that have the type org.joda.time.Partial were being
-- serialized and stored in a BLOB. This may brake when the Joda
-- framework is updated and does not allow an easy look up of the
-- column to confirm the value.

-- All fields that use the Partial type are now coded as a String
-- and stored in a TEXT column. The TEXT is needed to accomodate
-- all the possible date/time elements a Partial considers. The
-- conversion can be optimized to possibly fit in a VARCHAR column.

ALTER TABLE `HOLIDAY` 
	MODIFY COLUMN `DATE` TEXT NOT NULL;

ALTER TABLE `RESULT` 
	MODIFY COLUMN `REGISTRATION_DATE` TEXT,
	MODIFY COLUMN `APPROVAL_DATE` TEXT;

ALTER TABLE `RESEARCH_PROJECT` 
	MODIFY COLUMN `START_DATE` TEXT DEFAULT NULL,
	MODIFY COLUMN `END_DATE` TEXT DEFAULT NULL;
