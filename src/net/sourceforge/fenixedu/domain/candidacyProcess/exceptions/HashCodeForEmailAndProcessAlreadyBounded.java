package net.sourceforge.fenixedu.domain.candidacyProcess.exceptions;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class HashCodeForEmailAndProcessAlreadyBounded extends DomainException {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	public HashCodeForEmailAndProcessAlreadyBounded() {

	}

	public HashCodeForEmailAndProcessAlreadyBounded(String key, String... args) {
		super(key, args);
	}

}
