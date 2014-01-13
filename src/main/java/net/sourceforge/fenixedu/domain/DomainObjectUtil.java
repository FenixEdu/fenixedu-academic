package net.sourceforge.fenixedu.domain;

import java.util.Comparator;

import pt.ist.fenixframework.DomainObject;

public final class DomainObjectUtil {

    public static final Comparator<DomainObject> COMPARATOR_BY_ID = new Comparator<DomainObject>() {
        @Override
        public int compare(DomainObject o1, DomainObject o2) {
            return o1.getExternalId().compareTo(o2.getExternalId());
        }
    };

}
