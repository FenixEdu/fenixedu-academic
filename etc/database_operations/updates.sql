alter table `PROJECT_ACCESS change column KEY_PROJECT KEY_PROJECT text;

alter table PROJECT_ACCESS change column IT_PROJECT IT_PROJECT tinyint(1) DEFAULT '0';
