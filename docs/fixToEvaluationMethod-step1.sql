alter table CURRICULUM
 add column  EVALUATION_ELEMENTS text null after PROGRAM_EN
,  add column  EVALUATION_ELEMENTS_EN text null after EVALUATION_ELEMENTS
, type=InnoDB;