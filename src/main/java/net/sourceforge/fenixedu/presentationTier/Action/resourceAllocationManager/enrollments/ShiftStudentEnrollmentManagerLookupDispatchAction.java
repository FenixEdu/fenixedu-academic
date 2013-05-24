package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.enrollments;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.enrollment.shift.ReadClassTimeTableByStudent;
import net.sourceforge.fenixedu.applicationTier.Servico.enrollment.shift.WriteStudentAttendingCourse;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.Servico.student.ReadStudentTimeTable;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.presentationTier.Action.commons.TransactionalLookupDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixTransactionException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "resourceAllocationManager", path = "/studentShiftEnrollmentManagerLoockup",
        input = "/studentShiftEnrollmentManager.do?method=prepare", attribute = "studentShiftEnrollmentForm",
        formBean = "studentShiftEnrollmentForm", scope = "request", validate = false, parameter = "method")
@Forwards(
        value = {
                @Forward(name = "prepareShiftEnrollment",
                        path = "/studentShiftEnrollmentManager.do?method=prepareShiftEnrollment"),
                @Forward(name = "prepareEnrollmentViewWarning",
                        path = "/studentShiftEnrollmentManager.do?method=prepareStartViewWarning"),
                @Forward(name = "showShiftsToEnroll", path = "/student/enrollment/showShiftsToEnroll.jsp",
                        tileProperties = @Tile(navLocal = "/student/enrollment/listClasses.jsp",
                                title = "private.student.subscribe.courses")),
                @Forward(name = "studentFirstPage", path = "/dotIstPortal.do?prefix=/student&page=/index.do",
                        contextRelative = true),
                @Forward(name = "beginTransaction", path = "/studentShiftEnrollmentManager.do?method=start&firstTime=true") })
@Exceptions(
        value = {
                @ExceptionHandling(
                        type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixTransactionException.class,
                        key = "error.transaction.enrolment",
                        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class,
                        scope = "request"),
                @ExceptionHandling(
                        type = net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException.class,
                        key = "error.message.OutsideOfCurrentClassesEnrolmentPeriodForDegreeCurricularPlan",
                        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class,
                        scope = "request"),
                @ExceptionHandling(
                        type = net.sourceforge.fenixedu.applicationTier.Filtro.enrollment.ClassEnrollmentAuthorizationFilter.CurrentClassesEnrolmentPeriodUndefinedForDegreeCurricularPlan.class,
                        key = "error.message.CurrentClassesEnrolmentPeriodUndefinedForDegreeCurricularPlan",
                        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class,
                        scope = "request"),
                @ExceptionHandling(
                        type = net.sourceforge.fenixedu.applicationTier.Filtro.enrollment.ClassEnrollmentAuthorizationFilter.OutsideOfCurrentClassesEnrolmentPeriodForDegreeCurricularPlan.class,
                        key = "error.message.OutsideOfCurrentClassesEnrolmentPeriodForDegreeCurricularPlan",
                        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class,
                        scope = "request") })
public class ShiftStudentEnrollmentManagerLookupDispatchAction extends TransactionalLookupDispatchAction {

    private Registration getAndSetRegistration(final HttpServletRequest request) {
        final Registration registration =
                rootDomainObject.readRegistrationByOID(getIntegerFromRequest(request, "registrationOID"));
        if (!registration.getPerson().getStudent().getRegistrationsToEnrolInShiftByStudent().contains(registration)) {
            return null;
        }

        request.setAttribute("registration", registration);
        request.setAttribute("registrationOID", registration.getIdInternal().toString());
        request.setAttribute("ram", Boolean.TRUE);
        return registration;
    }

