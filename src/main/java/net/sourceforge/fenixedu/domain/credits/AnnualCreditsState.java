package net.sourceforge.fenixedu.domain.credits;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.LocalDate;

import pt.ist.fenixframework.Atomic;

public class AnnualCreditsState extends AnnualCreditsState_Base {

    public AnnualCreditsState(ExecutionYear executionYear) {
        super();
        setExecutionYear(executionYear);
        setOrientationsCalculationDate(new LocalDate(executionYear.getBeginCivilYear(), 12, 31));
        setIsOrientationsCalculated(false);
        setIsFinalCreditsCalculated(false);
        setIsCreditsClosed(false);
        setRootDomainObject(RootDomainObject.getInstance());
    }

    @Atomic
    public static AnnualCreditsState getAnnualCreditsState(ExecutionYear executionYear) {
        AnnualCreditsState annualCreditsState = executionYear.getAnnualCreditsState();
        if (annualCreditsState == null) {
            annualCreditsState = new AnnualCreditsState(executionYear);
        }
        return annualCreditsState;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.credits.AnnualTeachingCredits> getAnnualTeachingCredits() {
        return getAnnualTeachingCreditsSet();
    }

    @Deprecated
    public boolean hasAnyAnnualTeachingCredits() {
        return !getAnnualTeachingCreditsSet().isEmpty();
    }

    @Deprecated
    public boolean hasIsCreditsClosed() {
        return getIsCreditsClosed() != null;
    }

    @Deprecated
    public boolean hasCloseCreditsDate() {
        return getCloseCreditsDate() != null;
    }

    @Deprecated
    public boolean hasIsFinalCreditsCalculated() {
        return getIsFinalCreditsCalculated() != null;
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasUnitCreditsInterval() {
        return getUnitCreditsInterval() != null;
    }

    @Deprecated
    public boolean hasSharedUnitCreditsInterval() {
        return getSharedUnitCreditsInterval() != null;
    }

    @Deprecated
    public boolean hasFinalCalculationDate() {
        return getFinalCalculationDate() != null;
    }

    @Deprecated
    public boolean hasLastModifiedDate() {
        return getLastModifiedDate() != null;
    }

    @Deprecated
    public boolean hasCreationDate() {
        return getCreationDate() != null;
    }

    @Deprecated
    public boolean hasOrientationsCalculationDate() {
        return getOrientationsCalculationDate() != null;
    }

    @Deprecated
    public boolean hasIsOrientationsCalculated() {
        return getIsOrientationsCalculated() != null;
    }

    @Deprecated
    public boolean hasExecutionYear() {
        return getExecutionYear() != null;
    }

}
