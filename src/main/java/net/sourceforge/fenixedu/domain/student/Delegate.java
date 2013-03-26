package net.sourceforge.fenixedu.domain.student;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.organizationalStructure.DegreeUnit;

import org.joda.time.Interval;

public class Delegate extends Delegate_Base {

    public Delegate() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public boolean isActiveForExecutionYear(final ExecutionYear executionYear) {
        return hasDelegateFunction()
                && getDelegateFunction().belongsToPeriod(executionYear.getBeginDateYearMonthDay(),
                        executionYear.getEndDateYearMonthDay());
    }

    public boolean isActiveForFirstExecutionYear(final ExecutionYear executionYear) {
        if (hasDelegateFunction() && getDelegateFunction().getBeginDate() != null) {
            Interval interval =
                    new Interval(getDelegateFunction().getBeginDate().toDateTimeAtMidnight(), getDelegateFunction().getEndDate()
                            .toDateTimeAtMidnight().plusDays(1));
            return executionYear.overlapsInterval(interval);
        }
        return false;
    }

    protected Degree getDegree() {
        return ((DegreeUnit) getDelegateFunction().getFunction().getUnit()).getDegree();
    }

    public void delete() {
        getDelegateFunction().delete();
        removeDelegateFunction();
        removeRegistration();
        removeRootDomainObject();
        deleteDomainObject();
    }

}
