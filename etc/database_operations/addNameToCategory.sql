alter table CATEGORY add column NAME longtext;

update CATEGORY set CATEGORY.NAME = concat('pt', length(CATEGORY.LONG_NAME), ':', CATEGORY.LONG_NAME);