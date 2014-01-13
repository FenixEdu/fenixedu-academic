package net.sourceforge.fenixedu.webServices.jersey.api;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import net.fortuna.ical4j.model.Calendar;
import net.sourceforge.fenixedu.applicationTier.Factory.RoomSiteComponentBuilder;
import net.sourceforge.fenixedu.applicationTier.Servico.student.EnrolStudentInWrittenEvaluation;
import net.sourceforge.fenixedu.applicationTier.Servico.student.UnEnrollStudentInWrittenEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.ExecutionCourseView;
import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoGenericEvent;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoLessonInstance;
import net.sourceforge.fenixedu.dataTransferObject.InfoShowOccupation;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteRoomTimeTable;
import net.sourceforge.fenixedu.dataTransferObject.InfoWrittenEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.InfoWrittenTest;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.PersonInformationBean;
import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationConclusionBean;
import net.sourceforge.fenixedu.domain.AdHocEvaluation;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeInfo;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Photograph;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Project;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.accounting.Entry;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.paymentCodes.AccountingEventPaymentCode;
import net.sourceforge.fenixedu.domain.contacts.EmailAddress;
import net.sourceforge.fenixedu.domain.contacts.WebAddress;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.BibliographicReferences.BibliographicReference;
import net.sourceforge.fenixedu.domain.onlineTests.OnlineTest;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.domain.space.Room;
import net.sourceforge.fenixedu.domain.space.Space;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.student.curriculum.ICurriculum;
import net.sourceforge.fenixedu.domain.student.curriculum.ICurriculumEntry;
import net.sourceforge.fenixedu.domain.util.icalendar.CalendarFactory;
import net.sourceforge.fenixedu.domain.util.icalendar.EventBean;
import net.sourceforge.fenixedu.presentationTier.Action.ICalendarSyncPoint;
import net.sourceforge.fenixedu.presentationTier.Action.externalServices.OAuthUtils;
import net.sourceforge.fenixedu.presentationTier.backBeans.student.enrolment.DisplayEvaluationsForStudentToEnrol;
import net.sourceforge.fenixedu.util.ContentType;
import net.sourceforge.fenixedu.util.EvaluationType;
import net.sourceforge.fenixedu.util.FenixConfigurationManager;
import net.sourceforge.fenixedu.webServices.jersey.beans.FenixCalendar;
import net.sourceforge.fenixedu.webServices.jersey.beans.FenixCalendar.FenixCalendarEvent;
import net.sourceforge.fenixedu.webServices.jersey.beans.FenixCurriculum;
import net.sourceforge.fenixedu.webServices.jersey.beans.FenixCurriculum.FenixCourseInfo;
import net.sourceforge.fenixedu.webServices.jersey.beans.FenixEvaluation;
import net.sourceforge.fenixedu.webServices.jersey.beans.FenixPayment;
import net.sourceforge.fenixedu.webServices.jersey.beans.FenixPayment.NotPayedEvent;
import net.sourceforge.fenixedu.webServices.jersey.beans.FenixPayment.PaymentEvent;
import net.sourceforge.fenixedu.webServices.jersey.beans.FenixPerson;
import net.sourceforge.fenixedu.webServices.jersey.beans.FenixPerson.FenixPhoto;
import net.sourceforge.fenixedu.webServices.jersey.beans.FenixPerson.FenixRole;
import net.sourceforge.fenixedu.webServices.jersey.beans.FenixPersonCourses;
import net.sourceforge.fenixedu.webServices.jersey.beans.FenixPersonCourses.FenixEnrolment;
import net.sourceforge.fenixedu.webServices.jersey.beans.publico.FenixAbout;
import net.sourceforge.fenixedu.webServices.jersey.beans.publico.FenixCourse;
import net.sourceforge.fenixedu.webServices.jersey.beans.publico.FenixCourse.FenixCompetence;
import net.sourceforge.fenixedu.webServices.jersey.beans.publico.FenixCourse.FenixCompetence.BiblioRef;
import net.sourceforge.fenixedu.webServices.jersey.beans.publico.FenixCourseEvaluation;
import net.sourceforge.fenixedu.webServices.jersey.beans.publico.FenixCourseGroup;
import net.sourceforge.fenixedu.webServices.jersey.beans.publico.FenixCourseStudents;
import net.sourceforge.fenixedu.webServices.jersey.beans.publico.FenixDegree;
import net.sourceforge.fenixedu.webServices.jersey.beans.publico.FenixDegree.FenixDegreeInfo;
import net.sourceforge.fenixedu.webServices.jersey.beans.publico.FenixDegree.FenixTeacher;
import net.sourceforge.fenixedu.webServices.jersey.beans.publico.FenixExecutionCourse;
import net.sourceforge.fenixedu.webServices.jersey.beans.publico.FenixSchedule;
import net.sourceforge.fenixedu.webServices.jersey.beans.publico.FenixSpace;
import net.sourceforge.fenixedu.webServices.jersey.beans.publico.FenixSpace.Room.RoomEvent.WrittenEvaluationEvent;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixframework.DomainObject;
import pt.utl.ist.fenix.tools.resources.DefaultResourceBundleProvider;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

import com.google.common.base.Joiner;
import com.google.common.net.HttpHeaders;
import com.qmino.miredot.annotations.ReturnType;

@SuppressWarnings("unchecked")
@Path("/v1")
public class FenixAPIv1 {

    private static final Logger logger = LoggerFactory.getLogger(FenixAPIv1.class);

    public final static String PERSONAL_SCOPE = "info";
    public final static String SCHEDULE_SCOPE = "schedule";
    public final static String EVALUATIONS_SCOPE = "evaluations";
    public final static String CURRICULAR_SCOPE = "curricular";
    public final static String PAYMENTS_SCOPE = "payments";

    public final static String JSON_UTF8 = "application/json; charset=utf-8";

    public static final DateTimeFormatter formatDay = DateTimeFormat.forPattern("dd/MM/yyyy");
    public static final DateTimeFormatter formatHour = DateTimeFormat.forPattern("HH:mm");
    public static final SimpleDateFormat dataFormatDay = new SimpleDateFormat("dd/MM/yyyy");
    public static final SimpleDateFormat dataFormatHour = new SimpleDateFormat("HH:mm");

    private static final String ENROL = "yes";
    private static final String UNENROL = "no";

    private String mls(MultiLanguageString mls) {
        if (mls == null) {
            return StringUtils.EMPTY;
        }
        return mls.getContent();
    }

    private FenixPhoto getPhoto(final Person person) {
        FenixPhoto photo = null;
        try {
            final Photograph personalPhoto = person.getPersonalPhoto();
            if (person.isPhotoAvailableToCurrentUser()) {
                final byte[] avatar = personalPhoto.getDefaultAvatar();
                String type = ContentType.PNG.getMimeType();
                String data = Base64.encodeBase64String(avatar);
                photo = new FenixPhoto(type, data);
            }
        } catch (Exception npe) {
        }
        return photo;
    }

