package net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice.lists;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.academicAdministration.SearchStudentsByCurricularCourseParametersBean;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.lists.SearchCourseResponsiblesParametersBean;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.BundleUtil;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixframework.pstm.AbstractDomainObject;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

@Mapping(path = "/listCourseResponsibles", module = "departmentAdmOffice")
@Forwards({ @Forward(name = "chooseCurricularCourse", path = "/departmentAdmOffice/lists/listCourseResponsibles.jsp",
        tileProperties = @Tile(title = "private.administrationofcreditsofdepartmentteachers.lists.responsiblesbycourse")) })
public class ListCourseResponsibles extends FenixDispatchAction {

    protected static final String RESOURCE_MODULE = "academicAdminOffice";

    public ActionForward prepareByCurricularCourse(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("searchBean", getOrCreateSearchBean());
        return mapping.findForward("chooseCurricularCourse");
    }

    private SearchStudentsByCurricularCourseParametersBean getOrCreateSearchBean() {
        SearchStudentsByCurricularCourseParametersBean bean = getRenderedObject("searchBean");
        if (bean == null) {
            bean =
                    new SearchStudentsByCurricularCourseParametersBean(new TreeSet<Degree>(AccessControl.getPerson()
                            .getEmployee().getCurrentDepartmentWorkingPlace().getDegrees()));
        }
        return bean;
    }

    public ActionForward chooseExecutionYearPostBack(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        SearchStudentsByCurricularCourseParametersBean searchBean = getOrCreateSearchBean();
        RenderUtils.invalidateViewState();
        request.setAttribute("searchBean", searchBean);
        request.setAttribute("courseResponsibles", getCourseResponsibles(request, searchBean));

        return mapping.findForward("chooseCurricularCourse");
    }

    private Department getDepartment(final HttpServletRequest request) {
        return getUserView(request).getPerson().getEmployee().getCurrentDepartmentWorkingPlace();
    }

    private List<SearchCourseResponsiblesParametersBean> getCourseResponsibles(HttpServletRequest request,
            SearchStudentsByCurricularCourseParametersBean searchBean) {

        final List<SearchCourseResponsiblesParametersBean> result = new ArrayList<SearchCourseResponsiblesParametersBean>();
        final Department department = getDepartment(request);
        final ExecutionYear executionYear = searchBean.getExecutionYear();
        int semester;

        for (semester = 1; semester <= 2; semester++) {
            ExecutionSemester execSemester = executionYear.getExecutionSemesterFor(semester);
            for (ExecutionCourse execCourse : execSemester.getAssociatedExecutionCourses()) {
                for (Professorship professorship : execCourse.getProfessorships()) {
                    if (professorship.isResponsibleFor()) {
                        Person person = professorship.getPerson();
                        Employee employee = person.getEmployee();
                        Department dept =
                                employee.getLastDepartmentWorkingPlace(executionYear.getBeginDateYearMonthDay(),
                                        executionYear.getEndDateYearMonthDay());
                        CurricularCourse curricCourse = null;
                        CompetenceCourse compCourse = null;
                        ExecutionDegree execDegree = null;

                        if (dept != null && dept.equals(department)) {
                            for (CurricularCourse aux : execCourse.getAssociatedCurricularCoursesSet()) {
                                curricCourse = aux;

                                if (curricCourse != null) {
                                    compCourse = curricCourse.getCompetenceCourse();
                                    execDegree = curricCourse.getExecutionDegreeFor(executionYear);

                                    if (compCourse != null && execDegree != null) {
                                        SearchCourseResponsiblesParametersBean bean =
                                                new SearchCourseResponsiblesParametersBean(curricCourse, compCourse, person,
                                                        execSemester, execDegree.getCampus(), execDegree.getDegree());
                                        result.add(bean);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    private ExecutionYear getExecutionYearParameter(final HttpServletRequest request) {
        final String executionYearIdString = request.getParameter("executionYearId");
        final Integer executionYearId =
                executionYearIdString != null && executionYearIdString.length() > 0 ? Integer.valueOf(executionYearIdString) : null;
        return executionYearId == null ? null : AbstractDomainObject.fromExternalId(executionYearId);
    }

    public ActionForward downloadStatistics(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        SearchStudentsByCurricularCourseParametersBean searchBean = getOrCreateSearchBean();
        final ExecutionYear executionYear = getExecutionYearParameter(request);
        searchBean.setExecutionYear(executionYear);

        final String filename = getResourceMessage("label.statistics") + "_" + executionYear.getName().replace('/', '-');
        final Spreadsheet spreadsheet = new Spreadsheet(filename);
        addStatisticsHeaders(spreadsheet);
        addStatisticsInformation(spreadsheet, getCourseResponsibles(request, searchBean));

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment; filename=" + filename + ".xls");
        ServletOutputStream writer = response.getOutputStream();

        spreadsheet.exportToXLSSheet(writer);
        writer.flush();
        response.flushBuffer();

        return null;
    }

    private void addStatisticsHeaders(final Spreadsheet spreadsheet) {
        spreadsheet.setHeader(getResourceMessage("label.curricular.course.from.curriculum"));
        spreadsheet.setHeader(getResourceMessage("label.competence.course.name"));
        spreadsheet.setHeader(getResourceMessage("degree"));
        spreadsheet.setHeader(getResourceMessage("campus"));
        spreadsheet.setHeader(getResourceMessage("label.responsible"));
        spreadsheet.setHeader(getResourceMessage("label.semester"));
    }

    static private String getResourceMessage(String key) {
        return BundleUtil.getMessageFromModuleOrApplication(RESOURCE_MODULE, key);
    }

    private void addStatisticsInformation(final Spreadsheet spreadsheet,
            List<SearchCourseResponsiblesParametersBean> responsibleList) {

        for (SearchCourseResponsiblesParametersBean bean : responsibleList) {
            final Row row = spreadsheet.addRow();
            row.setCell(bean.getCurricularCourse().getName());
            row.setCell(bean.getCompetenceCourse().getName());
            row.setCell(bean.getDegree().getSigla());
            row.setCell(bean.getCampus().getName());
            row.setCell(bean.getResponsible().getName());
            row.setCell(bean.getExecutionSemester().getSemester().toString());
        }
    }

}
