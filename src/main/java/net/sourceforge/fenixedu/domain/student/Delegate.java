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

    public Degree getDegree() {
        return ((DegreeUnit) getDelegateFunction().getFunction().getUnit()).getDegree();
    }

    public void delete() {
        getDelegateFunction().delete();
        setDelegateFunction(null);
        setRegistration(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    @Deprecated
    public boolean hasRegistration() {
        return getRegistration() != null;
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasDelegateFunction() {
        return getDelegateFunction() != null;
    }

}