    public ActionForward addCourses(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixTransactionException, FenixFilterException {

        super.validateToken(request, actionForm, mapping, "error.transaction.enrollment");

        final Registration registration = getAndSetRegistration(request);
        if (registration == null) {
            addActionMessage(request, "errors.impossible.operation");
            return mapping.getInputForward();
        }

        checkParameter(request);

        final IUserView userView = getUserView(request);
        final DynaActionForm form = (DynaActionForm) actionForm;
        final Integer executionCourseId = (Integer) form.get("wantedCourse");

        try {
            WriteStudentAttendingCourse.runWriteStudentAttendingCourse(registration, executionCourseId);

        } catch (NotAuthorizedException exception) {
            addActionMessage(request, "error.attend.curricularCourse.impossibleToEnroll");
            return mapping.getInputForward();

        } catch (DomainException e) {
            addActionMessage(request, e.getMessage(), e.getArgs());
            return mapping.findForward("prepareShiftEnrollment");

        } catch (FenixServiceException exception) {
            addActionMessage(request, "errors.impossible.operation");
            return mapping.getInputForward();
        }

        return mapping.findForward("prepareShiftEnrollment");
    }

    public ActionForward removeCourses(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixTransactionException, FenixFilterException {

        super.validateToken(request, actionForm, mapping, "error.transaction.enrollment");

        final Registration registration = getAndSetRegistration(request);
        if (registration == null) {
            addActionMessage(request, "errors.impossible.operation");
            return mapping.getInputForward();
        }

        checkParameter(request);

        final DynaActionForm form = (DynaActionForm) actionForm;
        final Integer executionCourseId = (Integer) form.get("removedCourse");
        if (executionCourseId == null) {
            return mapping.findForward("prepareShiftEnrollment");
        }

//        try {
//            ServiceManagerServiceFactory.executeService("DeleteStudentAttendingCourse", new Object[] { registration,
//                    executionCourseId });
//
//        } catch (DomainException e) {
//            addActionMessage(request, e.getMessage());
//            return mapping.getInputForward();
//
//        } catch (FenixServiceException e) {
//            addActionMessage(request, "errors.impossible.operation");
//            return mapping.getInputForward();
//        }
//
//        return mapping.findForward("prepareShiftEnrollment");
        throw new UnsupportedOperationException("Service DeleteStudentAttendingCourse no longer exists!");
    }

    public ActionForward proceedToShiftEnrolment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixFilterException, FenixServiceException {

        checkParameter(request);
        final Integer classIdSelected = readClassSelected(request);

        final Registration registration = getAndSetRegistration(request);
        if (registration == null) {
            addActionMessage(request, "errors.impossible.operation");
            return mapping.getInputForward();
        }

        final ExecutionCourse executionCourse = getExecutionCourse(request);
        final List<SchoolClass> schoolClassesToEnrol =
                readStudentSchoolClassesToEnrolUsingExecutionCourse(request, registration, executionCourse);
        request.setAttribute("schoolClassesToEnrol", schoolClassesToEnrol);

        if (schoolClassesToEnrol.isEmpty()) {
            return mapping.findForward("prepareShiftEnrollment");
        }

        final SchoolClass schoolClass = setSelectedSchoolClass(request, classIdSelected, schoolClassesToEnrol);

        final IUserView userView = getUserView(request);

        final List infoClasslessons =
                ReadClassTimeTableByStudent.runReadClassTimeTableByStudent(registration, schoolClass, executionCourse);

        request.setAttribute("infoClasslessons", infoClasslessons);
        request.setAttribute("infoClasslessonsEndTime", Integer.valueOf(getEndTime(infoClasslessons)));

        final List infoLessons = ReadStudentTimeTable.run(registration);
        request.setAttribute("person", registration.getPerson());
        request.setAttribute("infoLessons", infoLessons);
        request.setAttribute("infoLessonsEndTime", Integer.valueOf(getEndTime(infoLessons)));

        return mapping.findForward("showShiftsToEnroll");
    }

    private SchoolClass setSelectedSchoolClass(HttpServletRequest request, final Integer classIdSelected,
            final List<SchoolClass> schoolClassesToEnrol) {

        final SchoolClass schoolClass =
                (classIdSelected != null) ? searchSchoolClassFrom(schoolClassesToEnrol, classIdSelected) : schoolClassesToEnrol
                        .get(0);
        request.setAttribute("selectedSchoolClass", schoolClass);

        return schoolClass;
    }

    private SchoolClass searchSchoolClassFrom(final List<SchoolClass> schoolClassesToEnrol, final Integer classId) {
        return (SchoolClass) CollectionUtils.find(schoolClassesToEnrol, new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                return ((SchoolClass) object).getIdInternal().equals(classId);
            }
        });
    }

