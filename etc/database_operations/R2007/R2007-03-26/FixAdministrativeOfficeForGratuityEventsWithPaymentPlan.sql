update ACCOUNTING_EVENT 
SET KEY_ADMINISTRATIVE_OFFICE = 2
WHERE OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityEventWithPaymentPlan' and KEY_ADMINISTRATIVE_OFFICE IS NULL;

