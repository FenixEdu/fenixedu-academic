alter table RESOURCE add column IDENTIFICATION varchar(100) default NULL;

select concat('update RESOURCE set RESOURCE.IDENTIFICATION = ' , NUMBER , ' where RESOURCE.ID_INTERNAL = ' , ID_INTERNAL , ';') as "" from RESOURCE where OJB_CONCRETE_CLASS like 'net.sourceforge.fenixedu.domain.material.Extension';

alter table RESOURCE drop column NUMBER;

alter table RESOURCE add column DELIVERER_ENTERPRISE varchar(100) default null;
alter table RESOURCE add column LOADED_DATE varchar(10) default NULL;
alter table RESOURCE add column TO_BE_INSPECTED_DATE varchar(10) default NULL;

alter table RESOURCE change column BAR_CODE_NUMBER BAR_CODE_NUMBER varchar(100);