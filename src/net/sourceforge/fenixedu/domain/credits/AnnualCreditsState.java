package net.sourceforge.fenixedu.domain.credits;

import net.sourceforge.fenixedu.domain.ExecutionYear;

import org.joda.time.LocalDate;

import pt.ist.fenixWebFramework.services.Service;

public class AnnualCreditsState extends AnnualCreditsState_Base {

    public AnnualCreditsState(ExecutionYear executionYear) {
	super();
	setExecutionYear(executionYear);
	setOrientationsCalculationDate(new LocalDate(executionYear.getBeginCivilYear(), 12, 31));
	setIsOrientationsCalculated(false);
	setIsFinalCreditsCalculated(false);
	setIsCreditsClosed(false);
    }

    @Service
    public static AnnualCreditsState getAnnualCreditsState(ExecutionYear executionYear) {
	AnnualCreditsState annualCreditsState = executionYear.getAnnualCreditsState();
	if (annualCreditsState == null) {
	    annualCreditsState = new AnnualCreditsState(executionYear);
	}
	return annualCreditsState;
    }

}
