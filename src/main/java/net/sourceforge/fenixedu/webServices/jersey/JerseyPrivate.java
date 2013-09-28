package net.sourceforge.fenixedu.webServices.jersey;

import pt.ist.fenixWebFramework.security.UserView;

import pt.ist.fenixframework.DomainObject;

import pt.utl.ist.fenix.tools.resources.DefaultResourceBundleProvider;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.student.EnrolStudentInWrittenEvaluation;
import net.sourceforge.fenixedu.applicationTier.Servico.student.UnEnrollStudentInWrittenEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.PersonInformationBean;
import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationConclusionBean;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.Evaluation;
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
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.photograph.PictureAvatar;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.student.curriculum.ICurriculum;
import net.sourceforge.fenixedu.domain.student.curriculum.ICurriculumEntry;
import net.sourceforge.fenixedu.domain.util.icalendar.CalendarFactory;
import net.sourceforge.fenixedu.domain.util.icalendar.EventBean;
import net.sourceforge.fenixedu.presentationTier.Action.ICalendarSyncPoint;
import net.sourceforge.fenixedu.presentationTier.Action.externalServices.OAuthUtils;
import net.sourceforge.fenixedu.presentationTier.backBeans.student.enrolment.DisplayEvaluationsForStudentToEnrol;
import net.sourceforge.fenixedu.webServices.jersey.beans.FenixCalendar;
import net.sourceforge.fenixedu.webServices.jersey.beans.FenixCalendar.FenixCalendarEvent;
import net.sourceforge.fenixedu.webServices.jersey.beans.FenixCurriculum;
import net.sourceforge.fenixedu.webServices.jersey.beans.FenixCurriculum.FenixCourseInfo;
import net.sourceforge.fenixedu.webServices.jersey.beans.FenixEvaluations;
import net.sourceforge.fenixedu.webServices.jersey.beans.FenixEvaluations.FenixEvaluation;
import net.sourceforge.fenixedu.webServices.jersey.beans.FenixPayment;
import net.sourceforge.fenixedu.webServices.jersey.beans.FenixPayment.NotPayedEvent;
import net.sourceforge.fenixedu.webServices.jersey.beans.FenixPayment.PaymentEvent;
import net.sourceforge.fenixedu.webServices.jersey.beans.FenixPerson;
import net.sourceforge.fenixedu.webServices.jersey.beans.FenixPerson.FenixPhoto;
import net.sourceforge.fenixedu.webServices.jersey.beans.FenixPerson.FenixRole;
import net.sourceforge.fenixedu.webServices.jersey.beans.FenixPersonCourses;
import net.sourceforge.fenixedu.webServices.jersey.beans.FenixPersonCourses.FenixCourse;
import net.sourceforge.fenixedu.webServices.jersey.beans.FenixPersonCourses.FenixEnrolment;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.simple.JSONObject;

import com.google.common.base.Joiner;
import com.google.common.net.HttpHeaders;
import com.qmino.miredot.annotations.ReturnType;

@SuppressWarnings("unchecked")
@Path("/private/v1")
public class JerseyPrivate {

