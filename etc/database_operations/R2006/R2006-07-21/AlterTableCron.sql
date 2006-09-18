alter table CRON_SCRIPT_STATE add column EMAILS text default null;
alter table CRON_SCRIPT_STATE add column WHEN_TO_SEND_EMAIL text default null;