    private Person getPerson() {
        User user = Authenticate.getUser();
        if (user != null) {
            return user.getPerson();
        }
        return null;
    }

    /**
     * It will return name, istid, campus, email, photo and contacts
     * 
     * @summary Personal Information
     * @return only public contacts and photo are available
     * @servicetag PERSONAL_SCOPE
     */
    @GET
    @Produces(JSON_UTF8)
    @Path("person")
    @FenixAPIScope(PERSONAL_SCOPE)
    public FenixPerson person() {

        final Person person = getPerson();
        PersonInformationBean pib = new PersonInformationBean(person, true);

        final Set<FenixRole> roles = new HashSet<FenixRole>();

        if (isTeacher(person) || person.hasRole(RoleType.TEACHER)) {
            roles.add(new FenixPerson.TeacherFenixRole(pib.getTeacherDepartment()));
        }

        if (person.hasRole(RoleType.STUDENT)) {
            roles.add(new FenixPerson.StudentFenixRole(pib.getStudentDegrees()));

        }

        if (person.hasRole(RoleType.ALUMNI)) {
            roles.add(new FenixPerson.AlumniFenixRole());
        }

        final String name = pib.getName();
        final String istid = person.getIstUsername();
        final String campus = pib.getCampus();
        final String email = pib.getEmail();
        final List<String> personalEmails = pib.getPersonalEmails();
        final List<String> workEmails = pib.getWorkEmails();
        List<String> personalWebAdresses = pib.getPersonalWebAdresses();
        List<String> workWebAdresses = pib.getWorkWebAdresses();
        final FenixPhoto photo = getPhoto(person);

        return new FenixPerson(campus, roles, photo, name, istid, email, personalEmails, workEmails, personalWebAdresses,
                workWebAdresses);
    }

    /**
     * Person courses (enrolled if student and teaching if teacher)
     * 
     * @summary Person courses
     * @param sem
     *            selected semester ("1" | "2")
     * @param year
     *            selected year ("yyyy/yyyy")
     * @return enrolled courses and teaching courses
     * @servicetag CURRICULAR_SCOPE
     */
    @FenixAPIScope(CURRICULAR_SCOPE)
    @GET
    @Produces(JSON_UTF8)
    @Path("person/courses/")
    public FenixPersonCourses personCourses(@QueryParam("sem") String sem, @QueryParam("year") String year) {

        final Person person = getPerson();

        // PersonInformationBean pib = new PersonInformationBean(person);
        ExecutionSemester executionSemester = getExecutionSemester(sem, year);

        String executionYear = executionSemester.getYear();
        Integer semester = executionSemester.getSemester();

        List<FenixEnrolment> enrolments = new ArrayList<FenixEnrolment>();

        final Student foundStudent = person.getStudent();

        for (Registration registration : foundStudent.getAllRegistrations()) {
            for (Enrolment enrolment : registration.getEnrolments(executionSemester)) {
                String id = enrolment.getExecutionCourseFor(executionSemester).getExternalId();
                String grade = enrolment.getGrade().getValue();
                String sigla = enrolment.getExecutionCourseFor(executionSemester).getSigla();
                String name = enrolment.getExecutionCourseFor(executionSemester).getName();
                enrolments.add(new FenixEnrolment(id, sigla, name, grade));
            }
        }

        List<FenixPersonCourses.FenixCourse> teachingCourses = new ArrayList<FenixPersonCourses.FenixCourse>();

        for (final Professorship professorship : person.getProfessorships(executionSemester)) {
            final ExecutionCourse executionCourse = professorship.getExecutionCourse();
            // final ExecutionSemester executionCourseSemester =
            // executionCourse.getExecutionPeriod();
            if (executionCourse.getExecutionPeriod().equals(executionSemester)) {
                String id = executionCourse.getExternalId();
                String name = executionCourse.getName();
                String sigla = executionCourse.getSigla();
                teachingCourses.add(new FenixPersonCourses.FenixCourse(id, sigla, name));

            }
        }
        return new FenixPersonCourses(executionYear, semester, enrolments, teachingCourses);
    }

    /* CALENDARS */

    private FenixCalendar getFenixCalendar(String year, List<EventBean> eventBeans) {

        List<FenixCalendarEvent> events = new ArrayList<FenixCalendarEvent>();
        for (EventBean eventBean : eventBeans) {

            String startDay = formatDay.print(eventBean.getBegin());
            String endDay = formatDay.print(eventBean.getEnd());
            String startTime = formatHour.print(eventBean.getBegin());
            String endTime = formatHour.print(eventBean.getEnd());
            String location = eventBean.getLocation();
            String title = eventBean.getTitle();
            String url = eventBean.getUrl();
            String note = eventBean.getNote();
            boolean allDay = eventBean.isAllDay();

            events.add(new FenixCalendarEvent(startDay, endDay, startTime, endTime, location, title, url, note, allDay));
        }
        return new FenixCalendar(year, events);
    }

    private String evaluationCalendarICal(Person person, String serverScheme, String serverName, int serverPort) {
        ICalendarSyncPoint calendarSyncPoint = new ICalendarSyncPoint();

        List<EventBean> listEventBean = calendarSyncPoint.getExams(person.getUser(), serverScheme, serverName, serverPort);
        listEventBean.addAll(calendarSyncPoint.getTeachingExams(person.getUser(), serverScheme, serverName, serverPort));

        Calendar createCalendar = CalendarFactory.createCalendar(listEventBean);

        return createCalendar.toString();
    }

    private FenixCalendar evaluationCalendarJson(Person person, String serverScheme, String serverName, Integer serverPort) {
        final User user = person.getUser();

        String year = ExecutionYear.readCurrentExecutionYear().getName();

        ICalendarSyncPoint calendarSyncPoint = new ICalendarSyncPoint();
        List<EventBean> listEventBean = calendarSyncPoint.getExams(user, serverScheme, serverName, serverPort);
        listEventBean.addAll(calendarSyncPoint.getTeachingExams(user, serverScheme, serverName, serverPort));

        return getFenixCalendar(year, listEventBean);
    }

    /**
     * calendar of all written evaluations (tests and exams). Available for
     * students and teachers.
     * 
     * @summary Evaluations calendar
     * @param format
     *            ("calendar" or "json")
     * @return If format is "calendar", returns iCal format. If not returns the
     *         following json.
     * @servicetag SCHEDULE_SCOPE
     */
    @FenixAPIScope(SCHEDULE_SCOPE)
    @GET
    @Path("person/calendar/evaluations")
    @ReturnType("net.sourceforge.fenixedu.webServices.jersey.beans.FenixCalendar")
    public Response calendarEvaluation(@QueryParam("format") String format, @Context HttpServletRequest httpRequest) {
        validateFormat(format);
        final Person person = getPerson();
        if (!person.hasRole(RoleType.STUDENT)) {
            return Response.status(Status.OK).header(HttpHeaders.CONTENT_TYPE, JSON_UTF8).entity("{}").build();
        }

        final String serverName = httpRequest.getServerName();
        final int serverPort = httpRequest.getServerPort();
        final String serverScheme = httpRequest.getScheme();
        if ("calendar".equals(format)) {
            String evaluationCalendarICal = evaluationCalendarICal(person, serverScheme, serverName, serverPort);
            return Response.ok(evaluationCalendarICal, "text/calendar;charset=UTF-8").build();
        } else {
            return Response.status(Status.OK).header(HttpHeaders.CONTENT_TYPE, JSON_UTF8)
                    .entity(evaluationCalendarJson(person, serverScheme, serverName, serverPort)).build();
        }
    }

