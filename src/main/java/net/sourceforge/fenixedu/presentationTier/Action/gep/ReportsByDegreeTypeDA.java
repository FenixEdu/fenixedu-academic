package net.sourceforge.fenixedu.presentationTier.Action.gep;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.QueueJob;
import net.sourceforge.fenixedu.domain.ReportFileFactory;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.reports.CourseLoadAndResponsiblesReportFile;
import net.sourceforge.fenixedu.domain.reports.CourseLoadReportFile;
import net.sourceforge.fenixedu.domain.reports.DissertationsProposalsReportFile;
import net.sourceforge.fenixedu.domain.reports.DissertationsWithExternalAffiliationsReportFile;
import net.sourceforge.fenixedu.domain.reports.EctsLabelCurricularCourseReportFile;
import net.sourceforge.fenixedu.domain.reports.EctsLabelDegreeReportFile;
import net.sourceforge.fenixedu.domain.reports.EffectiveTeachingLoadReportFile;
import net.sourceforge.fenixedu.domain.reports.EtiReportFile;
import net.sourceforge.fenixedu.domain.reports.EurAceReportFile;
import net.sourceforge.fenixedu.domain.reports.FlunkedReportFile;
import net.sourceforge.fenixedu.domain.reports.GepReportFile;
import net.sourceforge.fenixedu.domain.reports.GraduationReportFile;
import net.sourceforge.fenixedu.domain.reports.PublicationReportFile;
import net.sourceforge.fenixedu.domain.reports.RaidesDfaReportFile;
import net.sourceforge.fenixedu.domain.reports.RaidesGraduationReportFile;
import net.sourceforge.fenixedu.domain.reports.RaidesPhdReportFile;
import net.sourceforge.fenixedu.domain.reports.RaidesSpecializationReportFile;
import net.sourceforge.fenixedu.domain.reports.RegistrationReportFile;
import net.sourceforge.fenixedu.domain.reports.StatusAndApprovalReportFile;
import net.sourceforge.fenixedu.domain.reports.SummaryOccupancyReportFile;
import net.sourceforge.fenixedu.domain.reports.TeacherCreditsReportFile;
import net.sourceforge.fenixedu.domain.reports.TeachersByShiftReportFile;
import net.sourceforge.fenixedu.domain.reports.TeachersListFromGiafReportFile;
import net.sourceforge.fenixedu.domain.reports.TimetablesReportFile;
import net.sourceforge.fenixedu.domain.reports.TutorshipProgramReportFile;
import net.sourceforge.fenixedu.domain.reports.WrittenEvaluationReportFile;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.StringUtils;

import org.apache.commons.collections.Predicate;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixframework.FenixFramework;

@Mapping(module = "gep", path = "/reportsByDegreeType", scope = "session", parameter = "method")
@Forwards(value = {
        @Forward(name = "selectDegreeType", path = "/gep/reportsByDegreeType.jsp", tileProperties = @Tile(
                title = "private.gep.gepportal.reportsbydegreetype")),
        @Forward(name = "viewReports", path = "/gep/viewReports.jsp", tileProperties = @Tile(
                title = "private.gep.gepportal.reportsbydegreetype")) })
public class ReportsByDegreeTypeDA extends FenixDispatchAction {

    private static final int MAX_AUTHORIZED_REPORT_FILES = 20;

    public static class ReportBean implements Serializable {
        private DegreeType degreeType;

        private ExecutionYear executionYearReference;

        public DegreeType getDegreeType() {
            return degreeType;
        }

        public void setDegreeType(DegreeType degreeType) {
            this.degreeType = degreeType;
        }

        public ExecutionYear getExecutionYear() {
            return executionYearReference;
        }

        public String getExecutionYearOID() {
            return getExecutionYear() == null ? null : getExecutionYear().getExternalId();
        }

        public void setExecutionYear(final ExecutionYear executionYear) {
            executionYearReference = executionYear;
        }

