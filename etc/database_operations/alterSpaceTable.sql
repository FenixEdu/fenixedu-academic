alter table `SPACE` add column `PERSON_OCCUPATIONS_ACCESS_GROUP` blob default NULL;
alter table `SPACE` add column `EXTENSION_OCCUPATIONS_ACCESS_GROUP` blob default NULL;

alter table `MATERIAL` change column `KEY_ROOT_DOMAIN_OBJECT` `KEY_ROOT_DOMAIN_OBJECT` int(11) not NULL default 1;