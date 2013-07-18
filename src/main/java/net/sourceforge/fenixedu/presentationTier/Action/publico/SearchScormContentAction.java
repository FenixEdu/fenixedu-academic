package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.SearchDSpaceBean;
import net.sourceforge.fenixedu.dataTransferObject.SearchDSpaceBean.SearchElement;
import net.sourceforge.fenixedu.dataTransferObject.SearchDSpaceCoursesBean;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourseSite;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.functionalities.AbstractFunctionalityContext;
import net.sourceforge.fenixedu.presentationTier.Action.SearchDSpaceGeneralAction;
import net.sourceforge.fenixedu.presentationTier.Action.manager.FileContentCreationBean.EducationalResourceType;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.file.FileSearchCriteria.SearchField;
import pt.utl.ist.fenix.tools.file.FilesetMetadataQuery.ConjunctionType;
import pt.utl.ist.fenix.tools.file.VirtualPath;
import pt.utl.ist.fenix.tools.file.VirtualPathNode;

@Mapping(module = "publico", path = "/searchScormContent", scope = "session", parameter = "method")
@Forwards(value = { @Forward(name = "search", path = "search-scorm-content") })
public class SearchScormContentAction extends SearchDSpaceGeneralAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        String executionCourseId = request.getParameter("executionCourseID");
        ExecutionCourse course = null;
        if (executionCourseId != null) {
            course =
                    (ExecutionCourse) RootDomainObject.readDomainObjectByOID(ExecutionCourse.class,
                            Integer.valueOf(executionCourseId));
        } else {
            ExecutionCourseSite site =
                    (ExecutionCourseSite) AbstractFunctionalityContext.getCurrentContext(request).getSelectedContainer();
            course = site.getSiteExecutionCourse();
        }

        request.setAttribute("executionCourse", course);

        return super.execute(mapping, form, request, response);
    }

    public ActionForward prepareSearchForExecutionCourse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws  FenixServiceException {

        String executionCourseId = request.getParameter("executionCourseID");
        if (executionCourseId == null) {
            return prepareSearch(mapping, form, request, response, "search");
        }

        ExecutionCourse course =
                (ExecutionCourse) RootDomainObject.readDomainObjectByOID(ExecutionCourse.class,
                        Integer.valueOf(executionCourseId));
        SearchDSpaceCoursesBean bean = (SearchDSpaceCoursesBean) createNewBean();
        bean.addSearchElement(new SearchElement(SearchField.COURSE, course.getNome(), ConjunctionType.AND));
        bean.setExecutionYear(course.getExecutionYear());
        bean.setExecutionPeriod(course.getExecutionPeriod());

        request.setAttribute("bean", bean);

        return mapping.findForward("search");

    }

    public ActionForward changeTimeStamp(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws  FenixServiceException {

        SearchDSpaceCoursesBean bean = (SearchDSpaceCoursesBean) RenderUtils.getViewState("search").getMetaObject().getObject();
        RenderUtils.invalidateViewState("executionPeriodField");

        request.setAttribute("bean", bean);
        request.setAttribute("numberOfPages", bean.getNumberOfPages());
        request.setAttribute("pageNumber", bean.getPage());

        return mapping.findForward("search");
    }

    public ActionForward addNewSearchCriteria(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws  FenixServiceException {
        return super.addNewSearchCriteria(mapping, form, request, response, "search");
    }

    public ActionForward removeSearchCriteria(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws  FenixServiceException {
        return super.removeSearchCriteria(mapping, form, request, response, "search");
    }

    public ActionForward prepareSearchScormContents(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws  FenixServiceException {

        return super.prepareSearch(mapping, form, request, response, "search");
    }

    public ActionForward searchScormContents(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws  FenixServiceException {

        return super.searchContent(mapping, form, request, response, "search");
    }

    public ActionForward moveIndex(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws  FenixServiceException {

        return super.moveIndex(mapping, form, request, response, "search");
    }

    @Override
    protected VirtualPath getSearchPath(HttpServletRequest request) {

        SearchDSpaceCoursesBean bean = (SearchDSpaceCoursesBean) getBean(request);
        final VirtualPath searchPath = new VirtualPath();
        searchPath.addNode(new VirtualPathNode("Courses", "Courses"));
        ExecutionYear executionYear = bean.getExecutionYear();
        if (executionYear != null) {
            searchPath.addNode(new VirtualPathNode("EY" + executionYear.getIdInternal(), executionYear.getYear()));
            ExecutionSemester executionSemester = bean.getExecutionPeriod();
            if (executionSemester != null) {
                searchPath.addNode(new VirtualPathNode("EP" + executionSemester.getIdInternal(), executionSemester.getName()));
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
        for (int i = 0; types != null && i < types.length; i++) {
            typesList.add(EducationalResourceType.valueOf(types[i]));
        }
        bean.setEducationalResourceTypes(typesList);

        if (executionYearId != null && executionYearId.length() > 0) {
            bean.setExecutionYear((ExecutionYear) RootDomainObject.readDomainObjectByOID(ExecutionYear.class,
                    Integer.valueOf(executionYearId)));
        }
        if (executionPeriodId != null && executionPeriodId.length() > 0) {
            bean.setExecutionPeriod((ExecutionSemester) RootDomainObject.readDomainObjectByOID(ExecutionSemester.class,
                    Integer.valueOf(executionPeriodId)));
        }

        return bean;
    }

}
