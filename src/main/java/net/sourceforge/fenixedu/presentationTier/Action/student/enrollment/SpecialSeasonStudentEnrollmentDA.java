package net.sourceforge.fenixedu.presentationTier.Action.student.enrollment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha.SpecialSeasonBolonhaStudentEnrolmentBean;
import net.sourceforge.fenixedu.domain.EnrolmentPeriodInSpecialSeasonEvaluations;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.studentEnrolment.bolonha.AcademicAdminOfficeSpecialSeasonBolonhaStudentEnrolmentDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixframework.FenixFramework;

@Mapping(path = "/enrollment/evaluations/specialSeason", module = "student")
@Forwards({
        @Forward(name = "showDegreeModulesToEnrol", path = "/student/enrollment/evaluations/specialSeasonShowDegreeModules.jsp",
                tileProperties = @Tile(title = "private.student.subscribe.specialseason")),
        @Forward(name = "showPickSCPAndSemester", path = "/student/enrollment/evaluations/specialSeasonPickSCPAndSemester.jsp",
                tileProperties = @Tile(title = "private.student.subscribe.specialseason")),
        @Forward(name = "showStudentEnrollmentMenu", path = "/student/enrollment/evaluations/specialSeason.jsp",
                tileProperties = @Tile(title = "private.student.subscribe.specialseason")) })
public class SpecialSeasonStudentEnrollmentDA extends AcademicAdminOfficeSpecialSeasonBolonhaStudentEnrolmentDA {

    public ActionForward entryPoint(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final Student student = getLoggedStudent(request);

        final List<StudentCurricularPlan> scps = generateSCPList(student);
        if (enrollmentPeriodNotOpen(new ArrayList<StudentCurricularPlan>(scps))) {
            EnrolmentPeriodInSpecialSeasonEvaluations enrolmentPeriod = getNextEnrollmentPeriod(scps);
            if (enrolmentPeriod == null) {
                addActionMessage("warning", request, "message.out.curricular.course.enrolment.period.default");
                request.setAttribute("disableContinue", true);
            } else {
                addActionMessage("warning", request, "message.out.special.season.enrolment.period.upcoming", enrolmentPeriod
                        .getStartDateDateTime().toString("dd-MM-yyyy"),
                        enrolmentPeriod.getEndDateDateTime().toString("dd-MM-yyyy"));
                request.setAttribute("disableContinue", true);
            }
        } else if (hasPendingDebts(student)) {
            addActionMessage("error", request, "error.special.season.cannot.enroll.due.to.pending.debts");
            request.setAttribute("disableContinue", true);

        } else if (scps.isEmpty()) {
            request.setAttribute("disableContinue", true);
        }

        return mapping.findForward("showStudentEnrollmentMenu");
    }

    public ActionForward pickSCP(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        final Student student = getLoggedStudent(request);
        SpecialSeasonStudentEnrollmentBean bean = new SpecialSeasonStudentEnrollmentBean(student);
        final List<StudentCurricularPlan> scps = generateSCPList(student);

        if (scps.size() == 1) {
            bean.setScp(scps.iterator().next());
        } else {
            request.setAttribute("scps", scps);
        }

        request.setAttribute("bean", bean);
        return mapping.findForward("showPickSCPAndSemester");
    }

    public ActionForward pickSemester(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final Student student = getLoggedStudent(request);
        final String scpOid = request.getParameter("scpOid");
        final StudentCurricularPlan scp = FenixFramework.getDomainObject(scpOid);
        SpecialSeasonStudentEnrollmentBean bean = new SpecialSeasonStudentEnrollmentBean(student, scp);

        request.setAttribute("bean", bean);
        return mapping.findForward("showPickSCPAndSemester");
    }

    public ActionForward showDegreeModules(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        SpecialSeasonStudentEnrollmentBean bean = getRenderedObject("bean");

        if (!hasStatute(bean.getStudent(), bean.getExecutionSemester(), bean.getScp().getRegistration())) {

            if (!(bean.getScp().getRegistration().isSeniorStatuteApplicable(bean.getExecutionSemester().getExecutionYear()))) {
                addActionMessage("error", request, "error.special.season.not.granted");
                request.setAttribute("bean", bean);
                return mapping.findForward("showPickSCPAndSemester");
            }

            bean.getScp().getRegistration().grantSeniorStatute(bean.getExecutionSemester().getExecutionYear());
        }

        request.setAttribute("bolonhaStudentEnrollmentBean",
                new SpecialSeasonBolonhaStudentEnrolmentBean(bean.getScp(), bean.getExecutionSemester()));
        request.setAttribute("bean", bean);
        return mapping.findForward("showDegreeModulesToEnrol");
    }

