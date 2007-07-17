package net.sourceforge.fenixedu.domain.assiduousness;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.DateTime;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

public class MissingClocking extends MissingClocking_Base {

    public MissingClocking(Assiduousness assiduousness, DateTime date,
	    JustificationMotive justificationMotive, DateTime lastModifiedDate, Employee modifiedBy,
	    Integer oracleSequence) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setAssiduousness(assiduousness);
	setDate(date);
	setJustificationMotive(justificationMotive);
	setLastModifiedDate(lastModifiedDate);
	setModifiedBy(modifiedBy);
	setOracleSequence(oracleSequence);
    }

    public MissingClocking(Assiduousness assiduousness, DateTime date,
	    JustificationMotive justificationMotive, Employee modifiedBy) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setAssiduousness(assiduousness);
	setDate(date);
	setJustificationMotive(justificationMotive);
	setLastModifiedDate(new DateTime());
	setModifiedBy(modifiedBy);
	setOracleSequence(0);
    }

    public MissingClocking(Assiduousness assiduousness, JustificationMotive justificationMotive,
	    DateTime dateTime, Employee modifiedBy) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setAssiduousness(assiduousness);
	setDate(dateTime);
	setJustificationMotive(justificationMotive);
	setLastModifiedDate(new DateTime());
	setModifiedBy(modifiedBy);
	setOracleSequence(0);// TODO é mesmo preciso por a zero ??
    }

    public void modify(DateTime date, JustificationMotive justificationMotive, Employee modifiedBy) {
	setDate(date);
	setJustificationMotive(justificationMotive);
	setLastModifiedDate(new DateTime());
	setModifiedBy(modifiedBy);
    }

    public TimeOfDay getTime() {
	return getDate().toTimeOfDay();
    }

    // Check if the Leave occured in a particular date
    public boolean occuredInDate(YearMonthDay date) {
	return (getDate().toYearMonthDay().isAfter(date) || getDate().toYearMonthDay().isEqual(date));
    }

    public boolean isMissingClocking() {
	return true;
    }

}
