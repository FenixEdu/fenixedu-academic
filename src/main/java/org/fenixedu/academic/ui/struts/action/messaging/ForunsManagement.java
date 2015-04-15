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
/**
 * 
 */
package org.fenixedu.academic.ui.struts.action.messaging;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.messaging.ConversationMessage;
import org.fenixedu.academic.domain.messaging.ConversationThread;
import org.fenixedu.academic.domain.messaging.Forum;
import org.fenixedu.academic.dto.messaging.CreateConversationMessageBean;
import org.fenixedu.academic.dto.messaging.CreateConversationThreadAndMessageBean;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.messaging.AddForumEmailSubscriber;
import org.fenixedu.academic.service.services.messaging.CreateConversationMessage;
import org.fenixedu.academic.service.services.messaging.CreateConversationThreadAndMessage;
import org.fenixedu.academic.service.services.messaging.RemoveForumEmailSubscriber;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.utils.RequestUtils;
import org.fenixedu.academic.util.MultiLanguageString;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt"> Goncalo Luiz</a><br/>
 *         Created on May 5, 2006, 10:42:00 AM
 * 
 * @author pcma
 */
public abstract class ForunsManagement extends FenixDispatchAction {

    private static final Integer DEFAULT_PAGE_SIZE = 20;

    public ActionForward viewForum(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        prepareViewForum(request);
        return mapping.findForward("viewForum");
    }

    public ActionForward prepareCreateThreadAndMessage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        request.setAttribute("forum", getRequestedForum(request));
        request.setAttribute("person", getLoggedPerson(request));

