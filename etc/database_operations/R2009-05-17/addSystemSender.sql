alter table `ROOT_DOMAIN_OBJECT` add column `KEY_SYSTEM_SENDER` int(11), add column `OID_SYSTEM_SENDER` bigint unsigned default null;
alter table `ROOT_DOMAIN_OBJECT` add index (`KEY_SYSTEM_SENDER`), add index (`OID_SYSTEM_SENDER`);
alter table `SENDER` add column `KEY_SYSTEM_ROOT_DOMAIN_OBJECT` int(11), add column `OID_SYSTEM_ROOT_DOMAIN_OBJECT` bigint unsigned default null;
alter table `SENDER` add index (`KEY_SYSTEM_ROOT_DOMAIN_OBJECT`), add index (`OID_SYSTEM_ROOT_DOMAIN_OBJECT`);
