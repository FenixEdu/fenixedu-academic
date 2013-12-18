package net.sourceforge.fenixedu.domain;

import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixframework.Atomic;

public class EnrolmentInstructions extends EnrolmentInstructions_Base {

    public EnrolmentInstructions() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public EnrolmentInstructions(final ExecutionSemester executionSemester) {
        this();
        setExecutionSemester(executionSemester);
        setInstructions("");
    }

    @Atomic
    public static void createIfNecessary(final ExecutionSemester executionSemester) {
        if (executionSemester.getEnrolmentInstructions() == null) {
            new EnrolmentInstructions(executionSemester);
        }
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasInstructions() {
        return getInstructions() != null;
    }

    @Deprecated
    public boolean hasExecutionSemester() {
        return getExecutionSemester() != null;
    }

}
