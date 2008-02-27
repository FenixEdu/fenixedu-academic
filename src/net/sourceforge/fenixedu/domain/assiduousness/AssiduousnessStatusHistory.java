package net.sourceforge.fenixedu.domain.assiduousness;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.util.DateInterval;

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

    public DateInterval getValidInterval() {
	return new DateInterval(getBeginDate(), getEndDate());
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
