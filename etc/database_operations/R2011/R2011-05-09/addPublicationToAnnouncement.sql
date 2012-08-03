
-- Inserted at 2011-05-09T18:17:24.797+01:00

alter table `CONTENT` add `PUBLICATION` tinyint(1);
update `CONTENT` set `PUBLICATION` = 1;