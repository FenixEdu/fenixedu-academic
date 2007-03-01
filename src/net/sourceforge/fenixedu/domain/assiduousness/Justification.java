package net.sourceforge.fenixedu.domain.assiduousness;

import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.Partial;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

public class Justification extends Justification_Base {

    public Justification() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public Partial getPartialDate() {
        Partial p = new Partial();
        YearMonthDay y = getDate().toYearMonthDay();
        if (y != null) {
            for (int i = 0; i < y.getFields().length; i++) {
                p = p.with(y.getFieldType(i), y.getValue(i));
            }
        }
        TimeOfDay t = getDate().toTimeOfDay();
        if (t != null && !t.isEqual(new TimeOfDay(0, 0, 0))) {
            for (int i = 0; i < t.getFields().length; i++) {
                p = p.with(t.getFieldType(i), t.getValue(i));
            }
        }
        return p;
    }

    public Partial getPartialEndDate() {
	if (this.isLeave()) {
            return ((Leave) this).getPartialEndDate();
        }
        return null;
    }
    
    public Integer getModifiedByNumber(){
        if(getModifiedBy() != null){
            return getModifiedBy().getEmployeeNumber();
        }
        return null;
    }
}
