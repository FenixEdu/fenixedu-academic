-- Add new columns to FileItem
ALTER TABLE `FILE` 
	ADD COLUMN `VISIBLE` BOOLEAN DEFAULT 1,
	ADD COLUMN `ORDER_IN_ITEM` BOOLEAN DEFAULT 0,
	ADD COLUMN `KEY_ITEM` INTEGER;

ALTER TABLE `FILE` ADD INDEX `KEY_ITEM`(`KEY_ITEM`);

--
-- Associates each FileItem with the last associated Item
-- This works well since each FileItem is associated with only one Item
--

UPDATE `FILE` AS f, `ITEM_FILE_ITEM` AS ifi 
	SET f.`KEY_ITEM` = ifi.`KEY_ITEM`
	WHERE ifi.`KEY_FILE_ITEM` = f.`ID_INTERNAL` 
		AND f.`OJB_CONCRETE_CLASS` LIKE 'net.sourceforge.fenixedu.domain.FileItem';

-- Remove relation table
DROP TABLE `ITEM_FILE_ITEM`;

-- Give a diferent order to each FileItem
-- SET @pos = 0;

-- UPDATE FILE 
-- SET ORDER_IN_ITEM = (SELECT @pos := @pos + 1)
-- WHERE OJB_CONCRETE_CLASS LIKE '%FileItem'
-- ORDER BY KEY_ITEM, DISPLAY_NAME;

-- Remove permittedGroupType 
ALTER TABLE `FILE_ITEM` 
	DROP COLUMN `PERMITTED_GROUP_TYPE`;