        public ExecutionYear getExecutionYearFourYearsBack() {
            final ExecutionYear executionYear = getExecutionYear();
            return executionYear == null ? null : ReportsByDegreeTypeDA.getExecutionYearFourYearsBack(executionYear);
        }
    }

    public static ExecutionYear getExecutionYearFourYearsBack(final ExecutionYear executionYear) {
        ExecutionYear executionYearFourYearsBack = executionYear;
        if (executionYear != null) {
            for (int i = 5; i > 1; i--) {
                final ExecutionYear previousExecutionYear = executionYearFourYearsBack.getPreviousExecutionYear();
                if (previousExecutionYear != null) {
                    executionYearFourYearsBack = previousExecutionYear;
                }
            }
        }
        return executionYearFourYearsBack;
    }

    public List<QueueJob> getLatestJobs() {
        return QueueJob.getAllJobsForClassOrSubClass(GepReportFile.class, 5);
    }

    @SuppressWarnings("unused")
    public ActionForward selectDegreeType(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        ReportBean reportBean = getRenderedObject();

        DegreeType degreeType;
        ExecutionYear executionYear;
        if (reportBean == null) {
            degreeType = getDegreeType(request);
            executionYear = getExecutionYear(request);
            reportBean = new ReportBean();
            reportBean.setDegreeType(degreeType);
            reportBean.setExecutionYear(executionYear);
        } else {
            degreeType = reportBean.getDegreeType();
            executionYear = reportBean.getExecutionYear();
        }
        RenderUtils.invalidateViewState();

        request.setAttribute("reportBean", reportBean);
        request.setAttribute("queueJobList", getLatestJobs());
        countReports(request, degreeType, executionYear);
        return mapping.findForward("selectDegreeType");
    }

    private void countReports(HttpServletRequest request, DegreeType degreeType, ExecutionYear executionYear) {
        for (Integer i = 1;; ++i) {
            Class aClass = getClassForParameter(i.toString());
            if (aClass == null) {
                break;
            }
            request.setAttribute("numberOfReportsType" + i, getCountReportsForParameters(degreeType, executionYear, aClass));
        }
    }

    private int getCountReportsForParameters(DegreeType degreeType, ExecutionYear executionYear, Class reportClass) {
        Predicate predicate = new FindSelectedGepReports(executionYear, degreeType, reportClass);

        List<GepReportFile> selectedJobs =
                (List<GepReportFile>) org.apache.commons.collections.CollectionUtils.select(rootDomainObject.getQueueJobSet(),
                        predicate);

        return getValidCounterForReports(selectedJobs.size());
    }

    private int getValidCounterForReports(int totalCounter) {
        if (totalCounter > MAX_AUTHORIZED_REPORT_FILES) {
            return MAX_AUTHORIZED_REPORT_FILES;
        } else {
            return totalCounter;
        }
    }

    public ActionForward cancelQueuedJob(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        cancelQueuedJob(request);
        return selectDegreeType(mapping, actionForm, request, response);
    }

    public ActionForward cancelQueuedJobFromViewReports(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        cancelQueuedJob(request);
        return viewReports(mapping, actionForm, request, response);
    }

    private void cancelQueuedJob(HttpServletRequest request) {
        QueueJob job = getDomainObject(request, "id");
        job.cancel();
    }

    public ActionForward resendJob(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        resendJob(request);
        return selectDegreeType(mapping, actionForm, request, response);
    }

    public ActionForward resendJobFromViewReports(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        resendJob(request);
        return viewReports(mapping, actionForm, request, response);
    }

    private void resendJob(HttpServletRequest request) {
        QueueJob job = getDomainObject(request, "id");
        job.resend();
    }

