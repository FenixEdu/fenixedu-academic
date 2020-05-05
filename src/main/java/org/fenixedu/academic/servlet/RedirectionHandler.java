package org.fenixedu.academic.servlet;

import javax.servlet.http.HttpServletRequest;

import org.fenixedu.bennu.core.domain.User;

public interface RedirectionHandler {

    public boolean isToRedirect(final User user, final HttpServletRequest request);

    public String redirectionPath(final User user, final HttpServletRequest request);

}
