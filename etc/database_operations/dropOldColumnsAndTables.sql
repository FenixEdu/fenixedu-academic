drop table CATEGORY;
drop table PROFESSIONAL_SITUATION;
alter table `TEACHER` drop TEACHER_NUMBER;
alter table `T_S_D_TEACHER` drop key OID_CATEGORY, drop OID_CATEGORY;
alter table `CAREER` drop key OID_CATEGORY, drop OID_CATEGORY;
alter table `EMPLOYEE` drop ACTIVE;