    private List<SchoolClass> readStudentSchoolClassesToEnrolUsingExecutionCourse(HttpServletRequest request,
            final Registration registration, final ExecutionCourse executionCourse) {

        final List<SchoolClass> schoolClassesToEnrol = new ArrayList<SchoolClass>();
        if (executionCourse != null) {
            request.setAttribute("executionCourse", executionCourse);
            schoolClassesToEnrol.addAll(registration.getSchoolClassesToEnrolBy(executionCourse));

        } else {
            schoolClassesToEnrol.addAll(registration.getSchoolClassesToEnrol());
        }

        Collections.sort(schoolClassesToEnrol, SchoolClass.COMPARATOR_BY_NAME);
        return schoolClassesToEnrol;
    }

    private ExecutionCourse getExecutionCourse(HttpServletRequest request) {
        if (!StringUtils.isEmpty(request.getParameter("executionCourseID"))) {
            return rootDomainObject.readExecutionCourseByOID(Integer.valueOf(request.getParameter("executionCourseID")));
        } else {
            return null;
        }
    }

    private int getEndTime(List<InfoLesson> infoLessons) {
        int endTime = 0;
        for (final InfoLesson infoLesson : infoLessons) {
            int tempEnd = infoLesson.getFim().get(Calendar.HOUR_OF_DAY);
            if (infoLesson.getFim().get(Calendar.MINUTE) > 0) {
                tempEnd = tempEnd + 1;
            }
            if (endTime < tempEnd) {
                endTime = tempEnd;
            }
        }
        return endTime;
    }

    private void checkParameter(HttpServletRequest request) {
        final String selectCourses = request.getParameter("selectCourses");
        if (selectCourses != null) {
            request.setAttribute("selectCourses", selectCourses);
        }
    }

    private Integer readClassSelected(HttpServletRequest request) {
        String classIdSelectedString = request.getParameter("classId");
        Integer classIdSelected = null;
        if (classIdSelectedString != null) {
            classIdSelected = Integer.valueOf(classIdSelectedString);
        } else {
            classIdSelected = (Integer) request.getAttribute("classId");
        }
        return classIdSelected;
    }

    public ActionForward exitEnrollment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        return mapping.findForward("studentFirstPage");
    }

    public ActionForward prepareStartViewWarning(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        if (getAndSetRegistration(request) == null) {
            addActionMessage(request, "errors.impossible.operation");
            return mapping.getInputForward();
        } else {
            return mapping.findForward("prepareEnrollmentViewWarning");
        }
    }

    @Override
    protected String getLookupMapName(HttpServletRequest request, String keyName, ActionMapping mapping) throws ServletException {
        // some mapping forwards are made with this method name (in portuguese)
        // through annotations in actions, the problem is that if the language
        // is not portuguese
        // the system will not find the associated key in the correspondent
        // properties file of that language, naturally, hence this hack
        if (keyName.equals("Escolher Turnos")) {
            return getKeyMethodMap().get("link.shift.enrolement.edit").toString();
        }
        String key = super.getLookupMapName(request, keyName, mapping);
        if (key == null) {
            if (request.getParameter("method").equals("Escolher Turma")) {
                key = "link.shift.enrolement.edit";
            }
        }
        return key;
    }

    @Override
    protected Map getKeyMethodMap() {
        Map map = new HashMap();
        map.put("button.addCourse", "addCourses");
        map.put("button.removeCourse", "removeCourses");
        map.put("button.continue.enrolment", "prepareStartViewWarning");
        map.put("button.exit.shift.enrollment", "exitEnrollment");
        map.put("label.class", "proceedToShiftEnrolment");
        map.put("link.shift.enrolement.edit", "proceedToShiftEnrolment");
        map.put("button.clean", "proceedToShiftEnrolment");
        map.put("Escolher Turma", "exitEnrollment");
        return map;
    }

}