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
 * @author <a href="mailto:goncalo@ist.utl.pt"> Goncalo Luiz</a><br/> Created on May 5, 2006, 10:42:00
 *         AM
 * 
 */
public class ForunsManagement extends FenixDispatchAction {

    private static final Integer DEFAULT_PAGE_SIZE = 10;

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
	return new GenericPair<List, List<Integer>>(sortedList.subList(startIndex, listSize>0?endIndex + 1:0),
		pageNumbers);
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
	    HttpServletResponse response) {

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

	return mapping.findForward("viewForum");
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

    public ActionForward viewThread(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
	    FenixFilterException {
	ConversationThread thread = this.getRequestedThread(request);

	Integer pageNumber = getPageNumber(request);
	GenericPair<List, List<Integer>> result = pageList(thread.getConversationMessages(), pageNumber,
		DEFAULT_PAGE_SIZE, ConversationMessage.CONVERSATION_MESSAGE_COMPARATOR_BY_CREATION_DATE);
	List<Integer> pageNumbers = result.getRight();

	request.setAttribute("forum", this.getRequestedForum(request));
	request.setAttribute("thread", thread);
	request.setAttribute("person", this.getLoggedPerson(request));
	request.setAttribute("showReplyBox", this.getShowReplyBox(request));
	request.setAttribute("currentPageNumber", pageNumber);
	request.setAttribute("pageNumbers", pageNumbers);
	request.setAttribute("messages", result.getLeft());

	return mapping.findForward("viewThread");
    }

    public ActionForward createMessage(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
	    FenixFilterException {

	ConversationThread t = this.getRequestedThread(request);
	int lastPage = this.computeNumberOfPages(DEFAULT_PAGE_SIZE, t.getConversationMessagesCount());
	request.setAttribute("pageNumber", Integer.valueOf(lastPage).toString());

	return this.viewThread(mapping, actionForm, request, response);
    }

    public Integer getForumId(HttpServletRequest request) {
	return Integer.valueOf(request.getParameter("forumId"));
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

}
