package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.presentationTier.Action.SearchDSpaceGeneralAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.utl.ist.fenix.tools.file.VirtualPath;
import pt.utl.ist.fenix.tools.file.VirtualPathNode;

public class SearchScormContentAction extends SearchDSpaceGeneralAction {

	public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ManageExecutionCourseDA.propageContextIds(request);
		setAttributeItem(request);
		return super.execute(mapping, actionForm, request, response);
	}

	private void setAttributeItem(HttpServletRequest request) {
		request.setAttribute("item",RootDomainObject.readDomainObjectByOID(Item.class, Integer.valueOf(request.getParameter("itemID"))));
	}
	
	public ActionForward prepareSearchScormContents(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
			FenixServiceException {

		return super.prepareSearch(mapping, form, request, response, "search");
	}

	public ActionForward searchScormContents(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
			FenixServiceException {

		return super.searchContent(mapping, form, request, response, "search");
	}

	public ActionForward moveIndex(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {

		return super.moveIndex(mapping, form, request, response, "search");
	}
	
	@Override
	protected VirtualPath getSearchPath() {
		
		final VirtualPath searchPath = new VirtualPath();
		searchPath.addNode(new VirtualPathNode("Courses", "Courses"));
		
		return searchPath;
	}
	

}