    public final static String PERSONAL_SCOPE = "info";
    public final static String SCHEDULE_SCOPE = "schedule";
    public final static String ENROLMENTS_SCOPE = "enrollments";
    public final static String REGISTRATIONS_SCOPE = "registrations";
    public final static String CURRICULAR_SCOPE = "curricular";

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
                final PictureAvatar avatar = personalPhoto.getAvatar();
                String type = avatar.getPictureFileFormat().getMimeType();
                String data = Base64.encodeBase64String(avatar.getBytes());
                photo = new FenixPhoto(type, data);
            }
        } catch (NullPointerException npe) {
        }
        return photo;
    }

    private Person getPerson() {
        IUserView user = UserView.getUser();
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
     * @param sem selected semester ("1" | "2")
     * @param year selected year ("yyyy/yyyy")
     * @return enrolled courses and teaching courses
     * @servicetag CURRICULAR_SCOPE
     */
    @FenixAPIScope(CURRICULAR_SCOPE)
    @GET
    @Produces(JSON_UTF8)
    @Path("person/courses/")
    public FenixPersonCourses personCourses(@QueryParam("sem") String sem, @QueryParam("year") String year) {

        final Person person = getPerson();

//        PersonInformationBean pib = new PersonInformationBean(person);
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

        List<FenixCourse> teachingCourses = new ArrayList<FenixCourse>();

        for (final Professorship professorship : person.getProfessorships(executionSemester)) {
            final ExecutionCourse executionCourse = professorship.getExecutionCourse();
//            final ExecutionSemester executionCourseSemester = executionCourse.getExecutionPeriod();
            if (executionCourse.getExecutionPeriod().equals(executionSemester)) {
                String id = executionCourse.getExternalId();
                String name = executionCourse.getName();
                String sigla = executionCourse.getSigla();
                teachingCourses.add(new FenixCourse(id, sigla, name));

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

    private FenixCalendar evaluationCalendarJson(Person person) {
        final User user = person.getUser();

        String year = ExecutionYear.readCurrentExecutionYear().getName();

        ICalendarSyncPoint calendarSyncPoint = new ICalendarSyncPoint();
        List<EventBean> listEventBean = calendarSyncPoint.getExams(user, "", "", 0);
        listEventBean.addAll(calendarSyncPoint.getTeachingExams(user, "", "", 0));

        return getFenixCalendar(year, listEventBean);
    }

    /**
     * calendar of all written evaluations (tests and exams). Available for students and teachers.
     * 
     * @summary Evaluations calendar
     * @param format ("calendar" or "json")
     * @return If format is "calendar", returns iCal format. If not returns the following json.
     * @servicetag SCHEDULE_SCOPE
     */
    @FenixAPIScope(SCHEDULE_SCOPE)
    @GET
    @Path("person/calendar/evaluations")
    @ReturnType("net.sourceforge.fenixedu.webServices.jersey.beans.FenixCalendar")
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
     * @param format ("calendar" or "json")
     * @return If format is "calendar", returns iCal format. If not returns the following json.
     * @servicetag SCHEDULE_SCOPE
     */
    @FenixAPIScope(SCHEDULE_SCOPE)
    @GET
    @Path("person/calendar/classes")
    @ReturnType("net.sourceforge.fenixedu.webServices.jersey.beans.FenixCalendar")
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
                String end = studentCurricularPlan.getEndDate().toString(formatDay);

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

                    //enrolments.getExecutionCourseFor(enrolments.getExecutionPeriod()).getExternalId());

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
     * @servicetag PERSONAL_SCOPE
     */
    @FenixAPIScope(PERSONAL_SCOPE)
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
     * @servicetag ENROLMENTS_SCOPE
     */
    @FenixAPIScope(ENROLMENTS_SCOPE)
    @GET
    @Path("person/evaluations")
    @Produces(JSON_UTF8)
    public FenixEvaluations evaluations(@Context HttpServletResponse response, @Context HttpServletRequest request,
            @Context ServletContext context) {

//        Person person = getPerson();

        new JerseyFacesContext(context, request, response);
        DisplayEvaluationsForStudentToEnrol manageEvaluationsForStudents = new DisplayEvaluationsForStudentToEnrol();

        List<FenixEvaluation> enrolled =
                processEvaluation(manageEvaluationsForStudents, manageEvaluationsForStudents.getEnroledEvaluations());
        List<FenixEvaluation> notEnrolled =
                processEvaluation(manageEvaluationsForStudents, manageEvaluationsForStudents.getNotEnroledEvaluations());

        return new FenixEvaluations(enrolled, notEnrolled);
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
            String hour = dataFormatHour.format(evaluation.getDay().getTime());

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

            evaluations.add(new FenixEvaluation(name, type, id, inEnrolmentPeriod, day, hour, startHour, endHour, rooms,
                    enrollmentBeginDay, enrollmentEndDay, isEnrolled, course));
        }
        return evaluations;
    }

    /**
     * Enrols in evaluation represented by oid if enrol is "yes". unenrols evaluation if enrol is "no".
     * 
     * @summary evaluation enrollment
     * @param oid evaluations id
     * @param enrol ( "yes" or "no")
     * @return all evaluations
     * @servicetag ENROLMENTS_SCOPE
     */
    @FenixAPIScope(ENROLMENTS_SCOPE)
    @PUT
    @Produces(JSON_UTF8)
    @Path("person/evaluations/{id}")
    public FenixEvaluations evaluations(@PathParam("id") String oid, @QueryParam("format") String enrol,
            @Context HttpServletResponse response, @Context HttpServletRequest request, @Context ServletContext context) {
//        JSONObject jsonResult = new JSONObject();

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

    @SuppressWarnings("unchecked")
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

}
