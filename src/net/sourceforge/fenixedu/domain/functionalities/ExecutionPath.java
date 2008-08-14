package net.sourceforge.fenixedu.domain.functionalities;

import java.util.UUID;

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
	setContentId(UUID.randomUUID().toString());
    }

    public void delete() {
	removeFunctionality();
	removeRootDomainObject();
	super.deleteDomainObject();
    }

}
