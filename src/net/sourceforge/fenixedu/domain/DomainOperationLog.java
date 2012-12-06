package net.sourceforge.fenixedu.domain;

import java.util.Comparator;

import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.joda.time.DateTime;

public class DomainOperationLog extends DomainOperationLog_Base {

    protected static final String SEPARATOR = " - ";
    protected static final String DATE_FORMAT = "dd/MM/yyyy HH:mm:ss.SSS";

    public static final Comparator<DomainOperationLog> COMPARATOR_BY_WHEN_DATETIME = new Comparator<DomainOperationLog>() {

	@Override
	public int compare(DomainOperationLog log1, DomainOperationLog log2) {
	    final DateTime d1 = log1.getWhenDateTime();
	    final DateTime d2 = log2.getWhenDateTime();
	    int res = d2.compareTo(d1);
	    return res;
	}
    };

    public DomainOperationLog() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	this.setPerson(AccessControl.getPerson());
	this.setWhenDateTime(new DateTime());
    }

}
