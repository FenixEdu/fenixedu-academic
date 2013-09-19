package net.sourceforge.fenixedu.webServices.jersey;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import net.fortuna.ical4j.model.Calendar;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.student.EnrolStudentInWrittenEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.PersonInformationBean;
import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationConclusionBean;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Photograph;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.accounting.Entry;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.paymentCodes.AccountingEventPaymentCode;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.photograph.PictureAvatar;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.student.curriculum.ICurriculum;
import net.sourceforge.fenixedu.domain.student.curriculum.ICurriculumEntry;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;
import net.sourceforge.fenixedu.domain.util.icalendar.CalendarFactory;
import net.sourceforge.fenixedu.domain.util.icalendar.EventBean;
import net.sourceforge.fenixedu.presentationTier.Action.ICalendarSyncPoint;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.pstm.AbstractDomainObject;
import pt.utl.ist.fenix.tools.resources.DefaultResourceBundleProvider;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

import com.google.common.net.HttpHeaders;

@Path("/private/v1")
public class JerseyPrivate {

    public final static String PERSONAL_SCOPE = "info";
    public final static String SCHEDULE_SCOPE = "schedule";
    public final static String ENROLMENTS_SCOPE = "enrollments";
    public final static String REGISTRATIONS_SCOPE = "registrations";
    public final static String CURRICULAR_SCOPE = "curricular";

    private final static String JSON_UTF8 = "application/json; charset=utf-8";

    private static final DateTimeFormatter formatDay = DateTimeFormat.forPattern("dd/MM/yyyy");
    private static final DateTimeFormatter formatHour = DateTimeFormat.forPattern("HH:mm");

    private List<Evaluation> evaluationsWithEnrolmentPeriodOpened;
    private List<Evaluation> evaluationsWithEnrolmentPeriodClosed;
    protected Integer evaluationType;
    private ExecutionSemester executionSemester;
    private Map<Integer, String> studentRooms;
    private List<Evaluation> notEnroledEvaluations;
    private List<Evaluation> enroledEvaluations;
    private List<Evaluation> evaluationsWithoutEnrolmentPeriod;
    private Map<Integer, List<ExecutionCourse>> executionCourses;
    private Map<Integer, Boolean> enroledEvaluationsForStudent;

    protected static final Integer ALL = Integer.valueOf(0);
    protected static final Integer EXAMS = Integer.valueOf(1);
    protected static final Integer WRITTENTESTS = Integer.valueOf(2);

