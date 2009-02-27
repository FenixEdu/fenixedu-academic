


-- Inserted at 2009-02-11T18:16:21.572Z

alter table USER add column PRIVATE_KEY blob;





-- Inserted at 2009-02-16T20:40:18.974Z

alter table USER add column PRIVATE_KEY_CREATION timestamp NULL default NULL;
alter table USER add column PRIVATE_KEY_VALIDITY timestamp NULL default NULL;


