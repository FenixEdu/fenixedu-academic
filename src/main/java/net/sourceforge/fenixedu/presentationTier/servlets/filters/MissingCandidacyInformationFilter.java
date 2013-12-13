package net.sourceforge.fenixedu.presentationTier.servlets.filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import pt.ist.bennu.core.domain.Bennu;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.functionalities.FilterFunctionalityContext;

public class MissingCandidacyInformationFilter implements Filter {

    private static final String EDIT_CANDIDACY_INFORMATION_PATH =
            "/student/editMissingCandidacyInformation.do?method=prepareEdit&"
                    + net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter.CONTEXT_ATTRIBUTE_NAME
                    + "=/estudante/estudante";

    private static final String CANDIDACY_INFORMATION_VALID_KEY = "CANDIDACY_INFORMATION_VALID";

    @Override
    public void init(FilterConfig config) throws ServletException {

    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain)
            throws IOException, ServletException {

        final HttpServletRequest request = (HttpServletRequest) servletRequest;

        if (request.getRequestURI().endsWith("/logoff.do") || request.getRequestURI().endsWith("/login.do")
                || request.getRequestURI().endsWith("/exceptionHandlingAction.do")
                || request.getRequestURI().endsWith("/editMissingCandidacyInformation.do")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        if (AccessControl.getPerson() != null && AccessControl.getPerson().hasStudent()) {

            final Boolean validCandidacyInformation =
                    (Boolean) request.getSession().getAttribute(CANDIDACY_INFORMATION_VALID_KEY);
            if (validCandidacyInformation != null && validCandidacyInformation) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            } else {
                if (!AccessControl.getPerson().getStudent().hasAnyMissingPersonalInformation()) {
                    request.getSession().setAttribute(CANDIDACY_INFORMATION_VALID_KEY, Boolean.TRUE);
                    filterChain.doFilter(servletRequest, servletResponse);
                    return;
                }
            }

            final List<Content> contents = new ArrayList<Content>();
            Bennu.getInstance().getRootPortal().addPathContentsForTrailingPath(contents, "estudante/estudante");
            final FilterFunctionalityContext context =
                    new FilterFunctionalityContext((HttpServletRequest) servletRequest, contents);
            servletRequest.setAttribute(FilterFunctionalityContext.CONTEXT_KEY, context);
            servletRequest.getRequestDispatcher(EDIT_CANDIDACY_INFORMATION_PATH).forward(servletRequest, servletResponse);

        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

}
