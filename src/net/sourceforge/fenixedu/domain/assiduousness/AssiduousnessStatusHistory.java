package net.sourceforge.fenixedu.domain.assiduousness;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

public class AssiduousnessStatusHistory extends AssiduousnessStatusHistory_Base {

    public AssiduousnessStatusHistory(Assiduousness assiduousness, AssiduousnessStatus assiduousnessStatus,
	    YearMonthDay beginDate, YearMonthDay endDate, DateTime lastModifiedDate, Employee modifiedBy) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setAssiduousness(assiduousness);
	setAssiduousnessStatus(assiduousnessStatus);
	setBeginDate(beginDate);
	setEndDate(endDate);
	setLastModifiedDate(lastModifiedDate);
	setModifiedBy(modifiedBy);
    }

    public AssiduousnessStatusHistory(Assiduousness assiduousness, AssiduousnessStatus assiduousnessStatus,
	    YearMonthDay beginDate, YearMonthDay endDate, Employee modifiedBy) {
	AssiduousnessStatusHistory lastAssiduousnessStatusHistory = assiduousness.getLastAssiduousnessStatusHistory();
	if (lastAssiduousnessStatusHistory != null) {
	    if(lastAssiduousnessStatusHistory.getAssiduousnessStatus().equals(assiduousnessStatus)) {
		throw new DomainException("error.sameAssiduousnessStatus");
	    }

	    if (lastAssiduousnessStatusHistory.getEndDate() == null) {
		if (!beginDate.isAfter(ClosedMonth.getLastMonthClosed().getLastClosedYearMonthDay())) {
		    throw new DomainException("error.datesInclosedMonth");
		}
		lastAssiduousnessStatusHistory.setEndDate(beginDate.minusDays(1));
	    }
	}

	setRootDomainObject(RootDomainObject.getInstance());
	setAssiduousness(assiduousness);
	setAssiduousnessStatus(assiduousnessStatus);
	setBeginDate(beginDate);
	setEndDate(endDate);
	setLastModifiedDate(new DateTime());
	setModifiedBy(modifiedBy);

    }

    public void editAssiduousnessStatusHistory(YearMonthDay endDate, Employee modifiedBy) {
	if (endDate != null) {
	    YearMonthDay lastYearMonthDayClosed = ClosedMonth.getLastMonthClosed().getLastClosedYearMonthDay();
	    if (!endDate.isAfter(lastYearMonthDayClosed)) {
		throw new DomainException("error.datesInclosedMonth");
	    }
	    if (!endDate.isAfter(getBeginDate())) {
		throw new DomainException("error.invalidDateInterval");
	    }
	}
	setEndDate(endDate);
    }

    public boolean isEditable() {
	return getAssiduousness().getLastAssiduousnessStatusHistory().getIdInternal().equals(getIdInternal())
		&& (getEndDate() == null || !getEndDate().isBefore(ClosedMonth.getLastMonthClosed().getLastClosedYearMonthDay()));
    }

    public boolean isDeletable() {
	return getAssiduousness().getLastAssiduousnessStatusHistory().equals(this)
		&& !getBeginDate().isBefore(ClosedMonth.getLastMonthClosed().getLastClosedYearMonthDay());
    }

    private boolean canBeDeleted() {
	return getAssiduousnessClosedMonths().isEmpty();
    }

    public void delete() {
	if (canBeDeleted()) {
	    removeRootDomainObject();
	    removeAssiduousness();
	    removeAssiduousnessStatus();
	    removeModifiedBy();
	    deleteDomainObject();
	}
    }
}
