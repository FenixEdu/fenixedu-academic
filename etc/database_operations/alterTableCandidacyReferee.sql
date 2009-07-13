
alter table `PHD_CANDIDACY_REFEREE_LETTER` add column `ACADEMIC_PERFORMANCE` text;
alter table `PHD_CANDIDACY_REFEREE_LETTER` add column `CAPACITY` text;
alter table `PHD_CANDIDACY_REFEREE_LETTER` add column `HOW_LONG_KNOWN_APPLICANT` text;
alter table `PHD_CANDIDACY_REFEREE_LETTER` add column `POTENCIAL_TO_EXCEL_PHD` text;
alter table `PHD_CANDIDACY_REFEREE_LETTER` add column `RANK_IN_CLASS` text;
alter table `PHD_CANDIDACY_REFEREE_LETTER` add column `SOCIAL_AND_COMMUNICATION_SKILLS` text;

alter table `PHD_CANDIDACY_REFEREE_LETTER` drop column RANK, drop column RANK_VALUE, drop column OVERALL_PROMISE, drop column DATE, drop column REFEREE_PHONE;
