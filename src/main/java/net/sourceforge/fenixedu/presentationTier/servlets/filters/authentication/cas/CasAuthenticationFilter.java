/*
 * @(#)CasAuthenticationFilter.java
 *
 * Copyright 2009 Instituto Superior Tecnico
 * Founding Authors: João Figueiredo, Luis Cruz, Paulo Abrantes, Susana Fernandes
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the Bennu Web Application Infrastructure.
 *
 *   The Bennu Web Application Infrastructure is free software: you can 
 *   redistribute it and/or modify it under the terms of the GNU Lesser General 
 *   Public License as published by the Free Software Foundation, either version 
 *   3 of the License, or (at your option) any later version.*
 *
 *   Bennu is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with Bennu. If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package net.sourceforge.fenixedu.presentationTier.servlets.filters.authentication.cas;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.Authenticate;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.presentationTier.Action.commons.LogOffAction;
import net.sourceforge.fenixedu.util.HostAccessControl;

import org.apache.commons.lang.CharEncoding;

import pt.ist.fenixWebFramework.FenixWebFramework;
import pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter;
import edu.yale.its.tp.cas.client.CASAuthenticationException;
import edu.yale.its.tp.cas.client.CASReceipt;
import edu.yale.its.tp.cas.client.ProxyTicketValidator;

public class CasAuthenticationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        final HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        final String ticket = httpServletRequest.getParameter("ticket");
        if (ticket != null && httpServletRequest.getRequestURI().indexOf("/login") < 0) {
            LogOffAction.killSession(httpServletRequest);

            final String serverName = servletRequest.getServerName();
            final String remoteHostName = getRemoteHostName(httpServletRequest);
            final String requestURL = httpServletRequest.getRequestURL().toString();
            try {
                final CASReceipt receipt = getCASReceipt(serverName, ticket, requestURL);

                try {
                    final IUserView userView = Authenticate.runAuthenticate(receipt, requestURL, remoteHostName);
                    if (userView != null && !userView.getRoleTypes().isEmpty()) {
                        final HttpSession httpSession = getHttpSession(servletRequest);
                        httpSession.setAttribute(SetUserViewFilter.USER_SESSION_ATTRIBUTE, userView);
                    }
                } catch (FenixServiceException ex) {
                } catch (ExcepcaoPersistencia e) {
                }
            } catch (CASAuthenticationException e) {
                e.printStackTrace();
                // do nothing ... the user just won't have a session
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    public static CASReceipt getCASReceipt(final String serverName, final String casTicket, final String requestURL)
            throws UnsupportedEncodingException, CASAuthenticationException {
        final String casValidateUrl = FenixWebFramework.getConfig().getCasConfig(serverName).getCasValidateUrl();
        final String casServiceUrl =
                URLEncoder.encode(requestURL.replace("http://", "https://").replace(":8080", ""), CharEncoding.UTF_8);

        ProxyTicketValidator pv = new ProxyTicketValidator();
        pv.setCasValidateUrl(casValidateUrl);
        pv.setServiceTicket(casTicket);
        pv.setService(casServiceUrl);
        pv.setRenew(false);

        return CASReceipt.getReceipt(pv);
    }

    public static String getRemoteHostName(HttpServletRequest request) {
        String remoteHostName;
        final String remoteAddress = HostAccessControl.getRemoteAddress(request);
        try {
            remoteHostName = InetAddress.getByName(remoteAddress).getHostName();
        } catch (UnknownHostException e) {
            remoteHostName = remoteAddress;
        }
        return remoteHostName;
    }

    protected HttpSession getHttpSession(final ServletRequest servletRequest) {
        if (servletRequest instanceof HttpServletRequest) {
            final HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            final HttpSession httpSession = httpServletRequest.getSession(false);
            if (httpSession != null) {
                httpSession.invalidate();
            }
            return httpServletRequest.getSession(true);
        }
        return null;
    }

}
