
-- Inserted at 2008-08-29T11:59:22.530+01:00

alter table INQUIRIES_REGISTRY add index (KEY_CURRICULAR_COURSE);
alter table PHOTOGRAPH add index (KEY_NEXT);
alter table PHOTOGRAPH add index (KEY_PENDING_HOLDER);
alter table PHOTOGRAPH add index (KEY_PREVIOUS);