    private boolean isRepeatedJob(Person person, HttpServletRequest request, Class aClass) {
        final DegreeType degreeType = getDegreeType(request);
        request.setAttribute("degreeType", degreeType);
        final ExecutionYear executionYear = getExecutionYear(request);
        request.setAttribute("executionYearID", (executionYear == null) ? null : executionYear.getExternalId());
        final String fileType = getFileType(request);
        for (QueueJob queueJob : QueueJob.getAllJobsForClassOrSubClass(GepReportFile.class, 5)) {
            GepReportFile gepReportFile = (GepReportFile) queueJob;
            if ((gepReportFile.getPerson() == person) && (gepReportFile.getClass() == aClass) && (!gepReportFile.getDone())
                    && (gepReportFile.getExecutionYear() == executionYear) && (gepReportFile.getDegreeType() == degreeType)
                    && (fileType.equals(gepReportFile.getType()))) {
                return true;
            }
        }
        return false;
    }

    private String getFileType(final HttpServletRequest httpServletRequest) {
        return httpServletRequest.getParameter("format");
    }

    private DegreeType getDegreeType(final HttpServletRequest httpServletRequest) {
        final String degreeTypeString = httpServletRequest.getParameter("degreeType");
        return StringUtils.isEmpty(degreeTypeString) ? null : DegreeType.valueOf(degreeTypeString);
    }

    private ExecutionYear getExecutionYear(final HttpServletRequest httpServletRequest) {
        final String OIDString = httpServletRequest.getParameter("executionYearID");
        return StringUtils.isEmpty(OIDString) ? null : FenixFramework.<ExecutionYear> getDomainObject(OIDString);
    }

    private String getFormat(final HttpServletRequest httpServletRequest) {
        return httpServletRequest.getParameter("format");
    }

    @SuppressWarnings("unused")
    public ActionForward downloadEurAce(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        if (isRepeatedJob(AccessControl.getPerson(), request, getClassForParameter(request.getParameter("type")))) {
            return selectDegreeType(mapping, actionForm, request, response);
        }
        final DegreeType degreeType = getDegreeType(request);
        final ExecutionYear executionYear = getExecutionYear(request);
        final String format = getFormat(request);

        prepareNewJobResponse(request, ReportFileFactory.createEurAceReportFile(format, degreeType, executionYear));

        return selectDegreeType(mapping, actionForm, request, response);
    }

    @SuppressWarnings("unused")
    public ActionForward downloadEctsLabelForCurricularCourses(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (isRepeatedJob(AccessControl.getPerson(), request, getClassForParameter(request.getParameter("type")))) {
            return selectDegreeType(mapping, actionForm, request, response);
        }

        final DegreeType degreeType = getDegreeType(request);
        final ExecutionYear executionYear = getExecutionYear(request);
        final String format = getFormat(request);

        prepareNewJobResponse(request,
                ReportFileFactory.createEctsLabelCurricularCourseReportFile(format, degreeType, executionYear));

        return selectDegreeType(mapping, actionForm, request, response);
    }

    @SuppressWarnings("unused")
    public ActionForward downloadEctsLabelForDegrees(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        if (isRepeatedJob(AccessControl.getPerson(), request, getClassForParameter(request.getParameter("type")))) {
            return selectDegreeType(mapping, actionForm, request, response);
        }
        final DegreeType degreeType = getDegreeType(request);
        final ExecutionYear executionYear = getExecutionYear(request);
        final String format = getFormat(request);

        prepareNewJobResponse(request, ReportFileFactory.createEctsLabelDegreeReportFile(format, degreeType, executionYear));

        return selectDegreeType(mapping, actionForm, request, response);
    }

    @SuppressWarnings("unused")
    public ActionForward downloadStatusAndAproval(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        if (isRepeatedJob(AccessControl.getPerson(), request, getClassForParameter(request.getParameter("type")))) {
            return selectDegreeType(mapping, actionForm, request, response);
        }
        final DegreeType degreeType = getDegreeType(request);
        final ExecutionYear executionYear = getExecutionYear(request);
        final String format = getFormat(request);

        prepareNewJobResponse(request, ReportFileFactory.createStatusAndApprovalReportFile(format, degreeType, executionYear));

        return selectDegreeType(mapping, actionForm, request, response);
    }

