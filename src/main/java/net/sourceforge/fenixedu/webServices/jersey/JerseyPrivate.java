package net.sourceforge.fenixedu.webServices.jersey;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import net.fortuna.ical4j.model.Calendar;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.PersonInformationBean;
import net.sourceforge.fenixedu.dataTransferObject.student.ExecutionPeriodStatisticsBean;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.accounting.Entry;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.util.icalendar.CalendarFactory;
import net.sourceforge.fenixedu.domain.util.icalendar.EventBean;
import net.sourceforge.fenixedu.presentationTier.Action.ICalendarSyncPoint;

import org.apache.commons.lang.StringUtils;
import org.apache.fop.fo.Status;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import pt.ist.fenixWebFramework.rendererExtensions.util.RendererMessageResourceProvider;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

import com.google.common.net.HttpHeaders;

@Path("/private/v1")
public class JerseyPrivate {

    //private final static String istID = "ist158444";
    private static final SimpleDateFormat dataFormatDay = new SimpleDateFormat("dd/MM/yyyy");
    private static final SimpleDateFormat dataFormatHour = new SimpleDateFormat("HH:mm");

    private static final DateTimeFormatter formatDay = DateTimeFormat.forPattern("dd/MM/yyyy");
    private static final DateTimeFormatter formatHour = DateTimeFormat.forPattern("HH:mm");

