package net.sourceforge.fenixedu.presentationTier.Action.commons.student;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.student.ReadStudentCurricularPlan;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.student.ReadStudentCurriculum;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.predicates.AcademicPredicates;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.coordinator.CoordinatedDegreeInfo;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.presentationTier.renderers.student.curriculum.StudentCurricularPlanRenderer.EnrolmentStateFilterType;
import net.sourceforge.fenixedu.presentationTier.renderers.student.curriculum.StudentCurricularPlanRenderer.OrganizationType;
import net.sourceforge.fenixedu.presentationTier.renderers.student.curriculum.StudentCurricularPlanRenderer.ViewType;
import net.sourceforge.fenixedu.util.BundleUtil;
import org.apache.commons.lang.StringUtils;
import net.sourceforge.fenixedu.util.StudentCurricularPlanIDDomainType;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 * @author David Santos
 * @author André Fernandes / João Brito
 */
@Mapping(path = "/viewStudentCurriculum", module = "academicAdministration",
        formBean = "studentCurricularPlanAndEnrollmentsSelectionForm")
@Forwards({ @Forward(name = "ShowStudentCurriculum", path = "/student/curriculum/displayStudentCurriculum_bd.jsp"),
        @Forward(name = "ShowStudentCurricularPlans", path = "df.page.showStudentCurricularPlans"),
        @Forward(name = "ShowStudentCurriculumForCoordinator", path = "df.page.showStudentCurriculumForCoordinator"),
        @Forward(name = "NotAuthorized", path = "df.page.notAuthorized") })
