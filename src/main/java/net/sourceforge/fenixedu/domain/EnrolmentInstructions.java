package net.sourceforge.fenixedu.domain;

import pt.ist.fenixframework.Atomic;

public class EnrolmentInstructions extends EnrolmentInstructions_Base {

    public EnrolmentInstructions() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
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

}
