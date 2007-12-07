package net.sourceforge.fenixedu.domain.functionalities;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class ExecutionPath extends ExecutionPath_Base {
    
    protected ExecutionPath() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public ExecutionPath(final Functionality functionality, final String executionPath) {
        this();
        setFunctionality(functionality);
        setExecutionPath(executionPath);
    }

    public void delete() {
	removeFunctionality();
	removeRootDomainObject();
	super.deleteDomainObject();
    }
    
}
