package net.sourceforge.fenixedu.domain.functionalities;

import java.util.UUID;

import org.fenixedu.bennu.core.domain.Bennu;

public class ExecutionPath extends ExecutionPath_Base {

    protected ExecutionPath() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public ExecutionPath(final Functionality functionality, final String executionPath) {
        this();
        setFunctionality(functionality);
        setExecutionPath(executionPath);
        setContentId(UUID.randomUUID().toString());
    }

    public void delete() {
        setFunctionality(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasContentId() {
        return getContentId() != null;
    }

    @Deprecated
    public boolean hasFunctionality() {
        return getFunctionality() != null;
    }

    @Deprecated
    public boolean hasExecutionPath() {
        return getExecutionPath() != null;
    }

}