    @SuppressWarnings("unused")
    public ActionForward downloadEti(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        if (isRepeatedJob(AccessControl.getPerson(), request, getClassForParameter(request.getParameter("type")))) {
            return selectDegreeType(mapping, actionForm, request, response);
        }
        final DegreeType degreeType = getDegreeType(request);
        final ExecutionYear executionYear = getExecutionYear(request);
        final String format = getFormat(request);

        prepareNewJobResponse(request, ReportFileFactory.createEtiReportFile(format, degreeType, executionYear));

        return selectDegreeType(mapping, actionForm, request, response);
    }

    @SuppressWarnings("unused")
    public ActionForward downloadCourseLoadAndResponsibles(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (isRepeatedJob(AccessControl.getPerson(), request, getClassForParameter(request.getParameter("type")))) {
            return selectDegreeType(mapping, actionForm, request, response);
        }
        final DegreeType degreeType = getDegreeType(request);
        final ExecutionYear executionYear = getExecutionYear(request);
        final String format = getFormat(request);

        prepareNewJobResponse(request,
                ReportFileFactory.createCourseLoadAndResponsiblesReportFile(format, degreeType, executionYear));

        return selectDegreeType(mapping, actionForm, request, response);
    }

    @SuppressWarnings("unused")
    public ActionForward downloadRegistrations(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        if (isRepeatedJob(AccessControl.getPerson(), request, getClassForParameter(request.getParameter("type")))) {
            return selectDegreeType(mapping, actionForm, request, response);
        }
        final DegreeType degreeType = getDegreeType(request);
        final ExecutionYear executionYear = getExecutionYear(request);
        final String format = getFormat(request);

        prepareNewJobResponse(request, ReportFileFactory.createRegistrationReportFile(format, degreeType, executionYear));

        return selectDegreeType(mapping, actionForm, request, response);
    }

    @SuppressWarnings("unused")
    public ActionForward downloadFlunked(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        if (isRepeatedJob(AccessControl.getPerson(), request, getClassForParameter(request.getParameter("type")))) {
            return selectDegreeType(mapping, actionForm, request, response);
        }
        final DegreeType degreeType = getDegreeType(request);
        final ExecutionYear executionYear = getExecutionYear(request);
        final String format = getFormat(request);

        prepareNewJobResponse(request, ReportFileFactory.createFlunkedReportFile(format, degreeType, executionYear));

        return selectDegreeType(mapping, actionForm, request, response);
    }

    @SuppressWarnings("unused")
    public ActionForward downloadTeachersListFromGiaf(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        if (isRepeatedJob(AccessControl.getPerson(), request, getClassForParameter(request.getParameter("type")))) {
            return selectDegreeType(mapping, actionForm, request, response);
        }
        final DegreeType degreeType = getDegreeType(request);
        final ExecutionYear executionYear = getExecutionYear(request);
        final String format = getFormat(request);

        prepareNewJobResponse(request, ReportFileFactory.createTeachersListFromGiafReportFile(format, degreeType, executionYear));

        return selectDegreeType(mapping, actionForm, request, response);
    }

    @SuppressWarnings("unused")
    public ActionForward downloadTimetables(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        if (isRepeatedJob(AccessControl.getPerson(), request, getClassForParameter(request.getParameter("type")))) {
            return selectDegreeType(mapping, actionForm, request, response);
        }
        final DegreeType degreeType = getDegreeType(request);
        final ExecutionYear executionYear = getExecutionYear(request);
        final String format = getFormat(request);

        prepareNewJobResponse(request, ReportFileFactory.createTimetablesReportFile(format, degreeType, executionYear));

        return selectDegreeType(mapping, actionForm, request, response);
    }

    @SuppressWarnings("unused")
    public ActionForward downloadDissertationsProposals(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        if (isRepeatedJob(AccessControl.getPerson(), request, getClassForParameter(request.getParameter("type")))) {
            return selectDegreeType(mapping, actionForm, request, response);
        }
        final DegreeType degreeType = getDegreeType(request);
        final ExecutionYear executionYear = getExecutionYear(request);
        final String format = getFormat(request);

        prepareNewJobResponse(request,
                ReportFileFactory.createDissertationsProposalsReportFile(format, degreeType, executionYear));

        return selectDegreeType(mapping, actionForm, request, response);
    }

