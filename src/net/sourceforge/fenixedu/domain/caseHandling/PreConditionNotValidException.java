package net.sourceforge.fenixedu.domain.caseHandling;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class PreConditionNotValidException extends DomainException {

	public PreConditionNotValidException() {
		super("error.precondition.not.valid", (String[]) null);
	}

	public PreConditionNotValidException(final String key, final String... args) {
		super(key, args);
	}

	public PreConditionNotValidException(final String key, final Throwable cause, final String... args) {
		super(key, cause, args);
	}

}
