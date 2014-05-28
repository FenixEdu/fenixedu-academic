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

import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.util.email.ExecutionCourseSender;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.MessageDeleteService;
import net.sourceforge.fenixedu.domain.util.email.Sender;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.messaging.MessagingApplication.MessagingEmailsApp;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.util.CollectionPager;

@StrutsFunctionality(app = MessagingEmailsApp.class, path = "view-sent-emails", titleKey = "label.email.sent")
@Mapping(path = "/viewSentEmails", module = "messaging")
@Forwards({ @Forward(name = "view.sent.emails", path = "/messaging/viewSentEmails.jsp"),
        @Forward(name = "view.email", path = "/messaging/viewEmail.jsp") })
public class ViewSentEmailsDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward viewSentEmails(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        final String senderParam = request.getParameter("senderId");
        if (senderParam != null && !senderParam.isEmpty()) {
            return viewSentEmails(mapping, request, senderParam);
        }

        final User userView = Authenticate.getUser();
        final Set<Sender> sendersGroups = new TreeSet<Sender>(Sender.COMPARATOR_BY_FROM_NAME);
        final TreeSet<ExecutionCourseSender> sendersGroupsCourses =
                new TreeSet<ExecutionCourseSender>(ExecutionCourseSender.COMPARATOR_BY_EXECUTION_COURSE_SENDER);
        for (final Sender sender : Bennu.getInstance().getUtilEmailSendersSet()) {
            boolean allow = sender.getMembers().isMember(userView);
            boolean isExecutionCourseSender = sender instanceof ExecutionCourseSender;
            if (allow && !isExecutionCourseSender) {
                sendersGroups.add(sender);
            }
            if (allow && isExecutionCourseSender) {
                sendersGroupsCourses.add((ExecutionCourseSender) sender);
            }
        }
        if (isSenderUnique(sendersGroups, sendersGroupsCourses)) {
            if (sendersGroupsCourses.size() == 1) {
                return viewSentEmails(mapping, request, (sendersGroupsCourses.iterator().next()).getExternalId());
            } else {
                return viewSentEmails(mapping, request, sendersGroups.iterator().next().getExternalId());
            }
        }
        request.setAttribute("sendersGroups", sendersGroups);
        request.setAttribute("sendersGroupsCourses", sendersGroupsCourses);

        final Person person = AccessControl.getPerson();
        if (person != null && person.hasRole(RoleType.MANAGER)) {
            SearchSendersBean searchSendersBean = getRenderedObject("searchSendersBean");
            if (searchSendersBean == null) {
                searchSendersBean = new SearchSendersBean();
            }
            request.setAttribute("searchSendersBean", searchSendersBean);
        }

        return mapping.findForward("view.sent.emails");
    }

    public ActionForward viewSentEmails(final ActionMapping mapping, final HttpServletRequest request, final String senderId) {
        final Sender sender = FenixFramework.getDomainObject(senderId);
        final int numberOfMessagesByPage = 40;
        final CollectionPager<Message> pager = new CollectionPager<Message>(sender.getMessagesSet(), numberOfMessagesByPage);
        request.setAttribute("numberOfPages", getNumberOfPages(pager));
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
        final Message message =
                messageParam != null && !messageParam.isEmpty() ? FenixFramework.<Message> getDomainObject(messageParam) : null;
        return viewEmail(mapping, request, message);
    }

    public ActionForward deleteMessage(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) {
        final String messageParam = request.getParameter("messagesId");
        final Message message =
                messageParam != null && !messageParam.isEmpty() ? FenixFramework.<Message> getDomainObject(messageParam) : null;
        if (message == null) {
            return viewSentEmails(mapping, actionForm, request, response);
        } else {
            final Sender sender = message.getSender();
            MessageDeleteService.delete(message);
            return viewSentEmails(mapping, request, sender.getExternalId());
        }
    }

    private int getNumberOfPages(CollectionPager<?> pager) {
        if (pager.getCollection().size() <= pager.getMaxElementsPerPage()) {
            return 0;
        }
        return pager.getNumberOfPages();
    }

    private boolean isSenderUnique(Set<?> arg0, Set<?> arg1) {
        if (arg0 == null) {
            return arg1.size() == 1;
        }
        if (arg1 == null) {
            return arg0.size() == 1;
        }
        return arg0.size() + arg0.size() == 1;
    }

    @Atomic
    public ActionForward resubmit(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        Message message = getMessageFromRequest(request);
        message.setSent(null);
        message.setRootDomainObjectFromPendingRelation(rootDomainObject);
        request.setAttribute("message", message);
        return mapping.findForward("view.email");

    }

    private Message getMessageFromRequest(HttpServletRequest request) {
        final String messageParam = request.getParameter("messagesId");
        return FenixFramework.getDomainObject(messageParam);
    }

}
