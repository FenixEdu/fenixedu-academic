


-- Inserted at 2008-11-04T16:16:46.802Z




create table STUDENT_INQUIRIES_COURSE_RESULT (
  `APPROVED_RATIO` double,
  `AVAILABLE_TO_INQUIRY` tinyint(1),
  `AVERAGE_PERC_WEEKLY_HOURS` double,
  `AVERAGE__N_D_E` double,
  `AVERAGE__N_H_T_A` double,
  `AVERAGE__P1_3` double,
  `AVERAGE__P1_4` double,
  `AVERAGE__P2_1` double,
  `AVERAGE__P2_2` double,
  `AVERAGE__P2_3` double,
  `AVERAGE__P2_4` double,
  `AVERAGE__P3_1` double,
  `AVERAGE__P3_2` double,
  `AVERAGE__P3_3` double,
  `AVERAGE__P3_4` double,
  `AVERAGE__P4` double,
  `AVERAGE__P5` double,
  `AUDIT_C_U` tinyint(1),
  `ECTS` double,
  `ESTIMATED_ECTS_AVERAGE` double,
  `ESTIMATED_ECTS_NUMBER` int(11),
  `ESTIMATED_ECTS_STANDARD_DEVIATION` double,
  `EVALUATED_RATIO` double,
  `GRADE_AVERAGE` double,
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `INTERNAL_DISCLOSURE` tinyint(1),
  `INVALID_INQUIRY_ANSWERS_NUMBER` double,
  `INVALID_INQUIRY_ANSWERS_RATIO` double,
  `KEY_EXECUTION_COURSE` int(11),
  `KEY_EXECUTION_DEGREE` int(11),
  `KEY_ROOT_DOMAIN_OBJECT` int(11),
  `NO_INQUIRY_ANSWERS_NUMBER` double,
  `NO_INQUIRY_ANSWERS_RATIO` double,
  `NUMBER_OF_ENROLLED` int(11),
  `NUMBER_PERC__N_H_T_A` int(11),
  `NUMBER__N_D_E` int(11),
  `NUMBER__N_H_T_A` int(11),
  `NUMBER__P1_1` double,
  `NUMBER__P1_2_A` double,
  `NUMBER__P1_2_B` double,
  `NUMBER__P1_2_C` double,
  `NUMBER__P1_2_D` double,
  `NUMBER__P1_2_E` double,
  `NUMBER__P1_2_G` double,
  `NUMBER__P1_3` int(11),
  `NUMBER__P1_4` int(11),
  `NUMBER__P2_1` int(11),
  `NUMBER__P2_2` int(11),
  `NUMBER__P2_3` int(11),
  `NUMBER__P2_4` int(11),
  `NUMBER__P3_1` int(11),
  `NUMBER__P3_2` int(11),
  `NUMBER__P3_3` int(11),
  `NUMBER__P3_4` int(11),
  `NUMBER__P4` int(11),
  `NUMBER__P5` int(11),
  `NUMBER__P_1_2_F` double,
  `PERC_10_12` double,
  `PERC_13_14` double,
  `PERC_15_16` double,
  `PERC_17_18` double,
  `PERC_19_20` double,
  `PERC_FLUNKED` double,
  `PERC_NON_EVALUATED` double,
  `PERC__P1_3_1` double,
  `PERC__P1_3_2` double,
  `PERC__P1_3_3` double,
  `PERC__P1_3_4` double,
  `PERC__P1_3_5` double,
  `PERC__P1_3_6` double,
  `PERC__P1_3_7` double,
  `PERC__P1_3_8` double,
  `PERC__P1_3_9` double,
  `PERC__P1_4_1` double,
  `PERC__P1_4_2` double,
  `PERC__P1_4_3` double,
  `PERC__P2_1_0` double,
  `PERC__P2_1_1` double,
  `PERC__P2_1_2` double,
  `PERC__P2_1_3` double,
  `PERC__P2_2_0` double,
  `PERC__P2_2_1` double,
  `PERC__P2_2_2` double,
  `PERC__P2_2_3` double,
  `PERC__P2_3_0` double,
  `PERC__P2_3_1` double,
  `PERC__P2_3_2` double,
  `PERC__P2_3_3` double,
  `PERC__P2_4_0` double,
  `PERC__P2_4_1` double,
  `PERC__P2_4_2` double,
  `PERC__P2_4_3` double,
  `PERC__P3_1_1` double,
  `PERC__P3_1_2` double,
  `PERC__P3_1_3` double,
  `PERC__P3_1_4` double,
  `PERC__P3_1_5` double,
  `PERC__P3_1_6` double,
  `PERC__P3_1_7` double,
  `PERC__P3_1_8` double,
  `PERC__P3_1_9` double,
  `PERC__P3_2_1` double,
  `PERC__P3_2_2` double,
  `PERC__P3_2_3` double,
  `PERC__P3_2_4` double,
  `PERC__P3_2_5` double,
  `PERC__P3_2_6` double,
  `PERC__P3_2_7` double,
  `PERC__P3_2_8` double,
  `PERC__P3_2_9` double,
  `PERC__P3_3_1` double,
  `PERC__P3_3_2` double,
  `PERC__P3_3_3` double,
  `PERC__P3_3_4` double,
  `PERC__P3_3_5` double,
  `PERC__P3_3_6` double,
  `PERC__P3_3_7` double,
  `PERC__P3_3_8` double,
  `PERC__P3_3_9` double,
  `PERC__P3_4_1` double,
  `PERC__P3_4_2` double,
  `PERC__P3_4_3` double,
  `PERC__P3_4_4` double,
  `PERC__P3_4_5` double,
  `PERC__P3_4_6` double,
  `PERC__P3_4_7` double,
  `PERC__P3_4_8` double,
  `PERC__P3_4_9` double,
  `PERC__P4_1` double,
  `PERC__P4_2` double,
  `PERC__P4_3` double,
  `PERC__P4_4` double,
  `PERC__P4_5` double,
  `PERC__P4_6` double,
  `PERC__P4_7` double,
  `PERC__P4_8` double,
  `PERC__P4_9` double,
  `PERC__P5_1` double,
  `PERC__P5_2` double,
  `PERC__P5_3` double,
  `PERC__P5_4` double,
  `PERC__P5_5` double,
  `PERC__P5_6` double,
  `PERC__P5_7` double,
  `PERC__P5_8` double,
  `PERC__P5_9` double,
  `PERC___P1_2_A` double,
  `PERC___P1_2_B` double,
  `PERC___P1_2_C` double,
  `PERC___P1_2_D` double,
  `PERC___P1_2_E` double,
  `PERC___P1_2_F` double,
  `PERC___P1_2_G` double,
  `PUBLIC_DISCLOSURE` tinyint(1),
  `SCHEDULE_LOAD` double,
  `STANDARD_DEVIATION_PERC__N_H_T_A` double,
  `STANDARD_DEVIATION__N_D_E` double,
  `STANDARD_DEVIATION__N_H_T_A` double,
  `STANDARD_DEVIATION__P1_3` double,
  `STANDARD_DEVIATION__P1_4` double,
  `STANDARD_DEVIATION__P2_1` double,
  `STANDARD_DEVIATION__P2_2` double,
  `STANDARD_DEVIATION__P2_3` double,
  `STANDARD_DEVIATION__P2_4` double,
  `STANDARD_DEVIATION__P3_1` double,
  `STANDARD_DEVIATION__P3_2` double,
  `STANDARD_DEVIATION__P3_3` double,
  `STANDARD_DEVIATION__P3_4` double,
  `STANDARD_DEVIATION__P4` double,
  `STANDARD_DEVIATION__P5` double,
  `UNSATISFACTORY_RESULTS_C_U_EVALUATION` tinyint(1),
  `UNSATISFACTORY_RESULTS_C_U_ORGANIZATION` tinyint(1),
  `UNSATISFACTORY_RESULTS_ESF_E_C_T_S_C_U` tinyint(1),
  `VALID_INITIAL_FORM_ANSWERS_NUMBER` double,
  `VALID_INITIAL_FORM_ANSWERS_RATIO` double,
  `VALID_INQUIRY_ANSWERS_NUMBER` double,
  `VALID_INQUIRY_ANSWERS_RATIO` double,
  primary key (ID_INTERNAL),
  index (KEY_EXECUTION_COURSE),
  index (KEY_EXECUTION_DEGREE),
  index (KEY_ROOT_DOMAIN_OBJECT)
) type=InnoDB ;




