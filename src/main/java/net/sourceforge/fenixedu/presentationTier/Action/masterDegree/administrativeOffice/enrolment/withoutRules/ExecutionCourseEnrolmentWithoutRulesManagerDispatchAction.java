package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.enrolment.withoutRules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadExecutionPeriodsEnrolment;
import net.sourceforge.fenixedu.applicationTier.Servico.enrollment.DeleteEnrollmentsList;
import net.sourceforge.fenixedu.applicationTier.Servico.enrollment.WriteBolonhaEnrolmentsList;
import net.sourceforge.fenixedu.applicationTier.Servico.enrollment.WriteEnrollmentsList;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.enrolment.withoutRules.PrepareDegreesListByStudentNumber;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.enrolment.withoutRules.ReadCurricularCoursesToEnroll;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.enrolment.withoutRules.ReadCurricularCoursesToEnrollSuperUser;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.enrolment.withoutRules.ReadStudentCurricularPlanForEnrollmentsWithoutRules;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degree.enrollment.CurricularCourse2Enroll;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.util.ExecutionDegreesFormat;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

@Mapping(
        module = "masterDegreeAdministrativeOffice",
        path = "/courseEnrolmentWithoutRulesManagerDA",
        input = "/courseEnrolmentWithoutRulesManagerDA.do?method=prepareEnrollmentChooseStudentAndExecutionYear&degreeType=MASTER_DEGREE&page=0",
        attribute = "curricularCoursesEnrollmentWithoutRuleForm", formBean = "curricularCoursesEnrollmentWithoutRuleForm",
        scope = "request", parameter = "method")
@Forwards(
        value = {
                @Forward(name = "globalEnrolment", path = "/globalEnrolment.do"),
                @Forward(name = "readCurricularCourseEnrollmentList",
                        path = "/courseEnrolmentWithoutRulesManagerDA.do?method=readEnrollments"),
                @Forward(name = "choosesForEnrollment", path = "df.page.choosesForEnrollment"),
                @Forward(name = "showCurricularCourseToEnroll", path = "df.page.showCurricularCourseToEnroll"),
                @Forward(name = "curricularCourseEnrollmentList", path = "df.page.curricularCourseEnrollmentList"),
                @Forward(name = "prepareEnrollmentChooseStudentWithoutRules",
                        path = "df.page.prepareEnrollmentChooseStudentWithoutRules"), @Forward(name = "exit", path = "/index.do") })
public class ExecutionCourseEnrolmentWithoutRulesManagerDispatchAction extends FenixDispatchAction {

    private static final int MAX_CURRICULAR_YEARS = 5;

    private static final int MAX_CURRICULAR_SEMESTERS = 2;