public class CurriculumDispatchAction extends FenixDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        CoordinatedDegreeInfo.setCoordinatorContext(request);
        return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        RenderUtils.invalidateViewState();

        Registration registration = null;

        final String registrationOID = getRegistrationOID(request);
        final Student loggedStudent = getUserView(request).getPerson().getStudent();

        if (registrationOID != null) {
            registration = FenixFramework.getDomainObject(registrationOID);
        } else if (loggedStudent != null) {
            /**
             * We no longer want to filter students with 1 registration only.
             * All taskflows are now forced to render "chooseRegistration.jsp"
             * in order to make it possible to present the
             * ExtraCurricularActivities information. This old block of code is
             * kept commented only for legacy purpose.
             */
            // if (loggedStudent.getRegistrations().size() == 1) {
            // registration = loggedStudent.getRegistrations().iterator().next();
            // } else {
            request.setAttribute("student", loggedStudent);

            List<Registration> validRegistrations = getValidRegistrations(loggedStudent);

            request.setAttribute("validRegistrations", validRegistrations);

            return mapping.findForward("chooseRegistration");
            // }
        }

        if (registration == null) {
            return mapping.findForward("NotAuthorized");
        } else {
            return getStudentCP(registration, mapping, (DynaActionForm) form, request);
        }
    }

    private List<Registration> getValidRegistrations(Student loggedStudent) {
        List<Registration> result = new ArrayList<Registration>();

        for (Registration registration : loggedStudent.getRegistrations()) {
            if (!registration.isCanceled()) {
                result.add(registration);
            }
        }

        return result;
    }

    private String getRegistrationOID(HttpServletRequest request) {
        String registrationOID = request.getParameter("registrationOID");
        if (registrationOID == null) {
            registrationOID = (String) request.getAttribute("registrationOID");
        }

        return (registrationOID == null || registrationOID.equals("")) ? null : registrationOID;
    }

    public ActionForward prepareReadByStudentNumber(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        RenderUtils.invalidateViewState();

        DynaActionForm actionForm = (DynaActionForm) form;
        Registration registration = null;

        final String degreeCurricularPlanId = (String) actionForm.get("degreeCurricularPlanID");
        Student student = getStudent(actionForm);
        if (student != null) {
            if (!StringUtils.isEmpty(degreeCurricularPlanId)) {
                DegreeCurricularPlan degreeCurricularPlan = FenixFramework.getDomainObject(degreeCurricularPlanId);
                registration = student.readRegistrationByDegreeCurricularPlan(degreeCurricularPlan);
            } else {
                final Collection<Registration> registrations = student.getRegistrations();
                if (!registrations.isEmpty()) {
                    registration = registrations.iterator().next();
                }
            }
        }

        if (registration == null) {
            return mapping.findForward("NotAuthorized");
        } else {
            return getStudentCP(registration, mapping, actionForm, request);
        }

    }

    private Student getStudent(DynaActionForm form) {
        final Integer studentNumber = Integer.valueOf((String) form.get("studentNumber"));
        Student student = Student.readStudentByNumber(studentNumber);
        // if (student != null) {
        // Teacher teacher = AccessControl.getPerson().getTeacher();
        // for (Tutorship tutorship : student.getTutorships()) {
        // if (tutorship.getTeacher().equals(teacher))
        // return student;
        // }
        // for (Coordinator coordinator :
        // AccessControl.getPerson().getCoordinators()) {
        // DegreeCurricularPlan dcp =
        // coordinator.getExecutionDegree().getDegreeCurricularPlan();
        // for (Registration registration : student.getRegistrations()) {
        // if (registration.getStudentCurricularPlan(dcp) != null)
        // return student;
        // }
        // }
        // }
        // return null;
        return student;
    }

    protected ActionForward getStudentCPForSupervisor(final Registration registration, final ActionMapping mapping,
            DynaActionForm actionForm, final HttpServletRequest request) {

        return getStudentCP(registration, mapping, actionForm, request);
    }

    private ActionForward getStudentCP(final Registration registration, final ActionMapping mapping, DynaActionForm actionForm,
            final HttpServletRequest request) {
        request.setAttribute("registration", registration);

        String studentCPID = getStudentCPID(request, actionForm);
        if (StringUtils.isEmpty(studentCPID)) {
            studentCPID = getDefaultStudentCPID(registration).getId().toString();
            actionForm.set("studentCPID", studentCPID);
        }
        request.setAttribute("selectedStudentCurricularPlans", getSelectedStudentCurricularPlans(registration, studentCPID));
        request.setAttribute("scpsLabelValueBeanList", getSCPsLabelValueBeanList(registration.getStudentCurricularPlans()));

        if (StringUtils.isEmpty(actionForm.getString("viewType"))) {
            actionForm.set("viewType", ViewType.ALL.name());
        }

        if (StringUtils.isEmpty(actionForm.getString("select"))) {
            actionForm
                    .set("select",
                            AcademicPredicates.VIEW_FULL_STUDENT_CURRICULUM.evaluate(AccessControl.getPerson()) ? EnrolmentStateFilterType.ALL
                                    .name() : EnrolmentStateFilterType.APPROVED_OR_ENROLED.name());
        }

        if (StringUtils.isEmpty(actionForm.getString("organizedBy"))) {
            String organizedBy =
                    registration.getDegreeType() == DegreeType.MASTER_DEGREE ? OrganizationType.EXECUTION_YEARS.name() : OrganizationType.GROUPS
                            .name();
            actionForm.set("organizedBy", organizedBy);
        }

        if (StringUtils.isEmpty(request.getParameter("degreeCurricularPlanID"))) {
            return mapping.findForward("ShowStudentCurriculum");
        } else {
            request.setAttribute("degreeCurricularPlanID", request.getParameter("degreeCurricularPlanID"));
            return mapping.findForward("ShowStudentCurriculumForCoordinator");
        }
    }

    private List<StudentCurricularPlan> getSelectedStudentCurricularPlans(final Registration registration,
            final String studentCPID) {
        final List<StudentCurricularPlan> result;

        final StudentCurricularPlanIDDomainType scpIdType = new StudentCurricularPlanIDDomainType(studentCPID);
        if (scpIdType.isNewest()) {
            result = Collections.singletonList(registration.getLastStudentCurricularPlan());
        } else if (scpIdType.isAll()) {
            result = getSortedStudentCurricularPlans(registration);
        } else {
            result = Collections.singletonList(getStudentCurricularPlan(registration, studentCPID));
        }

        return result;
    }

    private StudentCurricularPlanIDDomainType getDefaultStudentCPID(final Registration registration) {
        return registration.isBolonha() ? StudentCurricularPlanIDDomainType.NEWEST : StudentCurricularPlanIDDomainType.ALL;
    }

    private String getStudentCPID(HttpServletRequest request, DynaActionForm actionForm) {
        String result = request.getParameter("studentCPID");
        if (result == null) {
            result = (String) request.getAttribute("studentCPID");
        } else if (result == null) {
            result = (String) actionForm.get("studentCPID");
        }

        return result;
    }

    private List<StudentCurricularPlan> getSortedStudentCurricularPlans(final Registration registration) {
        final List<StudentCurricularPlan> result = new ArrayList<StudentCurricularPlan>();
        result.addAll(registration.getStudentCurricularPlans());
        Collections.sort(result, new BeanComparator("startDateYearMonthDay"));

        return result;
    }

    private StudentCurricularPlan getStudentCurricularPlan(final Registration registration, final String scpId) {
        for (final StudentCurricularPlan studentCurricularPlan : registration.getStudentCurricularPlansSet()) {
            if (studentCurricularPlan.getExternalId().equals(scpId)) {
                return studentCurricularPlan;
            }
        }

        return null;
    }

    private List<LabelValueBean> getSCPsLabelValueBeanList(Collection<StudentCurricularPlan> studentCurricularPlans) {
        final List<LabelValueBean> result = new ArrayList<LabelValueBean>();

        result.add(new LabelValueBean(StudentCurricularPlanIDDomainType.NEWEST_STRING, StudentCurricularPlanIDDomainType.NEWEST
                .toString()));
        result.add(new LabelValueBean(StudentCurricularPlanIDDomainType.ALL_STRING, StudentCurricularPlanIDDomainType.ALL
                .toString()));

        for (final StudentCurricularPlan studentCurricularPlan : studentCurricularPlans) {
            final StringBuilder label = new StringBuilder();

            label.append(studentCurricularPlan.getRegistration().getDegreeNameWithDescription());
            label.append(", ").append(studentCurricularPlan.getDegreeCurricularPlan().getName());

            if (studentCurricularPlan.getSpecialization() != null) {
                label.append(" - ").append(BundleUtil.getEnumName(studentCurricularPlan.getSpecialization()));
            }

            label.append(" - ").append(studentCurricularPlan.getStartDateYearMonthDay());

            result.add(new LabelValueBean(label.toString(), String.valueOf(studentCurricularPlan.getExternalId())));
        }

        return result;
    }

    public ActionForward getCurriculumForCoordinator(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws ExistingActionException, FenixServiceException {

        // get and set the degreeCurricularPlanID from the request and onto the
        // request
        String degreeCurricularPlanID = null;
        if (request.getParameter("degreeCurricularPlanID") != null) {
            degreeCurricularPlanID = request.getParameter("degreeCurricularPlanID");
            request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
        }

        final String studentCurricularPlanID = getStudentCPID(request, (DynaActionForm) form);

        String executionDegreeID = getExecutionDegree(request);
        List result = null;
        try {
            // TODO check
            result = ReadStudentCurriculum.runReadStudentCurriculum(executionDegreeID, studentCurricularPlanID);
        } catch (NotAuthorizedException e) {
            return mapping.findForward("NotAuthorized");
        }
        // TODO Remove this exception! It returns null and it is not supposed!
        catch (Exception exp) {
            exp.printStackTrace();
            return null;
        }

        BeanComparator courseName = new BeanComparator("infoCurricularCourse.name");
        BeanComparator executionYear = new BeanComparator("infoExecutionPeriod.infoExecutionYear.year");
        ComparatorChain chainComparator = new ComparatorChain();
        chainComparator.addComparator(courseName);
        chainComparator.addComparator(executionYear);

        Collections.sort(result, chainComparator);

        InfoStudentCurricularPlan infoStudentCurricularPlan = null;
        try {

            infoStudentCurricularPlan = ReadStudentCurricularPlan.run(studentCurricularPlanID);
        } catch (ExistingServiceException e) {
            throw new ExistingActionException(e);
        }

        request.setAttribute(PresentationConstants.CURRICULUM, result);
        request.setAttribute(PresentationConstants.STUDENT_CURRICULAR_PLAN, infoStudentCurricularPlan);

        return mapping.findForward("ShowStudentCurriculum");
    }

    private String getExecutionDegree(HttpServletRequest request) {
        String executionDegreeIdString = request.getParameter("executionDegreeId");
        if (executionDegreeIdString == null) {
            executionDegreeIdString = (String) request.getAttribute("executionDegreeId");
        }
        request.setAttribute("executionDegreeId", executionDegreeIdString);

        return executionDegreeIdString;
    }

}