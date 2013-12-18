package net.sourceforge.fenixedu.domain;

import org.fenixedu.bennu.core.domain.Bennu;

import net.sourceforge.fenixedu.domain.credits.event.ICreditsEventOriginator;

public class ShiftProfessorship extends ShiftProfessorship_Base implements ICreditsEventOriginator {

    public ShiftProfessorship() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public void delete() {
        setShift(null);
        setProfessorship(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    @Override
    public boolean belongsToExecutionPeriod(ExecutionSemester executionSemester) {
        return this.getProfessorship().getExecutionCourse().getExecutionPeriod().equals(executionSemester);
    }

    @Deprecated
    public boolean hasPercentage() {
        return getPercentage() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasProfessorship() {
        return getProfessorship() != null;
    }

    @Deprecated
    public boolean hasShift() {
        return getShift() != null;
    }

}