    public ActionForward exit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        return mapping.findForward("exit");
    }

    public ActionForward prepareEnrollmentChooseStudentAndExecutionYear(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        List<InfoExecutionPeriod> executionPeriods =
                ReadExecutionPeriodsEnrolment.runReadExecutionPeriodsEnrollmentFenix(DegreeType.valueOf(readAndSetDegreeType(
                        request, (DynaActionForm) form)));

        if (executionPeriods == null || executionPeriods.size() <= 0) {
            addActionMessage(request, "error.impossible.operations");
            return mapping.findForward("globalEnrolment");
        }

        sortExecutionPeriods(executionPeriods, (DynaActionForm) form);

        List executionYearLabels = buildLabelValueBeanForJsp(executionPeriods);
        request.setAttribute("executionPeriods", executionYearLabels);

        return mapping.findForward("prepareEnrollmentChooseStudentWithoutRules");
    }

    private String readAndSetDegreeType(HttpServletRequest request, DynaActionForm form) {
        String degreeType = request.getParameter("degreeType");
        if (degreeType == null) {
            degreeType = (String) request.getAttribute("degreeType");
            if (degreeType == null) {
                degreeType = form.getString("degreeType");
            }
        }
        request.setAttribute("degreeType", degreeType);
        return degreeType;
    }

    private void sortExecutionPeriods(List executionPeriods, DynaActionForm form) {
        ComparatorChain comparator = new ComparatorChain();
        comparator.addComparator(new BeanComparator("infoExecutionYear.year"), true);
        comparator.addComparator(new BeanComparator("semester"), true);
        Collections.sort(executionPeriods, comparator);

        int size = executionPeriods.size();
        for (int i = (size - 1); i >= 0; i--) {
            InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) executionPeriods.get(i);
            if (infoExecutionPeriod.getState().equals(PeriodState.CURRENT)) {
                form.set("executionPeriod", infoExecutionPeriod.getExternalId().toString());
                break;
            }
        }
    }

    private List buildLabelValueBeanForJsp(List infoExecutionPeriods) {
        List executionPeriodsLabels = new ArrayList();
        CollectionUtils.collect(infoExecutionPeriods, new Transformer() {
            @Override
            public Object transform(Object arg0) {
                InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) arg0;

                LabelValueBean executionYear =
                        new LabelValueBean(infoExecutionPeriod.getName() + " - "
                                + infoExecutionPeriod.getInfoExecutionYear().getYear(), infoExecutionPeriod.getExternalId()
                                .toString());
                return executionYear;
            }
        }, executionPeriodsLabels);
        return executionPeriodsLabels;
    }

    public ActionForward readEnrollments(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final DynaActionForm form = (DynaActionForm) actionForm;

        if (StringUtils.isEmpty(form.getString("studentNumber"))) {
            addActionMessage(request, "error.no.student");
            return mapping.getInputForward();
        }

        final Integer studentNumber = Integer.valueOf(form.getString("studentNumber"));
        final ExecutionSemester executionSemester = getExecutionPeriod(form);

        StudentCurricularPlan studentCurricularPlan = null;
        try {
            studentCurricularPlan =
                    ReadStudentCurricularPlanForEnrollmentsWithoutRules.runReadStudentCurricularPlanForEnrollmentsWithoutRules(
                            getStudent(form), getDegreeType(form), executionSemester);

        } catch (NotAuthorizedException e) {
            e.printStackTrace();
            addActionMessage(request, "error.exception.notAuthorized2");
            return mapping.getInputForward();

        } catch (FenixServiceException e) {
            e.printStackTrace();

            if (e.getMessage() != null && e.getMessage().endsWith("noCurricularPlans")) {
                addActionMessage(request, e.getMessage(), studentNumber.toString());
            } else if (e.getMessage() != null && e.getMessage().endsWith("not.from.chosen.execution.year")) {
                addActionMessage(request, e.getMessage(), studentNumber.toString());
            } else {
                addActionMessage(request, "error.impossible.operations");
            }
            return mapping.getInputForward();
        }

        checkIfStudentHasPayedTuition(request, studentCurricularPlan);

        request.setAttribute("executionPeriod", executionSemester);
        request.setAttribute("studentCurricularPlan", studentCurricularPlan);
        request.setAttribute("studentCurrentSemesterEnrollments",
                studentCurricularPlan.getAllStudentEnrolledEnrollmentsInExecutionPeriod(executionSemester));

        return mapping.findForward("curricularCourseEnrollmentList");
    }

    private ExecutionSemester getExecutionPeriod(final DynaActionForm form) {
        return AbstractDomainObject.fromExternalId(form.getString("executionPeriod"));
    }

    private DegreeType getDegreeType(final DynaActionForm form) {
        return DegreeType.valueOf(form.getString("degreeType"));
    }

    private Registration getStudent(final DynaActionForm form) {
        final Integer studentNumber = Integer.valueOf(form.getString("studentNumber"));
        Registration registration = Registration.readStudentByNumberAndDegreeType(studentNumber, getDegreeType(form));
        if (registration == null && getDegreeType(form).equals(DegreeType.MASTER_DEGREE)) {
            registration =
                    Registration.readStudentByNumberAndDegreeType(studentNumber, DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA);
        }
        return registration;
    }

    private void checkIfStudentHasPayedTuition(HttpServletRequest request, StudentCurricularPlan studentCurricularPlan) {
        if (studentCurricularPlan.getRegistration().getPayedTuition().equals(Boolean.FALSE)
                && !studentCurricularPlan.getRegistration().getDegreeType().equals(DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA)) {
            addActionMessage(request, "error.message.noTuitonPayed");
        }
    }

    public ActionForward unEnrollCourses(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final DynaActionForm form = (DynaActionForm) actionForm;

        final Integer studentNumber = Integer.valueOf((String) form.get("studentNumber"));
        final List<String> unenrollmentsList = Arrays.asList((String[]) form.get("unenrollments"));

        try {
            DeleteEnrollmentsList.runDeleteEnrollmentsList(getStudent(form), getDegreeType(form), unenrollmentsList);

        } catch (NotAuthorizedException e) {
            addActionMessage(request, "error.exception.notAuthorized2");
            return mapping.getInputForward();

        } catch (DomainException e) {
            addActionMessage(request, e.getMessage());
            return mapping.getInputForward();

        } catch (FenixServiceException e) {
            e.printStackTrace();
            addActionMessage(request, "error.impossible.operations.unenroll", studentNumber.toString());
            return mapping.getInputForward();
        }

        return mapping.findForward("readCurricularCourseEnrollmentList");
    }

    public ActionForward prepareEnrollmentCourses(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final DynaActionForm form = (DynaActionForm) actionForm;
        final ExecutionSemester executionSemester = getExecutionPeriod(form);

        StudentCurricularPlan studentCurricularPlan = null;
        List<ExecutionDegree> result = null;
        try {

            final Registration registration = getStudent(form);
            studentCurricularPlan = registration.getLastStudentCurricularPlan();

            result =
                    PrepareDegreesListByStudentNumber.runPrepareDegreesListByStudentNumber(registration, getDegreeType(form),
                            executionSemester);

        } catch (NotAuthorizedException e) {
            e.printStackTrace();
            addActionMessage(request, "error.exception.notAuthorized2");
            return mapping.getInputForward();

        } catch (FenixServiceException e) {
            addActionMessage(request, "error.impossible.operations");
            return mapping.findForward("readCurricularCourseEnrollmentList");
        }

        prepareEnrollmentCoursesInformation(request, form, studentCurricularPlan, result);

        // set attributes used by courseEnrollment context
        request.setAttribute("executionPeriod", executionSemester);
        request.setAttribute("studentCurricularPlan", studentCurricularPlan);

        return mapping.findForward("choosesForEnrollment");
    }

    private void prepareEnrollmentCoursesInformation(final HttpServletRequest request, final DynaActionForm form,
            final StudentCurricularPlan studentCurricularPlan, final List<ExecutionDegree> result) {

        setDefaultExecutionDegree(form, studentCurricularPlan, result);

        sortExecutionDegrees(result);

        request.setAttribute(
                PresentationConstants.DEGREE_LIST,
                ExecutionDegreesFormat.buildLabelValueBeansForExecutionDegree(result,
                        getResources(request, "ENUMERATION_RESOURCES"), request));

        request.setAttribute(PresentationConstants.ENROLMENT_YEAR_LIST_KEY, getListOfChosenCurricularYears());
        request.setAttribute(PresentationConstants.ENROLMENT_SEMESTER_LIST_KEY, getListOfChosenCurricularSemesters());

    }

    private void setDefaultExecutionDegree(final DynaActionForm form, final StudentCurricularPlan studentCurricularPlan,
            final List<ExecutionDegree> executionDegrees) {

        if (StringUtils.isEmpty(form.getString("executionDegree"))) {
            List<ExecutionDegree> intersection =
                    (List<ExecutionDegree>) CollectionUtils.intersection(executionDegrees, studentCurricularPlan
                            .getDegreeCurricularPlan().getExecutionDegreesSet());
            form.set("executionDegree", intersection.get(0).getExternalId().toString());
        }
    }

    private void sortExecutionDegrees(List<ExecutionDegree> result) {
        Collections.sort(result, new Comparator<ExecutionDegree>() {
            @Override
            public int compare(ExecutionDegree o1, ExecutionDegree o2) {
                final String name = "" + o1.getDegree().getDegreeType().name() + o1.getDegree().getName();
                final String name2 = "" + o2.getDegree().getDegreeType().name() + o2.getDegree().getName();
                return name.compareToIgnoreCase(name2);
            }
        });
    }

    private List getListOfChosenCurricularYears() {
        final List<Integer> result = new ArrayList<Integer>();
        for (int i = 1; i <= MAX_CURRICULAR_YEARS; i++) {
            result.add(Integer.valueOf(i));
        }
        return result;
    }

    private List getListOfChosenCurricularSemesters() {
        final List<Integer> result = new ArrayList<Integer>();
        for (int i = 1; i <= MAX_CURRICULAR_SEMESTERS; i++) {
            result.add(Integer.valueOf(i));
        }
        return result;
    }

    public ActionForward readCoursesToEnroll(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final DynaActionForm form = (DynaActionForm) actionForm;

        final Integer studentNumber = Integer.valueOf(form.getString("studentNumber"));
        final ExecutionSemester executionSemester = getExecutionPeriod(form);

        final String executionDegreeID = form.getString("executionDegree");
        final List<Integer> curricularYearsList = Arrays.asList((Integer[]) form.get("curricularYears"));
        final List<Integer> curricularSemesters = Arrays.asList((Integer[]) form.get("curricularSemesters"));

        StudentCurricularPlan studentCurricularPlan = null;
        List<CurricularCourse2Enroll> curricularCourses2Enroll = null;
        try {

            final Integer userType = (Integer) form.get("userType");
            final Registration registration = getStudent(form);
            studentCurricularPlan = registration.getLastStudentCurricularPlan();

            if (userType.equals(0)) {
                curricularCourses2Enroll =
                        ReadCurricularCoursesToEnroll.runReadCurricularCoursesToEnroll(
                                registration.getLastStudentCurricularPlan(), getDegreeType(form), executionSemester,
                                executionDegreeID, curricularYearsList, curricularSemesters);

            } else {
                curricularCourses2Enroll =
                        ReadCurricularCoursesToEnrollSuperUser.runReadCurricularCoursesToEnrollSuperUser(
                                registration.getLastStudentCurricularPlan(), getDegreeType(form), executionSemester,
                                executionDegreeID, curricularYearsList, curricularSemesters);
            }

        } catch (NotAuthorizedException e) {
            e.printStackTrace();
            addActionMessage(request, "error.exception.notAuthorized2");
            return mapping.getInputForward();

        } catch (FenixServiceException e) {
            e.printStackTrace();
            if (e.getMessage() != null && e.getMessage().endsWith("noCurricularPlans")) {
                addActionMessage(request, e.getMessage(), studentNumber.toString());
            } else if (e.getMessage() != null && !e.getMessage().endsWith("noCurricularPlans")) {
                addActionMessage(request, e.getMessage());
            } else {
                addActionMessage(request, "error.impossible.operations");
            }
            return mapping.getInputForward();
        }

        checkIfStudentHasPayedTuition(request, studentCurricularPlan);

        // set attributes used by courseEnrollment context
        request.setAttribute("executionPeriod", executionSemester);
        request.setAttribute("studentCurricularPlan", studentCurricularPlan);

        sortCurricularCourses2Enrol(curricularCourses2Enroll);
        request.setAttribute("curricularCourses2Enroll", curricularCourses2Enroll);
        form.set("enrollmentTypes", getInitializedMap(curricularCourses2Enroll));

        return mapping.findForward("showCurricularCourseToEnroll");
    }

    private void sortCurricularCourses2Enrol(List<CurricularCourse2Enroll> curricularCourse2Enroll) {
        Collections.sort(curricularCourse2Enroll, new Comparator<CurricularCourse2Enroll>() {
            @Override
            public int compare(CurricularCourse2Enroll o1, CurricularCourse2Enroll o2) {
                return o1.getCurricularCourse().getName().compareTo(o2.getCurricularCourse().getName());
            }
        });
    }

    private Object getInitializedMap(List<CurricularCourse2Enroll> curricularCourses2Enroll) {
        final Map result = new HashMap();
        for (final CurricularCourse2Enroll curricularCourse2Enroll : curricularCourses2Enroll) {
            result.put(curricularCourse2Enroll.getCurricularCourse().getExternalId(), Integer.valueOf(0));
        }
        return result;
    }

    public ActionForward enrollCourses(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final DynaActionForm form = (DynaActionForm) actionForm;

        final Integer studentNumber = Integer.valueOf(form.getString("studentNumber"));
        final List<String> curricularCourses = Arrays.asList((String[]) form.get("curricularCourses"));
        final Map optionalEnrollments = (HashMap) form.get("enrollmentTypes");

        try {
            Registration registration = getStudent(form);
            if (registration.getDegreeType().isBolonhaType()) {
                WriteBolonhaEnrolmentsList.runWriteBolonhaEnrolmentsList(registration.getActiveStudentCurricularPlan(),
                        getDegreeType(form), getExecutionPeriod(form), curricularCourses, optionalEnrollments,
                        getUserView(request));
            } else {
                WriteEnrollmentsList.runWriteEnrollmentsList(registration.getActiveStudentCurricularPlan(), getDegreeType(form),
                        getExecutionPeriod(form), curricularCourses, optionalEnrollments, getUserView(request));
            }

        } catch (NotAuthorizedException e) {
            e.printStackTrace();
            addActionMessage(request, "error.exception.notAuthorized2");
            return mapping.getInputForward();

        } catch (FenixServiceException e) {
            e.printStackTrace();
            if (e.getMessage() != null && e.getMessage().endsWith("noCurricularPlans")) {
                addActionMessage(request, e.getMessage(), studentNumber.toString());
            } else if (e.getMessage() != null && !e.getMessage().endsWith("noCurricularPlans")) {
                addActionMessage(request, e.getMessage());
            } else {
                addActionMessage(request, "error.impossible.operations");
            }
            return mapping.getInputForward();
        }

        return mapping.findForward("readCurricularCourseEnrollmentList");
    }

    public ActionForward viewRegistrations(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final DynaActionForm actionForm = (DynaActionForm) form;

        if (StringUtils.isEmpty(actionForm.getString("studentNumber"))) {
            addActionMessage(request, "error.no.student");
            return mapping.getInputForward();
        }

        Student student = Student.readStudentByNumber(Integer.valueOf(actionForm.getString("studentNumber")));
        if (student == null) {
            addActionMessage(request, "error.no.student");
            return mapping.getInputForward();
        }

        request.setAttribute("registrations", student.getRegistrationsByDegreeType(DegreeType.DEGREE));
        return mapping.findForward("viewRegistrations");
    }

    public ActionForward viewStudentCurricularPlans(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        final DynaActionForm actionForm = (DynaActionForm) form;
        // actionForm.set("executionPeriod",
        // request.getParameter("executionPeriod"));
        Registration registration = AbstractDomainObject.fromExternalId(request.getParameter("registrationID"));
        if (registration == null) {
            throw new FenixActionException("invalid registration id");
        }

        // This code will be removed
        final List<StudentCurricularPlan> studentCurricularPlans = new ArrayList<StudentCurricularPlan>();
        for (final StudentCurricularPlan studentCurricularPlan : registration.getStudentCurricularPlansSet()) {
            if (!studentCurricularPlan.isBoxStructure()) {
                studentCurricularPlans.add(studentCurricularPlan);
            }
        }
        request.setAttribute("studentCurricularPlans", studentCurricularPlans);
        return mapping.findForward("viewStudentCurricularPlans");
    }

    public ActionForward readEnrolments2(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        final DynaActionForm actionForm = (DynaActionForm) form;

        final ExecutionSemester executionSemester = getExecutionPeriod(actionForm);
        StudentCurricularPlan studentCurricularPlan =
                AbstractDomainObject.fromExternalId(request.getParameter("studentCurricularPlan"));

        if (studentCurricularPlan == null || executionSemester == null) {
            throw new FenixActionException("invalid arguments");
        }

        checkIfStudentHasPayedTuition(request, studentCurricularPlan);

        actionForm.set("studentNumber", studentCurricularPlan.getRegistration().getStudent().getNumber().toString());
        actionForm.set("degreeType", studentCurricularPlan.getRegistration().getDegreeType().getName());

        request.setAttribute("executionPeriod", executionSemester);
        request.setAttribute("studentCurricularPlan", studentCurricularPlan);

        List<Enrolment> allStudentEnrolledEnrollmentsInExecutionPeriod =
                studentCurricularPlan.getAllStudentEnrolledEnrollmentsInExecutionPeriod(executionSemester);
        for (Enrolment enrolment : allStudentEnrolledEnrollmentsInExecutionPeriod) {
            enrolment.setAccumulatedEctsCredits(studentCurricularPlan.getAccumulatedEctsCredits(executionSemester,
                    enrolment.getCurricularCourse()));
        }
        request.setAttribute("studentCurrentSemesterEnrollments", allStudentEnrolledEnrollmentsInExecutionPeriod);

        return mapping.findForward("curricularCourseEnrollmentList2");
    }

    public ActionForward unEnrollCourses2(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final DynaActionForm form = (DynaActionForm) actionForm;

        final StudentCurricularPlan studentCurricularPlan =
                AbstractDomainObject.fromExternalId((String) form.get("studentCurricularPlan"));
        final List<String> unenrollmentsList = Arrays.asList((String[]) form.get("unenrollments"));

        try {
            DeleteEnrollmentsList.runDeleteEnrollmentsList(studentCurricularPlan.getRegistration(), getDegreeType(form),
                    unenrollmentsList);

        } catch (NotAuthorizedException e) {
            addActionMessage(request, "error.exception.notAuthorized2");
            return mapping.getInputForward();

        } catch (DomainException e) {
            addActionMessage(request, e.getMessage());
            return mapping.getInputForward();

        } catch (FenixServiceException e) {
            e.printStackTrace();
            addActionMessage(request, "error.impossible.operations.unenroll", studentCurricularPlan.getRegistration()
                    .getStudent().getNumber().toString());
            return mapping.getInputForward();
        }

        return readEnrolments2(mapping, form, request, response);
    }

    public ActionForward prepareEnrollmentCourses2(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final DynaActionForm form = (DynaActionForm) actionForm;
        final ExecutionSemester executionSemester = getExecutionPeriod(form);

        final StudentCurricularPlan studentCurricularPlan =
                AbstractDomainObject.fromExternalId((String) form.get("studentCurricularPlan"));
        List<ExecutionDegree> result = null;
        try {

            result =
                    PrepareDegreesListByStudentNumber.runPrepareDegreesListByStudentNumber(
                            studentCurricularPlan.getRegistration(), getDegreeType(form), executionSemester);

        } catch (NotAuthorizedException e) {
            e.printStackTrace();
            addActionMessage(request, "error.exception.notAuthorized2");
            return mapping.getInputForward();

        } catch (FenixServiceException e) {
            addActionMessage(request, "error.impossible.operations");
            return mapping.findForward("readCurricularCourseEnrollmentList");
        }

        prepareEnrollmentCoursesInformation(request, form, studentCurricularPlan, result);

        // set attributes used by courseEnrollment context
        request.setAttribute("executionPeriod", executionSemester);
        request.setAttribute("studentCurricularPlan", studentCurricularPlan);

        return mapping.findForward("choosesForEnrollment2");
    }

    public ActionForward readCoursesToEnroll2(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final DynaActionForm form = (DynaActionForm) actionForm;

        final Integer studentNumber = Integer.valueOf(form.getString("studentNumber"));
        final ExecutionSemester executionSemester = getExecutionPeriod(form);

        final String executionDegreeID = form.getString("executionDegree");
        final List<Integer> curricularYearsList = Arrays.asList((Integer[]) form.get("curricularYears"));
        final List<Integer> curricularSemesters = Arrays.asList((Integer[]) form.get("curricularSemesters"));

        final StudentCurricularPlan studentCurricularPlan =
                AbstractDomainObject.fromExternalId((String) form.get("studentCurricularPlan"));
        List<CurricularCourse2Enroll> curricularCourses2Enroll = null;
        try {

            final Integer userType = (Integer) form.get("userType");

            if (userType.equals(0)) {
                curricularCourses2Enroll =
                        ReadCurricularCoursesToEnroll.runReadCurricularCoursesToEnroll(studentCurricularPlan,
                                getDegreeType(form), executionSemester, executionDegreeID, curricularYearsList,
                                curricularSemesters);
            } else {
                curricularCourses2Enroll =
                        ReadCurricularCoursesToEnrollSuperUser.runReadCurricularCoursesToEnrollSuperUser(studentCurricularPlan,
                                getDegreeType(form), executionSemester, executionDegreeID, curricularYearsList,
                                curricularSemesters);
            }

        } catch (NotAuthorizedException e) {
            e.printStackTrace();
            addActionMessage(request, "error.exception.notAuthorized2");
            return mapping.getInputForward();

        } catch (FenixServiceException e) {
            e.printStackTrace();
            if (e.getMessage() != null && e.getMessage().endsWith("noCurricularPlans")) {
                addActionMessage(request, e.getMessage(), studentNumber.toString());
            } else if (e.getMessage() != null && !e.getMessage().endsWith("noCurricularPlans")) {
                addActionMessage(request, e.getMessage());
            } else {
                addActionMessage(request, "error.impossible.operations");
            }
            return mapping.getInputForward();
        }

        checkIfStudentHasPayedTuition(request, studentCurricularPlan);

        // set attributes used by courseEnrollment context
        request.setAttribute("executionPeriod", executionSemester);
        request.setAttribute("studentCurricularPlan", studentCurricularPlan);

        sortCurricularCourses2Enrol(curricularCourses2Enroll);
        request.setAttribute("curricularCourses2Enroll", curricularCourses2Enroll);
        form.set("enrollmentTypes", getInitializedMap(curricularCourses2Enroll));

        return mapping.findForward("showCurricularCourseToEnroll2");
    }

    public ActionForward enrollCourses2(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final DynaActionForm form = (DynaActionForm) actionForm;

        final Integer studentNumber = Integer.valueOf(form.getString("studentNumber"));
        final List<String> curricularCourses = Arrays.asList((String[]) form.get("curricularCourses"));
        final Map optionalEnrollments = (HashMap) form.get("enrollmentTypes");
        final StudentCurricularPlan studentCurricularPlan =
                AbstractDomainObject.fromExternalId((String) form.get("studentCurricularPlan"));

        try {
            if (studentCurricularPlan.getRegistration().getDegreeType().isBolonhaType()) {
                WriteBolonhaEnrolmentsList.runWriteBolonhaEnrolmentsList(studentCurricularPlan, getDegreeType(form),
                        getExecutionPeriod(form), curricularCourses, optionalEnrollments, getUserView(request));
            } else {
                WriteEnrollmentsList.runWriteEnrollmentsList(studentCurricularPlan, getDegreeType(form),
                        getExecutionPeriod(form), curricularCourses, optionalEnrollments, getUserView(request));
            }

        } catch (NotAuthorizedException e) {
            e.printStackTrace();
            addActionMessage(request, "error.exception.notAuthorized2");
            return mapping.getInputForward();

        } catch (FenixServiceException e) {
            e.printStackTrace();
            if (e.getMessage() != null && e.getMessage().endsWith("noCurricularPlans")) {
                addActionMessage(request, e.getMessage(), studentNumber.toString());
            } else if (e.getMessage() != null && !e.getMessage().endsWith("noCurricularPlans")) {
                addActionMessage(request, e.getMessage());
            } else {
                addActionMessage(request, "error.impossible.operations");
            }
            return mapping.getInputForward();
        }

        return readEnrolments2(mapping, form, request, response);
    }

}