/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.messaging;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.dataTransferObject.messaging.CreateConversationMessageBean;
import net.sourceforge.fenixedu.dataTransferObject.messaging.CreateConversationThreadAndMessageBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.messaging.ConversationMessage;
import net.sourceforge.fenixedu.domain.messaging.ConversationThread;
import net.sourceforge.fenixedu.domain.messaging.Forum;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt"> Goncalo Luiz</a><br/> Created
 *         on May 5, 2006, 10:42:00 AM
 * 
 */
public abstract class ForunsManagement extends FenixDispatchAction {

    private static final Integer DEFAULT_PAGE_SIZE = 20;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("contextPrefix", getContextPrefix(request));
        return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        List<Forum> foruns = RootDomainObject.getInstance().getForuns();
        request.setAttribute("foruns", foruns);

        return mapping.findForward("viewForuns");
    }

    @SuppressWarnings("unchecked")
    private GenericPair<List, List<Integer>> pageList(List listToPage, Integer pageNumber,
            Integer pageSize, Comparator comparator) {
        int listSize = listToPage.size();
        int totalPages = computeNumberOfPages(pageSize, listSize);

        int startIndex = (pageNumber - 1) * pageSize;
        int endIndex = startIndex + (pageSize - 1);
        int lastIndex = (listSize > 0) ? (listSize - 1) : 0;

        if (endIndex > lastIndex) {
            endIndex = lastIndex;
        }

        List sortedList = new ArrayList(listToPage);
        Collections.sort(sortedList, comparator);
        Collections.reverse(sortedList);

        List<Integer> pageNumbers = new ArrayList<Integer>();
        for (int i = 1; i <= totalPages; i++) {
            pageNumbers.add(i);
        }
        // must add 1 to endIndex, since subList excludes the final element
        return new GenericPair<List, List<Integer>>(sortedList.subList(startIndex,
                listSize > 0 ? endIndex + 1 : 0), pageNumbers);
    }

    /**
     * @param pageSize
     * @param listSize
     * @return
     */
    private int computeNumberOfPages(Integer pageSize, int listSize) {
        int totalPages = (int) StrictMath.ceil(listSize / Double.valueOf(pageSize));
        return totalPages;
    }

    @SuppressWarnings("unchecked")
    public ActionForward viewForum(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixFilterException, FenixServiceException {

        prepareViewForum(request);

        return mapping.findForward("viewForum");
    }

    private void prepareViewForum(HttpServletRequest request) throws FenixFilterException,
            FenixServiceException {

        Forum forum = this.getRequestedForum(request);
        request.setAttribute("forum", forum);

        Integer pageNumber = getPageNumber(request);
        GenericPair<List, List<Integer>> result = pageList(forum.getConversationThreads(), pageNumber,
                DEFAULT_PAGE_SIZE, ConversationThread.CONVERSATION_THREAD_COMPARATOR_BY_CREATION_DATE);
        List<ConversationThread> conversationThreadsPage = result.getLeft();
        List<Integer> pageNumbers = result.getRight();
        request.setAttribute("conversationThreads", conversationThreadsPage);
        request.setAttribute("currentPageNumber", pageNumber);
        request.setAttribute("pageNumbers", pageNumbers);

        Person loggedPerson = getLoggedPerson(request);
        request.setAttribute("receivingMessagesByEmail", forum
                .isPersonReceivingMessagesByEmail(loggedPerson));

        request.setAttribute("loggedPersonCanWrite", forum.getWritersGroup().isMember(loggedPerson));
    }

    public ActionForward prepareCreateThreadAndMessage(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixServiceException {

        Forum forum = getRequestedForum(request);
        request.setAttribute("forum", forum);

        IUserView userView = getUserView(request);
        request.setAttribute("person", userView.getPerson());

        return mapping.findForward("createThreadAndMessage");

    }

    public ActionForward createThreadAndMessage(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixServiceException {

        CreateConversationThreadAndMessageBean createConversationThreadAndMessageBean = (CreateConversationThreadAndMessageBean) RenderUtils
                .getViewState("createThreadAndMessage").getMetaObject().getObject();

        try {

            ServiceUtils.executeService(getUserView(request), "CreateConversationThreadAndMessage",
                    new Object[] { createConversationThreadAndMessageBean });
        } catch (DomainException e) {
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(e.getKey()));

            saveMessages(request, actionMessages);

            return prepareCreateThreadAndMessage(mapping, form, request, response);
        }

        return this.viewForum(mapping, form, request, response);
    }

    public ActionForward viewThread(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
            FenixFilterException {

        ConversationThread thread = this.getRequestedThread(request);
        request.setAttribute("thread", thread);

        Integer pageNumber = getPageNumber(request);
        if (this.getGoToLastPage(request)) {
            pageNumber = this.computeNumberOfPages(DEFAULT_PAGE_SIZE, thread
                    .getConversationMessagesCount());
        }

        GenericPair<List, List<Integer>> result = pageList(thread.getConversationMessages(), pageNumber,
                DEFAULT_PAGE_SIZE, ConversationMessage.CONVERSATION_MESSAGE_COMPARATOR_BY_CREATION_DATE);
        List<Integer> pageNumbers = result.getRight();
        request.setAttribute("currentPageNumber", pageNumber);
        request.setAttribute("pageNumbers", pageNumbers);
        request.setAttribute("messages", result.getLeft());

        Person loggedPerson = getLoggedPerson(request);
        request.setAttribute("person", loggedPerson);

        Forum forum = getRequestedForum(request);
        request.setAttribute("forum", this.getRequestedForum(request));

        request.setAttribute("loggedPersonCanWrite", forum.getWritersGroup().isMember(loggedPerson));
        request.setAttribute("showReplyBox", this.getShowReplyBox(request));

        return mapping.findForward("viewThread");
    }

    public ActionForward prepareCreateMessage(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
            FenixFilterException {

        ConversationThread conversationThread = this.getRequestedThread(request);
        int lastPage = this.computeNumberOfPages(DEFAULT_PAGE_SIZE, conversationThread
                .getConversationMessagesCount());
        Integer quotedMessageId = this.getQuotedMessageId(request);

        MessageResources resources = this.getResources(request, "MESSAGING_RESOURCES");
        String quotationText = null;
        if (quotedMessageId != null) {
            ConversationMessage message = rootDomainObject.readConversationMessageByOID(quotedMessageId);

            String creatorIstUsername = message.getCreator().getIstUsername();

            String author = message.getCreator().getName() + " (" + message.getCreator().getIstUsername()
                    + ")";

//            String quotationLeadingCharacter = resources.getMessage(this.getLocale(request),
//                    "messaging.viewThread.quotationTextLeadingCharacter");
//            String[] messageLines = message.getBody().split(System.getProperty("line.separator"));
//            StringBuffer quotationTextBuffer = new StringBuffer();
//            for (int i = 0; i < messageLines.length; i++) {
//                quotationTextBuffer.append(creatorIstUsername).append(quotationLeadingCharacter).append(
//                        messageLines[i]);
//            }
            
            quotationText = resources.getMessage(this.getLocale(request),
                    "messaging.viewThread.quotationText", author, message.getBody());

        }

        request.setAttribute("quotationText", quotationText);
        request.setAttribute("pageNumber", Integer.valueOf(lastPage).toString());

        return viewThread(mapping, actionForm, request, response);

    }

    public ActionForward createMessage(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
            FenixFilterException {

        CreateConversationMessageBean createConversationMessageBean = (CreateConversationMessageBean) RenderUtils
                .getViewState("createMessage").getMetaObject().getObject();

        try {

            ServiceUtils.executeService(getUserView(request), "CreateConversationMessage",
                    new Object[] { createConversationMessageBean });
        } catch (DomainException e) {
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(e.getKey()));

            saveMessages(request, actionMessages);

            return prepareCreateMessage(mapping, actionForm, request, response);
        }

        return viewThread(mapping, actionForm, request, response);
    }

    public ActionForward emailSubscribe(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixServiceException {

        ServiceUtils.executeService(getUserView(request), "AddForumEmailSubscriber", new Object[] {
                getForumId(request), getUserView(request).getPerson().getIdInternal() });

        prepareViewForum(request);

        return mapping.findForward("viewForum");
    }

    public ActionForward emailUnsubscribe(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixServiceException {

        ServiceUtils.executeService(getUserView(request), "RemoveForumEmailSubscriber", new Object[] {
                getForumId(request), getUserView(request).getPerson().getIdInternal() });

        prepareViewForum(request);

        return mapping.findForward("viewForum");
    }

    public Integer getForumId(HttpServletRequest request) {
        return Integer.valueOf(request.getParameter("forumId"));
    }

    public Boolean getGoToLastPage(HttpServletRequest request) {
        return Boolean.valueOf(request.getParameter("goToLastPage"));
    }

    public Integer getQuotedMessageId(HttpServletRequest request) {
        Integer quotedMessageId = null;
        try {
            quotedMessageId = Integer.valueOf(request.getParameter("quotedMessageId"));
        } catch (NumberFormatException e) {
            // ok, lets just return null
        }

        return quotedMessageId;
    }

    public Integer getPageNumber(HttpServletRequest request) {
        String pageNumberString = request.getParameter("pageNumber");
        if (pageNumberString == null) {
            pageNumberString = (String) request.getAttribute("pageNumber");
        }
        return (pageNumberString != null) ? Integer.valueOf(pageNumberString) : 1;
    }

    private Integer getThreadId(HttpServletRequest request) {
        return Integer.valueOf(request.getParameter("threadId"));
    }

    private Boolean getShowReplyBox(HttpServletRequest request) {
        return Boolean.valueOf(request.getParameter("showReplyBox"));
    }

    private ConversationThread getRequestedThread(HttpServletRequest request) {
        return rootDomainObject.readConversationThreadByOID(this.getThreadId(request));
    }

    private Forum getRequestedForum(HttpServletRequest request) {
        return rootDomainObject.readForumByOID(this.getForumId(request));
    }

    private String getContextPrefix(HttpServletRequest request) {
        String contextPrefix = getContextInformation(request);

        if (contextPrefix.contains("?")) {
            contextPrefix += "&amp;";
        } else {
            contextPrefix += "?";
        }

        return contextPrefix;
    }

    /**
     * Method to override in action specific context, to allow specification of
     * additional parameters regarding to action context. Example:
     * /actionXpto.do?someObjectID=1234
     * 
     * 
     * 
     * @return
     */
    protected abstract String getContextInformation(HttpServletRequest request);

}