        return mapping.findForward("createThreadAndMessage");

    }

    public ActionForward createThreadAndMessage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        CreateConversationThreadAndMessageBean createConversationThreadAndMessageBean =
                (CreateConversationThreadAndMessageBean) RenderUtils.getViewState("createThreadAndMessage").getMetaObject()
                        .getObject();

        try {
            CreateConversationThreadAndMessage.runCreateConversationThreadAndMessage(createConversationThreadAndMessageBean);
        } catch (DomainException e) {
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(e.getKey()));

            saveMessages(request, actionMessages);

            return prepareCreateThreadAndMessage(mapping, form, request, response);
        }

        return this.viewForum(mapping, form, request, response);
    }

    private ActionForward viewThreadOnPage(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response, Integer pageNumber) throws FenixServiceException {

        ConversationThread thread = this.getRequestedThread(request);
        request.setAttribute("thread", thread);

        request.setAttribute("pageNumber", pageNumber);
        request.setAttribute("pageNumbers", computeNumberOfPages(DEFAULT_PAGE_SIZE, thread.getMessageSet().size()));
        request.setAttribute("messages", getContentToDisplay(thread.getMessageSet(), pageNumber, DEFAULT_PAGE_SIZE));

        Person loggedPerson = getLoggedPerson(request);
        request.setAttribute("person", loggedPerson);

        Forum forum = getRequestedForum(request);
        request.setAttribute("forum", this.getRequestedForum(request));

        request.setAttribute("loggedPersonCanWrite", forum.getWritersGroup().isMember(Authenticate.getUser()));
        request.setAttribute("showReplyBox", this.getShowReplyBox(request));

        return mapping.findForward("viewThread");

    }

    public ActionForward viewThread(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException, IOException {

        if (getLoggedPerson(request) == null) {
            RequestUtils.sendLoginRedirect(request, response);
            return null;
        }
        return viewThreadOnPage(mapping, actionForm, request, response, getPageNumber(request));
    }

    public ActionForward prepareCreateMessage(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        request.setAttribute("quotationText", getQuotationText(request));
        return viewThreadOnPage(mapping, actionForm, request, response,
                computeNumberOfPages(DEFAULT_PAGE_SIZE, getRequestedThread(request).getMessageSet().size()));

    }

    public ActionForward createMessage(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        CreateConversationMessageBean createConversationMessageBean =
                (CreateConversationMessageBean) RenderUtils.getViewState("createMessage").getMetaObject().getObject();

        try {
            CreateConversationMessage.runCreateConversationMessage(createConversationMessageBean);
        } catch (DomainException e) {
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(e.getKey()));

            saveMessages(request, actionMessages);

            return prepareCreateMessage(mapping, actionForm, request, response);
        }

        return viewThreadOnPage(mapping, actionForm, request, response,
                computeNumberOfPages(DEFAULT_PAGE_SIZE, getRequestedThread(request).getMessageSet().size()));
    }

    public ActionForward emailSubscribe(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        AddForumEmailSubscriber.run(getRequestedForum(request), getLoggedPerson(request));

        prepareViewForum(request);

        return mapping.findForward("viewForum");
    }

    public ActionForward emailUnsubscribe(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        RemoveForumEmailSubscriber.run(getRequestedForum(request), getLoggedPerson(request));

        prepareViewForum(request);

        return mapping.findForward("viewForum");
    }

    protected String getQuotedMessageId(HttpServletRequest request) {
        return request.getParameter("quoteMessageId");
    }

    protected Integer getPageNumber(HttpServletRequest request) {
        String pageNumberString = request.getParameter("pageNumber");
        return (pageNumberString != null) ? Integer.valueOf(pageNumberString) : 1;
    }

    protected Boolean getShowReplyBox(HttpServletRequest request) {
        return Boolean.valueOf(request.getParameter("showReplyBox"));
    }

    protected ConversationThread getRequestedThread(HttpServletRequest request) {
        return (ConversationThread) FenixFramework.getDomainObject(request.getParameter("threadId"));
    }

    protected Forum getRequestedForum(HttpServletRequest request) {
        return (Forum) FenixFramework.getDomainObject(request.getParameter("forumId"));
    }

    private <T extends Comparable<T>> List<T> getContentToDisplay(Collection<T> messages, Integer pageNumber, Integer pageSize) {
        List<T> nodeCopy = new ArrayList<T>(messages);
        Collections.sort(nodeCopy);
        int start = (pageNumber - 1) * pageSize;
        return nodeCopy.subList(start, Math.min(nodeCopy.size(), start + pageSize));
    }

    private int computeNumberOfPages(Integer pageSize, int listSize) {
        int totalPages = (int) StrictMath.ceil(listSize / Double.valueOf(pageSize));
        return totalPages;
    }

    private MultiLanguageString getQuotationText(HttpServletRequest request) {

        String quotedMessageId = this.getQuotedMessageId(request);
        String quotationText = null;
        if (quotedMessageId != null) {
            MessageResources resources = this.getResources(request, "MESSAGING_RESOURCES");
            ConversationMessage message = (ConversationMessage) FenixFramework.getDomainObject(quotedMessageId);

            String author = message.getCreator().getName() + " (" + message.getCreator().getUsername() + ")";

            quotationText =
                    resources.getMessage(this.getLocale(request), "messaging.viewThread.quotationText", author, message.getBody()
                            .getContent());

        }
        return new MultiLanguageString(quotationText);
    }

    private void prepareViewForum(HttpServletRequest request) throws FenixServiceException {

        Forum forum = this.getRequestedForum(request);
        request.setAttribute("forum", forum);

        Integer pageNumber = getPageNumber(request);
        request.setAttribute("pageNumber", pageNumber);

        request.setAttribute("conversationThreads",
                getContentToDisplay(forum.getConversationThreadSet(), pageNumber, DEFAULT_PAGE_SIZE));

        request.setAttribute("pageNumbers", computeNumberOfPages(DEFAULT_PAGE_SIZE, forum.getConversationThreadSet().size()));
        Person loggedPerson = getLoggedPerson(request);
        request.setAttribute("receivingMessagesByEmail", forum.isPersonReceivingMessagesByEmail(loggedPerson));

        request.setAttribute("loggedPersonCanWrite", forum.getWritersGroup().isMember(Authenticate.getUser()));
    }

}