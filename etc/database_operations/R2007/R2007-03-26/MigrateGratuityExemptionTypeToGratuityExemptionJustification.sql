insert into EXEMPTION_JUSTIFICATION(KEY_EXEMPTION,OJB_CONCRETE_CLASS,GRATUITY_EXEMPTION_JUSTIFICATION_TYPE)  
select ID_INTERNAL,'net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityExemptionJustification',GRATUITY_EXEMPTION_TYPE 
from EXEMPTION 
where OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.accounting.events.gratuity.PercentageGratuityExemption' or
OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.accounting.events.gratuity.ValueGratuityExemption';
