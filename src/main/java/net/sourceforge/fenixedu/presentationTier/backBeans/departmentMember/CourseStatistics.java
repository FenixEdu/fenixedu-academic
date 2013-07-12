package net.sourceforge.fenixedu.presentationTier.backBeans.departmentMember;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.ServletOutputStream;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadCurrentExecutionPeriod;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadNotClosedExecutionYears;
import net.sourceforge.fenixedu.applicationTier.Servico.department.ComputeCompetenceCourseStatistics;
import net.sourceforge.fenixedu.applicationTier.Servico.department.ComputeDegreeCourseStatistics;
import net.sourceforge.fenixedu.applicationTier.Servico.department.ComputeExecutionCourseStatistics;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.department.CompetenceCourseStatisticsDTO;
import net.sourceforge.fenixedu.dataTransferObject.department.DegreeCourseStatisticsDTO;
import net.sourceforge.fenixedu.dataTransferObject.department.ExecutionCourseStatisticsDTO;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.studentCurriculum.BranchCurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;

import org.joda.time.DateTime;

import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

/**
 * 
 * @author pcma
 * 
 */
public class CourseStatistics extends FenixBackingBean {
    private List<CompetenceCourseStatisticsDTO> competenceCourses;

    private List<DegreeCourseStatisticsDTO> degreeCourses;

    private List<ExecutionCourseStatisticsDTO> executionCourses;

    private List<SelectItem> executionPeriods;

    private CompetenceCourse competenceCourse;

    public Department getDepartment() {
        return getUserView().getPerson().getTeacher().getLastWorkingDepartment();
    }

    public String getCompetenceCourseId() {
        return (String) this.getViewState().getAttribute("competenceCourseId");
    }

    public void setCompetenceCourseId(String competenceCourseId) {
        this.getViewState().setAttribute("competenceCourseId", competenceCourseId);
    }

    public String getDegreeId() {
        return (String) this.getViewState().getAttribute("degreeId");
    }

    public void setDegreeId(String degreeId) {
        this.getViewState().setAttribute("degreeId", degreeId);
    }

    public String getExecutionPeriodId() {
        String executionPeriodId = (String) this.getViewState().getAttribute("executionYearPeriod");

        if (executionPeriodId == null) {
            executionPeriodId = (String) getRequestAttribute("executionPeriodId");

            if (executionPeriodId == null) {
                InfoExecutionPeriod infoExecutionPeriod = ReadCurrentExecutionPeriod.run();

                if (infoExecutionPeriod == null) {
                    executionPeriodId = (String) this.getExecutionPeriods().get(this.executionPeriods.size() - 1).getValue();
                } else {
                    executionPeriodId = infoExecutionPeriod.getExternalId();
                }
            }

            this.getViewState().setAttribute("executionPeriodId", executionPeriodId);
        }

        return executionPeriodId;
    }

    public void setExecutionPeriodId(String executionPeriodId) {
        this.getViewState().setAttribute("executionPeriodId", executionPeriodId);
        setRequestAttribute("executionPeriodId", executionPeriodId);
    }

    public void onExecutionPeriodChangeForCompetenceCourses(ValueChangeEvent valueChangeEvent) throws FenixServiceException {
        setExecutionPeriodId((String) valueChangeEvent.getNewValue());
        loadCompetenceCourses();
    }

    public void onExecutionPeriodChangeForDegreeCourses(ValueChangeEvent valueChangeEvent) throws FenixServiceException {
        setExecutionPeriodId((String) valueChangeEvent.getNewValue());
        loadDegreeCourses();
    }

    public void onExecutionPeriodChangeForExecutionCourses(ValueChangeEvent valueChangeEvent) throws FenixServiceException {
        setExecutionPeriodId((String) valueChangeEvent.getNewValue());
        loadExecutionCourses();
    }

    public List<SelectItem> getExecutionPeriods() {
        if (this.executionPeriods == null) {

            List<InfoExecutionYear> executionYearsList = ReadNotClosedExecutionYears.run();
            List<SelectItem> result = new ArrayList<SelectItem>();
            for (InfoExecutionYear executionYear : executionYearsList) {
                Collection<ExecutionSemester> executionSemesters =
                        FenixFramework.<ExecutionYear> getDomainObject(executionYear.getExternalId()).getExecutionPeriods();
                for (ExecutionSemester executionSemester : executionSemesters) {
                    result.add(new SelectItem(executionSemester.getExternalId(), executionSemester.getExecutionYear().getYear()
                            + " - " + executionSemester.getName()));
                }
            }
            this.executionPeriods = result;
        }
        return this.executionPeriods;
    }

    private void loadCompetenceCourses() throws FenixServiceException {
        String departmentID = getUserView().getPerson().getTeacher().getLastWorkingDepartment().getExternalId();
        competenceCourses =
                ComputeCompetenceCourseStatistics.runComputeCompetenceCourseStatistics(departmentID, this.getExecutionPeriodId());
    }

    public List<CompetenceCourseStatisticsDTO> getCompetenceCourses() throws FenixServiceException {
        if (competenceCourses == null) {
            loadCompetenceCourses();
        }

        return competenceCourses;
    }

    private void loadDegreeCourses() throws FenixServiceException {
        degreeCourses =
                ComputeDegreeCourseStatistics.runComputeDegreeCourseStatistics(getCompetenceCourseId(), getExecutionPeriodId());
    }

