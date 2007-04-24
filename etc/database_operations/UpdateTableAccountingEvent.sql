update ACCOUNTING_EVENT set WHEN_SENT_LETTER ='2006-11-15' 
where (OJB_CONCRETE_CLASS='net.sourceforge.fenixedu.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceEvent'
  or OJB_CONCRETE_CLASS='net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityEventWithPaymentPlan')
and WHEN_OCCURED < '2006-11-15 20:00:00';

update ACCOUNTING_EVENT set WHEN_SENT_LETTER ='2007-03-02' 
where (OJB_CONCRETE_CLASS='net.sourceforge.fenixedu.domain.accounting.events.gratuity.DfaGratuityEvent'
  or OJB_CONCRETE_CLASS='net.sourceforge.fenixedu.domain.accounting.events.insurance.InsuranceEvent')
and WHEN_OCCURED < '2007-03-02 18:00:00' and EVENT_STATE_DATE < '2007-03-01 18:00:00'

