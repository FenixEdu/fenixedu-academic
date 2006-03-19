package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.credits.event.ICreditsEventOriginator;

public class ShiftProfessorship extends ShiftProfessorship_Base implements ICreditsEventOriginator {

    public ShiftProfessorship() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete() {
        removeShift();
        removeProfessorship();
        super.deleteDomainObject();
    }

    public boolean belongsToExecutionPeriod(ExecutionPeriod executionPeriod) {
        return this.getProfessorship().getExecutionCourse().getExecutionPeriod().equals(executionPeriod);
    }

}
