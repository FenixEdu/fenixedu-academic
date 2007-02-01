select concat('UPDATE GRATUITY_VALUES SET PENALTY_APPLICABLE = 0 WHERE ID_INTERNAL = ',ID_INTERNAL,';') as "" 
from GRATUITY_VALUES 
where ID_INTERNAL not in (select ID_INTERNAL 
							from GRATUITY_VALUES 
							where KEY_EXECUTION_DEGREE in (select ID_INTERNAL 
															from EXECUTION_DEGREE 
															where KEY_EXECUTION_YEAR  = 45));

