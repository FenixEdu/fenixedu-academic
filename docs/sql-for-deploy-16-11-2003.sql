alter table PROFESSORSHIPS add CREDITS float(11, 2) default '0';

-- in updateDegreeCurricularPlan.sql
alter table DEGREE_CURRICULAR_PLAN add DESCRIPTION text;
alter table DEGREE_CURRICULAR_PLAN add DESCRIPTION_EN text;