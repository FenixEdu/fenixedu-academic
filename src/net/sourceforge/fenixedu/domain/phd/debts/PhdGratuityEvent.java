package net.sourceforge.fenixedu.domain.phd.debts;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdProgram;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public class PhdGratuityEvent extends PhdGratuityEvent_Base {    
    public  PhdGratuityEvent(PhdIndividualProgramProcess process, int year) {
        super();
        if (process.hasPhdGratuityEventForYear(year)){
            throw new DomainException("error.PhdRegistrationFee.process.already.has.registration.fee.for.this.year");
        }
        init(EventType.PHD_GRATUITY, process.getPerson());
        setYear(year);
        setPhdIndividualProgramProcess(process);
    }

    @Override
    protected PhdProgram getPhdProgram() {
	return getPhdIndividualProgramProcess().getPhdProgram();
    }
    
    @Service
    static public PhdGratuityEvent create(PhdIndividualProgramProcess phdIndividualProgramProcess, int year){
	return new PhdGratuityEvent(phdIndividualProgramProcess,year);
    }
    
    @Override
    public LabelFormatter getDescriptionForEntryType(final EntryType entryType) {
	return new LabelFormatter().appendLabel(entryType.name(), "enum").appendLabel(" - ").appendLabel("" + getYear()).appendLabel(" (").appendLabel(
		getPhdProgram().getName().getContent()).appendLabel(")");
    }
    
    @Override
    public boolean isExemptionAppliable() {
        return true;
    }
    
    public DateTime getLimitDateToPay() {
	LocalDate whenFormalizedRegistration = getPhdIndividualProgramProcess().getWhenFormalizedRegistration();
	
	PhdGratuityPaymentPeriod phdGratuityPeriod = ((PhdGratuityPR) getPostingRule()).getPhdGratuityPeriod(whenFormalizedRegistration);
	
	return new LocalDate(getYear(),phdGratuityPeriod.getMonthLastPayment(),phdGratuityPeriod.getDayLastPayment()).toDateMidnight().toDateTime();
    }
    
}
