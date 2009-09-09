alter table `MEASUREMENT_TEST_ROOM` add column `ROOM_ORDER` int(11) not null;
alter table `MEASUREMENT_TEST_ROOM` add unique index (KEY_SHIFT, ROOM_ORDER);
alter table `MEASUREMENT_TEST` add column `ENTRY_PHASE` int(11) not null;



