/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HostAccessControl {
    private static final Logger logger = LoggerFactory.getLogger(HostAccessControl.class);

    private final Map<String, List<InetAddress>> configuration = new HashMap<>();

    HostAccessControl() {
    }

    public HostAccessControl(Map<String, String> hostControlNames) {
        for (String host : hostControlNames.keySet()) {
            List<InetAddress> addresses = new ArrayList<>();
            for (String address : hostControlNames.get(host).trim().split("\\s*,\\s*")) {
                try {
                    addresses.addAll(Arrays.asList(InetAddress.getAllByName(address)));
                } catch (UnknownHostException e) {
                    logger.warn("could not find host {}, host ignored.", host);
                }
            }
            configuration.put(host, addresses);
        }
    }

    public boolean isAllowed(String name, ServletRequest request) {
        return isAllowed(name, getRemoteAddress(request));
    }

    public boolean isAllowed(Class<?> type, ServletRequest request) {
        return isAllowed(type.getName(), request);
    }

    public boolean isAllowed(Object object, ServletRequest request) {
        return isAllowed(object.getClass(), request);
    }

    private boolean isAllowed(String name, String address) {
        try {
            InetAddress remoteAddress = InetAddress.getByName(address);

            List<InetAddress> hostList = this.configuration.get(name);
            if (hostList == null) {
                logger.warn(name + " denied[" + remoteAddress + "]: allowed hosts not defined");
                return false;
            }

            for (InetAddress allowedHost : hostList) {
                if (remoteAddress.equals(allowedHost)) {
                    logger.debug(name + " allowed[" + remoteAddress + "]: matches group " + hostList);

                    return true;
                }
            }

            logger.warn(name + " denied[" + remoteAddress + "]: is not member of " + hostList);
        } catch (UnknownHostException e) {
            logger.warn(name + " denied[" + address + "]: could not find host");
        }

        return false;
    }

    public static String getRemoteAddress(ServletRequest request) {
        if (request instanceof HttpServletRequest) {
            final HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            final String xForwardFor = httpServletRequest.getHeader("x-forwarded-for");
            if (xForwardFor != null && xForwardFor.length() > 0) {
                return xForwardFor;
            }
        }
        return request.getRemoteAddr();
    }

}
