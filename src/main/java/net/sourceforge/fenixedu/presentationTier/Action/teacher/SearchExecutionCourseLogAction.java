package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.teacher.executionCourse.SearchExecutionCourseLogBean;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourseLog;
import net.sourceforge.fenixedu.domain.ExecutionCourseLog.ExecutionCourseLogTypes;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.coordinator.CoordinatedDegreeInfo;
import net.sourceforge.fenixedu.util.Month;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.predicates.Predicate;
import pt.utl.ist.fenix.tools.util.CollectionPager;

@Mapping(module = "teacher", path = "/searchECLog", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "search", path = "/teacher/viewLogSearch.jsp", tileProperties = @Tile(
        navLocal = "/teacher/commons/executionCourseAdministrationNavbar.jsp", title = "private.teacher.changesLog",
        bundle = "TITLES_RESOURCES")) })
public class SearchExecutionCourseLogAction extends FenixDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        CoordinatedDegreeInfo.setCoordinatorContext(request);
        return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward prepareInit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        ExecutionCourse executionCourse = getDomainObject(request, "objectCode");
        SearchExecutionCourseLogBean seclb = new SearchExecutionCourseLogBean(executionCourse);
        seclb.setExecutionCourseLogTypes(new ArrayList<ExecutionCourseLogTypes>());

        request.setAttribute("searchBean", seclb);
        request.setAttribute("executionCourse", seclb.getExecutionCourse());

        return mapping.findForward("search");
    }

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        ExecutionCourse executionCourse = getDomainObject(request, "objectCode");
        SearchExecutionCourseLogBean seclb = readSearchBean(request, executionCourse);

        searchLogs(seclb);
        request.setAttribute("searchBean", seclb);
        request.setAttribute("executionCourse", seclb.getExecutionCourse());

        prepareAttendsCollectionPages(request, seclb, seclb.getExecutionCourse());
        return mapping.findForward("search");
    }

    private SearchExecutionCourseLogBean readSearchBean(HttpServletRequest request, ExecutionCourse executionCourse) {
        String executionCourseID = request.getParameter("executionCourse");
        if (executionCourseID != null) {
            SearchExecutionCourseLogBean seclb =
                    new SearchExecutionCourseLogBean(FenixFramework.<ExecutionCourse> getDomainObject(executionCourseID));

            String viewPhoto = request.getParameter("viewPhoto");
            if (viewPhoto != null && viewPhoto.equalsIgnoreCase("true")) {
                seclb.setViewPhoto(true);
            } else {
                seclb.setViewPhoto(false);
            }

            String logTypes = request.getParameter("executionCourseLogTypes");
            if (logTypes != null) {
                List<ExecutionCourseLogTypes> list = new ArrayList<ExecutionCourseLogTypes>();
                for (String logType : logTypes.split(":")) {
                    list.add(ExecutionCourseLogTypes.valueOf(logType));
                }
                seclb.setExecutionCourseLogTypes(list);
            }

            String professorships = request.getParameter("professorships");
            if (professorships != null) {
                List<Professorship> list = new ArrayList<Professorship>();
                for (String professorship : professorships.split(":")) {
                    list.add(FenixFramework.<Professorship> getDomainObject(professorship));
                }
                seclb.setProfessorships(list);
            }

            String months = request.getParameter("months");
            if (months != null) {
                List<Month> list = new ArrayList<Month>();
                for (String month : months.split(":")) {
                    list.add(Month.fromInt(Integer.valueOf(month).intValue()));
                }
                seclb.setMonths(list);
            }

            return seclb;
        } else {
            return new SearchExecutionCourseLogBean(executionCourse);
        }
    }

    private void prepareAttendsCollectionPages(HttpServletRequest request, SearchExecutionCourseLogBean seclb,
            ExecutionCourse executionCourse) {
        Collection<ExecutionCourseLog> ecLogs = seclb.getExecutionCourseLogs();
        List<ExecutionCourseLog> listECLogs = new ArrayList<ExecutionCourseLog>(ecLogs);
        Collections.sort(listECLogs, ExecutionCourseLog.COMPARATOR_BY_WHEN_DATETIME);
        int entriesPerPage = 20;
        final CollectionPager<ExecutionCourseLog> pager = new CollectionPager<ExecutionCourseLog>(listECLogs, entriesPerPage);
        request.setAttribute("numberOfPages", (listECLogs.size() / entriesPerPage) + 1);

        final String pageParameter = request.getParameter("pageNumber");
        final Integer page = StringUtils.isEmpty(pageParameter) ? Integer.valueOf(1) : Integer.valueOf(pageParameter);
        request.setAttribute("pageNumber", page);

        SearchExecutionCourseLogBean logPagesBean = new SearchExecutionCourseLogBean(executionCourse);

        searchLogs(logPagesBean);

        logPagesBean.setExecutionCourseLogs(pager.getPage(page));
        if (seclb.getViewPhoto()) {
            logPagesBean.setViewPhoto(true);
        }
        request.setAttribute("logPagesBean", logPagesBean);
    }

    // copied from ExecutionCourse, was public
    private void searchLogs(SearchExecutionCourseLogBean bean) {
        final Predicate<ExecutionCourseLog> filter = bean.getFilters();
        final Collection<ExecutionCourseLog> validLogs = new HashSet<ExecutionCourseLog>();
        for (final ExecutionCourseLog log : bean.getExecutionCourse().getExecutionCourseLogs()) {
            if (filter.eval(log)) {
                validLogs.add(log);
            }
        }
        bean.setExecutionCourseLogs(validLogs);
    }

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        request.setAttribute("objectCode", request.getAttribute("objectCode"));

        SearchExecutionCourseLogBean bean = getRenderedObject();
        RenderUtils.invalidateViewState();
        searchLogs(bean);

        request.setAttribute("searchBean", bean);
        request.setAttribute("executionCourse", bean.getExecutionCourse());

        prepareAttendsCollectionPages(request, bean, bean.getExecutionCourse());

        return mapping.findForward("search");
    }

}
