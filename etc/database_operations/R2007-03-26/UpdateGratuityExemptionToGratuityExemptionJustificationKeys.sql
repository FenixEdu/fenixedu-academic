select concat('UPDATE EXEMPTION SET KEY_EXEMPTION_JUSTIFICATION = ',ID_INTERNAL, 
' WHERE (KEY_EXEMPTION_JUSTIFICATION IS NULL OR KEY_EXEMPTION_JUSTIFICATION = 0) AND (OJB_CONCRETE_CLASS=\'net.sourceforge.fenixedu.domain.accounting.events.gratuity.PercentageGratuityExemption\' OR OJB_CONCRETE_CLASS=\'net.sourceforge.fenixedu.domain.accounting.events.gratuity.ValueGratuityExemption\') AND ID_INTERNAL = ',KEY_EXEMPTION,';')  
as "" from EXEMPTION_JUSTIFICATION  
where OJB_CONCRETE_CLASS='net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityExemptionJustification';
