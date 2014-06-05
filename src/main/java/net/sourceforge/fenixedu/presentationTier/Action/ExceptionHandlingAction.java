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
/*
 * Created on 25/Fev/2003
 *
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.support.SupportRequestBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.FenixConfigurationManager;

import org.apache.commons.validator.EmailValidator;
import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.util.CoreConfiguration;
import org.fenixedu.bennu.portal.domain.MenuFunctionality;
import org.fenixedu.bennu.portal.domain.MenuItem;
import org.fenixedu.bennu.portal.servlet.BennuPortalDispatcher;
import org.fenixedu.bennu.portal.servlet.PortalLayoutInjector;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

import com.google.common.base.Objects;
import com.google.common.base.Strings;

/**
 * @author João Mota
 */
@Mapping(path = "/exceptionHandlingAction")
public final class ExceptionHandlingAction extends FenixDispatchAction {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlingAction.class);

    private static final String SEPARATOR =
            "- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -";

    private static final String hostname = getHostName();

    private static String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "<Host name unknown>";
        }
    }

    @Mapping(path = "/showErrorPage")
    public static class ShowErrorPageAction extends Action {
        @Override
        public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                HttpServletResponse response) throws Exception {
            if (!response.isCommitted()) {
                response.reset();
            }
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            PortalLayoutInjector.skipLayoutOn(request);
            if (CoreConfiguration.getConfiguration().developmentMode()) {
                return new ActionForward("/debugExceptionPage.jsp");
            }
            return new ActionForward("/supportHelpInquiry.jsp");
        }
    }

    public ActionForward prepareSupportHelp(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        SupportRequestBean requestBean = SupportRequestBean.generateExceptionBean(AccessControl.getPerson());
        final String parameter = request.getParameter("contextId");
        if (parameter != null && !parameter.isEmpty()) {
            requestBean.setSelectedFunctionality(FenixFramework.<MenuFunctionality> getDomainObject(parameter));
        }

        request.setAttribute("requestBean", requestBean);
        return new ActionForward("/supportHelpInquiry.jsp");
    }

    public final ActionForward supportFormFieldValidation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        request.setAttribute("exceptionInfo", request.getParameter("exceptionInfo"));
        request.setAttribute("requestBean", getRenderedObject("requestBean"));

        return new ActionForward("/supportHelpInquiry.jsp");
    }

    public final ActionForward processSupportRequest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        SupportRequestBean requestBean = (SupportRequestBean) getObjectFromViewState("requestBean");
        if (requestBean != null && requestBean.getSelectedFunctionality() == null) {
            MenuFunctionality selected = BennuPortalDispatcher.getSelectedFunctionality(request);
            if (selected != null) {
                requestBean.setSelectedFunctionality(selected);
            }
        }

        String mailSubject = generateEmailSubject(request, requestBean, getLoggedPerson(request));
        String mailBody = generateEmailBody(request, requestBean, getLoggedPerson(request));

        if (CoreConfiguration.getConfiguration().developmentMode()) {
            logger.warn("Submitted error form from {}: '{}'\n{}", requestBean.getResponseEmail(), mailSubject, mailBody);
        } else {
            String fromEmail = requestBean.getResponseEmail();
            sendEmail(EmailValidator.getInstance().isValid(fromEmail) ? fromEmail : CoreConfiguration.getConfiguration()
                    .defaultSupportEmailAddress(), mailSubject, mailBody);
        }

        request.getSession().removeAttribute(Globals.ERROR_KEY);

        return new ActionForward("/showErrorPageRegistered.do", true);
    }

    private String generateEmailSubject(HttpServletRequest request, SupportRequestBean requestBean, Person loggedPerson) {
        StringBuilder builder = new StringBuilder();
        builder.append("[")
                .append(requestBean.getSelectedFunctionality() != null ? requestBean.getSelectedFunctionality().getPathFromRoot()
                        .get(0).getTitle().getContent() : "").append("] ");
        builder.append("[").append(getRequestTypeAsString(requestBean)).append("] ");
        builder.append("[").append(getRequestPriorityAsString(requestBean)).append("] ");
        builder.append(requestBean.getSubject());
        return builder.toString();
    }

    private String generateEmailBody(HttpServletRequest request, SupportRequestBean requestBean, Person loggedPerson) {

        StringBuilder builder = new StringBuilder();
        appendNewLine(builder, 1);
        builder.append(SEPARATOR);
        appendNewLine(builder, 1);
        appendRoles(builder, loggedPerson);
        appendNewLine(builder, 1);
        appendUserInfo(builder, loggedPerson, requestBean);
        appendNewLine(builder, 1);
        appendFormInfo(builder, requestBean);
        appendNewLine(builder, 1);
        appendUserInfo(builder, request.getParameter("userAgent"));
        appendNewLine(builder, 1);
        appendExtraInfo(builder);
        appendNewLine(builder, 1);
        builder.append(SEPARATOR);
        appendNewLine(builder, 2);
        appendComments(builder, requestBean, request.getParameter("exceptionInfo"));
        return builder.toString();
    }

    private void appendExtraInfo(StringBuilder builder) {
        generateLabel(builder, "When").append('[').append(DateTime.now()).append("]\n");
        generateLabel(builder, "Host").append('[').append(hostname).append(']');
    }

    private void appendUserInfo(StringBuilder builder, String userAgent) {
        generateLabel(builder, "Browser/OS").append("[").append(userAgent).append("]");
    }

    private void appendComments(StringBuilder builder, SupportRequestBean requestBean, String exceptionInfo) {

        builder.append(requestBean.getMessage());
        if (!Strings.isNullOrEmpty(exceptionInfo)) {
            appendNewLine(builder, 4);
            builder.append(exceptionInfo);
            appendNewLine(builder, 1);
        }
    }

    private void appendFormInfo(StringBuilder builder, SupportRequestBean requestBean) {

        generateLabel(builder, "Email").append("[").append(requestBean.getResponseEmail()).append("]");
        appendNewLine(builder, 1);

        generateLabel(builder, "Portal").append("[");

        if (requestBean.getSelectedFunctionality() != null) {
            boolean first = true;
            for (MenuItem item : requestBean.getSelectedFunctionality().getPathFromRoot()) {
                if (first) {
                    first = false;
                } else {
                    builder.append(" > ");
                }
                builder.append(item.getTitle().getContent());
            }
        }

        builder.append("]");
        appendNewLine(builder, 1);

        generateLabel(builder,
                BundleUtil.getString(net.sourceforge.fenixedu.util.Bundle.APPLICATION, "label.type.single"))
                .append("[").append(getRequestTypeAsString(requestBean)).append("]");
        appendNewLine(builder, 1);

        generateLabel(builder, "Priority").append("[").append(getRequestPriorityAsString(requestBean)).append("]");
    }

    private void appendRoles(StringBuilder builder, Person loggedPerson) {
        generateLabel(builder, "Roles");
        builder.append("[");
        if (loggedPerson != null) {

            for (String role : loggedPerson.getMainRoles()) {
                builder.append("#").append(role).append(", ");
            }
            builder.setLength(builder.length() - 2);
        }
        builder.append("] ");
    }

    private StringBuilder generateLabel(StringBuilder builder, String label) {
        builder.append(label);
        for (int i = label.length(); i <= 15; i++) {
            builder.append('_');
        }
        return builder;
    }

    private void appendUserInfo(StringBuilder builder, Person loggedPerson, SupportRequestBean requestBean) {
        generateLabel(builder, BundleUtil.getString(net.sourceforge.fenixedu.util.Bundle.APPLICATION, "label.name"));
        if (loggedPerson != null) {
            builder.append("[").append(loggedPerson.getName()).append("]\n");
            generateLabel(builder, "Username");
            builder.append("[").append(loggedPerson.getUsername()).append("]");
        } else {
            builder.append(BundleUtil.getString(net.sourceforge.fenixedu.util.Bundle.APPLICATION,
                    "support.mail.session.error"));
        }
    }

    private void appendNewLine(StringBuilder builder, int number) {
        for (int i = 0; i < number; i++) {
            builder.append("\n");
        }
    }

    private String getRequestTypeAsString(SupportRequestBean requestBean) {
        return requestBean.getRequestType() != null ? BundleUtil.getString(
                net.sourceforge.fenixedu.util.BundleUtil.ENUMERATION_BUNDLE, requestBean.getRequestType().getQualifiedName()) : "";
    }

    private String getRequestPriorityAsString(SupportRequestBean requestBean) {
        return requestBean.getRequestPriority() != null ? BundleUtil.getString(
                net.sourceforge.fenixedu.util.BundleUtil.ENUMERATION_BUNDLE, requestBean.getRequestPriority().getQualifiedName())
                .split(" \\(")[0] : "";
    }

    private void sendEmail(String from, String subject, String body) {
        Properties props = new Properties();
        props.put("mail.smtp.host",
                Objects.firstNonNull(FenixConfigurationManager.getConfiguration().getMailSmtpHost(), "localhost"));
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(CoreConfiguration.getConfiguration()
                    .defaultSupportEmailAddress()));
            message.setSubject(subject);
            message.setText(body);
            Transport.send(message);
        } catch (Exception e) {
            logger.error("Could not send support email! Original message was: " + body, e);
        }
    }
}
