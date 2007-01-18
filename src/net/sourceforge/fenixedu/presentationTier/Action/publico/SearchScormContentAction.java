package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.SearchDSpaceBean;
import net.sourceforge.fenixedu.dataTransferObject.SearchDSpaceCoursesBean;
import net.sourceforge.fenixedu.dataTransferObject.SearchDSpaceBean.SearchElement;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.presentationTier.Action.SearchDSpaceGeneralAction;
import net.sourceforge.fenixedu.presentationTier.Action.manager.FileItemCreationBean.EducationalResourceType;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.utl.ist.fenix.tools.file.VirtualPath;
import pt.utl.ist.fenix.tools.file.VirtualPathNode;
import pt.utl.ist.fenix.tools.file.FileSearchCriteria.SearchField;
import pt.utl.ist.fenix.tools.file.FilesetMetadataQuery.ConjunctionType;

public class SearchScormContentAction extends SearchDSpaceGeneralAction {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String executionCourseId = request.getParameter("executionCourseID");
		ExecutionCourse course = (ExecutionCourse) RootDomainObject.readDomainObjectByOID(ExecutionCourse.class, Integer.valueOf(executionCourseId));
		request.setAttribute("executionCourse", course);
		
		return super.execute(mapping, form, request, response);
	}
	
	public ActionForward prepareSearchForExecutionCourse(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
			FenixServiceException {
	
		
		String executionCourseId = request.getParameter("executionCourseID");
		if(executionCourseId==null) {
			return prepareSearch(mapping, form, request, response, "search");
		}
		
			ExecutionCourse course = (ExecutionCourse) RootDomainObject.readDomainObjectByOID(ExecutionCourse.class, Integer.valueOf(executionCourseId));			
			SearchDSpaceCoursesBean bean = (SearchDSpaceCoursesBean) createNewBean();
			bean.addSearchElement(new SearchElement(SearchField.COURSE ,course.getNome(), ConjunctionType.AND));
			bean.setExecutionYear(course.getExecutionYear());
			bean.setExecutionPeriod(course.getExecutionPeriod());
			
			request.setAttribute("bean", bean);
			
			return mapping.findForward("search");
			
	}
	
	public ActionForward changeTimeStamp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
			FenixServiceException {
		
		SearchDSpaceCoursesBean bean = (SearchDSpaceCoursesBean) RenderUtils.getViewState("search").getMetaObject().getObject();
		RenderUtils.invalidateViewState("executionPeriodField");
		
		request.setAttribute("bean", bean);
		request.setAttribute("numberOfPages",bean.getNumberOfPages());
		request.setAttribute("page",bean.getPage());
		
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
		
		SearchDSpaceCoursesBean bean = (SearchDSpaceCoursesBean) getBean(request);
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
		
		String types[] = request.getParameterValues("type");
		List<EducationalResourceType> typesList = new ArrayList<EducationalResourceType>();
		for(int i=0;types!=null && i<types.length;i++) {
			typesList.add(EducationalResourceType.valueOf(types[i]));
		}
		bean.setEducationalResourceTypes(typesList);
		
		if(executionYearId!=null && executionYearId.length()>0) {
			bean.setExecutionYear((ExecutionYear)RootDomainObject.readDomainObjectByOID(ExecutionYear.class, Integer.valueOf(executionYearId)));
		}
		if(executionPeriodId!=null && executionPeriodId.length()>0) {
			bean.setExecutionPeriod((ExecutionPeriod)RootDomainObject.readDomainObjectByOID(ExecutionPeriod.class, Integer.valueOf(executionPeriodId)));
		}
		
		return bean;
	}
	
}