    public List<DegreeCourseStatisticsDTO> getDegreeCourses() throws FenixServiceException {
        if (degreeCourses == null) {
            loadDegreeCourses();
        }

        return degreeCourses;
    }

    private void loadExecutionCourses() throws FenixServiceException {
        executionCourses =
                ComputeExecutionCourseStatistics.runComputeExecutionCourseStatistics(this.getCompetenceCourseId(),
                        this.getDegreeId(), getExecutionPeriodId());
    }

    public List<ExecutionCourseStatisticsDTO> getExecutionCourses() throws FenixServiceException {
        if (executionCourses == null) {
            loadExecutionCourses();
        }

        return executionCourses;
    }

    public void onCompetenceCourseSelect(ActionEvent event) throws FenixServiceException {

        String competenceCourseId = getRequestParameter("competenceCourseId");
        setCompetenceCourseId(competenceCourseId);
    }

    public void onDegreeCourseSelect(ActionEvent event) throws FenixServiceException {
        String degreeId = getRequestParameter("degreeId");
        setDegreeId(degreeId);
    }

    public CompetenceCourse getCompetenceCourse() {
        return competenceCourse == null ? FenixFramework.<CompetenceCourse> getDomainObject(getCompetenceCourseId()) : competenceCourse;
    }

    private ResourceBundle getApplicationResources() {
        return getResourceBundle("resources/ApplicationResources");
    }

    /*
     * Export curricular course students
     */
    private CurricularCourse getCurricularCourseToExport() {

        final CompetenceCourse cc = getCompetenceCourse();
        final Degree degree = FenixFramework.getDomainObject(getDegreeId());

        for (final CurricularCourse curricularCourse : cc.getAssociatedCurricularCourses()) {
            if (curricularCourse.getDegree().equals(degree)) {
                return curricularCourse;
            }
        }

        return null;
    }

    public void exportStudentsToExcel() throws FenixServiceException {
        try {
            exportToXls(getFilename());
        } catch (IOException e) {
            throw new FenixServiceException();
        }
    }

    private String getFilename() {
        final ResourceBundle bundle = getApplicationResources();
        return String.format("%s_%s_%s.xls", new DateTime().toString("dd-MM-yyyy_HH:mm"), bundle.getString("label.students"),
                getCurricularCourseToExport().getName().replaceAll(" ", "_"));
    }

    private void exportToXls(String filename) throws IOException {
        this.getResponse().setContentType("application/vnd.ms-excel");
        this.getResponse().setHeader("Content-disposition", "attachment; filename=" + filename);
        ServletOutputStream outputStream = this.getResponse().getOutputStream();

        final Spreadsheet spreadsheet = createSpreadsheet();
        reportInfo(spreadsheet);

        spreadsheet.exportToXLSSheet(outputStream);
        outputStream.flush();

        this.getResponse().flushBuffer();
        FacesContext.getCurrentInstance().responseComplete();
    }

    private Spreadsheet createSpreadsheet() {
        return new Spreadsheet("-", getStudentsEnroledListHeaders());
    }

    private List<Object> getStudentsEnroledListHeaders() {
        final ResourceBundle bundle = getApplicationResources();

        final List<Object> headers = new ArrayList<Object>(8);
        headers.add(bundle.getString("label.student.number"));
        headers.add(bundle.getString("label.student.degree"));
        headers.add(bundle.getString("label.student.curricularCourse"));
        headers.add(bundle.getString("label.executionYear"));
        headers.add(bundle.getString("label.student.main.branch"));
        headers.add(bundle.getString("label.student.minor.branch"));
        headers.add(bundle.getString("label.student.number.of.enrolments"));
        return headers;
    }

    private void reportInfo(Spreadsheet spreadsheet) {
        final ExecutionYear executionYear = getExecutionYear();
        final CurricularCourse curricularCourse = getCurricularCourseToExport();

        for (final Enrolment enrolment : curricularCourse.getEnrolments()) {

            if (!enrolment.isValid(executionYear)) {
                continue;
            }

            final Row row = spreadsheet.addRow();

            row.setCell(enrolment.getStudent().getNumber());
            row.setCell(enrolment.getDegreeCurricularPlanOfStudent().getDegree().getPresentationName());
            row.setCell(enrolment.getName().getContent());
            row.setCell(enrolment.getExecutionYear().getName());

            final CycleCurriculumGroup cycle = enrolment.getParentCycleCurriculumGroup();

            final BranchCurriculumGroup major = cycle == null ? null : cycle.getMajorBranchCurriculumGroup();
            row.setCell(major != null ? major.getName().getContent() : "");

            final BranchCurriculumGroup minor = cycle == null ? null : cycle.getMinorBranchCurriculumGroup();
            row.setCell(minor != null ? minor.getName().getContent() : "");

            row.setCell(getNumberOfEnrolments(enrolment));
        }
    }

    private String getNumberOfEnrolments(final Enrolment enrolment) {
        return String.valueOf(enrolment.getStudentCurricularPlan().getEnrolments(enrolment.getCurricularCourse()).size());
    }

    private ExecutionYear getExecutionYear() {
        return FenixFramework.<ExecutionSemester> getDomainObject(getExecutionPeriodId()).getExecutionYear();
    }

    /*
     * End of export curricular course students
     */

}