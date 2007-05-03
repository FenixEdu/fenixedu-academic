-- add new option to hide/show i18n flags
ALTER TABLE `SITE`
	ADD COLUMN `SHOW_FLAGS` BOOLEAN DEFAULT 1;

UPDATE `SITE` SET `SHOW_FLAGS` = 1 WHERE 
`OJB_CONCRETE_CLASS` IN (
	'net.sourceforge.fenixedu.domain.DepartmentSite', 
	'net.sourceforge.fenixedu.domain.ResearchUnitSite'
);
