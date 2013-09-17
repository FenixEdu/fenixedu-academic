package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.pedagogicalCouncil.NumberBean;
import net.sourceforge.fenixedu.dataTransferObject.teacher.tutor.PerformanceGridTableDTO;
import net.sourceforge.fenixedu.dataTransferObject.teacher.tutor.PerformanceGridTableDTO.PerformanceGridLine;
import net.sourceforge.fenixedu.dataTransferObject.teacher.tutor.PerformanceGridTableDTO.PerformanceGridLine.PerformanceGridLineYearGroup;
import net.sourceforge.fenixedu.dataTransferObject.teacher.tutor.StudentsPerformanceInfoBean;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.tutor.ViewStudentsPerformanceGridDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.spreadsheet.SheetData;
import pt.utl.ist.fenix.tools.spreadsheet.SpreadsheetBuilder;
import pt.utl.ist.fenix.tools.spreadsheet.WorkbookExportFormat;
import pt.utl.ist.fenix.tools.util.i18n.Language;

@Mapping(path = "/tutorStudentsPerformanceGrid", module = "pedagogicalCouncil")
@Forwards({ @Forward(name = "viewStudentsPerformanceGrid",
        path = "/pedagogicalCouncil/tutorship/showStudentsPerformanceGrid.jsp", tileProperties = @Tile(
                title = "private.pedagogiccouncil.tutoring.viewperformancegrids")) })
public class TutorStudentsPerformanceGridDA extends ViewStudentsPerformanceGridDispatchAction {

    public ActionForward prepareTutorSearch(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("tutorateBean", new TutorSearchBean());
        return mapping.findForward("viewStudentsPerformanceGrid");
    }

