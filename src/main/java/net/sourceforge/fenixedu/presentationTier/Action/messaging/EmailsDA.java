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

import java.util.Arrays;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.util.email.EmailBean;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.domain.util.email.Sender;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.commons.FenixActionForward;
import net.sourceforge.fenixedu.presentationTier.Action.messaging.MessagingApplication.MessagingEmailsApp;
import net.sourceforge.fenixedu.util.Bundle;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = MessagingEmailsApp.class, path = "new-email", titleKey = "label.email.new")
@Mapping(path = "/emails", module = "messaging")
@Forwards({ @Forward(name = "new.email", path = "/messaging/newEmail.jsp"),
        @Forward(name = "cancel", path = "/messaging/announcements/announcementsStartPageHandler.do?method=news") })
public class EmailsDA extends FenixDispatchAction {
    public static final ActionForward FORWARD_TO_NEW_EMAIL = new ActionForward("emails", "/emails.do?method=forwardToNewEmail",
            false, "/messaging");

    public ActionForward cancel(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        return mapping.findForward("cancel");
    }

    public ActionForward forwardToNewEmail(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        return mapping.findForward("new.email");
    }

    @EntryPoint
    public ActionForward newEmail(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        EmailBean emailBean = getRenderedObject("emailBean");

        if (emailBean == null) {
            emailBean = (EmailBean) request.getAttribute("emailBean");
        }

        if (emailBean == null) {
            emailBean = new EmailBean();
            final Set<Sender> availableSenders = Sender.getAvailableSenders();
            if (availableSenders.size() == 1) {
                emailBean.setSender(availableSenders.iterator().next());
            }
        }
        RenderUtils.invalidateViewState();
        request.setAttribute("emailBean", emailBean);
        return mapping.findForward("new.email");
    }

    public ActionForward sendEmail(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        EmailBean emailBean = getRenderedObject("emailBean");
        RenderUtils.invalidateViewState();
        String validate = emailBean.validate();
        if (validate != null) {
            final String noneSentString = BundleUtil.getString(Bundle.APPLICATION, "error.email.none.sent");
            request.setAttribute("errorMessage", noneSentString + " " + validate);
            request.setAttribute("emailBean", emailBean);
            return mapping.findForward("new.email");
        }
        final Message message = emailBean.send();
        request.setAttribute("created", Boolean.TRUE);
        return new FenixActionForward(request, new ActionForward("/viewSentEmails.do?method=viewEmail&messagesId="
                + message.getExternalId(), true));
    }

    public static ActionForward sendEmail(HttpServletRequest request, Sender sender, Recipient... recipient) {
        EmailBean emailBean = new EmailBean();
        if (recipient != null) {
            emailBean.setRecipients(Arrays.asList(recipient));
        }
        if (sender != null) {
            emailBean.setSender(sender);
        }
        request.setAttribute("emailBean", emailBean);
        return FORWARD_TO_NEW_EMAIL;
    }

}
