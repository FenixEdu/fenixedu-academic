


-- Inserted at 2010-04-07T13:06:51.517+01:00

alter table `ERASMUS_STUDENT_DATA` add column `EN_ABLE_FOLLOW_LECURES` tinyint(1);
alter table `ERASMUS_STUDENT_DATA` add column `EN_ABLE_TO_FOLLOW_LECTURE_WITH_EXTRA_PREPARATION` tinyint(1);
alter table `ERASMUS_STUDENT_DATA` add column `EN_STUDYING_LANGUAGE` tinyint(1);
alter table `ERASMUS_STUDENT_DATA` add column `INTENSIVE_PORTUGUESE_COURSE_FEBRUARY` tinyint(1);
alter table `ERASMUS_STUDENT_DATA` add column `INTENSIVE_PORTUGUESE_COURSE_SEPTEMBER` tinyint(1);
alter table `ERASMUS_STUDENT_DATA` add column `PT_ABLE_FOLLOW_LECURES` tinyint(1);
alter table `ERASMUS_STUDENT_DATA` add column `PT_ABLE_TO_FOLLOW_LECTURE_WITH_EXTRA_PREPARATION` tinyint(1);
alter table `ERASMUS_STUDENT_DATA` add column `PT_STUDYING_LANGUAGE` tinyint(1);


alter table `ERASMUS_STUDENT_DATA` add column `APPLY_FOR` text;

alter table `PARTY` add column `E_IDENTIFIER` text;