    public ActionForward viewStudentsPerformanceGrid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        TutorSearchBean bean = (TutorSearchBean) getRenderedObject("tutorateBean");
        request.setAttribute("tutorateBean", bean);
        if (bean.getTeacher() != null) {
            Person person = bean.getTeacher().getPerson();
            generateStudentsPerformanceBean(request, person);
            request.setAttribute("tutor", person);
            RenderUtils.invalidateViewState();
            return prepareStudentsPerformanceGrid(mapping, actionForm, request, response, person);
        } else {
            RenderUtils.invalidateViewState();
            return mapping.findForward("viewStudentsPerformanceGrid");
        }
    }

    public ActionForward exportXls(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String tutorId = request.getParameter("tutorOID");
        Person person = (Person) FenixFramework.getDomainObject(tutorId);
        NumberBean numberBean = new NumberBean();
        numberBean.setId(person.getIstUsername());
        request.setAttribute("tutorateBean", numberBean);
        generateStudentsPerformanceBeanFromRequest(request, person);
        prepareStudentsPerformanceGrid(mapping, actionForm, request, response, person);
        final PerformanceGridTableDTO performanceGridTable =
                (PerformanceGridTableDTO) request.getAttribute("performanceGridTable");
        SheetData<PerformanceGridLine> builder =
                new SheetData<PerformanceGridLine>(performanceGridTable.getPerformanceGridTableLines()) {
                    @Override
                    protected void makeLine(PerformanceGridLine item) {
                        final ResourceBundle bundle =
                                ResourceBundle.getBundle("resources.ApplicationResources", Language.getLocale());
                        Registration registration = item.getRegistration();
                        addCell(bundle.getString("label.studentNumber"), registration.getNumber());
                        addCell(bundle.getString("label.name"), registration.getPerson().getName());
                        addCell(bundle.getString("label.entryPhase"), registration.getEntryPhase() != null ? registration
                                .getEntryPhase().ordinal() + 1 : null);
                        addCell(bundle.getString("label.entryGrade"), registration.getEntryGrade());
                        addCell(bundle.getString("label.aritmeticAverage"), item.getAritmeticAverage());
                        addCell(bundle.getString("label.approvedRatio"), (short) 2,
                                bundle.getString("label.first.semester.short"), (short) 1, item.getApprovedRatioFirstSemester()
                                        + " %", (short) 1);
                        addCell(bundle.getString("label.second.semester.short"), item.getApprovedRatioSecondSemester() + " %");
                        ExecutionYear monitoringYear = performanceGridTable.getMonitoringYear();
                        int yearCount = item.getStudentPerformanceByYear().size();
                        for (int year = 0; year < yearCount; year++) {
                            addCell(MessageFormat.format(bundle.getString("label.performanceGrid.year.tutorated"), year + 1,
                                    monitoringYear.getName()), (short) 2, bundle.getString("label.first.semester.short"),
                                    (short) 1, addSemesterCell(item, monitoringYear, year, 1, true), (short) 1);
                            addCell(bundle.getString("label.second.semester.short"),
                                    addSemesterCell(item, monitoringYear, year, 2, true));
                            addCell(MessageFormat.format(bundle.getString("label.performanceGrid.year.notTutorated"), year + 1),
                                    (short) 2, bundle.getString("label.first.semester.short"), (short) 1,
                                    addSemesterCell(item, monitoringYear, year, 1, false), (short) 1);
                            addCell(bundle.getString("label.second.semester.short"),
                                    addSemesterCell(item, monitoringYear, year, 2, false));
                        }
                    }

                    private Object addSemesterCell(PerformanceGridLine item, ExecutionYear monitoringYear, int year, int sem,
                            boolean tutorated) {
                        PerformanceGridLineYearGroup yearEnrols = item.getStudentPerformanceByYear().get(year);
                        List enrols;
                        if (sem == 1) {
                            enrols = yearEnrols.getFirstSemesterEnrolments();
                        } else {
                            enrols = yearEnrols.getSecondSemesterEnrolments();
                        }

                        int notApproved = 0;
                        int approved = 0;
                        int notEvaluated = 0;
                        for (Object object : enrols) {
                            Enrolment enrolment = (Enrolment) object;
                            if (tutorated && !enrolment.getExecutionYear().equals(monitoringYear)) {
                                continue;
                            }
                            if (!tutorated && enrolment.getExecutionYear().equals(monitoringYear)) {
                                continue;
                            }
                            if (enrolment.getEnrollmentState().equals(EnrollmentState.NOT_APROVED)) {
                                notApproved++;
                            } else if (enrolment.getEnrollmentState().equals(EnrollmentState.APROVED)) {
                                approved++;
                            } else if (enrolment.getEnrollmentState().equals(EnrollmentState.ENROLLED)
                                    || enrolment.getEnrollmentState().equals(EnrollmentState.NOT_EVALUATED)) {
                                notEvaluated++;
                            }
                        }
                        return notEvaluated + "/" + approved + "/" + notApproved;
                    }
                };
        response.setContentType("text/plain");
        response.setHeader("Content-disposition", "attachment; filename=" + person.getIstUsername() + "-students-performance.xls");
        new SpreadsheetBuilder().addSheet(person.getIstUsername() + "-students-performance.xls", builder).build(
                WorkbookExportFormat.EXCEL, response.getOutputStream());
        response.flushBuffer();
        return null;
    }

    @Override
    public ActionForward prepareAllStudentsStatistics(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String tutorId = request.getParameter("tutorOID");
        Person person = (Person) FenixFramework.getDomainObject(tutorId);
        NumberBean numberBean = new NumberBean();
        numberBean.setId(person.getIstUsername());
        request.setAttribute("tutorateBean", numberBean);
        StudentsPerformanceInfoBean bean = generateStudentsPerformanceBeanFromRequest(request, person);
        if (!bean.getTutorships().isEmpty()) {

            List<DegreeCurricularPlan> plans = new ArrayList<DegreeCurricularPlan>(bean.getDegree().getDegreeCurricularPlans());
            Collections.sort(plans,
                    DegreeCurricularPlan.DEGREE_CURRICULAR_PLAN_COMPARATOR_BY_DEGREE_TYPE_AND_EXECUTION_DEGREE_AND_DEGREE_CODE);

            List<StudentCurricularPlan> students =
                    plans.iterator().next().getStudentsCurricularPlanGivenEntryYear(bean.getStudentsEntryYear());

            putAllStudentsStatisticsInTheRequest(request, students, bean.getCurrentMonitoringYear());

            request.setAttribute("entryYear", bean.getStudentsEntryYear());
            request.setAttribute("totalEntryStudents", students.size());
        }
        return prepareStudentsPerformanceGrid(mapping, actionForm, request, response, person);
    }
}
