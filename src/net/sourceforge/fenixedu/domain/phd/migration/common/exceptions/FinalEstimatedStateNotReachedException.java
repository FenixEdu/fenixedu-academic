package net.sourceforge.fenixedu.domain.phd.migration.common.exceptions;

public class FinalEstimatedStateNotReachedException extends RuntimeException {
    public FinalEstimatedStateNotReachedException(String message) {
	super(message);
    }

    public FinalEstimatedStateNotReachedException() {
    }
}
