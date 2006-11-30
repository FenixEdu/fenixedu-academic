update ACCOUNTING_EVENT 
set OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.accounting.events.gratuity.DfaGratuityEvent' 
where OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityEvent';
