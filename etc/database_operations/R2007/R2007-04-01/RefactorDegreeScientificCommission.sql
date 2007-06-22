--
-- The dissertation coordination is in fact the scientific commission of a degree.
-- This is an independt group like the coordinators that is managed by the degree's
-- responsible coordinator the a given execution year.
--

ALTER TABLE `COORDINATOR` 
	DROP COLUMN `THESIS_COORDINATOR`;
	
CREATE TABLE `SCIENTIFIC_COMMISSION` (
	ID_INTERNAL INTEGER NOT NULL AUTO_INCREMENT,
	KEY_ROOT_DOMAIN_OBJECT INTEGER NOT NULL DEFAULT 1,
	KEY_PERSON INTEGER NOT NULL,
	KEY_EXECUTION_DEGREE INTEGER NOT NULL,
	CONTACT BOOLEAN NOT NULL DEFAULT 0,

	PRIMARY KEY (ID_INTERNAL),
	KEY KEY_PERSON (KEY_PERSON),
	KEY KEY_EXECUTION_DEGREE (KEY_EXECUTION_DEGREE)
);