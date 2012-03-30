drop table CATEGORY;
drop table PROFESSIONAL_SITUATION;
alter table `TEACHER` drop TEACHER_NUMBER;
alter table `FILE` drop key OID_PHD_PARTICIPANT, drop OID_PHD_PARTICIPANT;
alter table `PARTY` drop AVAILABLE_PHOTO;
alter table `T_S_D_TEACHER` drop key OID_CATEGORY, drop OID_CATEGORY;
alter table `CAREER` drop key OID_CATEGORY, drop OID_CATEGORY;
alter table `PHD_GRATUITY_PAYMENT_PERIOD` drop MONTH_END, drop DAY_LAST_PAYMENT, drop DAY_START, drop MONTH_START, drop MONTH_LAST_PAYMENT, drop DAY_END;
alter table `EMPLOYEE` drop ACTIVE;
