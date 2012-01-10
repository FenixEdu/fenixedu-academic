package net.sourceforge.fenixedu.domain.phd.debts;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public class PhdGratuityFctScholarshipExemption extends PhdGratuityFctScholarshipExemption_Base {

    public PhdGratuityFctScholarshipExemption() {
	super();
    }

    public PhdGratuityFctScholarshipExemption(Employee who, Event event, Money value) {
	PhdEventExemptionJustification exemptionJustification = new PhdEventExemptionJustification(this,
		PhdEventExemptionJustificationType.PHD_GRATUITY_FCT_SCHOLARSHIP_EXEMPTION, event.getWhenOccured().toLocalDate(),
		"Criado pela existencia de bolsa da FCT.");	
	super.init(who, event, exemptionJustification);
	
	createFctDebt();
	setValue(value);
	setRootDomainObject(RootDomainObject.getInstance());
	event.recalculateState(new DateTime());
    }

    private void createFctDebt() {
	FctScholarshipPhdGratuityContribuitionEvent event = new FctScholarshipPhdGratuityContribuitionEvent();
	this.setFctScholarshipPhdGratuityContribuitionEvent(event);
    }

    @Service
    public static PhdGratuityFctScholarshipExemption createPhdGratuityFctScholarshipExemption(Employee who, Money value,
	    PhdGratuityEvent event) {
	if (event.hasExemptionsOfType(PhdGratuityFctScholarshipExemption.class)){
	   throw new DomainException("error.already.has.scolarship"); 
	}
	PhdGratuityFctScholarshipExemption phdGratuityFctScholarshipExemption = new PhdGratuityFctScholarshipExemption(who,
		event, value);
	return phdGratuityFctScholarshipExemption;
    }
    
    @Override
    public LabelFormatter getDescription() {
	PhdGratuityEvent event = (PhdGratuityEvent) getEvent();
	return new LabelFormatter().appendLabel("Bolsa da FCT aplicada à Propina do Programa de Doutoramento de ").appendLabel(event.getPhdProgram().getName().getContent()).appendLabel(" referente a " +  event.getYear());
    }
   
    public void doDelete(){
	removeFctScholarshipPhdGratuityContribuitionEvent();
	super.delete();
    }
    
    @Override
    public void delete() {
	FctScholarshipPhdGratuityContribuitionEvent event = getFctScholarshipPhdGratuityContribuitionEvent();
	event.delete();
    }
    
    public Money getAmoutStillMissing(){
	return getFctScholarshipPhdGratuityContribuitionEvent().calculateAmountToPay();
    }
}
