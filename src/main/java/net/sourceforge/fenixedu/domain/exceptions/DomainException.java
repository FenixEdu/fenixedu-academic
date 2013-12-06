package net.sourceforge.fenixedu.domain.exceptions;

import javax.ws.rs.core.Response.Status;

public class DomainException extends pt.ist.bennu.core.domain.exceptions.DomainException {

    private static final String DEFAULT_BUNDLE = "resources.ApplicationResources";

    protected DomainException() {
        this(null, (String[]) null);
    }

    public DomainException(final String key, final String... args) {
        super(DEFAULT_BUNDLE, key, args);
    }

    public DomainException(Status status, String key, String... args) {
        super(status, DEFAULT_BUNDLE, key, args);
    }

    public DomainException(final String key, final Throwable cause, final String... args) {
        super(cause, DEFAULT_BUNDLE, key, args);
    }

}
