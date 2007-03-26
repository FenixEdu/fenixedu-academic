select concat('DELETE FROM EXEMPTION_JUSTIFICATION WHERE ID_INTERNAL =',EJ.ID_INTERNAL,';') as "" 
from EXEMPTION_JUSTIFICATION EJ left join EXEMPTION E on EJ.KEY_EXEMPTION = E.ID_INTERNAL 
where EJ.OJB_CONCRETE_CLASS like 'net.sourceforge.fenixedu.domain.accounting.events.%PenaltyExemptionJustification%' and E.ID_INTERNAL IS NULL;

