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
package net.sourceforge.fenixedu.presentationTier.Action.messaging;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.messaging.ExpandExecutionCourseMailAlias;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.util.FenixConfigurationManager;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/>
 * <br/>
 * <br/>
 *         Created on 0:32:09,17/Out/2005
 * @version $Id: ExecutionCourseAliasExpandingAction.java 36441 2008-06-07
 *          00:49:33Z lepc $
 */
@Mapping(module = "external", path = "/expandAlias", scope = "request", validate = false, parameter = "method")
public class ExecutionCourseAliasExpandingAction extends FenixAction {

    private static final Logger logger = LoggerFactory.getLogger(ExecutionCourseAliasExpandingAction.class);

    public static String emailAddressPrefix = "course-";

    private static Properties properties;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        String result = "400 Error: Email alias expanding service did not run";

        if (FenixConfigurationManager.getHostAccessControl().isAllowed(this, request)) {

            String address = request.getParameter("address");

            try {
                ExpandExecutionCourseMailAlias.ForwardMailsReport report =
                        ExpandExecutionCourseMailAlias.run(address, emailAddressPrefix, mailingListDomainConfiguration());

                switch (report.getStatus()) {
                case OK: {
                    StringBuilder buffer = new StringBuilder();
                    buffer.append("200 ");
                    boolean firstAddress = true;
                    for (String currentAddress : report.getExpandedAddresses()) {
                        if (!firstAddress) {
                            buffer.append(",");
                        }
                        buffer.append(currentAddress);
                        firstAddress = false;
                    }
                    buffer.append("\n");
                    result = buffer.toString();
                    break;
                }
                case DYNAMIC_MAIL_DELIVERY_DISABLE: {
                    result = "500 Target course has dynamic mail distribution in the DISABLED state\n";
                    break;
                }
                case EC_SITE_NOT_AVAILABLE: {
                    result = "500 Target course does not exist\n";
                    break;
                }
                case INVALID_ADDRESS: {
                    result = "500 Invalid address\n";
                    break;
                }
                case INVALID_HOST: {
                    result = "500 Invalid host\n";
                    break;
                }
                case UNKNOWN_EXECUTION_COURSE: {
                    result = "500 Target execution course does not exist\n";
                    break;
                }
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                result = "400 Got an exception when trying to send email: " + e.getClass().getName() + "\n";
            }
        } else {
            result = "500 requester not allowed\n";
        }
        try {
            sendAnswer(response, result);
        } catch (IOException e) {
            throw new FenixActionException(e);
        }

        return null;

    }

    public static String mailingListDomainConfiguration() {
        return FenixConfigurationManager.getConfiguration().getMailingListHostName();
    }

    private void sendAnswer(HttpServletResponse response, String result) throws IOException {
        ServletOutputStream writer = response.getOutputStream();
        response.setContentType("text/plain");
        writer.print(result);
        writer.flush();
        response.flushBuffer();
    }

}