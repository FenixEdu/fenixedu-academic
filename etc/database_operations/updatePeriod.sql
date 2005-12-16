select concat('update PERIOD set PERIOD.KEY_PREVIOUS_PERIOD = ', PERIOD.ID_INTERNAL, ' where PERIOD.ID_INTERNAL = ', PERIOD.KEY_PERIOD, ';') as "" from PERIOD where PERIOD.KEY_PERIOD is not null;
