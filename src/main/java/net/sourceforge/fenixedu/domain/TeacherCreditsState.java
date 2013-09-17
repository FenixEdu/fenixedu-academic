package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.credits.CreditsState;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.joda.time.DateTime;

public class TeacherCreditsState extends TeacherCreditsState_Base {

    public TeacherCreditsState(ExecutionSemester executionSemester) {
        super();
        setExecutionSemester(executionSemester);
        setBasicOperations();
        setCloseState();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public boolean isOpenState() {
        return getCreditState() == CreditsState.OPEN;
    }

    public boolean isCloseState() {
        return getCreditState() == CreditsState.CLOSE;
    }

    public void setOpenState() {
        setCreditState(CreditsState.OPEN);
        setBasicOperations();
    }

    public void setCloseState() {
        setCreditState(CreditsState.CLOSE);
        setBasicOperations();
    }

    private void setBasicOperations() {
        setPerson(AccessControl.getPerson());
        setLastModifiedDate(new DateTime());
    }

    public static TeacherCreditsState getTeacherCreditsState(ExecutionSemester executionSemester) {
        for (TeacherCreditsState teacherCreditsState : RootDomainObject.getInstance().getTeacherCreditsStateSet()) {
            if (teacherCreditsState.getExecutionSemester().equals(executionSemester)) {
                return teacherCreditsState;
            }
        }
        return null;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.TeacherCredits> getTeacherCredits() {
        return getTeacherCreditsSet();
    }

    @Deprecated
    public boolean hasAnyTeacherCredits() {
        return !getTeacherCreditsSet().isEmpty();
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasLastModifiedDate() {
        return getLastModifiedDate() != null;
    }

    @Deprecated
    public boolean hasExecutionSemester() {
        return getExecutionSemester() != null;
    }

    @Deprecated
    public boolean hasCreditState() {
        return getCreditState() != null;
    }

    @Deprecated
    public boolean hasPerson() {
        return getPerson() != null;
    }

}
