package net.sourceforge.fenixedu.domain.exceptions;

import net.sourceforge.fenixedu.util.InvocationResult;

public class DomainExceptionWithInvocationResult extends DomainException {

    /**
     * 
     */
    private static final long serialVersionUID = 5094019528013010319L;

    private InvocationResult invocationResult;

    public DomainExceptionWithInvocationResult(InvocationResult result) {
	super();
	this.invocationResult = result;
    }

    public InvocationResult getInvocationResult() {
	return invocationResult;
    }

}
