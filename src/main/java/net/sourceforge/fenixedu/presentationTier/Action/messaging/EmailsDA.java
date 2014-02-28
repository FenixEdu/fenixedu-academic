package net.sourceforge.fenixedu.presentationTier.Action.messaging;

import java.util.Arrays;
import java.util.ResourceBundle;
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

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;
import org.fenixedu.commons.i18n.I18N;

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
            final ResourceBundle resourceBundle = ResourceBundle.getBundle("resources.ApplicationResources", I18N.getLocale());
            final String noneSentString = resourceBundle.getString("error.email.none.sent");
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
