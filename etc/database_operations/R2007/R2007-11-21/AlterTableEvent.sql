alter table EVENT add column PAST_ADMINISTRATIVE_OFFICE_FEE_AND_INSURANCE_AMOUNT varchar(255) null;
alter table EVENT add column PAST_DEGREE_GRATUITY_AMOUNT varchar(255) null;
alter table EVENT add index (KEY_PHD_CANDIDACY);