    private Student getLoggedStudent(final HttpServletRequest request) {
        return getLoggedPerson(request).getStudent();
    }

    private final List<StudentCurricularPlan> generateSCPList(final Student student) {
        final List<StudentCurricularPlan> result = new ArrayList<StudentCurricularPlan>();

        for (final Registration registration : student.getRegistrations()) {
            if (registration.isActive() || registration.hasAnyEnrolmentsIn(ExecutionYear.readCurrentExecutionYear())) {
                result.add(registration.getLastStudentCurricularPlan());
            }
        }

        return result;
    }

    private boolean enrollmentPeriodNotOpen(List<StudentCurricularPlan> scps) {
        if (scps.isEmpty()) {
            return true;
        }

        /*
         * Iterative equivalent boolean result = true; for(StudentCurricularPlan
         * scp : scps) { boolean eval =
         * !(scp.getDegreeCurricularPlan().hasOpenSpecialSeasonEnrolmentPeriod(
         * ExecutionYear.readCurrentExecutionYear().getFirstExecutionPeriod())
         * || scp.getDegreeCurricularPlan().hasOpenSpecialSeasonEnrolmentPeriod(
         * ExecutionYear.readCurrentExecutionYear().getLastExecutionPeriod()) ||
         * scp.getDegreeCurricularPlan().hasOpenSpecialSeasonEnrolmentPeriod(
         * ExecutionYear.readCurrentExecutionYear().getPreviousExecutionYear().
         * getFirstExecutionPeriod()) ||
         * scp.getDegreeCurricularPlan().hasOpenSpecialSeasonEnrolmentPeriod(
         * ExecutionYear.readCurrentExecutionYear().getPreviousExecutionYear().
         * getLastExecutionPeriod())); result = result && eval; } return result;
         */

        final StudentCurricularPlan scp = scps.iterator().next();
        scps.remove(0);
        // Any SpecialSeason enrollment period opened for this/last year will
        // count.
        return !(scp.getDegreeCurricularPlan().hasOpenSpecialSeasonEnrolmentPeriod(
                ExecutionYear.readCurrentExecutionYear().getFirstExecutionPeriod())
                || scp.getDegreeCurricularPlan().hasOpenSpecialSeasonEnrolmentPeriod(
                        ExecutionYear.readCurrentExecutionYear().getLastExecutionPeriod())
                || scp.getDegreeCurricularPlan().hasOpenSpecialSeasonEnrolmentPeriod(
                        ExecutionYear.readCurrentExecutionYear().getPreviousExecutionYear().getFirstExecutionPeriod()) || scp
                .getDegreeCurricularPlan().hasOpenSpecialSeasonEnrolmentPeriod(
                        ExecutionYear.readCurrentExecutionYear().getPreviousExecutionYear().getLastExecutionPeriod()))
                && enrollmentPeriodNotOpen(scps);

    }

    private EnrolmentPeriodInSpecialSeasonEvaluations getNextEnrollmentPeriod(List<StudentCurricularPlan> scps) {
        final List<EnrolmentPeriodInSpecialSeasonEvaluations> nextOpenPeriodsForEachSCP =
                new ArrayList<EnrolmentPeriodInSpecialSeasonEvaluations>();
        EnrolmentPeriodInSpecialSeasonEvaluations result;
        for (final StudentCurricularPlan scp : scps) {
            result = scp.getDegreeCurricularPlan().getNextSpecialSeasonEnrolmentPeriod();
            if (result != null) {
                nextOpenPeriodsForEachSCP.add(result);
            }
        }
        return nextOpenPeriodsForEachSCP.isEmpty() ? null : Collections.min(nextOpenPeriodsForEachSCP,
                EnrolmentPeriodInSpecialSeasonEvaluations.COMPARATOR_BY_START);
    }

    private boolean hasPendingDebts(Student student) {
        return hasAnyAdministrativeOfficeFeeAndInsuranceInDebt(student, ExecutionYear.readCurrentExecutionYear())
                || hasAnyGratuityDebt(student, ExecutionYear.readCurrentExecutionYear())
                || hasAnyAdministrativeOfficeFeeAndInsuranceInDebt(student, ExecutionYear.readCurrentExecutionYear()
                        .getPreviousExecutionYear())
                || hasAnyGratuityDebt(student, ExecutionYear.readCurrentExecutionYear().getPreviousExecutionYear());
    }

    @Override
    protected String getAction() {
        return "/enrollment/evaluations/specialSeason.do";
    }

}
