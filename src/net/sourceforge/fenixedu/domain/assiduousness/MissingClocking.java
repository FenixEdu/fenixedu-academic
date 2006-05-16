package net.sourceforge.fenixedu.domain.assiduousness;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;

public class MissingClocking extends MissingClocking_Base {

    public MissingClocking(Assiduousness assiduousness, DateTime date,
            JustificationMotive justificationMotive, DateTime lastModifiedDate, Employee modifiedBy) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setAssiduousness(assiduousness);
        setDate(date);
        setJustificationMotive(justificationMotive);
        setLastModifiedDate(lastModifiedDate);
        setModifiedBy(modifiedBy);
        setOjbConcreteClass(MissingClocking.class.getName());
    }
    
    public TimeOfDay getTime() {
        return getDate().toTimeOfDay();
    }
    
    // Check if the Leave occured in a particular date
    public boolean occuredInDate(YearMonthDay date) {
        return (getDate().toYearMonthDay().isAfter(date) || getDate().toYearMonthDay().isEqual(date));
    }
    
    // Converts a Missing Clocking to Clocking - the clockingUnit is null
    public Clocking toClocking() {
        return new Clocking(getAssiduousness(), null, getDate());
    }
    
}
