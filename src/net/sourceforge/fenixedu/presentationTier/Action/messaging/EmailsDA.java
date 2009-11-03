package net.sourceforge.fenixedu.presentationTier.Action.messaging;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.util.email.EmailBean;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.MessageDeleteService;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.domain.util.email.Sender;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.StringUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.CollectionPager;

@Mapping(path = "/emails", module = "messaging")
@Forwards( { @Forward(name = "new.email", path = "/messaging/newEmail.jsp"),
	@Forward(name = "view.sent.emails", path = "/messaging/viewSentEmails.jsp"),
	@Forward(name = "view.email", path = "/messaging/viewEmail.jsp"), @Forward(name = "cancel", path = "/index.do") })
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

    public ActionForward newEmail(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	EmailBean emailBean = (EmailBean) getRenderedObject("emailBean");
	
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
	return forwardToNewEmail(mapping, actionForm, request, response);
    }

    public ActionForward sendEmail(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	EmailBean emailBean = (EmailBean) getRenderedObject("emailBean");
	RenderUtils.invalidateViewState();
	String validate = emailBean.validate();
	if (validate != null) {
	    request.setAttribute("errorMessage", validate);
	    request.setAttribute("emailBean", emailBean);
	    return mapping.findForward("new.email");
	}
	final Message message = emailBean.send();
	request.setAttribute("created", Boolean.TRUE);
	return viewEmail(mapping, request, message);
    }

    public ActionForward viewSentEmails(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	final String senderParam = request.getParameter("senderId");
	if (senderParam != null && !senderParam.isEmpty()) {
	    return viewSentEmails(mapping, request, new Integer(senderParam));
	}

	final IUserView userView = UserView.getUser();
	final Set<Sender> senders = new TreeSet<Sender>(Sender.COMPARATOR_BY_FROM_NAME);
	for (final Sender sender : RootDomainObject.getInstance().getUtilEmailSendersSet()) {
	    if (sender.getMembers().allows(userView)) {
		senders.add(sender);
	    }
	}
	if (senders.size() == 1) {
	    return viewSentEmails(mapping, request, senders.iterator().next().getIdInternal());
	} else {
	    request.setAttribute("senders", senders);
	    return mapping.findForward("view.sent.emails");
	}
    }

    public ActionForward viewSentEmails(final ActionMapping mapping, final HttpServletRequest request, final Integer senderId) {
	final Sender sender = rootDomainObject.readSenderByOID(senderId);
	final CollectionPager<Message> pager = new CollectionPager<Message>(sender.getMessagesSet(), 50);
	request.setAttribute("numberOfPages", 10);

	final String pageParameter = request.getParameter("pageNumber");
	final Integer page = StringUtils.isEmpty(pageParameter) ? Integer.valueOf(1) : Integer.valueOf(pageParameter);

	request.setAttribute("messages", pager.getPage(page));
	request.setAttribute("senderId", senderId);
	request.setAttribute("pageNumber", page);
	return viewSentEmails(mapping, request, sender);
    }

    public ActionForward viewSentEmails(final ActionMapping mapping, final HttpServletRequest request, final Sender sender) {
	request.setAttribute("sender", sender);
	return mapping.findForward("view.sent.emails");
    }

    public ActionForward viewEmail(final ActionMapping mapping, final HttpServletRequest request, final Message message) {
	request.setAttribute("message", message);
	return mapping.findForward("view.email");
    }

    public ActionForward viewEmail(final ActionMapping mapping, final ActionForm actionForm, final HttpServletRequest request,
	    final HttpServletResponse response) {
	final String messageParam = request.getParameter("messagesId");
	final Message message = messageParam != null && !messageParam.isEmpty() ? rootDomainObject.readMessageByOID(new Integer(
		messageParam)) : null;
	return viewEmail(mapping, request, message);
    }

    public ActionForward deleteMessage(final ActionMapping mapping, final ActionForm actionForm,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final String messageParam = request.getParameter("messagesId");
	final Message message = messageParam != null && !messageParam.isEmpty() ? rootDomainObject.readMessageByOID(new Integer(
		messageParam)) : null;
	if (message == null) {
	    return viewSentEmails(mapping, actionForm, request, response);
	} else {
	    final Sender sender = message.getSender();
	    MessageDeleteService.delete(message);
	    return viewSentEmails(mapping, request, sender);
	}
    }

    public static ActionForward sendEmail(HttpServletRequest request, Sender sender, Recipient... recipient) {
	EmailBean emailBean = new EmailBean();
	if (recipient != null)
	    emailBean.setRecipients(Arrays.asList(recipient));
	if (sender != null)
	    emailBean.setSender(sender);
	request.setAttribute("emailBean", emailBean);
	return FORWARD_TO_NEW_EMAIL;
    }

}
