delete from ACCOUNTING_EVENT where OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityEventWithPaymentPlan' and WHEN_OCCURED > '2006-11-13';
delete from PAYMENT_CODE;
delete from ACCOUNTING_EVENT where OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceEvent' and WHEN_OCCURED > '2006-11-13';

