select concat('update EXECUTION_PERIOD set EXECUTION_INTERVAL = concat(EXECUTION_INTERVAL, ":",' , ID_INTERNAL  , ');') as "" from ACADEMIC_CALENDAR_ENTRY where OJB_CONCRETE_CLASS like '%Root%';
select concat('update EXECUTION_YEAR set EXECUTION_INTERVAL = concat(EXECUTION_INTERVAL, ":",' , ID_INTERNAL  , ');') as "" from ACADEMIC_CALENDAR_ENTRY where OJB_CONCRETE_CLASS like '%Root%';
 