    @SuppressWarnings("unused")
    public ActionForward downloadSummaries(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        if (isRepeatedJob(AccessControl.getPerson(), request, getClassForParameter(request.getParameter("type")))) {
            return selectDegreeType(mapping, actionForm, request, response);
        }
        final ExecutionYear executionYear = getExecutionYear(request);
        final String format = getFormat(request);

        prepareNewJobResponse(request, ReportFileFactory.createSummaryOccupancyReportFile(format, executionYear));

        return selectDegreeType(mapping, actionForm, request, response);
    }

    @SuppressWarnings("unused")
    public ActionForward downloadWrittenEvaluations(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        if (isRepeatedJob(AccessControl.getPerson(), request, getClassForParameter(request.getParameter("type")))) {
            return selectDegreeType(mapping, actionForm, request, response);
        }
        final ExecutionYear executionYear = getExecutionYear(request);
        final String format = getFormat(request);

        prepareNewJobResponse(request, ReportFileFactory.createWrittenEvaluationReportFile(format, executionYear));

        return selectDegreeType(mapping, actionForm, request, response);
    }

    @SuppressWarnings("unused")
    public ActionForward downloadDissertationsWithExternalAffiliations(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (isRepeatedJob(AccessControl.getPerson(), request, getClassForParameter(request.getParameter("type")))) {
            return selectDegreeType(mapping, actionForm, request, response);
        }
        final DegreeType degreeType = getDegreeType(request);
        final ExecutionYear executionYear = getExecutionYear(request);
        final String format = getFormat(request);

        prepareNewJobResponse(request,
                ReportFileFactory.createDissertationsWithExternalAffiliationsReportFile(format, degreeType, executionYear));

        return selectDegreeType(mapping, actionForm, request, response);
    }

    @SuppressWarnings("unused")
    public ActionForward downloadGraduations(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        if (isRepeatedJob(AccessControl.getPerson(), request, getClassForParameter(request.getParameter("type")))) {
            return selectDegreeType(mapping, actionForm, request, response);
        }
        final DegreeType degreeType = getDegreeType(request);
        final ExecutionYear executionYear = getExecutionYear(request);
        final String format = getFormat(request);

        prepareNewJobResponse(request, ReportFileFactory.createGraduationReportFile(format, degreeType, executionYear));

        return selectDegreeType(mapping, actionForm, request, response);
    }

    @SuppressWarnings("unused")
    public ActionForward downloadPublications(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        if (isRepeatedJob(AccessControl.getPerson(), request, getClassForParameter(request.getParameter("type")))) {
            return selectDegreeType(mapping, actionForm, request, response);
        }
        final String format = getFormat(request);

        prepareNewJobResponse(request, ReportFileFactory.createPublicationsReportFile(format));

        return selectDegreeType(mapping, actionForm, request, response);
    }

    @SuppressWarnings("unused")
    public ActionForward downloadTeachersByShift(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        if (isRepeatedJob(AccessControl.getPerson(), request, getClassForParameter(request.getParameter("type")))) {
            return selectDegreeType(mapping, actionForm, request, response);
        }
        final DegreeType degreeType = getDegreeType(request);
        final ExecutionYear executionYear = getExecutionYear(request);
        final String format = getFormat(request);

        prepareNewJobResponse(request, ReportFileFactory.createTeachersByShiftReportFile(format, degreeType, executionYear));

        return selectDegreeType(mapping, actionForm, request, response);
    }

    @SuppressWarnings("unused")
    public ActionForward downloadCourseLoads(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        if (isRepeatedJob(AccessControl.getPerson(), request, getClassForParameter(request.getParameter("type")))) {
            return selectDegreeType(mapping, actionForm, request, response);
        }
        final DegreeType degreeType = getDegreeType(request);
        final ExecutionYear executionYear = getExecutionYear(request);
        final String format = getFormat(request);

        prepareNewJobResponse(request, ReportFileFactory.createCourseLoadReportFile(format, degreeType, executionYear));

        return selectDegreeType(mapping, actionForm, request, response);
    }

