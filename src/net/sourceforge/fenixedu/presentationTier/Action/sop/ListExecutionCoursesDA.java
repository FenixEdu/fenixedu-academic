package net.sourceforge.fenixedu.presentationTier.Action.sop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.report.Spreadsheet;
import net.sourceforge.fenixedu.util.report.Spreadsheet.Row;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public class ListExecutionCoursesDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final Collection<ExecutionPeriod> executionPeriods = rootDomainObject.getExecutionPeriodsSet();
        final List<ExecutionPeriod> sortedExecutionPeriods = new ArrayList<ExecutionPeriod>(executionPeriods);
        Collections.sort(sortedExecutionPeriods);
        Collections.reverse(sortedExecutionPeriods);
        request.setAttribute("executionPeriods", sortedExecutionPeriods);

        return mapping.findForward("show-choose-execution-period-page");
    }

    public ActionForward selectExecutionPeriod(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final ExecutionPeriod executionPeriod = retrieveDomainObject(form, request);
        request.setAttribute("executionPeriod", executionPeriod);
        return prepare(mapping, form, request, response);
    }

    public ActionForward downloadExecutionCourseGroupings(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final ExecutionPeriod executionPeriod = retrieveDomainObject(form, request);

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment; filename=executionCourseGroupings_" + executionPeriod.getQualifiedName().replace(' ', '_') + "_.xls");

        final ServletOutputStream servletOutputStream = response.getOutputStream();
        exportToXls(servletOutputStream, executionPeriod);
        servletOutputStream.flush();
        response.flushBuffer();

        return null;
    }

    private ExecutionPeriod retrieveDomainObject(ActionForm form, HttpServletRequest request) throws FenixFilterException, FenixServiceException {
        final DynaActionForm dynaActionForm = (DynaActionForm) form;
        final String executionPeriodIDString = (String) dynaActionForm.get("executionPeriodID");
        final Integer executionPeriodID = executionPeriodIDString != null && executionPeriodIDString.length() > 0
                ? Integer.valueOf(executionPeriodIDString) : null;

        return rootDomainObject.readExecutionPeriodByOID(executionPeriodID);
    }

    private void exportToXls(final ServletOutputStream servletOutputStream, final ExecutionPeriod executionPeriod) throws IOException {
        final List<Object> headers = getHeaders();
        final Spreadsheet spreadsheet = new Spreadsheet("Execution Course Groupings", headers);
        fillSpreadSheet(spreadsheet, executionPeriod);
        spreadsheet.exportToXLSSheet(servletOutputStream);
    }

    private List<Object> getHeaders() {
        final List<Object> headers = new ArrayList<Object>();
        headers.add("Execution Course");
        headers.add("Responsible Fors");
        headers.add("Number of Degrees");
        headers.add("Degrees");
        headers.add("Curricular Years");
        headers.add("Degree Types");
        headers.add("Emails");
        return headers;
    }

    private void fillSpreadSheet(final Spreadsheet spreadsheet, final ExecutionPeriod executionPeriod) {
        for (final ExecutionCourse executionCourse : executionPeriod.getAssociatedExecutionCourses()) {
            final Row row = spreadsheet.addRow();

            row.setCell(executionCourse.getNome());

            final StringBuilder responsibleForStringBuilder = new StringBuilder();
            final StringBuilder responsibleForEmailsStringBuilder = new StringBuilder();
            boolean isFirstResp = true;
            for (final Professorship professorship : executionCourse.getProfessorships()) {
                if (professorship.getResponsibleFor().booleanValue()) {
                    if (isFirstResp) {
                        isFirstResp = false;
                    } else {
                        responsibleForStringBuilder.append("; ");
                        responsibleForEmailsStringBuilder.append("; ");
                    }

                    final Teacher teacher = professorship.getTeacher();
                    responsibleForStringBuilder.append(teacher.getTeacherNumber().toString());

                    responsibleForStringBuilder.append(" ");

                    final Person person = teacher.getPerson();
                    responsibleForStringBuilder.append(person.getName());
                    responsibleForEmailsStringBuilder.append(person.getEmail());
                }
            }
            row.setCell(responsibleForStringBuilder.toString());

            final Map<Degree, Set<Integer>> degreeOccurenceMap = constructDegreeOccurenceMap(executionPeriod, executionCourse);

            row.setCell(Integer.toString(degreeOccurenceMap.size()));

            boolean hasDegreeDegree = false;
            boolean hasMasterDegree = false;

            final StringBuilder degreeStringBuilder = new StringBuilder();
            boolean isFirst = true;
            for (final Degree degree : degreeOccurenceMap.keySet()) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    degreeStringBuilder.append("; ");
                }

                if (degree.getTipoCurso() == DegreeType.DEGREE) {
                    hasDegreeDegree = true;
                } else if (degree.getTipoCurso() == DegreeType.MASTER_DEGREE) {
                    hasMasterDegree = true;
                }

                degreeStringBuilder.append(degree.getSigla());
            }
            row.setCell(degreeStringBuilder.toString());

            final StringBuilder degreeCurricularYearStringBuilder = new StringBuilder();
            isFirst = true;
            for (final Entry<Degree, Set<Integer>> entry : degreeOccurenceMap.entrySet()) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    degreeCurricularYearStringBuilder.append("; ");
                }

                final Degree degree = entry.getKey();
                degreeCurricularYearStringBuilder.append(degree.getSigla());
                degreeCurricularYearStringBuilder.append('(');
                boolean isFirstYear = true;
                for (final Integer curricularYearInteger : entry.getValue()) {
                    if (isFirstYear) {
                        isFirstYear = false;
                    } else {
                        degreeCurricularYearStringBuilder.append(", ");
                    }
                    degreeCurricularYearStringBuilder.append(curricularYearInteger.toString());
                }
                degreeCurricularYearStringBuilder.append(')');
            }
            row.setCell(degreeCurricularYearStringBuilder.toString());

            final StringBuilder degreeTypeStringBuilder = new StringBuilder();
            if (hasDegreeDegree) {
                degreeTypeStringBuilder.append(DegreeType.DEGREE);
            }
            if (hasDegreeDegree && hasMasterDegree) {
                degreeTypeStringBuilder.append(", ");
            }
            if (hasMasterDegree) {
                degreeTypeStringBuilder.append(DegreeType.MASTER_DEGREE);
            }
            row.setCell(degreeTypeStringBuilder.toString());

            row.setCell(responsibleForEmailsStringBuilder.toString());
        }
    }

    private Map<Degree, Set<Integer>> constructDegreeOccurenceMap(final ExecutionPeriod executionPeriod, final ExecutionCourse executionCourse) {
        final Map<Degree, Set<Integer>> degreeOccurenceMap = new HashMap<Degree, Set<Integer>>();
        for (final CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCourses()) {
            final List<CurricularCourseScope> curricularCourseScopes = curricularCourse.getActiveScopesInExecutionPeriod(executionPeriod);
            for (final CurricularCourseScope curricularCourseScope : curricularCourseScopes) {
                final Degree degree = curricularCourse.getDegreeCurricularPlan().getDegree();
                final CurricularYear curricularYear = curricularCourseScope.getCurricularSemester().getCurricularYear();

                final Set<Integer> curricularYears;
                if (degreeOccurenceMap.containsKey(degree)) {
                    curricularYears = degreeOccurenceMap.get(degree);
                } else {
                    curricularYears = new TreeSet<Integer>();
                    degreeOccurenceMap.put(degree, curricularYears);
                }

                curricularYears.add(curricularYear.getYear());
            }
        }
        return degreeOccurenceMap;
    }

}