    private String classesCalendarICal(Person person, String serverScheme, String serverName, int serverPort) {
        ICalendarSyncPoint calendarSyncPoint = new ICalendarSyncPoint();

        List<EventBean> listEventBean = calendarSyncPoint.getClasses(person.getUser(), serverScheme, serverName, serverPort);
        listEventBean.addAll(calendarSyncPoint.getTeachingClasses(person.getUser(), serverScheme, serverName, serverPort));

        Calendar createCalendar = CalendarFactory.createCalendar(listEventBean);

        return createCalendar.toString();
    }

    private FenixCalendar classesCalendarJson(Person person) {

        final User user = person.getUser();
        String year = ExecutionYear.readCurrentExecutionYear().getName();

        ICalendarSyncPoint calendarSyncPoint = new ICalendarSyncPoint();
        List<EventBean> listEventBean = calendarSyncPoint.getClasses(user, "", "", 0);
        listEventBean.addAll(calendarSyncPoint.getTeachingClasses(user, "", "", 0));

        return getFenixCalendar(year, listEventBean);
    }

    /**
     * calendar of all lessons of students and teachers
     * 
     * @summary Classes calendar
     * @param format
     *            ("calendar" or "json")
     * @return If format is "calendar", returns iCal format. If not returns the
     *         following json.
     * @servicetag SCHEDULE_SCOPE
     */
    @FenixAPIScope(SCHEDULE_SCOPE)
    @GET
    @Path("person/calendar/classes")
    @ReturnType("net.sourceforge.fenixedu.webServices.jersey.beans.FenixCalendar")
    public Response calendarClasses(@QueryParam("format") String format, @Context HttpServletRequest httpRequest) {
        validateFormat(format);
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

    /**
     * Complete curriculum (for students)
     * 
     * @summary Curriculum
     * @servicetag CURRICULAR_SCOPE
     */
    @FenixAPIScope(CURRICULAR_SCOPE)
    @GET
    @Path("person/curriculum")
    @Produces(JSON_UTF8)
    public List<FenixCurriculum> personCurriculum() {
        Person person = getPerson();
        List<FenixCurriculum> curriculum = new ArrayList<>();

        if (person.getStudent() != null) {
            curriculum = getStudentStatistics(person.getStudent().getRegistrationsSet());
        }
        return curriculum;
    }

    private List<FenixCurriculum> getStudentStatistics(Set<Registration> registrationsList) {

        final List<FenixCurriculum> curriculums = new ArrayList<FenixCurriculum>();

        for (Registration registration : registrationsList) {
            for (StudentCurricularPlan studentCurricularPlan : registration.getStudentCurricularPlansSet()) {
                String name = studentCurricularPlan.getName();
                String id = studentCurricularPlan.getDegree().getExternalId();
                DegreeType degreeType = studentCurricularPlan.getDegreeType();
                String campus = studentCurricularPlan.getCurrentCampus().getName();
                String presentationName = studentCurricularPlan.getPresentationName();
                String start = studentCurricularPlan.getStartDateYearMonthDay().toString(formatDay);
                String end = null;
                if (studentCurricularPlan.getEndDate() != null) {
                    end = studentCurricularPlan.getEndDate().toString(formatDay);
                }
                RegistrationConclusionBean registrationConclusionBean = new RegistrationConclusionBean(registration);

                ICurriculum icurriculum = registrationConclusionBean.getCurriculumForConclusion();

                BigDecimal ects = icurriculum.getSumEctsCredits();
                BigDecimal average = icurriculum.getAverage();

                Integer calculatedAverage = icurriculum.getRoundedAverage();

                boolean isFinished = registrationConclusionBean.isConcluded();

                String approvedCourses = StringUtils.EMPTY;
                if (icurriculum.getCurriculumEntries() != null) {
                    approvedCourses = Integer.toString(icurriculum.getCurriculumEntries().size());
                }

                final List<FenixCourseInfo> courseInfos = new ArrayList<>();

                for (ICurriculumEntry iCurriculumEntry : icurriculum.getCurriculumEntries()) {

                    String entryName = mls(iCurriculumEntry.getPresentationName());
                    String entryGradeValue = iCurriculumEntry.getGradeValue();
                    BigDecimal entryEcts = iCurriculumEntry.getEctsCreditsForCurriculum();

                    String executionCourseOid = StringUtils.EMPTY;
                    if (iCurriculumEntry instanceof Enrolment) {
                        Enrolment enrolment = (Enrolment) iCurriculumEntry;
                        ExecutionCourse executionCourse = enrolment.getExecutionCourseFor(enrolment.getExecutionPeriod());
                        if (executionCourse != null) {
                            executionCourseOid = executionCourse.getExternalId();
                        }
                    }

                    // enrolments.getExecutionCourseFor(enrolments.getExecutionPeriod()).getExternalId());

                    Integer semester = iCurriculumEntry.getExecutionPeriod().getSemester();
                    String year = iCurriculumEntry.getExecutionYear().getYear();
                    courseInfos
                            .add(new FenixCourseInfo(entryName, entryGradeValue, entryEcts, executionCourseOid, semester, year));

                }
                curriculums.add(new FenixCurriculum(id, name, degreeType, campus, presentationName, start, end, ects, average,
                        calculatedAverage, isFinished, approvedCourses, courseInfos));
            }
        }
        return curriculums;
    }

    private List<Event> calculateNotPayedEvents(final Person person) {

        final List<Event> result = new ArrayList<Event>();

        result.addAll(person.getNotPayedEventsPayableOn(null, false));
        result.addAll(person.getNotPayedEventsPayableOn(null, true));

        return result;
    }

    /**
     * Information about gratuity payments (payed and not payed)
     * 
     * @summary Gratuity payments
     * @return
     * @servicetag PAYMENTS_SCOPE
     */
    @FenixAPIScope(PAYMENTS_SCOPE)
    @GET
    @Path("person/payments")
    @Produces(JSON_UTF8)
    public FenixPayment personPayments() {

        Properties props = new Properties();
        props.setProperty("application", "resources.ApplicationResources");
        props.setProperty("enum", "resources.EnumerationResources");
        props.setProperty("default", "resources.ApplicationResources");
        DefaultResourceBundleProvider provider = new DefaultResourceBundleProvider(props);
        Person person = getPerson();

        List<PaymentEvent> payed = new ArrayList<>();

        for (Entry entry : person.getPayments()) {
            String amount = entry.getOriginalAmount().getAmountAsString();
            String name = entry.getPaymentMode().getName();
            String description = entry.getDescription().toString(provider);
            String date = formatDay.print(entry.getWhenRegistered());
            payed.add(new PaymentEvent(amount, name, description, date));
        }

        List<Event> notPayedEvents = calculateNotPayedEvents(person);
        List<NotPayedEvent> notPayed = new ArrayList<>();

        for (Event event : notPayedEvents) {

            for (AccountingEventPaymentCode accountingEventPaymentCode : event.getNonProcessedPaymentCodes()) {
                String description = accountingEventPaymentCode.getDescription();
                String startDate = formatDay.print(accountingEventPaymentCode.getStartDate());
                String endDate = formatDay.print(accountingEventPaymentCode.getEndDate());
                String entity = accountingEventPaymentCode.getEntityCode();
                String reference = accountingEventPaymentCode.getFormattedCode();
                String amount = accountingEventPaymentCode.getMaxAmount().getAmountAsString();
                notPayed.add(new NotPayedEvent(description, startDate, endDate, entity, reference, amount));
            }
        }

        return new FenixPayment(payed, notPayed);
    }

    /**
     * Written evaluations for students
     * 
     * @summary Evaluations
     * @return enrolled and not enrolled student's evaluations
     * @servicetag EVALUATIONS_SCOPE
     */
    @FenixAPIScope(EVALUATIONS_SCOPE)
    @GET
    @Path("person/evaluations")
    @Produces(JSON_UTF8)
    public List<FenixEvaluation> evaluations(@Context HttpServletResponse response, @Context HttpServletRequest request,
            @Context ServletContext context) {

        Person person = getPerson();
        if (!person.hasRole(RoleType.STUDENT)) {
            List<FenixEvaluation> evaluations = new ArrayList<FenixEvaluation>();
            return evaluations;
        }

        new JerseyFacesContext(context, request, response);

        DisplayEvaluationsForStudentToEnrol manageEvaluationsForStudents = new DisplayEvaluationsForStudentToEnrol();

        List<FenixEvaluation> evaluations =
                processEvaluation(manageEvaluationsForStudents, manageEvaluationsForStudents.getEnroledEvaluations());
        List<FenixEvaluation> notEnrolled =
                processEvaluation(manageEvaluationsForStudents, manageEvaluationsForStudents.getNotEnroledEvaluations());
        evaluations.addAll(notEnrolled);

        return evaluations;
    }

    private List<FenixEvaluation> processEvaluation(DisplayEvaluationsForStudentToEnrol processEvaluation,
            List<Evaluation> listEvaluation) {

        List<FenixEvaluation> evaluations = new ArrayList<>();

        for (Evaluation eval : listEvaluation) {
            WrittenEvaluation evaluation = (WrittenEvaluation) eval;

            String name = evaluation.getPresentationName();
            String type = evaluation.getEvaluationType().toString();
            String id = evaluation.getExternalId();

            boolean inEnrolmentPeriod = evaluation.isInEnrolmentPeriod();
            String day = dataFormatDay.format(evaluation.getDay().getTime());

            String startHour = dataFormatHour.format(evaluation.getBeginning().getTime());
            String endHour = dataFormatHour.format(evaluation.getEnd().getTime());

            String rooms = evaluation.getAssociatedRoomsAsString();

            String enrollmentBeginDay = dataFormatDay.format(evaluation.getEnrollmentBeginDay().getTime());
            String enrollmentEndDay = dataFormatDay.format(evaluation.getEnrollmentEndDay().getTime());
            boolean isEnrolled = processEvaluation.getEnroledEvaluations().contains(evaluation);

            Set<String> courses = new HashSet<String>();
            for (ExecutionCourse course : evaluation.getAssociatedExecutionCoursesSet()) {
                courses.add(course.getName());
            }

            String course = Joiner.on(",").join(courses);

            evaluations.add(new FenixEvaluation(name, type, id, inEnrolmentPeriod, day, startHour, endHour, rooms,
                    enrollmentBeginDay, enrollmentEndDay, isEnrolled, course));
        }
        return evaluations;
    }

    /**
     * Enrols in evaluation represented by oid if enrol is "yes". unenrols
     * evaluation if enrol is "no".
     * 
     * @summary evaluation enrollment
     * @param oid
     *            evaluations id
     * @param enrol
     *            ( "yes" or "no")
     * @return all evaluations
     * @servicetag EVALUATIONS_SCOPE
     */
    @FenixAPIScope(EVALUATIONS_SCOPE)
    @PUT
    @Produces(JSON_UTF8)
    @Path("person/evaluations/{id}")
    public List<FenixEvaluation> evaluations(@PathParam("id") String oid, @QueryParam("enrol") String enrol,
            @Context HttpServletResponse response, @Context HttpServletRequest request, @Context ServletContext context) {
        // JSONObject jsonResult = new JSONObject();
        validateEnrol(enrol);
        try {
            WrittenEvaluation eval = getDomainObject(oid, WrittenEvaluation.class);
            if (!StringUtils.isBlank(enrol)) {
                if (enrol.equalsIgnoreCase(ENROL)) {
                    EnrolStudentInWrittenEvaluation.runEnrolStudentInWrittenEvaluation(getPerson().getUsername(),
                            eval.getExternalId());
                } else if (enrol.equalsIgnoreCase(UNENROL)) {
                    UnEnrollStudentInWrittenEvaluation.runUnEnrollStudentInWrittenEvaluation(getPerson().getUsername(),
                            eval.getExternalId());
                }
            }
            return evaluations(response, request, context);

        } catch (Exception e) {
            throw newApplicationError(Status.BAD_REQUEST, "problem found", "problem found");
        }
    }

    public boolean isTeacher(Person person) {
        if (person == null) {
            return false;
        }

        Set<Professorship> professorshipsSet = person.getProfessorshipsSet();
        if (!professorshipsSet.isEmpty()) {
            for (Professorship professorship : professorshipsSet) {
                ExecutionCourse executionCourse = professorship.getExecutionCourse();
                ExecutionSemester readActualExecutionSemester = ExecutionSemester.readActualExecutionSemester();
                if (readActualExecutionSemester.equals(executionCourse.getExecutionPeriod())) {
                    return true;
                }
            }
        }

        return false;
    }

    private ExecutionSemester getExecutionSemester(String sem, String year) {
        ExecutionSemester executionSemester;

        validateSemester(sem);
        validateYear(year);

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

    private <T extends DomainObject> T getDomainObject(String externalId, Class<T> clazz) {
        T domainObject = OAuthUtils.getDomainObject(externalId, clazz);
        if (domainObject == null) {
            throw newApplicationError(Status.NOT_FOUND, "id not found", "id not found");
        }
        return domainObject;
    }

    private WebApplicationException newApplicationError(Status status, String error, String description) {
        JSONObject errorObject = new JSONObject();
        errorObject.put("error", error);
        errorObject.put("description", description);
        return new WebApplicationException(Response.status(status).entity(errorObject.toJSONString()).build());
    }

    /**
     * IST generic information
     * 
     * @summary IST information
     * @return news and events rss
     */
    @GET
    @Produces(JSON_UTF8)
    @Path("about")
    @FenixAPIPublic
    public FenixAbout about() {
        return FenixAbout.getInstance();
    }

    /***
     * DEGREES
     * 
     */
    private List<String> getTeacherPublicMail(Teacher teacher) {
        final List<String> emails = new ArrayList<>();
        if (teacher != null) {
            for (EmailAddress emailAddress : teacher.getPerson().getEmailAddresses()) {
                if (emailAddress.getVisibleToPublic()) {
                    emails.add(emailAddress.getPresentationValue());
                }
            }
        }
        return emails;
    }

    private List<String> getTeacherPublicWebAddress(Teacher teacher) {
        final List<String> urls = new ArrayList<>();
        if (teacher != null) {
            for (WebAddress webAddress : teacher.getPerson().getWebAddresses()) {
                if (webAddress.getVisibleToPublic()) {
                    urls.add(webAddress.getUrl());
                }
            }
        }
        return urls;
    }

    /**
     * All public degrees
     * 
     * @summary All degrees
     * @param year
     *            ("yyyy/yyyy")
     */
    @GET
    @Produces(JSON_UTF8)
    @Path("degrees")
    @FenixAPIPublic
    public List<FenixDegree> degrees(@QueryParam("year") String year) {

        ExecutionYear executionYear = getExecutionYear(year);

        List<FenixDegree> fenixDegrees = new ArrayList<>();

        for (final ExecutionDegree executionDegree : executionYear.getExecutionDegreesSet()) {
            fenixDegrees.add(getFenixDegree(executionYear, executionDegree));
        }
        return fenixDegrees;
    }

    private FenixDegree getFenixDegree(ExecutionYear executionYear, ExecutionDegree executionDegree) {
        final Degree degree = executionDegree.getDegree();
        List<String> degreeCampus = new ArrayList<>();

        String id = degree.getExternalId();
        String name = degree.getPresentationName(executionYear);
        String type = degree.getDegreeTypeName();
        String sigla = degree.getSigla();
        String typeName = degree.getDegreeType().getFilteredName();

        for (Campus campus : degree.getCampus(executionYear)) {
            degreeCampus.add(campus.getName());
        }

        FenixDegreeInfo fenixDegreeInfo = null;

        DegreeInfo degreeInfo = degree.getDegreeInfoFor(executionYear);
        if (degreeInfo == null) {
            degreeInfo = degree.getMostRecentDegreeInfo(executionYear);
        }

        if (degreeInfo != null) {

            String description = mls(degreeInfo.getDescription());
            String objectives = mls(degreeInfo.getObjectives());
            String designFor = mls(degreeInfo.getDesignedFor());
            String requisites = mls(degreeInfo.getDegreeInfoCandidacy().getAccessRequisites());
            String profissionalExits = mls(degreeInfo.getProfessionalExits());
            String history = mls(degreeInfo.getHistory());
            String operationRegime = mls(degreeInfo.getOperationalRegime());
            String gratuity = mls(degreeInfo.getGratuity());
            String links = mls(degreeInfo.getLinks());
            fenixDegreeInfo =
                    new FenixDegreeInfo(description, objectives, designFor, requisites, profissionalExits, history,
                            operationRegime, gratuity, links);
        }

        final List<FenixTeacher> teachers = new ArrayList<>();
        final Collection<Teacher> responsibleCoordinatorsTeachers = degree.getResponsibleCoordinatorsTeachers(executionYear);

        for (Teacher teacher : responsibleCoordinatorsTeachers) {
            String teacherName = teacher.getPerson().getName();
            String istId = teacher.getPerson().getIstUsername();
            List<String> mails = getTeacherPublicMail(teacher);
            List<String> urls = getTeacherPublicWebAddress(teacher);
            teachers.add(new FenixTeacher(teacherName, istId, mails, urls));
        }

        FenixDegree fenixDegree =
                new FenixDegree(executionYear.getName(), id, name, type, sigla, typeName, degreeCampus, fenixDegreeInfo, teachers);
        return fenixDegree;
    }

    /**
     * 
     * Retrieves information about degree with id
     * 
     * @summary Degree information
     * @param oid
     *            degree id
     * @param year
     *            ("yyyy/yyyy")
     */
    @GET
    @Produces(JSON_UTF8)
    @Path("degrees/{id}")
    @FenixAPIPublic
    public FenixDegree degreesByOid(@PathParam("id") String oid, @QueryParam("year") String year) {
        Degree degree = getDomainObject(oid, Degree.class);
        ExecutionYear executionYear = getExecutionYear(year);

        if (degree.isBolonhaMasterOrDegree()) {
            for (final ExecutionDegree executionDegree : degree.getExecutionDegreesForExecutionYear(executionYear)) {
                return getFenixDegree(executionYear, executionDegree);
            }
        }

        return new FenixDegree();
    }

    /**
     * Courses for degree with id
     * 
     * @summary Courses for specific degree
     * @param oid
     *            degree id
     * @param year
     *            ("yyyy/yyyy")
     */
    @GET
    @Produces(JSON_UTF8)
    @Path("degrees/{id}/courses")
    @FenixAPIPublic
    public List<FenixExecutionCourse> coursesByODegreesId(@PathParam("id") String oid, @QueryParam("year") String year) {

        Degree degree = getDomainObject(oid, Degree.class);

        ExecutionYear executionYear = getExecutionYear(year);

        ExecutionSemester[] executionSemesters = executionYear.getExecutionPeriodsSet().toArray(new ExecutionSemester[0]);

        final Set<ExecutionCourseView> executionCourses = new HashSet<ExecutionCourseView>();
        for (final DegreeCurricularPlan degreeCurricularPlan : degree.getDegreeCurricularPlansSet()) {
            if (degreeCurricularPlan.isActive()) {
                degreeCurricularPlan.addExecutionCourses(executionCourses, executionSemesters);
            }
        }

        List<FenixExecutionCourse> fenixExecutionCourses = new ArrayList<>();

        if (degree.isBolonhaMasterOrDegree()) {
            for (ExecutionCourseView executionCourseView : executionCourses) {
                ExecutionCourse ec = executionCourseView.getExecutionCourse();
                String sigla = ec.getSigla();
                String credits = getCredits(ec, degree);
                String name = ec.getName();
                String id = ec.getExternalId();
                String ecYear = ec.getExecutionYear().getName();
                String sem = ec.getExecutionPeriod().getName();

                fenixExecutionCourses.add(new FenixExecutionCourse(sigla, credits, name, id, ecYear, sem));
            }
        }
        return fenixExecutionCourses;
    }

    private String getCredits(ExecutionCourse ec, Degree degree) {
        for (CurricularCourse curricularCourse : ec.getAssociatedCurricularCoursesSet()) {
            if (degree.equals(curricularCourse.getDegree())) {
                return curricularCourse.getEctsCredits().toString();
            }
        }
        return "N/A";
    }

    private String getServerLink() {
        String serverLink;
        final String appName = FenixConfigurationManager.getConfiguration().getHTTPHost();
        final String appContext = FenixConfigurationManager.getConfiguration().appContext();
        final String httpPort = FenixConfigurationManager.getConfiguration().getHTTPPort();
        final String httpProtocol = FenixConfigurationManager.getConfiguration().getHTTPProtocol();

        if (StringUtils.isEmpty(httpPort)) {
            serverLink = String.format("%s://%s/", httpProtocol, appName);
        } else {
            serverLink = String.format("%s://%s:%s/", httpProtocol, appName, httpPort);
        }
        if (!StringUtils.isEmpty(appContext)) {
            serverLink += appContext;
        }
        serverLink = StringUtils.removeEnd(serverLink, "/");
        return serverLink;
    }

    /**
     * Detailed information about course
     * 
     * @summary Course information by id
     * @param oid
     *            course id
     * @return
     */
    @GET
    @Produces(JSON_UTF8)
    @Path("courses/{id}/")
    @FenixAPIPublic
    public FenixCourse coursesByOid(@PathParam("id") String oid) {
        ExecutionCourse executionCourse = getDomainObject(oid, ExecutionCourse.class);

        String acronym = executionCourse.getSigla();
        String name = executionCourse.getName();
        String evaluationMethod = executionCourse.getEvaluationMethodText();
        String academicTerm = executionCourse.getExecutionPeriod().getQualifiedName();

        Map<CompetenceCourse, Set<CurricularCourse>> curricularCourses =
                executionCourse.getCurricularCoursesIndexedByCompetenceCourse();

        List<FenixCourse.FenixCompetence> moreInfo = new ArrayList<>();

        for (Map.Entry<CompetenceCourse, Set<CurricularCourse>> entry : curricularCourses.entrySet()) {

            List<FenixCourse.FenixCompetence.BiblioRef> biblios = new ArrayList<>();
            for (BibliographicReference bibliographicReference : entry.getKey().getBibliographicReferences()
                    .getBibliographicReferencesSortedByOrder()) {

                String author = bibliographicReference.getAuthors();
                String reference = bibliographicReference.getReference();
                String title = bibliographicReference.getTitle();
                String bibYear = bibliographicReference.getYear();
                String type = bibliographicReference.getType().getName();
                String url = bibliographicReference.getUrl();

                biblios.add(new BiblioRef(author, reference, title, bibYear, type, url));
            }

            List<FenixCourse.FenixCompetence.Degree> degrees = new ArrayList<>();
            for (CurricularCourse curricularCourse : entry.getValue()) {
                String id = curricularCourse.getDegree().getExternalId();
                String dName = curricularCourse.getDegree().getPresentationName();
                String dacronym = curricularCourse.getAcronym();

                degrees.add(new FenixCourse.FenixCompetence.Degree(id, dName, dacronym));
            }

            String program = entry.getKey().getProgram();

            moreInfo.add(new FenixCompetence(program, biblios, degrees));

        }

        int numberOfStudents = executionCourse.getAttendsSet().size();

        List<FenixTeacher> teachers = new ArrayList<>();
        for (Professorship professorship : executionCourse.getProfessorshipsSet()) {

            String tname = professorship.getPerson().getName();
            String istid = professorship.getPerson().getIstUsername();
            List<String> mail = getTeacherPublicMail(professorship.getTeacher());
            List<String> url = getTeacherPublicWebAddress(professorship.getTeacher());

            teachers.add(new FenixTeacher(tname, istid, mail, url));
        }

        String annoucementLink =
                getServerLink().concat("/external/announcementsRSS.do?announcementBoardId=").concat(
                        executionCourse.getBoard().getExternalId().toString());

        String summaryLink =
                getServerLink().concat("/publico/summariesRSS.do?id=").concat(executionCourse.getExternalId().toString());

        return new FenixCourse(acronym, name, evaluationMethod, academicTerm, numberOfStudents, annoucementLink, summaryLink,
                moreInfo, teachers);
    }

    /**
     * Retrieve groups for course given by oid
     * 
     * @summary Course groups by course oid
     * @param oid
     *            course id
     */
    @GET
    @Produces(JSON_UTF8)
    @Path("courses/{id}/groups")
    @FenixAPIPublic
    public List<FenixCourseGroup> groupsCoursesByOid(@PathParam("id") final String oid) {

        final ExecutionCourse executionCourse = getDomainObject(oid, ExecutionCourse.class);

        final List<FenixCourseGroup> groupings = new ArrayList<>();

        for (final Grouping grouping : executionCourse.getGroupings()) {
            groupings.add(new FenixCourseGroup(grouping));
        }

        return groupings;

    }

    /**
     * All students for course by id
     * 
     * @summary Course students
     * @param oid
     *            course id
     * @return
     */
    @GET
    @Produces(JSON_UTF8)
    @Path("courses/{id}/students")
    @FenixAPIPublic
    public FenixCourseStudents studentsCoursesByOid(@PathParam("id") String oid) {
        ExecutionCourse executionCourse = getDomainObject(oid, ExecutionCourse.class);
        return new FenixCourseStudents(executionCourse);
    }

    /**
     * 
     * Returns evaluations for course by id (Test, Exams, Project, AdHoc,
     * OnlineTest)
     * 
     * @summary Course evaluations
     * @param oid
     * @return
     */
    @GET
    @Produces(JSON_UTF8)
    @Path("courses/{id}/evaluations")
    @FenixAPIPublic
    public List<FenixCourseEvaluation> evaluationCoursesByOid(@PathParam("id") String oid) {

        ExecutionCourse executionCourse = getDomainObject(oid, ExecutionCourse.class);

        final List<FenixCourseEvaluation> evals = new ArrayList<>();

        for (Evaluation evaluation : executionCourse.getAssociatedEvaluationsSet()) {
            if (evaluation instanceof WrittenEvaluation) {
                evals.add(getWrittenEvaluationJSON((WrittenEvaluation) evaluation));
            } else if (evaluation instanceof Project) {
                evals.add(getProjectEvaluationJSON((Project) evaluation));
            } else if (evaluation instanceof OnlineTest) {
                evals.add(getOnlineTestJSON((OnlineTest) evaluation));
            } else if (evaluation instanceof AdHocEvaluation) {
                evals.add(getAdhocEvaluationJSON((AdHocEvaluation) evaluation));
            }
        }
        return evals;
    }

    private FenixCourseEvaluation.AdHocEvaluation getAdhocEvaluationJSON(AdHocEvaluation adHocEvaluation) {
        return new FenixCourseEvaluation.AdHocEvaluation(adHocEvaluation.getPresentationName(), adHocEvaluation.getDescription());
    }

    private FenixCourseEvaluation.OnlineTest getOnlineTestJSON(OnlineTest onlineTest) {
        return new FenixCourseEvaluation.OnlineTest(onlineTest.getPresentationName());
    }

    private FenixCourseEvaluation.Project getProjectEvaluationJSON(Project projectEvaluation) {

        String name = projectEvaluation.getPresentationName();

        String beginningDay = null;
        String beginningTime = null;
        String endDay = null;
        String endTime = null;
        if (projectEvaluation.getProjectBeginDateTime() != null) {
            beginningDay = formatDay.print(projectEvaluation.getProjectBeginDateTime());
            beginningTime = formatHour.print(projectEvaluation.getProjectBeginDateTime());
        }
        if (projectEvaluation.getProjectEndDateTime() != null) {
            endDay = formatDay.print(projectEvaluation.getProjectEndDateTime());
            endTime = formatHour.print(projectEvaluation.getProjectEndDateTime());

        }
        return new FenixCourseEvaluation.Project(name, beginningDay, beginningTime, endDay, endTime);
    }

    private FenixCourseEvaluation.WrittenEvaluation getWrittenEvaluationJSON(WrittenEvaluation writtenEvaluation) {

        String name = writtenEvaluation.getPresentationName();
        EvaluationType type = writtenEvaluation.getEvaluationType();

        String day = dataFormatDay.format(writtenEvaluation.getDay().getTime());

        String beginningTime = dataFormatHour.format(writtenEvaluation.getBeginning().getTime());
        String endTime = dataFormatHour.format(writtenEvaluation.getEnd().getTime());

        List<FenixCourseEvaluation.WrittenEvaluation.Room> rooms = new ArrayList<>();

        for (AllocatableSpace allocationSpace : writtenEvaluation.getAssociatedRooms()) {

            String roomId = allocationSpace.getExternalId();
            String roomName = allocationSpace.getSpaceInformation().getPresentationName();
            String roomDescription = allocationSpace.getCompleteIdentification();
            rooms.add(new FenixCourseEvaluation.WrittenEvaluation.Room(roomId, roomName, roomDescription));
        }

        boolean isEnrolmentPeriod = writtenEvaluation.getIsInEnrolmentPeriod();

        final DateTime start = writtenEvaluation.getEnrolmentPeriodStart();
        final DateTime end = writtenEvaluation.getEnrolmentPeriodEnd();
        final String enrollmentPeriodStart = start == null ? null : start.toString("yyyy-MM-dd HH:mm:ss");
        final String enrollmentPeriodEnd = end == null ? null : end.toString("yyyy-MM-dd HH:mm:ss");

        if (type.equals(EvaluationType.EXAM_TYPE)) {
            return new FenixCourseEvaluation.Exam(name, day, beginningTime, endTime, isEnrolmentPeriod, enrollmentPeriodStart,
                    enrollmentPeriodEnd, rooms);
        } else {
            return new FenixCourseEvaluation.Test(name, day, beginningTime, endTime, isEnrolmentPeriod, enrollmentPeriodStart,
                    enrollmentPeriodEnd, rooms);
        }
    }

    /**
     * All lessons and lesson period of course by id
     * 
     * @summary Lesson schedule of course by id
     * @param oid
     * @return
     */
    @GET
    @Produces(JSON_UTF8)
    @Path("courses/{id}/schedule")
    @FenixAPIPublic
    public FenixSchedule scheduleCoursesByOid(@PathParam("id") String oid) {
        ExecutionCourse executionCourse = getDomainObject(oid, ExecutionCourse.class);
        return new FenixSchedule(executionCourse);
    }

    /**
     * Campus spaces
     * 
     * @summary All campus
     * @return
     */
    @GET
    @Produces(JSON_UTF8)
    @Path("spaces")
    @FenixAPIPublic
    public List<FenixSpace> spaces() {

        List<FenixSpace> campi = new ArrayList<>();

        for (Campus campus : Space.getAllCampus()) {
            campi.add(getSimpleSpace(campus));
        }
        return campi;
    }

    /**
     * Space information regarding space type (Campus, Building, Floor or Room)
     * 
     * @param oid
     * @param day
     *            ("dd/mm/yyyy")
     * @return
     */
    @GET
    @Produces(JSON_UTF8)
    @Path("spaces/{id}")
    @FenixAPIPublic
    public FenixSpace spacesByOid(@PathParam("id") String oid, @QueryParam("day") String day) {

        Space space = getDomainObject(oid, Space.class);
        if (space.isRoom()) {
            return getFenixRoom((Room) space, getRoomDay(day));
        }
        return getSpace(space);
    }

    private String getSpaceType(Space space) {
        if (space.isCampus()) {
            return "CAMPUS";
        } else if (space.isBuilding()) {
            return "BUILDING";
        } else if (space.isFloor()) {
            return "FLOOR";
        } else if (space.isRoom()) {
            return "ROOM";
        }
        return null;
    }

    private FenixSpace getSimpleSpace(Space space) {
        if (space == null) {
            return null;
        }
        return new FenixSpace(space.getExternalId(), space.getSpaceInformation().getPresentationName(), getSpaceType(space));
    }

    public FenixSpace getSpace(Space space) {
        String id = space.getExternalId();
        String name = space.getSpaceInformation().getPresentationName();
        List<FenixSpace> containedSpaces = getSimpleSpace(space.getContainedSpacesSet());
        FenixSpace parentSpace = getSimpleSpace(space.getSuroundingSpace());
        return new FenixSpace(id, name, getSpaceType(space), containedSpaces, parentSpace);
    }

    private List<FenixSpace> getSimpleSpace(Set<Space> containedSpacesSet) {
        List<FenixSpace> fenixSpaces = new ArrayList<FenixSpace>();
        for (Space space : containedSpacesSet) {
            fenixSpaces.add(getSimpleSpace(space));
        }
        return fenixSpaces;
    }

    private FenixSpace.Room getFenixRoom(Room room, java.util.Calendar rightNow) {

        InfoSiteRoomTimeTable bodyComponent = new InfoSiteRoomTimeTable();
        RoomSiteComponentBuilder builder = new RoomSiteComponentBuilder();
        List<FenixSpace.Room.RoomEvent> roomEvents = new ArrayList<FenixSpace.Room.RoomEvent>();

        try {
            builder.getComponent(bodyComponent, rightNow, room, null);
            for (Object occupation : bodyComponent.getInfoShowOccupation()) {
                InfoShowOccupation showOccupation = (InfoShowOccupation) occupation;

                FenixSpace.Room.RoomEvent roomEvent = null;

                if (showOccupation instanceof InfoLesson || showOccupation instanceof InfoLessonInstance) {
                    InfoShowOccupation lesson = showOccupation;
                    InfoExecutionCourse infoExecutionCourse = lesson.getInfoShift().getInfoDisciplinaExecucao();
                    String sigla = infoExecutionCourse.getSigla();
                    String name = infoExecutionCourse.getNome();
                    String id = infoExecutionCourse.getExecutionCourse().getExternalId();

                    String start = dataFormatHour.format(lesson.getInicio().getTime());
                    String end = dataFormatHour.format(lesson.getFim().getTime());
                    String weekday = lesson.getDiaSemana().getDiaSemanaString();

                    String info = lesson.getInfoShift().getShiftTypesCodePrettyPrint();

                    WrittenEvaluationEvent.ExecutionCourse course = new WrittenEvaluationEvent.ExecutionCourse(sigla, name, id);

                    roomEvent = new FenixSpace.Room.RoomEvent.LessonEvent(start, end, weekday, info, course);

                } else if (showOccupation instanceof InfoWrittenEvaluation) {
                    InfoWrittenEvaluation infoWrittenEvaluation = (InfoWrittenEvaluation) showOccupation;

                    List<WrittenEvaluationEvent.ExecutionCourse> courses = new ArrayList<>();

                    for (int iterEC = 0; iterEC < infoWrittenEvaluation.getAssociatedExecutionCourse().size(); iterEC++) {
                        InfoExecutionCourse infoEC = infoWrittenEvaluation.getAssociatedExecutionCourse().get(iterEC);
                        String sigla = infoEC.getSigla();
                        String name = infoEC.getNome();
                        String id = infoEC.getExecutionCourse().getExternalId();
                        courses.add(new WrittenEvaluationEvent.ExecutionCourse(sigla, name, id));
                    }

                    String start = null;
                    String end = null;
                    String weekday = null;

                    if (infoWrittenEvaluation instanceof InfoExam) {
                        InfoExam infoExam = (InfoExam) infoWrittenEvaluation;
                        start = infoExam.getBeginningHour();
                        end = infoExam.getEndHour();
                        weekday = infoWrittenEvaluation.getDiaSemana().getDiaSemanaString();
                        Integer season = infoExam.getSeason().getSeason();

                        roomEvent =
                                new FenixSpace.Room.RoomEvent.WrittenEvaluationEvent.ExamEvent(start, end, weekday, courses,
                                        season);

                    } else if (infoWrittenEvaluation instanceof InfoWrittenTest) {
                        InfoWrittenTest infoWrittenTest = (InfoWrittenTest) infoWrittenEvaluation;
                        String description = infoWrittenTest.getDescription();
                        start = dataFormatHour.format(infoWrittenTest.getInicio().getTime());
                        end = dataFormatHour.format(infoWrittenTest.getFim().getTime());
                        weekday = infoWrittenTest.getDiaSemana().getDiaSemanaString();

                        roomEvent =
                                new FenixSpace.Room.RoomEvent.WrittenEvaluationEvent.TestEvent(start, end, weekday, courses,
                                        description);
                    }

                } else if (showOccupation instanceof InfoGenericEvent) {

                    InfoGenericEvent infoGenericEvent = (InfoGenericEvent) showOccupation;
                    String description = infoGenericEvent.getDescription();
                    String title = infoGenericEvent.getTitle();
                    String start = dataFormatHour.format(infoGenericEvent.getInicio().getTime());
                    String end = dataFormatHour.format(infoGenericEvent.getFim().getTime());
                    String weekday = infoGenericEvent.getDiaSemana().getDiaSemanaString();

                    roomEvent = new FenixSpace.Room.RoomEvent.GenericEvent(start, end, weekday, description, title);
                }

                if (roomEvent != null) {
                    roomEvents.add(roomEvent);
                }

            }

            String id = room.getExternalId();
            String name = room.getSpaceInformation().getPresentationName();
            String description = room.getSpaceInformation().getDescription();
            Integer normalCapacity = room.getNormalCapacity();
            Integer examCapacity = room.getExamCapacity();

            return new FenixSpace.Room(id, name, getSimpleSpace(room.getSpaceFloor()), description, normalCapacity, examCapacity,
                    roomEvents);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw newApplicationError(Status.INTERNAL_SERVER_ERROR, "berserk!", "something went wrong");
        }
    }

    private java.util.Calendar getRoomDay(String day) {
        validateDay(day);
        java.util.Calendar rightNow = java.util.Calendar.getInstance();
        Date date = null;
        try {
            if (!StringUtils.isBlank(day)) {
                date = dataFormatDay.parse(day);
                rightNow.setTime(date);
            }
        } catch (ParseException e1) {
        }
        return rightNow;
    }

    private ExecutionYear getExecutionYear(String year) {
        validateYear(year);
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

    private void validateYear(String year) {
        if (!StringUtils.isBlank(year)) {
            String[] split = year.split("/");;
            if (split.length != 2) {
                throw newApplicationError(Status.BAD_REQUEST, "format_error", "year must be xxxx/yyyy");
            }
            try {
                int y1 = Integer.parseInt(split[0]);
                int y2 = Integer.parseInt(split[1]);
                int start = ExecutionYear.readFirstExecutionYear().getBeginCivilYear();
                int end = ExecutionYear.readFirstExecutionYear().getEndCivilYear();
                ExecutionYear currentYear = ExecutionYear.readCurrentExecutionYear();
                if (!(y1 >= start && y1 <= currentYear.getBeginCivilYear())) {
                    throw newApplicationError(Status.BAD_REQUEST, "format_error",
                            String.format("start year must be between %d and %d", start, currentYear.getBeginCivilYear()));
                }
                if (!(y2 >= end && y2 <= currentYear.getEndCivilYear())) {
                    throw newApplicationError(Status.BAD_REQUEST, "format_error",
                            String.format("end year must be between %d and %d", end, currentYear.getEndCivilYear()));
                }
                if (y2 - y1 != 1) {
                    throw newApplicationError(Status.BAD_REQUEST, "format_error",
                            String.format("difference between start and end year must be 1"));
                }
            } catch (NumberFormatException e) {
                throw newApplicationError(Status.BAD_REQUEST, "format_error", "year must be a number");
            }
        }
    }

    private void validateDay(String day) {
        if (!StringUtils.isBlank(day)) {
            boolean invalid = false;
            try {
                DateTime parse = formatDay.parseDateTime(day);
                invalid = parse == null;
            } catch (IllegalArgumentException | UnsupportedOperationException e) {
                invalid = true;
            } finally {
                if (invalid) {
                    throw newApplicationError(Status.BAD_REQUEST, "format_error", "day must be " + dataFormatDay.toPattern());
                }
            }
        }
    }

    private void validateSemester(String sem) {
        if (!StringUtils.isBlank(sem)) {
            if (!("1".equals(sem) || "2".equals(sem))) {
                throw newApplicationError(Status.BAD_REQUEST, "format_error", "semester must be 1 or 2");
            }
        }
    }

    private void validateFormat(String format) {
        if (!StringUtils.isBlank(format)) {
            if (!("calendar".equals(format) || "json".equals(format))) {
                throw newApplicationError(Status.BAD_REQUEST, "format_error", "format must be calendar or json");
            }
        }
    }

    private void validateEnrol(String enrol) {
        if (StringUtils.isBlank(enrol) || !(ENROL.equals(enrol) || UNENROL.equals(enrol))) {
            throw newApplicationError(Status.BAD_REQUEST, "format_error", "enrol must be yes or no");
        }
    }

}