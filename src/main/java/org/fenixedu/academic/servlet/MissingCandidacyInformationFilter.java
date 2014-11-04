/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.fenixedu.academic.predicate.AccessControl;

public class MissingCandidacyInformationFilter implements Filter {

    private static final String EDIT_CANDIDACY_INFORMATION_PATH =
            "/student/editMissingCandidacyInformation.do?method=prepareEdit";

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

        if (AccessControl.getPerson() != null && AccessControl.getPerson().getStudent() != null) {

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

            servletRequest.getRequestDispatcher(EDIT_CANDIDACY_INFORMATION_PATH).forward(servletRequest, servletResponse);

        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

}
