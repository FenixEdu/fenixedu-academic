
alter table ANUAL_BONUS_INSTALLMENT add index (KEY_ROOT_DOMAIN_OBJECT);
alter table ANUAL_BONUS_INSTALLMENT type = InnoDB;
alter table DEGREE_INFO add primary key (ID_INTERNAL);
alter table EMPLOYEE_BONUS_INSTALLMENT add index (KEY_ROOT_DOMAIN_OBJECT);
alter table EMPLOYEE_MONTHLY_BONUS_INSTALLMENT type = InnoDB;
alter table EMPLOYEE_MONTHLY_BONUS_INSTALLMENT add index (KEY_ROOT_DOMAIN_OBJECT);
alter table EXTERNAL_ENROLMENT add index (KEY_REGISTRATION);
alter table FILE add index (KEY_ABSTRACT_THESIS);
alter table FILE add index (KEY_BACKGROUND_BANNER);
alter table FILE add index (KEY_DISSERTATION_THESIS);
alter table FILE add index (KEY_MAIN_BANNER);
alter table FILE add index (KEY_NEW_PARKING_DOCUMENT);
alter table FILE add index (KEY_PROTOCOL);
alter table FILE add index (KEY_UNIT_SITE);


