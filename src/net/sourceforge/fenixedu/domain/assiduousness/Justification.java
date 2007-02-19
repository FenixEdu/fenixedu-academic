package net.sourceforge.fenixedu.domain.assiduousness;

import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.Partial;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;
import org.joda.time.chrono.GregorianChronology;

public class Justification extends Justification_Base {

    public Justification() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public Partial getPartialDate() {
        Partial p = new Partial();
        YearMonthDay y = getDate().toDateTime(GregorianChronology.getInstanceUTC()).toYearMonthDay();
        if (y != null) {
            for (int i = 0; i < y.getFields().length; i++) {
                p = p.with(y.getFieldType(i), y.getValue(i));
            }
        }
        TimeOfDay t = getDate().toDateTime(GregorianChronology.getInstanceUTC()).toTimeOfDay();
        if (t != null && !t.isEqual(new TimeOfDay(0, 0, 0))) {
            for (int i = 0; i < t.getFields().length; i++) {
                p = p.with(t.getFieldType(i), t.getValue(i));
            }
        }
        return p;
    }

    public Partial getPartialEndDate() {
        if (this instanceof Leave) {
            return ((Leave) this).getPartialEndDate();
        }
        return null;
    }
}
