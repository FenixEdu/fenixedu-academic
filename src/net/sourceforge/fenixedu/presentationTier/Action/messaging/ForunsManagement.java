/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.messaging;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.messaging.ConversationMessage;
import net.sourceforge.fenixedu.domain.messaging.ConversationThread;
import net.sourceforge.fenixedu.domain.messaging.Forum;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt"> Goncalo Luiz</a><br/> Created
 *         on May 5, 2006, 10:42:00 AM
 * 
 */
public class ForunsManagement extends FenixDispatchAction {

    private static final int CONVERSATION_THREADS_PAGE_SIZE = 10;

    public ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        List<Forum> foruns = RootDomainObject.getInstance().getForuns();
        request.setAttribute("foruns", foruns);

        return mapping.findForward("viewForuns");
    }

    private GenericPair<List<ConversationThread>, List<Integer>> getConversationThreadsPage(Forum forum,
            Integer pageNumber) {

        List<ConversationThread> conversationThreads = new ArrayList<ConversationThread>(forum
                .getConversationThreads());

        int totalPages = (int) StrictMath.ceil(conversationThreads.size()
                / Double.valueOf(CONVERSATION_THREADS_PAGE_SIZE));

        List<Integer> pages = new ArrayList<Integer>();
        for (int i = 1; i <= totalPages; i++) {
            pages.add(i);
        }

        Collections.sort(conversationThreads,
                ConversationThread.CONVERSATION_THREAD_COMPARATOR_BY_CREATION_DATE);

        Collections.reverse(conversationThreads);

        int startIndex = (pageNumber - 1) * CONVERSATION_THREADS_PAGE_SIZE;
        int endIndex = startIndex + (CONVERSATION_THREADS_PAGE_SIZE - 1);
        int lastIndex = (conversationThreads.size() > 0) ? (conversationThreads.size() - 1) : 0;

        if (endIndex > lastIndex) {
            endIndex = lastIndex;
        }

        return new GenericPair<List<ConversationThread>, List<Integer>>(conversationThreads.subList(
                startIndex, endIndex), pages);

    }

    public ActionForward viewForum(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        Forum forum = this.getRequestedForum(request);
        request.setAttribute("forum", forum);

        Integer pageNumber = getPageNumber(request);
        GenericPair<List<ConversationThread>, List<Integer>> result = getConversationThreadsPage(forum,
                pageNumber);
        List<ConversationThread> conversationThreadsPage = result.getLeft();
        List<Integer> pageNumbers = result.getRight();

        request.setAttribute("conversationThreads", conversationThreadsPage);
        request.setAttribute("currentPageNumber", pageNumber);
        request.setAttribute("pageNumbers", pageNumbers);

        return mapping.findForward("viewForum");
    }

    public ActionForward prepareCreateThreadAndMessage(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        Forum forum = getRequestedForum(request);
        request.setAttribute("forum", forum);

        return mapping.findForward("createThreadAndMessage");

    }

    public ActionForward viewThread(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("forum", this.getRequestedForum(request));
        request.setAttribute("thread", this.getRequestedThread(request));
        request.setAttribute("person", this.getLoggedPerson(request));
        request.setAttribute("showReplyBox", this.getShowReplyBox(request));

        SortedSet<ConversationMessage> messages = new TreeSet<ConversationMessage>(
                ConversationMessage.CONVERSATION_MESSAGE_COMPARATOR_BY_CREATION_DATE);
        messages.addAll(this.getRequestedThread(request).getConversationMessages());
        request.setAttribute("messages", messages);

        return mapping.findForward("viewThread");
    }

    public Integer getForumId(HttpServletRequest request) {
        return Integer.valueOf(request.getParameter("forumId"));
    }

    public Integer getPageNumber(HttpServletRequest request) {
        String pageNumberString = request.getParameter("pageNumber");
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

}
