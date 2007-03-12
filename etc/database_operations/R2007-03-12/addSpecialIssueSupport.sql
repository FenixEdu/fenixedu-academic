 alter table JOURNAL_ISSUE add column SPECIAL_ISSUE tinyint(1);
 alter table JOURNAL_ISSUE add column SPECIAL_ISSUE_COMMENT varchar(255);
 update JOURNAL_ISSUE SET SPECIAL_ISSUE=0;

