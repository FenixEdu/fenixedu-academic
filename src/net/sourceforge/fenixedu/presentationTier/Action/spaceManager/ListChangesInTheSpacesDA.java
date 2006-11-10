package net.sourceforge.fenixedu.presentationTier.Action.spaceManager;

import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.DomainObjectActionLog;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.utl.ist.fenix.tools.util.CollectionPager;

public class ListChangesInTheSpacesDA extends FenixDispatchAction {

    public ActionForward changesList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	final String pageNumberString = request.getParameter("pageNumber");
	final Integer pageNumber = !StringUtils.isEmpty(pageNumberString) ? Integer.valueOf(pageNumberString) : Integer.valueOf(1); 
	
	final SortedSet<DomainObjectActionLog> treeSet = new TreeSet<DomainObjectActionLog>(DomainObjectActionLog.COMPARATOR_BY_INSTANT);
	treeSet.addAll(rootDomainObject.getDomainObjectActionLogsSet());
	CollectionPager<DomainObjectActionLog> collectionPager = new CollectionPager<DomainObjectActionLog>(treeSet, 50);
	
	request.setAttribute("pageNumber", pageNumber);
	request.setAttribute("domainObjectActionLogs", collectionPager.getPage(pageNumber.intValue()));
	request.setAttribute("numberOfPages", Integer.valueOf(collectionPager.getNumberOfPages()));

	return mapping.findForward("listChangesInTheSpaces");
    }
}