-- Inserted at 2008-11-04T18:37:46.171Z

alter table STUDENT_INQUIRIES_TEACHING_RESULT add column AVERAGE__P6_1 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column AVERAGE__P6_2 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column AVERAGE__P6_3 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column AVERAGE__P7_1 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column AVERAGE__P7_2 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column AVERAGE__P7_3 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column AVERAGE__P7_4 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column AVERAGE__P8_1 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column AVERAGE__P8_2 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column AVERAGE__P9 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column INTERNAL_DEGREE_DISCLOSURE tinyint(1);
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column INTERNAL_DISCLOSURE tinyint(1);
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column KEY_EXECUTION_DEGREE int(11);
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column KEY_PROFESSORSHIP int(11);
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column KEY_ROOT_DOMAIN_OBJECT int(11);
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column NUMBER_OF_ANSWERS int(11);
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column NUMBER__P6_1 int(11);
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column NUMBER__P6_1_VALUES int(11);
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column NUMBER__P6_2 int(11);
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column NUMBER__P6_3 int(11);
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column NUMBER__P7_1 int(11);
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column NUMBER__P7_2 int(11);
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column NUMBER__P7_3 int(11);
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column NUMBER__P7_4 int(11);
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column NUMBER__P8_1 int(11);
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column NUMBER__P8_2 int(11);
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column NUMBER__P9 int(11);
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column P6_1_1_A double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column P6_1_1_B double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column P6_1_1_C double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column P6_1_1_D double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column P6_1_1_E double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column P6_1_GREATER_3 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P6_1_1 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P6_1_2 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P6_1_3 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P6_1_4 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P6_1_5 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P6_1_6 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P6_1_7 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P6_1_A double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P6_1_B double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P6_1_C double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P6_1_D double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P6_1_E double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P6_2_1 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P6_2_2 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P6_2_3 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P6_2_4 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P6_2_5 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P6_2_6 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P6_2_7 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P6_2_8 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P6_2_9 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P6_3_1 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P6_3_2 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P6_3_3 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P6_3_4 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P6_3_5 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P6_3_6 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P6_3_7 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P6_3_8 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P6_3_9 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P7_1_1 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P7_1_2 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P7_1_3 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P7_1_4 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P7_1_5 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P7_1_6 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P7_1_7 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P7_1_8 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P7_1_9 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P7_2_1 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P7_2_2 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P7_2_3 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P7_2_4 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P7_2_5 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P7_2_6 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P7_2_7 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P7_2_8 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P7_2_9 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P7_3_1 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P7_3_2 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P7_3_3 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P7_3_4 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P7_3_5 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P7_3_6 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P7_3_7 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P7_3_8 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P7_3_9 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P7_4_1 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P7_4_2 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P7_4_3 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P7_4_4 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P7_4_5 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P7_4_6 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P7_4_7 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P7_4_8 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P7_4_9 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P8_1_1 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P8_1_2 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P8_1_3 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P8_1_4 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P8_1_5 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P8_1_6 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P8_1_7 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P8_1_8 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P8_1_9 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P8_2_1 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P8_2_2 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P8_2_3 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P8_2_4 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P8_2_5 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P8_2_6 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P8_2_7 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P8_2_8 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P8_2_9 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P9_1 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P9_2 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PERC__P9_3 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PUBLIC_DEGREE_DISCLOSURE tinyint(1);
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column PUBLIC_DISCLOSURE tinyint(1);
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column SHIFT_TYPE text;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column STANDARD_DEVIATION__P6_1 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column STANDARD_DEVIATION__P6_2 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column STANDARD_DEVIATION__P6_3 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column STANDARD_DEVIATION__P7_1 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column STANDARD_DEVIATION__P7_2 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column STANDARD_DEVIATION__P7_3 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column STANDARD_DEVIATION__P7_4 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column STANDARD_DEVIATION__P8_1 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column STANDARD_DEVIATION__P8_2 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column STANDARD_DEVIATION__P9 double;
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column UNSATISFACTORY_RESULTS_ASSIDUITY tinyint(1);
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column UNSATISFACTORY_RESULTS_AUDITABLE tinyint(1);
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column UNSATISFACTORY_RESULTS_PEDAGOGICAL_CAPACITY tinyint(1);
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column UNSATISFACTORY_RESULTS_PRESENCIAL_LEARNING tinyint(1);
alter table STUDENT_INQUIRIES_TEACHING_RESULT add column UNSATISFACTORY_RESULTS_STUDENT_INTERACTION tinyint(1);
alter table STUDENT_INQUIRIES_TEACHING_RESULT add index (KEY_EXECUTION_DEGREE);
alter table STUDENT_INQUIRIES_TEACHING_RESULT add index (KEY_PROFESSORSHIP);
alter table STUDENT_INQUIRIES_TEACHING_RESULT add index (KEY_ROOT_DOMAIN_OBJECT);
