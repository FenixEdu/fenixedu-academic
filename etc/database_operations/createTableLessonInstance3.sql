select concat('update RESOURCE_ALLOCATION set BEGIN = "' , START_YEAR_MONTH_DAY , '", END = "' , END_YEAR_MONTH_DAY , '" where KEY_PERIOD = ' , ID_INTERNAL , ';') as "" from OCCUPATION_PERIOD;