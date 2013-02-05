package net.sourceforge.fenixedu.domain;

import pt.ist.fenixWebFramework.services.Service;

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

    @Service
    public static void createIfNecessary(final ExecutionSemester executionSemester) {
        if (executionSemester.getEnrolmentInstructions() == null) {
            new EnrolmentInstructions(executionSemester);
        }
    }

}