    private String mls(MultiLanguageString mls) {
        if (mls == null) {
            return StringUtils.EMPTY;
        }
        return mls.getContent();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("hello")
    @FenixAPIScope(PERSONAL_SCOPE)
    public String hellofenix() {
        return "Hello! Private V1";
    }

    @GET
    @Produces(JSON_UTF8)
    @Path("person")
    @FenixAPIScope(PERSONAL_SCOPE)
    public String person() {

        JSONObject jsonResult = new JSONObject();

        JSONArray jsonArrayRole = new JSONArray();

        final Person person = getPerson();
        if (person == null) {
            return jsonResult.toString();
        }

        if (isTeacher(person)) {
            JSONObject jsonRoleInfo = new JSONObject();
            jsonRoleInfo.put("roleName", "TEACHING");
            jsonArrayRole.add(jsonRoleInfo);
        }

        if (person.hasRole(RoleType.STUDENT)) {
            JSONObject jsonRoleInfo = new JSONObject();
            jsonRoleInfo.put("roleName", RoleType.STUDENT.getName());
            jsonArrayRole.add(jsonRoleInfo);
        }

        if (person.hasRole(RoleType.ALUMNI)) {
            JSONObject jsonRoleInfo = new JSONObject();
            jsonRoleInfo.put("roleName", RoleType.ALUMNI.getName());
            jsonArrayRole.add(jsonRoleInfo);
        }

        if (person.hasRole(RoleType.TEACHER)) {
            JSONObject jsonRoleInfo = new JSONObject();
            jsonRoleInfo.put("roleName", RoleType.TEACHER.getName());
            jsonArrayRole.add(jsonRoleInfo);
        }

        jsonResult.put("roles", jsonArrayRole);

        PersonInformationBean pib = new PersonInformationBean(person);
        jsonResult.put("name", pib.getName());
        jsonResult.put("campus", pib.getCampus());
        jsonResult.put("mail", pib.getEmail());
        jsonResult.put("istId", person.getIstUsername());

        jsonResult.put("personalMail", getInformation(pib.getPersonalEmails()));

        jsonResult.put("workMail", getInformation(pib.getWorkEmails()));

        jsonResult.put("webAddress", getInformation(pib.getPersonalWebAdresses()));

        jsonResult.put("workWebAddress", getInformation(pib.getWorkWebAdresses()));

        jsonResult.put("address", person.getAddress());

        addPhoto(jsonResult, person);

        return jsonResult.toJSONString();
    }

    private void addPhoto(final JSONObject jsonResult, final Person person) {
        final JSONObject jsonPhoto = new JSONObject();
        try {
            final Photograph personalPhoto = person.getPersonalPhoto();
            if (person.isPhotoAvailableToCurrentUser()) {
                final PictureAvatar avatar = personalPhoto.getAvatar();
                byte[] bytes = avatar.getBytes();
                jsonPhoto.put("type", avatar.getPictureFileFormat().getMimeType());
                jsonPhoto.put("data", Base64.encodeBase64String(bytes));
            }
        } catch (NullPointerException npe) {

        } finally {
            jsonResult.put("photo", jsonPhoto);
        }
    }

    private Person getPerson() {
        IUserView user = UserView.getUser();
        if (user != null) {
            return user.getPerson();
        }
        return null;
    }

    private JSONArray getInformation(List<String> list) {
        JSONArray jsonArray = new JSONArray();
        for (String string : list) {
            JSONObject jsonInfo = new JSONObject();
            jsonInfo.put("info", string);
            jsonArray.add(jsonInfo);
        }
        return jsonArray;

    }

    @FenixAPIScope(CURRICULAR_SCOPE)
    @GET
    @Produces(JSON_UTF8)
    @Path("person/courses/")
    public String personCourses(@QueryParam("sem") String sem, @QueryParam("year") String year) {

        final Person person = getPerson();
        JSONObject jsonResult = new JSONObject();

        if (person == null) {
            return jsonResult.toJSONString();
        }

        PersonInformationBean pib = new PersonInformationBean(person);
        ExecutionSemester executionSemester = getExecutionSemester(sem, year);
        jsonResult.put("year", executionSemester.getYear());
        jsonResult.put("semester", executionSemester.getSemester());

        JSONArray jsonEnrolments = new JSONArray();
        JSONArray jsonTeaching = new JSONArray();

        final Student foundStudent = person.getStudent();

        for (Registration registration : foundStudent.getAllRegistrations()) {
            for (Enrolment enrolment : registration.getEnrolments(executionSemester)) {
                JSONObject jsonEnrolmentInfo = new JSONObject();
                jsonEnrolmentInfo.put("courseId", enrolment.getExecutionCourseFor(executionSemester).getExternalId());
                jsonEnrolmentInfo.put("grade", enrolment.getGrade().getValue());
                jsonEnrolmentInfo.put("sigla", enrolment.getExecutionCourseFor(executionSemester).getSigla());
                jsonEnrolmentInfo.put("name", enrolment.getExecutionCourseFor(executionSemester).getName());
                jsonEnrolments.add(jsonEnrolmentInfo);
            }
        }

        for (final Professorship professorship : person.getProfessorships(executionSemester)) {
            JSONObject teaching = new JSONObject();
            final ExecutionCourse executionCourse = professorship.getExecutionCourse();
            final ExecutionSemester executionCourseSemester = executionCourse.getExecutionPeriod();

            teaching.put("id", executionCourse.getExternalId());
            teaching.put("name", executionCourse.getName());
            teaching.put("sigla", executionCourse.getSigla());
            jsonTeaching.add(teaching);
        }
        jsonResult.put("coursesEnrolled", jsonEnrolments);
        jsonResult.put("coursesTeaching", jsonTeaching);
        return jsonResult.toJSONString();
    }

    @FenixAPIScope(SCHEDULE_SCOPE)
    @GET
    @Path("person/calendar/evaluations")
    public Response calendarEvaluation(@QueryParam("format") String format, @Context HttpServletRequest httpRequest) {
        final Person person = getPerson();

        if ("calendar".equals(format)) {
            final String serverName = httpRequest.getServerName();
            final int serverPort = httpRequest.getServerPort();
            final String serverScheme = httpRequest.getScheme();

            String evaluationCalendarICal = evaluationCalendarICal(person, serverScheme, serverName, serverPort);
            return Response.ok(evaluationCalendarICal, "text/calendar;charset=UTF-8").build();
        } else {
            return Response.status(Status.OK).header(HttpHeaders.CONTENT_TYPE, "application/json; charset=UTF-8")
                    .entity(evaluationCalendarJson(person)).build();
        }
    }

    private String evaluationCalendarICal(Person person, String serverScheme, String serverName, int serverPort) {
        ICalendarSyncPoint calendarSyncPoint = new ICalendarSyncPoint();

        List<EventBean> listEventBean = calendarSyncPoint.getExams(person.getUser(), serverScheme, serverName, serverPort);
        listEventBean.addAll(calendarSyncPoint.getTeachingExams(person.getUser(), serverScheme, serverName, serverPort));

        Calendar createCalendar = CalendarFactory.createCalendar(listEventBean);

        return createCalendar.toString();
    }

    private String evaluationCalendarJson(Person person) {
        JSONObject jsonResult = new JSONObject();
        final User user = person.getUser();
        if (user == null) {
            return jsonResult.toJSONString();
        }

        JSONArray jsonEvaluation = new JSONArray();

        jsonResult.put("year", ExecutionYear.readCurrentExecutionYear().getName());

        ICalendarSyncPoint calendarSyncPoint = new ICalendarSyncPoint();
        List<EventBean> listEventBean = calendarSyncPoint.getExams(user, "", "", 0);
        listEventBean.addAll(calendarSyncPoint.getTeachingExams(user, "", "", 0));

        for (EventBean eventBean : listEventBean) {

            JSONObject jsonEvaluationInfo = new JSONObject();
            jsonEvaluationInfo.put("startDay", formatDay.print(eventBean.getBegin()));
            jsonEvaluationInfo.put("endDay", formatDay.print(eventBean.getEnd()));
            jsonEvaluationInfo.put("startTime", formatHour.print(eventBean.getBegin()));
            jsonEvaluationInfo.put("endTime", formatHour.print(eventBean.getEnd()));
            jsonEvaluationInfo.put("location", eventBean.getLocation());
            jsonEvaluationInfo.put("title ", eventBean.getTitle());
            jsonEvaluationInfo.put("url", eventBean.getUrl());
            jsonEvaluationInfo.put("note", eventBean.getNote());
            jsonEvaluationInfo.put("isAllDay", eventBean.isAllDay());
            jsonEvaluation.add(jsonEvaluationInfo);
        }
        jsonResult.put("evaluation", jsonEvaluation);
        return jsonResult.toJSONString();
    }

    @FenixAPIScope(SCHEDULE_SCOPE)
    @GET
    @Path("person/calendar/classes")
    public Response calendarClasses(@QueryParam("format") String format, @Context HttpServletRequest httpRequest) {
        Person person = getPerson();
        if ("calendar".equals(format)) {
            final String serverName = httpRequest.getServerName();
            final int serverPort = httpRequest.getServerPort();
            final String serverScheme = httpRequest.getScheme();
            String classesCalendarICal = classesCalendarICal(person, serverScheme, serverName, serverPort);
            return Response.ok(classesCalendarICal, "text/calendar; charset=UTF-8").build();
        } else {
            return Response.status(Status.OK).header(HttpHeaders.CONTENT_TYPE, JSON_UTF8).entity(classesCalendarJson(person))
                    .build();
        }
    }

    private String classesCalendarICal(Person person, String serverScheme, String serverName, int serverPort) {
        ICalendarSyncPoint calendarSyncPoint = new ICalendarSyncPoint();

        List<EventBean> listEventBean = calendarSyncPoint.getClasses(person.getUser(), serverScheme, serverName, serverPort);
        listEventBean.addAll(calendarSyncPoint.getTeachingClasses(person.getUser(), serverScheme, serverName, serverPort));

        Calendar createCalendar = CalendarFactory.createCalendar(listEventBean);

        return createCalendar.toString();
    }

    private String classesCalendarJson(Person person) {

        JSONObject jsonResult = new JSONObject();
        final User user = person.getUser();
        if (user == null) {
            return jsonResult.toJSONString();
        }

        JSONArray jsonEvaluation = new JSONArray();

        jsonResult.put("year", ExecutionYear.readCurrentExecutionYear().getName());
        ICalendarSyncPoint calendarSyncPoint = new ICalendarSyncPoint();
        List<EventBean> listEventBean = calendarSyncPoint.getClasses(user, "", "", 0);
        listEventBean.addAll(calendarSyncPoint.getTeachingClasses(user, "", "", 0));

        for (EventBean eventBean : listEventBean) {
            JSONObject jsonEvaluationInfo = new JSONObject();
            jsonEvaluationInfo.put("startDay", formatDay.print(eventBean.getBegin()));
            jsonEvaluationInfo.put("endDay", formatDay.print(eventBean.getEnd()));
            jsonEvaluationInfo.put("startTime", formatHour.print(eventBean.getBegin()));
            jsonEvaluationInfo.put("endTime", formatHour.print(eventBean.getEnd()));

            jsonEvaluationInfo.put("location", eventBean.getLocation());
            jsonEvaluationInfo.put("title ", eventBean.getTitle());
            jsonEvaluationInfo.put("url", eventBean.getUrl());
            jsonEvaluationInfo.put("note", eventBean.getNote());
            jsonEvaluationInfo.put("isAllDay", eventBean.isAllDay());
            jsonEvaluation.add(jsonEvaluationInfo);
        }
        jsonResult.put("classes", jsonEvaluation);
        return jsonResult.toJSONString();
    }

    @FenixAPIScope(CURRICULAR_SCOPE)
    @GET
    @Path("person/curriculum")
    @Produces(JSON_UTF8)
    public String personCurriculum() {
        Person person = getPerson();

        JSONArray jsonResult = new JSONArray();
        if (person.hasStudent()) {
            jsonResult = getStudentStatistics(person.getStudent().getRegistrations());

            /*
            if (person.getStudent().hasAlumni()) {
                //JSONArray jsonResult = getStudentStatistics(student.getAlumni().readAlumniRegistrations(bean));

            } else {
                JSONArray jsonResult = getStudentStatistics(student.getRegistrations());
            }
             */
        } else if (person.getStudent().hasAlumni()) {
//            jsonResult = getStudentStatistics(person.getStudent().getAlumni().readAlumniRegistrations(bean)());
        }
        return jsonResult.toJSONString();
    }

    private JSONArray getStudentStatistics(List<Registration> registrationsList) {

        JSONArray jsonResult = new JSONArray();

        if (registrationsList == null) {
            return jsonResult;
        }

        for (Registration registration : registrationsList) {
            for (StudentCurricularPlan studentCurricularPlan : registration.getStudentCurricularPlans()) {
                JSONObject jsonCurricularPlanInfo = new JSONObject();
                jsonCurricularPlanInfo.put("curricularName", studentCurricularPlan.getName());
                jsonCurricularPlanInfo.put("curricularExternalId", studentCurricularPlan.getDegree().getExternalId());
                jsonCurricularPlanInfo.put("curricularDegreeType", studentCurricularPlan.getDegreeType().getName());
                jsonCurricularPlanInfo.put("curricularCampus", studentCurricularPlan.getCurrentCampus().getName());
                jsonCurricularPlanInfo.put("curricularPresentationName", studentCurricularPlan.getPresentationName());
                jsonCurricularPlanInfo.put("curricularStart", studentCurricularPlan.getStartDateYearMonthDay() + "");
                jsonCurricularPlanInfo.put("curricularEnd", studentCurricularPlan.getEndDate() + "");

                RegistrationConclusionBean registrationConclusionBean = new RegistrationConclusionBean(registration);

                ICurriculum icurriculum = registrationConclusionBean.getCurriculumForConclusion();

                jsonCurricularPlanInfo.put("curricularEcts", icurriculum.getSumEctsCredits());
                jsonCurricularPlanInfo.put("curricularAverage", icurriculum.getAverage());

                jsonCurricularPlanInfo.put("curricularCalculatedAverage", icurriculum.getRoundedAverage());

                jsonCurricularPlanInfo.put("curricularFinished", registrationConclusionBean.isConcluded());

                JSONArray jsonCoursesInfo = new JSONArray();

                if (icurriculum.getCurriculumEntries() != null) {
                    jsonCurricularPlanInfo.put("curricularApprovedCourses", icurriculum.getCurriculumEntries().size());
                } else {
                    jsonCurricularPlanInfo.put("curricularApprovedCourses", "");

                }

                for (ICurriculumEntry iCurriculumEntry : icurriculum.getCurriculumEntries()) {
                    JSONObject jsonCourseInfo = new JSONObject();

                    jsonCourseInfo.put("courseName", mls(iCurriculumEntry.getPresentationName()));
                    jsonCourseInfo.put("courseGrade", iCurriculumEntry.getGradeValue());
                    jsonCourseInfo.put("courseEcts", iCurriculumEntry.getEctsCreditsForCurriculum());
                    String executionCourseOid = StringUtils.EMPTY;
                    if (iCurriculumEntry instanceof Enrolment) {
                        Enrolment enrolment = (Enrolment) iCurriculumEntry;
                        ExecutionCourse executionCourse = enrolment.getExecutionCourseFor(enrolment.getExecutionPeriod());
                        if (executionCourse != null) {
                            executionCourseOid = executionCourse.getExternalId();
                        }
                    }
                    /*
                     String executionCourseSigla = iCurriculumEntry.getCode();
                     
                    if (!StringUtils.isBlank(executionCourseSigla)) {
                        ExecutionCourse executionCourse =
                                ExecutionCourse.readBySiglaAndExecutionPeriod(executionCourseSigla,
                                        iCurriculumEntry.getExecutionPeriod());
                        if (executionCourse != null) {
                            executionCourseOid = executionCourse.getExternalId();
                        }
                    }
                    */
                    jsonCourseInfo.put("courseExternalId", executionCourseOid);
                    //enrolments.getExecutionCourseFor(enrolments.getExecutionPeriod()).getExternalId());

                    jsonCourseInfo.put("coursesSemester", iCurriculumEntry.getExecutionPeriod().getSemester());
                    jsonCourseInfo.put("courseYear", iCurriculumEntry.getExecutionYear().getYear());
                    jsonCoursesInfo.add(jsonCourseInfo);
                }
                jsonCurricularPlanInfo.put("curricularCourseInfo", jsonCoursesInfo);
                jsonResult.add(jsonCurricularPlanInfo);

            }
        }

        /*
                jsonCurricularPlanInfo.put("curricularEcts", registrationConclusionBean.getEctsCredits());
                jsonCurricularPlanInfo.put("curricularAverage", registrationConclusionBean.getAverage());

                jsonCurricularPlanInfo.put("curricularCalculatedAverage", registrationConclusionBean.getCalculatedAverage());
                jsonCurricularPlanInfo.put("curricularFinished",
                        registrationConclusionBean.getHasAccessToRegistrationConclusionProcess());
                jsonCurricularPlanInfo.put("curricularFinished", registrationConclusionBean.isConcluded());

                JSONArray jsonCoursesInfo = new JSONArray();

                if (registrationConclusionBean.getCycleCurriculumGroup() != null) {
                    jsonCurricularPlanInfo.put("curricularApprovedCourses", registrationConclusionBean.getCycleCurriculumGroup()
                            .getApprovedCurriculumLines().size());

                    for (CurriculumLine curriculumLines : registrationConclusionBean.getCycleCurriculumGroup()
                            .getApprovedCurriculumLines()) {

                        for (Enrolment enrolments : curriculumLines.getEnrolments()) {
                            JSONObject jsonCourseInfo = new JSONObject();

                            jsonCourseInfo.put("courseName", mls(enrolments.getPresentationName()));
                            jsonCourseInfo.put("courseGrade", enrolments.getGrade().getValue());
                            jsonCourseInfo.put("courseEcts", enrolments.getEctsCredits());
                            jsonCourseInfo.put("courseExternalId",
                                    enrolments.getExecutionCourseFor(enrolments.getExecutionPeriod()).getExternalId());
                            jsonCourseInfo.put("coursesSemester", enrolments.getExecutionPeriod().getSemester());
                            jsonCourseInfo.put("courseYear", enrolments.getExecutionYear().getYear());
                            jsonCoursesInfo.add(jsonCourseInfo);
                        }
                    }
                }
                jsonCurricularPlanInfo.put("curricularCourseInfo", jsonCoursesInfo);
                jsonResult.add(jsonCurricularPlanInfo);
            }



        }*/

        return jsonResult;
    }

    /*

      private JSONArray getStudentStatistics(List<Registration> registrations) {

        JSONArray jsonCurricularPlan = new JSONArray();
        JSONArray jsonExecutionSemester = new JSONArray();

        for (Registration registration : registrations) {

            for (StudentCurricularPlan studentCurricularPlan : registration.getStudentCurricularPlans()) {
                JSONObject jsonCurricularPlanInfo = new JSONObject();
                jsonCurricularPlanInfo.put("curricularName", studentCurricularPlan.getName());
                jsonCurricularPlanInfo.put("curricularStart", studentCurricularPlan.getStartDateYearMonthDay() + "");
                jsonCurricularPlanInfo.put("curricularEnd", studentCurricularPlan.getEndDate() + "");

                jsonCurricularPlanInfo.put("curricularPresentationName", studentCurricularPlan.getPresentationName());
                jsonCurricularPlanInfo.put("curricularApprovedCourses",
                        studentCurricularPlan.getNumberOfApprovedCurricularCourses());

                //studentCurricularPlan.getFinalAverage(cycleType);
                jsonCurricularPlanInfo.put("curricularDegreeType", studentCurricularPlan.getDegreeType().getName());
                jsonCurricularPlanInfo.put("curricularCampus", studentCurricularPlan.getCurrentCampus().getName());
                jsonCurricularPlanInfo.put("curricularApprovedEcts", studentCurricularPlan.getApprovedEctsCredits());

                for (ExecutionSemester executionSemester : studentCurricularPlan.getEnrolmentsExecutionPeriods()) {

                    jsonExecutionSemester.add(getRegistrationInfo(executionSemester, studentCurricularPlan));
                }
                jsonCurricularPlanInfo.put("curricularExecutionSemester", jsonExecutionSemester);
                jsonCurricularPlan.add(jsonCurricularPlanInfo);
            }

        }

        return jsonCurricularPlan;
    }

    public JSONObject getRegistrationInfo(ExecutionSemester executionSemester, StudentCurricularPlan studentCurricularPlan) {
        JSONObject jsonexecutionPeriodStatisticsBean = new JSONObject();
        JSONArray jsonexecutionCourseStatisticsBean = new JSONArray();

        ExecutionPeriodStatisticsBean executionPeriodStatisticsBean = new ExecutionPeriodStatisticsBean(executionSemester);
        executionPeriodStatisticsBean.addEnrolmentsWithinExecutionPeriod(studentCurricularPlan
                .getEnrolmentsByExecutionPeriod(executionSemester));

        jsonexecutionPeriodStatisticsBean.put("approvedCourses", executionPeriodStatisticsBean.getApprovedEnrolmentsNumber());
        jsonexecutionPeriodStatisticsBean.put("approvedRation", executionPeriodStatisticsBean.getApprovedRatio());
        jsonexecutionPeriodStatisticsBean.put("aritmeticAverage", executionPeriodStatisticsBean.getAritmeticAverage());
        jsonexecutionPeriodStatisticsBean.put("totalEnrolment", executionPeriodStatisticsBean.getTotalEnrolmentsNumber());
        jsonexecutionPeriodStatisticsBean.put("year", executionPeriodStatisticsBean.getExecutionPeriod().getYear());
        jsonexecutionPeriodStatisticsBean.put("sem", executionPeriodStatisticsBean.getExecutionPeriod().getSemester());

        for (Enrolment enrolment : executionPeriodStatisticsBean.getEnrolmentsWithinExecutionPeriod()) {
            JSONObject jsonCourseInfo = new JSONObject();
            jsonCourseInfo.put("courseName", mls(enrolment.getPresentationName()));
            jsonCourseInfo.put("courseGrade", enrolment.getGrade().getValue());
            jsonCourseInfo.put("courseEnrolments", enrolment.getNumberOfTotalEnrolmentsInThisCourse());
            jsonCourseInfo.put("couseCredits", enrolment.getEctsCredits());;
            ExecutionCourse executionCourse = enrolment.getExecutionCourseFor(executionPeriodStatisticsBean.getExecutionPeriod());

            if (executionCourse != null) {
                String externalID = executionCourse.getExternalId();
                jsonCourseInfo.put("courseId", externalID);
            }
            jsonexecutionCourseStatisticsBean.add(jsonCourseInfo);
        }
        jsonexecutionPeriodStatisticsBean.put("courseInfo", jsonexecutionCourseStatisticsBean);
        return jsonexecutionPeriodStatisticsBean;
    }
     */

    private List<Event> calculateNotPayedEvents(final Person person) {

        final List<Event> result = new ArrayList<Event>();

        result.addAll(person.getNotPayedEventsPayableOn(null, false));
        result.addAll(person.getNotPayedEventsPayableOn(null, true));

        return result;
    }

    @FenixAPIScope(PERSONAL_SCOPE)
    @GET
    @Path("person/payments")
    @Produces(JSON_UTF8)
    public String personPayments() {

        JSONObject jsonResult = new JSONObject();

        JSONArray jsonPayments = new JSONArray();
        JSONArray jsonNotPayed = new JSONArray();

        Properties props = new Properties();
        props.setProperty("application", "resources.ApplicationResources");
        props.setProperty("enum", "resources.EnumerationResources");
        props.setProperty("default", "resources.ApplicationResources");
        DefaultResourceBundleProvider provider = new DefaultResourceBundleProvider(props);
        Person person = getPerson();
        for (Entry entry : person.getPayments()) {
            JSONObject paymentInfo = new JSONObject();

            paymentInfo.put("amount", entry.getOriginalAmount().getAmountAsString());
            paymentInfo.put("name", entry.getPaymentMode().getName());
            paymentInfo.put("description", entry.getDescription().toString(provider));
            paymentInfo.put("date", formatDay.print(entry.getWhenRegistered()));
            jsonPayments.add(paymentInfo);

        }

        List<Event> notPayedEvents = calculateNotPayedEvents(person);

        for (Event event : notPayedEvents) {

            for (AccountingEventPaymentCode accountingEventPaymentCode : event.getNonProcessedPaymentCodes()) {
                JSONObject notPayedInfo = new JSONObject();

                notPayedInfo.put("description", accountingEventPaymentCode.getDescription());
                notPayedInfo.put("startDate", formatDay.print(accountingEventPaymentCode.getStartDate()));
                notPayedInfo.put("endDate", formatDay.print(accountingEventPaymentCode.getEndDate()));
                notPayedInfo.put("entity", accountingEventPaymentCode.getEntityCode());
                notPayedInfo.put("reference", accountingEventPaymentCode.getFormattedCode());
                notPayedInfo.put("amount", accountingEventPaymentCode.getMaxAmount().getAmountAsString());
                jsonNotPayed.add(notPayedInfo);
            }
        }
        jsonResult.put("payments", jsonPayments);
        jsonResult.put("notPayed", jsonNotPayed);
        return jsonResult.toString();
    }

    @FenixAPIScope(ENROLMENTS_SCOPE)
    @GET
    @Path("person/evaluations")
    @Produces(JSON_UTF8)
    public String evaluations() {
        JSONObject jsonResult = new JSONObject();
        JSONArray evaluationPeriodClose = new JSONArray();
        JSONArray evaluationPeriodOpen = new JSONArray();

        Person person = getPerson();
        processEvaluations(person.getStudent());

        for (Evaluation evaluation : evaluationsWithEnrolmentPeriodClosed) {
            JSONObject jsonEvaluationInfo = new JSONObject();
            jsonEvaluationInfo.put("evaluationName", evaluation.getPresentationName());
            jsonEvaluationInfo.put("evaluationType", evaluation.getEvaluationType().toString());
            jsonEvaluationInfo.put("evaluationType", evaluation.getExternalId());
            for (ExecutionCourse executionCourse : evaluation.getAssociatedExecutionCourses()) {
                System.out.println("-" + executionCourse.getName());
            }
            evaluationPeriodClose.add(jsonEvaluationInfo);
        }
        for (Evaluation evaluation : evaluationsWithEnrolmentPeriodOpened) {
            JSONObject jsonEvaluationInfo = new JSONObject();
            jsonEvaluationInfo.put("evaluationName", evaluation.getPresentationName());
            jsonEvaluationInfo.put("evaluationType", evaluation.getEvaluationType().toString());
            jsonEvaluationInfo.put("evaluationType", evaluation.getExternalId());
            for (ExecutionCourse executionCourse : evaluation.getAssociatedExecutionCourses()) {
                System.out.println("-" + executionCourse.getName());
            }
            evaluationPeriodOpen.add(jsonEvaluationInfo);
        }

        jsonResult.put("evaluationPeriodClose", evaluationPeriodClose);
        jsonResult.put("evaluationPeriodOpen", evaluationPeriodOpen);
        return jsonResult.toString();
    }

    //@Scope(REGISTRATIONS_SCOPE)
    @PUT
    @Produces(JSON_UTF8)
    @Path("person/evaluations/{oid}")
    public String evaluations(@PathParam("oid") String oid) {
        JSONObject jsonResult = new JSONObject();
        try {
            WrittenEvaluation eval = getDomainObject(oid);
            EnrolStudentInWrittenEvaluation.runEnrolStudentInWrittenEvaluation(getPerson().getUsername(), eval.getExternalId());
            return evaluations();
        } catch (Exception e) {
            throw newApplicationError(Status.BAD_REQUEST, "problem found", "problem found");
        }
    }

    /*
    public Integer getEvaluationType() {
        if (this.evaluationType == null) {
            this.evaluationType = ALL;
        }
        return this.evaluationType;
    }
     */
/*
    public String getEvaluationTypeString() {
        final Integer type = getEvaluationType();
        if (type != null && type.equals(EXAMS)) {
            return "net.sourceforge.fenixedu.domain.Exam";
        } else if (type != null && type.equals(WRITTENTESTS)) {
            return "net.sourceforge.fenixedu.domain.WrittenTest";
        }
        return "";
    }
 */
    public Map<Integer, String> getStudentRooms() {
        if (this.studentRooms == null) {
            this.studentRooms = new HashMap<Integer, String>();
        }
        return this.studentRooms;
    }

    public List<Evaluation> getEvaluationsWithoutEnrolmentPeriod() {
        if (this.evaluationsWithoutEnrolmentPeriod == null) {
            this.evaluationsWithoutEnrolmentPeriod = new ArrayList();
        }
        return this.evaluationsWithoutEnrolmentPeriod;
    }

    public Map<Integer, Boolean> getEnroledEvaluationsForStudent() {
        if (this.enroledEvaluationsForStudent == null) {
            this.enroledEvaluationsForStudent = new HashMap<Integer, Boolean>();
        }
        return this.enroledEvaluationsForStudent;
    }

    public Map<Integer, List<ExecutionCourse>> getExecutionCourses() {
        if (this.executionCourses == null) {
            this.executionCourses = new HashMap<Integer, List<ExecutionCourse>>();
        }
        return this.executionCourses;
    }

    private void processEvaluations(Student student) {
        this.evaluationsWithEnrolmentPeriodClosed = new ArrayList();
        this.evaluationsWithEnrolmentPeriodOpened = new ArrayList();

        //final String evaluationType = getEvaluationTypeString();
        for (final Registration registration : student.getRegistrations()) {

            if (!registration.hasStateType(getExecutionSemester("", ""), RegistrationStateType.REGISTERED)) {
                continue;
            }

            for (final WrittenEvaluation writtenEvaluation : registration.getWrittenEvaluations(getExecutionSemester("", ""))) {

                if (writtenEvaluation instanceof Exam) {
                    final Exam exam = (Exam) writtenEvaluation;
                    if (!exam.isExamsMapPublished()) {
                        continue;
                    }
                }

                try {
                    if (writtenEvaluation.isInEnrolmentPeriod()) {
                        this.evaluationsWithEnrolmentPeriodOpened.add(writtenEvaluation);
                    } else {
                        this.evaluationsWithEnrolmentPeriodClosed.add(writtenEvaluation);
                        final AllocatableSpace room = registration.getRoomFor(writtenEvaluation);
                        getStudentRooms().put(writtenEvaluation.getIdInternal(), room != null ? room.getNome() : "-");
                    }
                } catch (final DomainException e) {
                    getEvaluationsWithoutEnrolmentPeriod().add(writtenEvaluation);
                    final AllocatableSpace room = registration.getRoomFor(writtenEvaluation);
                    getStudentRooms().put(writtenEvaluation.getIdInternal(), room != null ? room.getNome() : "-");
                } finally {
                    getEnroledEvaluationsForStudent().put(writtenEvaluation.getIdInternal(),
                            Boolean.valueOf(registration.isEnroledIn(writtenEvaluation)));
                    getExecutionCourses().put(writtenEvaluation.getIdInternal(),
                            writtenEvaluation.getAttendingExecutionCoursesFor(registration));
                }

            }
        }

        Collections.sort(this.evaluationsWithEnrolmentPeriodClosed, new BeanComparator("dayDate"));
        Collections.sort(this.evaluationsWithEnrolmentPeriodOpened, new BeanComparator("dayDate"));
        Collections.sort(getEvaluationsWithoutEnrolmentPeriod(), new BeanComparator("dayDate"));
    }

    public boolean isTeacher(Person person) {
        if (person == null) {
            return false;
        }

        if (person.getProfessorshipsCount() != 0) {
            return true;
        }

        return false;
    }

    private ExecutionSemester getExecutionSemester(String sem, String year) {
        ExecutionSemester executionSemester;

        boolean isBlank = StringUtils.isBlank(sem) || StringUtils.isBlank(year);
        if (!isBlank) {
            int semester;
            try {
                semester = Integer.parseInt(sem);
            } catch (NumberFormatException e) {
                semester = 0;
            }
            executionSemester = ExecutionSemester.readBySemesterAndExecutionYear(semester, year);
            if (executionSemester == null) {
                executionSemester = ExecutionSemester.readActualExecutionSemester();
            }
        } else {
            executionSemester = ExecutionSemester.readActualExecutionSemester();
        }
        return executionSemester;
    }

    private ExecutionYear getExecutionYear(String year) {
        //year String "2012/2013"

        ExecutionYear executionYear;

        boolean isBlank = StringUtils.isBlank(year);
        if (!isBlank) {
            executionYear = ExecutionYear.readExecutionYearByName(year);
            if (executionYear == null) {
                executionYear = ExecutionYear.readCurrentExecutionYear();
            }
        } else {
            executionYear = ExecutionYear.readCurrentExecutionYear();
        }
        return executionYear;
    }

    @SuppressWarnings("unchecked")
    private <T extends DomainObject> T getDomainObject(String externalId) {
        try {
            T domainObject = (T) AbstractDomainObject.fromExternalId(externalId);
            if (domainObject == null) {
                throw newApplicationError(Status.NOT_FOUND, "id not found", "id not found");
            }
            return domainObject;
        } catch (NumberFormatException nfe) {
            throw newApplicationError(Status.BAD_REQUEST, "invalid id", "invalid id");
        }
    }

    private WebApplicationException newApplicationError(Status status, String error, String description) {
        JSONObject errorObject = new JSONObject();
        errorObject.put("error", error);
        errorObject.put("description", description);
        return new WebApplicationException(Response.status(status).entity(errorObject.toJSONString()).build());
    }

}
