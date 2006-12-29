package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.SearchDSpaceBean;
import net.sourceforge.fenixedu.dataTransferObject.SearchDSpaceCoursesBean;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.presentationTier.Action.SearchDSpaceGeneralAction;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

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

	protected ActionForward prepareSearchForExecutionCourse(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response, String forwardTo) throws FenixFilterException,
			FenixServiceException {
	
		String executionCourseId = request.getParameter("executionCourseId");
		if(executionCourseId==null) {
			return prepareSearch(mapping, form, request, response, "search");
		}
		
			ExecutionCourse course = (ExecutionCourse) RootDomainObject.readDomainObjectByOID(ExecutionCourse.class, Integer.valueOf(executionCourseId));			
			SearchDSpaceBean bean = createNewBean();
			bean.setExecutionYear(course.getExecutionYear());
			bean.setExecutionPeriod(course.getExecutionPeriod());
		//	bean.setadd(course.getNome());
			request.setAttribute("bean", bean);
			String searchType = request.getParameter("searchType");
			request.setAttribute("searchType", searchType);
			
			return mapping.findForward("search");
			
	}
	
	public ActionForward changeYear(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
			FenixServiceException {
		
		SearchDSpaceCoursesBean bean = (SearchDSpaceCoursesBean) RenderUtils.getViewState("search").getMetaObject().getObject();
		request.setAttribute("bean", bean);
		String searchType = request.getParameter("searchType");
		request.setAttribute("searchType", searchType);
		RenderUtils.invalidateViewState("executionPeriodField");
		return mapping.findForward("search");
	}
	
	
	public ActionForward addNewSearchCriteria(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
			FenixServiceException {
		return super.addNewSearchCriteria(mapping, form, request, response, "search");
	}
	
	public ActionForward removeSearchCriteria(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
			FenixServiceException {
		return super.removeSearchCriteria(mapping, form, request, response, "search");
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
	protected VirtualPath getSearchPath(HttpServletRequest request) {
		
		SearchDSpaceCoursesBean bean = (SearchDSpaceCoursesBean) RenderUtils.getViewState("search").getMetaObject().getObject();
		final VirtualPath searchPath = new VirtualPath();
		searchPath.addNode(new VirtualPathNode("Courses", "Courses"));
		ExecutionYear executionYear =bean.getExecutionYear(); 
		if(executionYear!=null) {
			searchPath.addNode(new VirtualPathNode("EY" + executionYear.getIdInternal(), executionYear.getYear()));
			ExecutionPeriod executionPeriod = bean.getExecutionPeriod();
			if(executionPeriod!=null) {
				searchPath.addNode(new VirtualPathNode("EP" + executionPeriod.getIdInternal(), executionPeriod
						.getName()));
			}
		}
		
		return searchPath;
	}
	
	@Override
	protected SearchDSpaceBean createNewBean() {
		return new SearchDSpaceCoursesBean();
	}

	@Override
	protected SearchDSpaceBean reconstructBeanFromRequest(HttpServletRequest request) {
		SearchDSpaceCoursesBean bean = (SearchDSpaceCoursesBean) super.reconstructBeanFromRequest(request);
		String executionYearId = request.getParameter("executionYearId");
		String executionPeriodId = request.getParameter("executionPeriodId");
		
		if(executionYearId!=null && executionYearId.length()>0) {
			bean.setExecutionYear((ExecutionYear)RootDomainObject.readDomainObjectByOID(ExecutionYear.class, Integer.valueOf(executionYearId)));
		}
		if(executionPeriodId!=null && executionPeriodId.length()>0) {
			bean.setExecutionPeriod((ExecutionPeriod)RootDomainObject.readDomainObjectByOID(ExecutionPeriod.class, Integer.valueOf(executionPeriodId)));
		}
		
		return bean;
	}
	
//	@Override
//	protected void putBeanInRequest(HttpServletRequest request, SearchDSpaceBean bean) {
//		super.putBeanInRequest(request, bean);
//		SearchDSpaceCoursesBean courseBean = (SearchDSpaceCoursesBean) bean;
//		ExecutionYear executionYear = courseBean.getExecutionYear();
//		if(executionYear!=null) {
//			request.setAttribute("executionYearId",executionYear.getIdInternal());
//		}
//		ExecutionPeriod executionPeriod = courseBean.getExecutionPeriod();
//		if(executionPeriod!=null) {
//			request.setAttribute("executionPeriodId",executionPeriod.getIdInternal());
//		}
//	}
}
