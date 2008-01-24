package net.sourceforge.fenixedu.domain.assiduousness;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.util.ClosedMonthDocumentType;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

public class ClosedMonthDocument extends ClosedMonthDocument_Base {

    public ClosedMonthDocument(ClosedMonthFile closedMonthFile, ClosedMonth closedMonth,
	    ClosedMonthDocumentType closedMonthDocumentType) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setClosedMonth(closedMonth);
	setClosedMonthFile(closedMonthFile);
	setClosedMonthDocumentType(closedMonthDocumentType);
	setCreatedWhen(new DateTime());
    }

    public void delete() {
	removeRootDomainObject();
	removeClosedMonth();
	getClosedMonthFile().delete();
	deleteDomainObject();
    }

    public String getFormattedCreationDate() {
	return getCreatedWhen().toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
