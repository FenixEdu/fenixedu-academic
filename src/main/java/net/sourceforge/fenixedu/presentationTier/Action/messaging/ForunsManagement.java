/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.messaging;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.messaging.AddForumEmailSubscriber;
import net.sourceforge.fenixedu.applicationTier.Servico.messaging.CreateConversationMessage;
import net.sourceforge.fenixedu.applicationTier.Servico.messaging.CreateConversationThreadAndMessage;
import net.sourceforge.fenixedu.applicationTier.Servico.messaging.RemoveForumEmailSubscriber;
import net.sourceforge.fenixedu.dataTransferObject.messaging.CreateConversationMessageBean;
import net.sourceforge.fenixedu.dataTransferObject.messaging.CreateConversationThreadAndMessageBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.contents.Node;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.messaging.ConversationMessage;
import net.sourceforge.fenixedu.domain.messaging.ConversationThread;
import net.sourceforge.fenixedu.domain.messaging.Forum;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.utils.RequestUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

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
        request.setAttribute("pageNumbers", computeNumberOfPages(DEFAULT_PAGE_SIZE, thread.getChildrenSet().size()));
        request.setAttribute("messages", getContentToDisplay(thread.getChildren(), pageNumber, DEFAULT_PAGE_SIZE));

        Person loggedPerson = getLoggedPerson(request);
        request.setAttribute("person", loggedPerson);

        Forum forum = getRequestedForum(request);
        request.setAttribute("forum", this.getRequestedForum(request));

        request.setAttribute("loggedPersonCanWrite", forum.getWritersGroup().isMember(loggedPerson));
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
                computeNumberOfPages(DEFAULT_PAGE_SIZE, getRequestedThread(request).getConversationMessagesCount()));

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
                computeNumberOfPages(DEFAULT_PAGE_SIZE, getRequestedThread(request).getConversationMessagesCount()));
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

    private List<Content> getContentToDisplay(Collection<Node> nodes, Integer pageNumber, Integer pageSize) {
        List<Node> nodeCopy = new ArrayList<Node>(nodes);
        Collections.sort(nodeCopy);
        List<Content> contents = new ArrayList<Content>();
        int start = (pageNumber - 1) * pageSize;
        for (Node node : nodeCopy.subList(start, Math.min(nodeCopy.size(), start + pageSize))) {
            contents.add(node.getChild());
        }
        return contents;
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

            String author = message.getCreator().getName() + " (" + message.getCreator().getIstUsername() + ")";

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

        request.setAttribute("conversationThreads", getContentToDisplay(forum.getChildren(), pageNumber, DEFAULT_PAGE_SIZE));

        request.setAttribute("pageNumbers", computeNumberOfPages(DEFAULT_PAGE_SIZE, forum.getChildrenSet().size()));
        Person loggedPerson = getLoggedPerson(request);
        request.setAttribute("receivingMessagesByEmail", forum.isPersonReceivingMessagesByEmail(loggedPerson));

        request.setAttribute("loggedPersonCanWrite", forum.getWritersGroup().isMember(loggedPerson));
    }

}