package net.sourceforge.fenixedu.domain;

import java.util.Comparator;

import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.joda.time.DateTime;

public class DomainOperationLog extends DomainOperationLog_Base {

    protected static final String SEPARATOR = " - ";
    protected static final String DATE_FORMAT = "dd/MM/yyyy HH:mm:ss.SSS";

    public static final Comparator<DomainOperationLog> COMPARATOR_BY_WHEN_DATETIME = new Comparator<DomainOperationLog>() {

        @Override
        public int compare(DomainOperationLog domainOperationLog1, DomainOperationLog domainOperationLog2) {
            final DateTime dateTime1 = domainOperationLog1.getWhenDateTime();
            final DateTime dateTime2 = domainOperationLog2.getWhenDateTime();
            int res = dateTime2.compareTo(dateTime1);
            return res;
        }
    };

    public DomainOperationLog() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        this.setPerson(AccessControl.getPerson());
        this.setWhenDateTime(new DateTime());
    }

    public void delete() {
        setPerson(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

}
