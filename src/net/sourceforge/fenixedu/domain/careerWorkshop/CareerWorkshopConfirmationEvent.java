package net.sourceforge.fenixedu.domain.careerWorkshop;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

public class CareerWorkshopConfirmationEvent extends CareerWorkshopConfirmationEvent_Base {
    
    public  CareerWorkshopConfirmationEvent(CareerWorkshopApplicationEvent applicationEvent, DateTime beginDate, DateTime endDate) {
        super();
        evaluateConsistency(applicationEvent, beginDate, endDate);
	setBeginDate(beginDate);
	setEndDate(endDate);
	setCareerWorkshopApplicationEvent(applicationEvent);
	setRootDomainObject(RootDomainObject.getInstance());
    }
    
    private void evaluateConsistency(CareerWorkshopApplicationEvent applicationEvent, DateTime beginDate, DateTime endDate) {
	if (applicationEvent == null)
	    throw new DomainException("error.careerWorkshop.creatingConfirmationEvent: Invalid ApplicationEvent");
	if (beginDate == null || endDate == null)
	    throw new DomainException("error.careerWorkshop.creatingConfirmationEvent: Invalid values for begin/end dates");
	if (!beginDate.isBefore(endDate))
	    throw new DomainException("error.careerWorkshop.creatingConfirmationEvent: Inconsistent values for begin/end dates");
	if (applicationEvent.getCareerWorkshopConfirmationEvent() != null)
	    throw new DomainException("error.careerWorkshop.creatingConfirmationEvent: ApplicationEvent already has confirmation period created");
    }
    
    public void delete() {
	if (!getCareerWorkshopConfirmations().isEmpty())
	    throw new DomainException("error.careerWorkshop.deletingConfirmationPeriod: There are confirmations already associated");
	removeConfirmations();
	setRootDomainObject(null);
	deleteDomainObject();
    }
    
    public String getFormattedBeginDate() {
	return getBeginDate().toString("dd-MM-yyyy");
    }

    public String getFormattedEndDate() {
	return getEndDate().toString("dd-MM-yyyy");
    }
    
}