    private static String mls(MultiLanguageString mls) {
        if (mls == null) {
            return StringUtils.EMPTY;
        }
        return mls.getContent();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("hello")
    @FenixAPIScope("info")
    public String hellofenix() {
        return "Hello! Private V1";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("person")
    @FenixAPIScope("info")
    public String person(@QueryParam("istid") String istid) {

        /*
        if (UserView.getUser() != null) {
            istid = UserView.getUser().getUsername();
        }
         */
        JSONObject jsonResult = new JSONObject();

        JSONArray jsonArrayRole = new JSONArray();
        final Person person = Person.readPersonByUsername(istid);

        if (isTeacher(istid)) {
            JSONObject jsonRoleInfo = new JSONObject();
            jsonRoleInfo.put("roleName", "TEACHING");
            jsonArrayRole.add(jsonRoleInfo);
        }

        if (person.hasRole(RoleType.STUDENT)) {
            JSONObject jsonRoleInfo = new JSONObject();
            jsonRoleInfo.put("roleName", RoleType.STUDENT);
            jsonArrayRole.add(jsonRoleInfo);
        }

        if (person.hasRole(RoleType.ALUMNI)) {
            JSONObject jsonRoleInfo = new JSONObject();
            jsonRoleInfo.put("roleName", RoleType.ALUMNI);
            jsonArrayRole.add(jsonRoleInfo);
        }

        if (person.hasRole(RoleType.TEACHER)) {
            JSONObject jsonRoleInfo = new JSONObject();
            jsonRoleInfo.put("roleName", RoleType.TEACHER);
            jsonArrayRole.add(jsonRoleInfo);
        }

        jsonResult.put("roles", jsonArrayRole);

        PersonInformationBean pib = new PersonInformationBean(person);
        jsonResult.put("name", pib.getName());
        jsonResult.put("campus", pib.getCampus());
        jsonResult.put("mail", pib.getEmail());

        jsonResult.put("personalMail", getInformation(pib.getPersonalEmails()));

        jsonResult.put("workMail", getInformation(pib.getWorkEmails()));

        jsonResult.put("webAddress", getInformation(pib.getPersonalWebAdresses()));

        jsonResult.put("workWebAddress", getInformation(pib.getWorkWebAdresses()));

        jsonResult.put("address", person.getAddress());

        return jsonResult.toJSONString();
    }

    private static JSONArray getInformation(List<String> list) {
        JSONArray jsonArray = new JSONArray();
        for (String string : list) {
            JSONObject jsonInfo = new JSONObject();
            jsonInfo.put("info", string);
            jsonArray.add(jsonInfo);
        }
        return jsonArray;

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("person/courses/")
    public static String personCourses(@QueryParam("sem") String sem, @QueryParam("year") String year,
            @QueryParam("istid") String istid) {
/*
        if (UserView.getUser() != null) {
            istid = UserView.getUser().getUsername();
        }
 */
        final Person person = Person.readPersonByIstUsername(istid);
        JSONObject jsonResult = new JSONObject();

        if (person == null) {
            return jsonResult.toJSONString();
        }

        PersonInformationBean pib = new PersonInformationBean(person);
        ExecutionSemester executionSemester = getExecutionSemester(sem, year);
        jsonResult.put("istid", istid);
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

    /*
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("person/schedule/")
    public static String personSchedule() {

        final Person person = Person.readPersonByUsername("ist164216");

        PersonInformationBean pib = new PersonInformationBean(person);

        JSONArray jsonLessons = new JSONArray();

        for (EnrolledLessonBean enrolledLessonBean : pib.getLessonsSchedule()) {
            JSONObject jsonLessonInfo = new JSONObject();
            jsonLessonInfo.put("course", enrolledLessonBean.getCourseAcronym());
            jsonLessonInfo.put("lesson_type", enrolledLessonBean.getLessonType());
            jsonLessonInfo.put("weekday", enrolledLessonBean.getWeekDay());
            jsonLessonInfo.put("room", enrolledLessonBean.getRoom());
            jsonLessonInfo.put("time_begin", enrolledLessonBean.getBegin());
            jsonLessonInfo.put("time_end", enrolledLessonBean.getEnd());
            jsonLessons.add(jsonLessonInfo);
        }
        return jsonLessons.toJSONString();
    }
     */

    @GET
    @Path("person/calendar/evaluations")
    public static Response calendarEvaluation(@QueryParam("istid") String istid, @QueryParam("format") String format,
            @Context HttpServletRequest httpRequest) {

        if ("calendar".equals(format)) {
            final String serverName = httpRequest.getServerName();
            final int serverPort = httpRequest.getServerPort();
            final String serverScheme = httpRequest.getScheme();
            return Response.status(Status.OK).header(HttpHeaders.CONTENT_TYPE, "text/calendar; charset=utf-8")
                    .entity(evaluationCalendarICal(istid, serverScheme, serverScheme, serverPort)).build();
        } else {
            return Response.status(Status.OK).header(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                    .entity(evaluationCalendarJson(istid)).build();
        }
    }

    private static String evaluationCalendarICal(String istid, String serverScheme, String serverName, int serverPort) {
        final User user = User.readUserByUserUId(istid);
        ICalendarSyncPoint calendarSyncPoint = new ICalendarSyncPoint();

        List<EventBean> listEventBean = calendarSyncPoint.getExams(user, serverScheme, serverName, serverPort);
        listEventBean.addAll(calendarSyncPoint.getTeachingExams(user, serverScheme, serverName, serverPort));

        Calendar createCalendar = CalendarFactory.createCalendar(listEventBean);

        return createCalendar.toString();
    }

    private static String evaluationCalendarJson(String istid) {
        JSONObject jsonResult = new JSONObject();
        final User user = User.readUserByUserUId(istid);
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

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("person/calendar/classes")
    public static Response calendarClasses(@QueryParam("istid") String istid, @QueryParam("format") String format,
            @Context HttpServletRequest httpRequest) {
        if ("calendar".equals(format)) {
            final String serverName = httpRequest.getServerName();
            final int serverPort = httpRequest.getServerPort();
            final String serverScheme = httpRequest.getScheme();
            return Response.status(Status.OK).header(HttpHeaders.CONTENT_TYPE, "text/calendar; charset=utf-8")
                    .entity(classesCalendarICal(istid, serverScheme, serverScheme, serverPort)).build();
        } else {
            return Response.status(Status.OK).header(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                    .entity(classesCalendarJson(istid)).build();
        }
    }

    private static String classesCalendarICal(String istid, String serverScheme, String serverName, int serverPort) {
        final User user = User.readUserByUserUId(istid);
        ICalendarSyncPoint calendarSyncPoint = new ICalendarSyncPoint();

        List<EventBean> listEventBean = calendarSyncPoint.getClasses(user, serverScheme, serverName, serverPort);
        listEventBean.addAll(calendarSyncPoint.getTeachingClasses(user, serverScheme, serverName, serverPort));

        Calendar createCalendar = CalendarFactory.createCalendar(listEventBean);

        return createCalendar.toString();
    }

    private static String classesCalendarJson(String istid) {

        JSONObject jsonResult = new JSONObject();
        final User user = User.readUserByUserUId(istid);
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

    @GET
    @Path("person/curriculum")
    public static String personCurriculum(@QueryParam("istid") String istid) {

        Person person = Person.readPersonByIstUsername(istid);

        JSONArray jsonResult = getStudentStatistics(person.getStudent().getRegistrations());

        return jsonResult.toJSONString();
    }

    private static JSONArray getStudentStatistics(List<Registration> registrations) {

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

    public static JSONObject getRegistrationInfo(ExecutionSemester executionSemester, StudentCurricularPlan studentCurricularPlan) {
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

    @GET
    @Path("person/payments")
    public static String personPayments(@QueryParam("istid") String istid) {

        JSONObject jsonResult = new JSONObject();

        JSONArray jsonPayments = new JSONArray();
        JSONArray jsonNotPayed = new JSONArray();

        Properties props = new Properties();
        props.setProperty("application", "APPLICATION_RESOURCES");
        props.setProperty("enum", "ENUMERATION_RESOURCES");
        props.setProperty("default", "APPLICATION_RESOURCES");
        RendererMessageResourceProvider provider = new RendererMessageResourceProvider(props);
        Person person = Person.readPersonByIstUsername(istid);
        for (Entry entry : person.getPayments()) {
            JSONObject paymentInfo = new JSONObject();

            paymentInfo.put("amount", entry.getOriginalAmount().getAmountAsString());
            paymentInfo.put("name", entry.getPaymentMode().getName());
            //System.out.println("-- " + entry.getDescription().toString(provider));
            //System.out.println("-- " + entry.getAccountingTransaction().getDescriptionForEntryType(entry.getEntryType()));
            paymentInfo.put("date", formatDay.print(entry.getWhenRegistered()));
            jsonPayments.add(paymentInfo);

        }

        for (Event entry : person.getNotPayedEvents()) {
            JSONObject notPayedInfo = new JSONObject();

            notPayedInfo.put("total", entry.getTotalAmountToPay());
            notPayedInfo.put("codes", entry.getPaymentCodesCount());
            notPayedInfo.put("amountToPay", entry.getAmountToPay());
            jsonNotPayed.add(notPayedInfo);
            //System.out.println("-- " + entry.getDescription());
        }
        jsonResult.put("payments", jsonPayments);
        jsonResult.put("notPayed", jsonNotPayed);
        return jsonResult.toString();
    }

    @GET
    @Path("person/evaluations")
    public static String evaluations(@QueryParam("istid") String istid) {
        JSONObject jsonResult = new JSONObject();

        Person person = Person.readPersonByIstUsername(istid);

        return null;
    }

    public static boolean isTeacher(String istId) {
        Teacher teacher = Teacher.readByIstId(istId);
        if (teacher != null) {
            return true;
        }

        Person person = Person.readPersonByIstUsername(istId);
        if (person == null) {
            return false;
        }

        if (person.getProfessorshipsCount() != 0) {
            return true;
        }
        return false;
    }

    private static ExecutionSemester getExecutionSemester(String sem, String year) {
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

    private static ExecutionYear getExecutionYear(String year) {
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
}