package org.fenixedu.core.ui.teacher;

import javax.ws.rs.core.Response.Status;

import org.fenixedu.bennu.core.domain.exceptions.DomainException;

public class StudentGroupException extends DomainException {

    private static final long serialVersionUID = -1572696779817798521L;

    public StudentGroupException(Status status, String bundle, String key, String... args) {
        super(status, bundle, key, args);
    }

    public StudentGroupException(String key, String... args) {
        super("resources.FenixSpaceResources", key, args);
    }

    public StudentGroupException(Throwable cause, Status status, String bundle, String key, String... args) {
        super(cause, status, bundle, key, args);
    }

    public StudentGroupException(Throwable cause, String bundle, String key, String... args) {
        super(cause, bundle, key, args);
    }

}
