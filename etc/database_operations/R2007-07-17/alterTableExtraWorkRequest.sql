update EXTRA_WORK_REQUEST set ADD_TO_WEEK_REST_TIME = 0 where ADD_TO_WEEK_REST_TIME is null;
alter table EXTRA_WORK_REQUEST modify ADD_TO_WEEK_REST_TIME tinyint(1) default 0;
alter table EXTRA_WORK_REQUEST modify ADD_TO_VACATIONS tinyint(1) default 0;