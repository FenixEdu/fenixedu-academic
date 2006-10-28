-- add support for multiple class in same table
ALTER TABLE `SITE`
	ADD COLUMN `OJB_CONCRETE_CLASS` VARCHAR(255) 
	NOT NULL DEFAULT 'net.sourceforge.fenixedu.domain.ExecutionCourseSite'
	AFTER `ID_INTERNAL`;

-- remove default value
ALTER TABLE `SITE`
	MODIFY COLUMN `OJB_CONCRETE_CLASS` VARCHAR(255) NOT NULL;

-- add index
ALTER TABLE `SITE` 
	ADD INDEX `OJB_CONCRETE_CLASS`(`OJB_CONCRETE_CLASS`);