    @SuppressWarnings("unused")
    public ActionForward downloadTutorshipProgram(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        if (isRepeatedJob(AccessControl.getPerson(), request, getClassForParameter(request.getParameter("type")))) {
            return selectDegreeType(mapping, actionForm, request, response);
        }
        final DegreeType degreeType = getDegreeType(request);
        final ExecutionYear executionYear = getExecutionYear(request);
        final String format = getFormat(request);

        prepareNewJobResponse(request, ReportFileFactory.createTutorshipProgramReportFile(format, degreeType, executionYear));

        return selectDegreeType(mapping, actionForm, request, response);
    }

    public ActionForward downloadRaidesGraduation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        if (isRepeatedJob(AccessControl.getPerson(), request, getClassForParameter(request.getParameter("type")))) {
            return selectDegreeType(mapping, actionForm, request, response);
        }
        prepareNewJobResponse(request, ReportFileFactory.createRaidesGraduationReportFile(getFormat(request),
                getDegreeType(request), getExecutionYear(request)));
        return selectDegreeType(mapping, actionForm, request, response);
    }

    public ActionForward downloadRaidesDfa(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        if (isRepeatedJob(AccessControl.getPerson(), request, getClassForParameter(request.getParameter("type")))) {
            return selectDegreeType(mapping, actionForm, request, response);
        }
        prepareNewJobResponse(request, ReportFileFactory.createRaidesDfaReportFile(getFormat(request), getDegreeType(request),
                getExecutionYear(request)));
        return selectDegreeType(mapping, actionForm, request, response);
    }

    public ActionForward downloadRaidesPhd(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        if (isRepeatedJob(AccessControl.getPerson(), request, getClassForParameter(request.getParameter("type")))) {
            return selectDegreeType(mapping, actionForm, request, response);
        }
        prepareNewJobResponse(request, ReportFileFactory.createRaidesPhdReportFile(getFormat(request), getDegreeType(request),
                getExecutionYear(request)));
        return selectDegreeType(mapping, actionForm, request, response);
    }

    public ActionForward downloadRaidesSpecialization(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        if (isRepeatedJob(AccessControl.getPerson(), request, getClassForParameter(request.getParameter("type")))) {
            return selectDegreeType(mapping, actionForm, request, response);
        }
        prepareNewJobResponse(request, ReportFileFactory.createRaidesSpecializationReportFile(getFormat(request),
                getDegreeType(request), getExecutionYear(request)));
        return selectDegreeType(mapping, actionForm, request, response);
    }

    public ActionForward downloadTeacherCreditsReportFile(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (isRepeatedJob(AccessControl.getPerson(), request, getClassForParameter(request.getParameter("type")))) {
            return selectDegreeType(mapping, actionForm, request, response);
        }
        final DegreeType degreeType = getDegreeType(request);
        final ExecutionYear executionYear = getExecutionYear(request);
        final String format = getFormat(request);

        prepareNewJobResponse(request, ReportFileFactory.createTeacherCreditsReportFile(format, degreeType, executionYear));

        return selectDegreeType(mapping, actionForm, request, response);
    }

    public ActionForward downloadEffectiveTeachingLoadReportFile(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (isRepeatedJob(AccessControl.getPerson(), request, getClassForParameter(request.getParameter("type")))) {
            return selectDegreeType(mapping, actionForm, request, response);
        }
        final DegreeType degreeType = getDegreeType(request);
        final ExecutionYear executionYear = getExecutionYear(request);
        final String format = getFormat(request);

        prepareNewJobResponse(request, ReportFileFactory.createEffectiveTeachingLoadReportFile(format, degreeType, executionYear));

        return selectDegreeType(mapping, actionForm, request, response);
    }

    private void prepareNewJobResponse(HttpServletRequest request, GepReportFile job) {

        ReportBean reportBean = getRenderedObject();
        if (reportBean == null) {
            reportBean = new ReportBean();
            reportBean.setDegreeType(job.getDegreeType());
            reportBean.setExecutionYear(job.getExecutionYear());
        }
        RenderUtils.invalidateViewState();

        request.setAttribute("job", job);
        request.setAttribute("reportBean", reportBean);
        request.setAttribute("queueJobList", getLatestJobs());
    }

    public Class getClassForParameter(String type) {
        int i = Integer.valueOf(type);
        switch (i) {
        case 1:
            return EurAceReportFile.class;
        case 2:
            return EctsLabelDegreeReportFile.class;
        case 3:
            return EctsLabelCurricularCourseReportFile.class;
        case 4:
            return StatusAndApprovalReportFile.class;
        case 5:
            return EtiReportFile.class;
        case 6:
            return RegistrationReportFile.class;
        case 7:
            return FlunkedReportFile.class;
        case 8:
            return TeachersByShiftReportFile.class;
        case 9:
            return CourseLoadReportFile.class;
        case 10:
            return GraduationReportFile.class;
        case 11:
            return DissertationsWithExternalAffiliationsReportFile.class;
        case 12:
            return DissertationsProposalsReportFile.class;
        case 13:
            return RaidesGraduationReportFile.class;
        case 14:
            return RaidesDfaReportFile.class;
        case 15:
            return RaidesPhdReportFile.class;
        case 16:
            return TutorshipProgramReportFile.class;
        case 17:
            return TeachersListFromGiafReportFile.class;
        case 18:
            return CourseLoadAndResponsiblesReportFile.class;
        case 19:
            return TimetablesReportFile.class;
        case 20:
            return RaidesSpecializationReportFile.class;
        case 21:
            return SummaryOccupancyReportFile.class;
        case 22:
            return WrittenEvaluationReportFile.class;
        case 23:
            return TeacherCreditsReportFile.class;
        case 24:
            return EffectiveTeachingLoadReportFile.class;
        case 25:
            return PublicationReportFile.class;
        default:
            return null;
        }
    }

    public static class FindSelectedGepReports implements Predicate {

        ExecutionYear executionYear;

        DegreeType degreeType;

        Class reportClass;

        int elements = 0;

        public FindSelectedGepReports(ExecutionYear executionYear, DegreeType degreeType, Class reportClass) {
            this.executionYear = executionYear;
            this.degreeType = degreeType;
            this.reportClass = reportClass;
        }

        @Override
        public boolean evaluate(Object object) {
            QueueJob queueJob = (QueueJob) object;
            try {
                GepReportFile gepReportFile = (GepReportFile) queueJob;
                if (gepReportFile.getClass() == this.reportClass) {
                    if (this.executionYear == gepReportFile.getExecutionYear()
                            && this.degreeType == gepReportFile.getDegreeType() && elements < MAX_AUTHORIZED_REPORT_FILES) {
                        elements++;
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } catch (ClassCastException E) {
                return false;
            }

        }
    }

    public ActionForward viewReports(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        final String type = request.getParameter("type");

        Class reportClass = getClassForParameter(type);
        final DegreeType degreeType = getDegreeType(request);
        final ExecutionYear executionYear = getExecutionYear(request);
        Predicate predicate = new FindSelectedGepReports(executionYear, degreeType, reportClass);

        List<GepReportFile> selectedJobs =
                (List<GepReportFile>) org.apache.commons.collections.CollectionUtils.select(rootDomainObject.getQueueJobSet(),
                        predicate);
        String reportName = "";
        if (selectedJobs.size() > 0) {
            reportName = selectedJobs.iterator().next().getJobName();
        }

        request.setAttribute("degreeType", degreeType);
        request.setAttribute("executionYear", executionYear);
        request.setAttribute("list", reportName);
        request.setAttribute("queueJobList", selectedJobs);

        return mapping.findForward("viewReports");
    }

}
