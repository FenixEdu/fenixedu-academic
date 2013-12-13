package net.sourceforge.fenixedu.presentationTier.servlets.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import net.sourceforge.fenixedu.domain.person.RoleType;
import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.security.Authenticate;

public class ManagerFilter implements Filter {

    @Override
    public void init(FilterConfig config) {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
            throws IOException, ServletException {

        final User userView = Authenticate.getUser();
        if (userView == null
                || !(userView.getPerson().hasRole(RoleType.MANAGER) || userView.getPerson().hasRole(RoleType.OPERATOR))) {
            throw new RuntimeException();
        }

        chain.doFilter(request, response);
    }

}