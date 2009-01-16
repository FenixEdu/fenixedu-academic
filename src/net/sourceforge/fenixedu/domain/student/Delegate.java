package net.sourceforge.fenixedu.domain.student;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.organizationalStructure.DegreeUnit;

public class Delegate extends Delegate_Base {

    public Delegate() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public boolean isActiveForExecutionYear(final ExecutionYear executionYear) {
	return getDelegateFunction().belongsToPeriod(executionYear.getBeginDateYearMonthDay(),
		executionYear.getEndDateYearMonthDay());
    }

    protected Degree getDegree() {
	return ((DegreeUnit) getDelegateFunction().getFunction().getUnit()).getDegree();
    }

}
