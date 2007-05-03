-- add new option to hide/show i18n flags
ALTER TABLE `SITE`
	ADD COLUMN `SHOW_FLAGS` BOOLEAN DEFAULT 1;
