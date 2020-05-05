package org.fenixedu.academic.servlet;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import com.google.common.collect.Lists;

public class AuthenticationRedirector {
    private static List<RedirectionHandler> handlers = Lists.newArrayList();

    public static void registerRedirectionHandler(final RedirectionHandler handler) {
        synchronized (handlers) {
            handlers.add(handler);
        }
    }

    public static void unregisterRedirectionHandler(final RedirectionHandler handler) {
        synchronized (handlers) {
            handlers.remove(handler);
        }
    }

    public static String getRedirectionPath(final HttpServletRequest request) {
        return getRedirectionPath(Authenticate.getUser(), request);
    }

    public static String getRedirectionPath(final User user, final HttpServletRequest request) {
        for (final RedirectionHandler RedirectionHandler : handlers) {
            if (RedirectionHandler.isToRedirect(user, request)) {
                return RedirectionHandler.redirectionPath(user, request);
            }
        }

        return null;
    }
}
