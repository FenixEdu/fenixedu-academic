package net.sourceforge.fenixedu.webServices.jersey.api;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import javax.ws.rs.core.StreamingOutput;

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
import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationCurriculumBean;
import net.sourceforge.fenixedu.domain.AdHocEvaluation;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeInfo;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Photograph;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Project;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.WrittenEvaluationEnrolment;
import net.sourceforge.fenixedu.domain.accounting.Entry;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.paymentCodes.AccountingEventPaymentCode;
import net.sourceforge.fenixedu.domain.contacts.EmailAddress;
import net.sourceforge.fenixedu.domain.contacts.WebAddress;
import net.sourceforge.fenixedu.domain.degreeStructure.BibliographicReferences.BibliographicReference;
import net.sourceforge.fenixedu.domain.onlineTests.OnlineTest;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.domain.space.Blueprint;
import net.sourceforge.fenixedu.domain.space.BlueprintFile;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.domain.space.Room;
import net.sourceforge.fenixedu.domain.space.Space;
import net.sourceforge.fenixedu.domain.space.SpaceInformation;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.student.curriculum.ICurriculum;
import net.sourceforge.fenixedu.domain.student.curriculum.ICurriculumEntry;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import net.sourceforge.fenixedu.domain.util.icalendar.CalendarFactory;
import net.sourceforge.fenixedu.domain.util.icalendar.ClassEventBean;
import net.sourceforge.fenixedu.domain.util.icalendar.EvaluationEventBean;
import net.sourceforge.fenixedu.domain.util.icalendar.EventBean;
import net.sourceforge.fenixedu.presentationTier.Action.ICalendarSyncPoint;
import net.sourceforge.fenixedu.presentationTier.Action.externalServices.OAuthUtils;
import net.sourceforge.fenixedu.presentationTier.Action.spaceManager.ManageSpaceBlueprintsDA;
import net.sourceforge.fenixedu.presentationTier.backBeans.student.enrolment.DisplayEvaluationsForStudentToEnrol;
import net.sourceforge.fenixedu.util.ContentType;
import net.sourceforge.fenixedu.util.EvaluationType;
import net.sourceforge.fenixedu.util.FenixConfigurationManager;
import net.sourceforge.fenixedu.webServices.jersey.beans.FenixCalendar;
import net.sourceforge.fenixedu.webServices.jersey.beans.FenixCalendar.FenixCalendarEvent;
import net.sourceforge.fenixedu.webServices.jersey.beans.FenixCalendar.FenixClassEvent;
import net.sourceforge.fenixedu.webServices.jersey.beans.FenixCalendar.FenixEvaluationEvent;
import net.sourceforge.fenixedu.webServices.jersey.beans.FenixCourse;
import net.sourceforge.fenixedu.webServices.jersey.beans.FenixCurriculum;
import net.sourceforge.fenixedu.webServices.jersey.beans.FenixPayment;
import net.sourceforge.fenixedu.webServices.jersey.beans.FenixPayment.PaymentEvent;
import net.sourceforge.fenixedu.webServices.jersey.beans.FenixPayment.PendingEvent;
import net.sourceforge.fenixedu.webServices.jersey.beans.FenixPerson;
import net.sourceforge.fenixedu.webServices.jersey.beans.FenixPerson.FenixPhoto;
import net.sourceforge.fenixedu.webServices.jersey.beans.FenixPerson.FenixRole;
import net.sourceforge.fenixedu.webServices.jersey.beans.FenixPersonCourses;
import net.sourceforge.fenixedu.webServices.jersey.beans.FenixPersonCourses.FenixEnrolment;
import net.sourceforge.fenixedu.webServices.jersey.beans.publico.FenixAbout;
import net.sourceforge.fenixedu.webServices.jersey.beans.publico.FenixCourseEvaluation;
import net.sourceforge.fenixedu.webServices.jersey.beans.publico.FenixCourseExtended;
import net.sourceforge.fenixedu.webServices.jersey.beans.publico.FenixCourseExtended.FenixCompetence;
import net.sourceforge.fenixedu.webServices.jersey.beans.publico.FenixCourseExtended.FenixCompetence.BiblioRef;
import net.sourceforge.fenixedu.webServices.jersey.beans.publico.FenixCourseGroup;
import net.sourceforge.fenixedu.webServices.jersey.beans.publico.FenixCourseStudents;
import net.sourceforge.fenixedu.webServices.jersey.beans.publico.FenixDegree;
import net.sourceforge.fenixedu.webServices.jersey.beans.publico.FenixDegreeExtended;
import net.sourceforge.fenixedu.webServices.jersey.beans.publico.FenixDegreeExtended.FenixDegreeInfo;
import net.sourceforge.fenixedu.webServices.jersey.beans.publico.FenixDegreeExtended.FenixTeacher;
import net.sourceforge.fenixedu.webServices.jersey.beans.publico.FenixDomainModel;
import net.sourceforge.fenixedu.webServices.jersey.beans.publico.FenixExecutionCourse;
import net.sourceforge.fenixedu.webServices.jersey.beans.publico.FenixPeriod;
import net.sourceforge.fenixedu.webServices.jersey.beans.publico.FenixRoomEvent;
import net.sourceforge.fenixedu.webServices.jersey.beans.publico.FenixSchedule;
import net.sourceforge.fenixedu.webServices.jersey.beans.publico.FenixSpace;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
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

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Ordering;
import com.google.common.net.HttpHeaders;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.qmino.miredot.annotations.ReturnType;

@SuppressWarnings("unchecked")
@Path("/fenix/v1")
public class FenixAPIv1 {

    private static final Logger logger = LoggerFactory.getLogger(FenixAPIv1.class);

    public final static String PERSONAL_SCOPE = "info";
    public final static String SCHEDULE_SCOPE = "schedule";
    public final static String EVALUATIONS_SCOPE = "evaluations";
    public final static String CURRICULAR_SCOPE = "curricular";
    public final static String PAYMENTS_SCOPE = "payments";

    public final static String JSON_UTF8 = "application/json; charset=utf-8";

    DateTimeFormatter formatDayHour = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm");

    public static final DateTimeFormatter formatDay = DateTimeFormat.forPattern("dd/MM/yyyy");
    public static final SimpleDateFormat dataFormatDay = new SimpleDateFormat("dd/MM/yyyy");
    public static final SimpleDateFormat dataFormatHour = new SimpleDateFormat("HH:mm");

    private static Gson gson;

    private static final String ENROL = "yes";
    private static final String UNENROL = "no";

    static {
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

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
            roles.add(new FenixPerson.TeacherFenixRole(pib.getTeacherDepartmentUnit()));
        }

        if (person.hasRole(RoleType.STUDENT)) {
            roles.add(new FenixPerson.StudentFenixRole(pib.getStudentRegistrations()));

        }

        if (person.hasRole(RoleType.ALUMNI)) {

            ArrayList<Registration> concludedRegistrations = new ArrayList<>();
            if (person.getStudent() != null) {
                concludedRegistrations.addAll(person.getStudent().getConcludedRegistrations());
            }
            roles.add(new FenixPerson.AlumniFenixRole(concludedRegistrations));
        }

        final String name = pib.getName();
        final String gender = person.getGender().name();
        final String birthday = person.getDateOfBirthYearMonthDay().toString(formatDay);
        final String username = person.getUsername();
        final String campus = pib.getCampus();
        final String email = pib.getEmail();
        final List<String> personalEmails = pib.getPersonalEmails();
        final List<String> workEmails = pib.getWorkEmails();
        List<String> personalWebAdresses = pib.getPersonalWebAdresses();
        List<String> workWebAdresses = pib.getWorkWebAdresses();
        final FenixPhoto photo = getPhoto(person);

        return new FenixPerson(campus, roles, photo, name, gender, birthday, username, email, personalEmails, workEmails,
                personalWebAdresses, workWebAdresses);
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
    public FenixPersonCourses personCourses(@QueryParam("academicTerm") String academicTerm) {

        final Person person = getPerson();

        // PersonInformationBean pib = new PersonInformationBean(person);
        Set<ExecutionSemester> semesters = getExecutionSemesters(academicTerm);

        List<FenixEnrolment> enrolments = new ArrayList<FenixEnrolment>();
        List<FenixCourse> teachingCourses = new ArrayList<FenixCourse>();

        for (ExecutionSemester executionSemester : semesters) {
            fillEnrolments(person, enrolments, executionSemester);
            fillTeachingCourses(person, teachingCourses, executionSemester);
        }

        return new FenixPersonCourses(enrolments, teachingCourses);
    }

    public Set<ExecutionSemester> getExecutionSemesters(String academicTerm) {
        ExecutionInterval executionInterval = ExecutionInterval.getExecutionInterval(getAcademicInterval(academicTerm));

        Set<ExecutionSemester> semesters = new HashSet<ExecutionSemester>();

        if (executionInterval instanceof ExecutionYear) {
            semesters.addAll(((ExecutionYear) executionInterval).getExecutionPeriods());
        } else if (executionInterval instanceof ExecutionSemester) {
            semesters.add((ExecutionSemester) executionInterval);
        }
        return semesters;
    }

    public void fillTeachingCourses(final Person person, List<FenixCourse> teachingCourses, ExecutionSemester executionSemester) {
        for (final Professorship professorship : person.getProfessorships(executionSemester)) {
            final ExecutionCourse executionCourse = professorship.getExecutionCourse();
            // final ExecutionSemester executionCourseSemester =
            // executionCourse.getExecutionPeriod();
            if (executionCourse.getExecutionPeriod().equals(executionSemester)) {
                teachingCourses.add(new FenixCourse(executionCourse));

            }
        }
    }

    public void fillEnrolments(final Person person, List<FenixEnrolment> enrolments, ExecutionSemester executionSemester) {
        final Student foundStudent = person.getStudent();

        for (Registration registration : foundStudent.getAllRegistrations()) {
            for (Enrolment enrolment : registration.getEnrolments(executionSemester)) {
                final ExecutionCourse executionCourse = enrolment.getExecutionCourseFor(executionSemester);
                String grade = enrolment.getGrade().getValue();
                double ects = enrolment.getEctsCredits();
                enrolments.add(new FenixEnrolment(executionCourse, grade, ects));
            }
        }
    }

    /* CALENDARS */

    private enum EventType {
        CLASS, EVALUATION;
    }

    private FenixCalendar getFenixCalendar(String academicTerm, List<EventBean> eventBeans, EventType eventType) {

        List<FenixCalendarEvent> events = new ArrayList<FenixCalendarEvent>();
        for (EventBean eventBean : eventBeans) {

            String start = formatDayHour.print(eventBean.getBegin());
            String end = formatDayHour.print(eventBean.getEnd());

            FenixPeriod eventPeriod = new FenixPeriod(start, end);
            String title = eventBean.getOriginalTitle();

            Set<FenixSpace> rooms = new HashSet<>();
            if (eventBean.getRooms() != null) {
                for (AllocatableSpace room : eventBean.getRooms()) {
                    if (room != null) {
                        rooms.add(FenixSpace.getSimpleSpace(room));
                    }
                }
            }

            FenixCalendarEvent event = null;

            switch (eventType) {
            case CLASS:
                final Shift classShift = ((ClassEventBean) eventBean).getClassShift();
                event = new FenixClassEvent(eventPeriod, rooms, title, new FenixCourse(classShift.getExecutionCourse()));
                break;

            case EVALUATION:

                Set<FenixCourse> fenixCourses =
                        FluentIterable.from(((EvaluationEventBean) eventBean).getCourses())
                                .transform(new Function<ExecutionCourse, FenixCourse>() {

                                    @Override
                                    public FenixCourse apply(ExecutionCourse course) {
                                        return new FenixCourse(course);
                                    }

                                }).toSet();
                event = new FenixEvaluationEvent(eventPeriod, rooms, title, fenixCourses);
                break;
            }

            events.add(event);
        }
        return new FenixCalendar(academicTerm, events);
    }

    private FenixCalendar getFenixClassCalendar(String academicTerm, List<EventBean> eventBeans) {
        return getFenixCalendar(academicTerm, eventBeans, EventType.CLASS);
    }

    private FenixCalendar getFenixEvaluationsCalendar(String academicTerm, List<EventBean> eventBeans) {
        return getFenixCalendar(academicTerm, eventBeans, EventType.EVALUATION);
    }

    private String evaluationCalendarICal(Person person) {
        ICalendarSyncPoint calendarSyncPoint = new ICalendarSyncPoint();

        List<EventBean> listEventBean = calendarSyncPoint.getExams(person.getUser());
        listEventBean.addAll(calendarSyncPoint.getTeachingExams(person.getUser()));

        Calendar createCalendar = CalendarFactory.createCalendar(listEventBean);

        return createCalendar.toString();
    }

    private FenixCalendar evaluationCalendarJson(Person person) {
        final User user = person.getUser();

        String academicTerm = ExecutionYear.readCurrentExecutionYear().getQualifiedName();

        ICalendarSyncPoint calendarSyncPoint = new ICalendarSyncPoint();
        List<EventBean> listEventBean = calendarSyncPoint.getExams(user);
        listEventBean.addAll(calendarSyncPoint.getTeachingExams(user));

        return getFenixEvaluationsCalendar(academicTerm, listEventBean);
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
    public Response calendarEvaluation(@QueryParam("format") String format) {
        validateFormat(format);
        final Person person = getPerson();
        if (!person.hasRole(RoleType.STUDENT)) {
            return Response.status(Status.OK).header(HttpHeaders.CONTENT_TYPE, JSON_UTF8).entity("{}").build();
        }

        if ("calendar".equals(format)) {
            String evaluationCalendarICal = evaluationCalendarICal(person);
            return Response.ok(evaluationCalendarICal, "text/calendar;charset=UTF-8").build();
        } else {
            return Response.status(Status.OK).header(HttpHeaders.CONTENT_TYPE, JSON_UTF8).entity(evaluationCalendarJson(person))
                    .build();
        }
    }

    private String classesCalendarICal(Person person) {
        ICalendarSyncPoint calendarSyncPoint = new ICalendarSyncPoint();

        List<EventBean> listEventBean = calendarSyncPoint.getClasses(person.getUser());
        listEventBean.addAll(calendarSyncPoint.getTeachingClasses(person.getUser()));

        Calendar createCalendar = CalendarFactory.createCalendar(listEventBean);

        return createCalendar.toString();
    }

    private FenixCalendar classesCalendarJson(Person person) {

        final User user = person.getUser();
        String academicTerm = ExecutionYear.readCurrentExecutionYear().getQualifiedName();

        ICalendarSyncPoint calendarSyncPoint = new ICalendarSyncPoint();
        List<EventBean> listEventBean = calendarSyncPoint.getClasses(user);
        listEventBean.addAll(calendarSyncPoint.getTeachingClasses(user));

        return getFenixClassCalendar(academicTerm, listEventBean);
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
    public Response calendarClasses(@QueryParam("format") String format) {
        validateFormat(format);
        Person person = getPerson();
        if ("calendar".equals(format)) {
            String classesCalendarICal = classesCalendarICal(person);
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
                String start = studentCurricularPlan.getStartDateYearMonthDay().toString(formatDay);

                final RegistrationCurriculumBean registrationCurriculumBean = new RegistrationCurriculumBean(registration);
                final Integer curricularYear = registrationCurriculumBean.getCurriculum().getCurricularYear();

                String end = null;
                if (studentCurricularPlan.getEndDate() != null) {
                    end = studentCurricularPlan.getEndDate().toString(formatDay);
                }
                RegistrationConclusionBean registrationConclusionBean = new RegistrationConclusionBean(registration);

                ICurriculum icurriculum = registrationConclusionBean.getCurriculumForConclusion();

                BigDecimal credits = icurriculum.getSumEctsCredits();
                BigDecimal average = icurriculum.getAverage();

                Integer calculatedAverage = icurriculum.getRoundedAverage();

                boolean isFinished = registrationConclusionBean.isConcluded();

                final List<FenixCurriculum.ApprovedCourse> courseInfos = new ArrayList<>();

                for (CycleCurriculumGroup cycleCurriculumGroup : studentCurricularPlan.getInternalCycleCurriculumGrops()) {
                    for (ICurriculumEntry iCurriculumEntry : cycleCurriculumGroup.getCurriculum().getCurriculumEntries()) {

                        String entryGradeValue = iCurriculumEntry.getGradeValue();
                        BigDecimal entryEcts = iCurriculumEntry.getEctsCreditsForCurriculum();

                        FenixCourse course = null;
                        if (iCurriculumEntry instanceof Enrolment) {
                            Enrolment enrolment = (Enrolment) iCurriculumEntry;
                            ExecutionCourse executionCourse = enrolment.getExecutionCourseFor(enrolment.getExecutionPeriod());
                            if (executionCourse != null) {
                                course = new FenixCourse(executionCourse);
                            } else {
                                String entryName = mls(iCurriculumEntry.getPresentationName());
                                course = new FenixCourse(null, null, entryName);
                            }

                        } else {
                            String entryName = mls(iCurriculumEntry.getPresentationName());
                            course = new FenixCourse(null, null, entryName);
                        }

                        courseInfos.add(new FenixCurriculum.ApprovedCourse(course, entryGradeValue, entryEcts));

                    }
                }
                curriculums.add(new FenixCurriculum(new FenixDegree(studentCurricularPlan.getDegree()), start, end, credits,
                        average, calculatedAverage, isFinished, curricularYear, courseInfos));
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
        List<PendingEvent> notPayed = new ArrayList<>();

        for (Event event : notPayedEvents) {

            for (AccountingEventPaymentCode accountingEventPaymentCode : event.getNonProcessedPaymentCodes()) {
                String description = accountingEventPaymentCode.getDescription();
                String startDate = formatDay.print(accountingEventPaymentCode.getStartDate()) + " 00:00";
                String endDate = formatDay.print(accountingEventPaymentCode.getEndDate()) + " 23:59";
                String entity = accountingEventPaymentCode.getEntityCode();
                String reference = accountingEventPaymentCode.getFormattedCode();
                String amount = accountingEventPaymentCode.getMinAmount().getAmountAsString();
                notPayed.add(new PendingEvent(description, new FenixPeriod(startDate, endDate), entity, reference, amount));
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
    public List<FenixCourseEvaluation.WrittenEvaluation> evaluations(@Context HttpServletResponse response,
            @Context HttpServletRequest request, @Context ServletContext context) {

        Person person = getPerson();
        final Student student = person.getStudent();
        if (!person.hasRole(RoleType.STUDENT) || student == null) {
            return new ArrayList<FenixCourseEvaluation.WrittenEvaluation>();
        }

        new JerseyFacesContext(context, request, response);

        DisplayEvaluationsForStudentToEnrol manageEvaluationsForStudents = new DisplayEvaluationsForStudentToEnrol();

        List<FenixCourseEvaluation.WrittenEvaluation> evaluations = new ArrayList<>();

        evaluations.addAll(processEvaluation(manageEvaluationsForStudents.getEnroledEvaluations(), true, student));
        evaluations.addAll(processEvaluation(manageEvaluationsForStudents.getNotEnroledEvaluations(), false, student));

        return evaluations;
    }

    private List<FenixCourseEvaluation.WrittenEvaluation> processEvaluation(List<Evaluation> listEvaluation,
            final Boolean isEnrolled, final Student student) {

        return FluentIterable.from(listEvaluation).transform(new Function<Evaluation, FenixCourseEvaluation.WrittenEvaluation>() {

            @Override
            public FenixCourseEvaluation.WrittenEvaluation apply(Evaluation writtenEvaluation) {
                return getWrittenEvaluationJSON((WrittenEvaluation) writtenEvaluation, isEnrolled, student);
            }

        }).toList();
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
    public List<FenixCourseEvaluation.WrittenEvaluation> evaluations(@PathParam("id") String oid,
            @QueryParam("enrol") String enrol, @Context HttpServletResponse response, @Context HttpServletRequest request,
            @Context ServletContext context) {
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
     * generic information about the institution
     * 
     * @summary Information about the institution
     * @return news and events rss
     */
    @GET
    @Produces(JSON_UTF8)
    @Path("about")
    @FenixAPIPublic
    public FenixAbout about() {
        return FenixAbout.getInstance();
    }

    /**
     * Academic Terms
     * 
     * @summary Lists all academic terms
     * @return all the academic terms available to be used in other endpoints as academicTerm query parameter.
     */
    @GET
    @Produces(JSON_UTF8)
    @Path("academicterms")
    @FenixAPIPublic
    public String academicTerms() {
        JsonObject obj = new JsonObject();
        for (ExecutionYear year : Bennu.getInstance().getExecutionYearsSet()) {
            JsonArray sems = new JsonArray();
            for (ExecutionSemester semester : year.getExecutionPeriodsSet()) {
                sems.add(new JsonPrimitive(semester.getQualifiedName()));
            }
            obj.add(year.getQualifiedName(), sems);
        }
        return gson.toJson(obj);
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
     * All information about degrees available
     * 
     * @summary All degrees
     * @param academicTerm
     * @see academicTerms
     * 
     */
    @GET
    @Produces(JSON_UTF8)
    @Path("degrees")
    @FenixAPIPublic
    public List<FenixDegreeExtended> degrees(@QueryParam("academicTerm") String academicTerm) {

        AcademicInterval academicInterval = getAcademicInterval(academicTerm);

        List<FenixDegreeExtended> fenixDegrees = new ArrayList<>();

        for (final ExecutionDegree executionDegree : ExecutionDegree.filterByAcademicInterval(academicInterval)) {
            fenixDegrees.add(getFenixDegree(executionDegree));
        }
        return fenixDegrees;
    }

    @GET
    @Produces(JSON_UTF8)
    @Path("canteen")
    @FenixAPIPublic
    public String canteen() {
        return FenixAPICanteen.get();
    }

    @GET
    @Produces(JSON_UTF8)
    @Path("shuttle")
    @FenixAPIPublic
    public String shuttle() {
        return getFileInfo("/api/shuttle.json");
    }

    private String getFileInfo(String file) {
        final InputStream resourceAsStream = getClass().getResourceAsStream(file);
        if (resourceAsStream == null) {
            return new JsonObject().toString();
        }
        try {
            return Streams.asString(resourceAsStream);
        } catch (IOException e) {
            return new JsonObject().toString();
        }
    }

    @GET
    @Produces(JSON_UTF8)
    @Path("degrees/all")
    @FenixAPIPublic
    public Set<FenixDegree> degreesAll() {

        Set<FenixDegree> degrees = new HashSet<FenixDegree>();

        for (final Degree degree : Degree.readNotEmptyDegrees()) {
            degrees.add(new FenixDegree(degree, true));
        }
        return degrees;
    }

    private FenixDegreeExtended getFenixDegree(ExecutionDegree executionDegree) {
        final Degree degree = executionDegree.getDegree();
        List<FenixSpace> degreeCampus = new ArrayList<>();
        ExecutionYear executionYear = executionDegree.getExecutionYear();

        String type = degree.getDegreeTypeName();
        String typeName = degree.getDegreeType().getFilteredName();
        String degreeUrl = FenixConfigurationManager.getFenixUrl() + degree.getSite().getReversePath();

        for (Campus campus : degree.getCampus(executionYear)) {
            degreeCampus.add(FenixSpace.getSimpleSpace(campus));
        }

        FenixDegreeExtended.FenixDegreeInfo fenixDegreeInfo = null;

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

        FenixDegreeExtended fenixDegree =
                new FenixDegreeExtended(executionYear.getQualifiedName(), degree, type, typeName, degreeUrl, degreeCampus,
                        fenixDegreeInfo, teachers);
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
    public FenixDegree degreesByOid(@PathParam("id") String oid, @QueryParam("academicTerm") String academicTerm) {
        Degree degree = getDomainObject(oid, Degree.class);
        final AcademicInterval academicInterval = getAcademicInterval(academicTerm, true);
        List<ExecutionDegree> executionDegrees = degree.getExecutionDegrees(academicInterval);
        if (!executionDegrees.isEmpty()) {
            final ExecutionDegree max = Ordering.from(ExecutionDegree.EXECUTION_DEGREE_COMPARATORY_BY_YEAR).max(executionDegrees);
            if (max != null) {
                return getFenixDegree(max);
            }
        }

        throw newApplicationError(Status.NOT_FOUND, "resource_not_found", "No degree information found for " + degree.getName()
                + " on " + academicInterval.getPresentationName());
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
    public List<FenixExecutionCourse> coursesByODegreesId(@PathParam("id") String oid,
            @QueryParam("academicTerm") String academicTerm) {

        Degree degree = getDomainObject(oid, Degree.class);

        ExecutionSemester[] executionSemesters = getExecutionSemesters(academicTerm).toArray(new ExecutionSemester[0]);

        final Set<ExecutionCourseView> executionCoursesViews = new HashSet<ExecutionCourseView>();
        for (final DegreeCurricularPlan degreeCurricularPlan : degree.getDegreeCurricularPlansSet()) {
            if (degreeCurricularPlan.isActive()) {
                degreeCurricularPlan.addExecutionCourses(executionCoursesViews, executionSemesters);
            }
        }

        List<FenixExecutionCourse> fenixExecutionCourses = new ArrayList<>();

        for (ExecutionCourseView executionCourseView : executionCoursesViews) {
            ExecutionCourse executionCourse = executionCourseView.getExecutionCourse();
            String sigla = executionCourse.getSigla();
            String credits = getCredits(executionCourse, degree);
            String name = executionCourse.getName();
            String id = executionCourse.getExternalId();
            String academicTermValue = executionCourse.getExecutionPeriod().getQualifiedName();

            fenixExecutionCourses.add(new FenixExecutionCourse(sigla, credits, name, id, academicTermValue));
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
        return FenixConfigurationManager.getFenixUrl();
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
    public FenixCourseExtended coursesByOid(@PathParam("id") String oid) {
        ExecutionCourse executionCourse = getDomainObject(oid, ExecutionCourse.class);

        String acronym = executionCourse.getSigla();
        String name = executionCourse.getName();
        String evaluationMethod = executionCourse.getEvaluationMethodText();
        String academicTerm = executionCourse.getExecutionPeriod().getQualifiedName();
        String courseUrl = FenixConfigurationManager.getFenixUrl() + executionCourse.getSite().getReversePath();

        Map<CompetenceCourse, Set<CurricularCourse>> curricularCourses =
                executionCourse.getCurricularCoursesIndexedByCompetenceCourse();

        List<FenixCourseExtended.FenixCompetence> moreInfo = new ArrayList<>();

        for (Map.Entry<CompetenceCourse, Set<CurricularCourse>> entry : curricularCourses.entrySet()) {

            List<FenixCourseExtended.FenixCompetence.BiblioRef> biblios = new ArrayList<>();
            final CompetenceCourse competenceCourse = entry.getKey();
            for (BibliographicReference bibliographicReference : competenceCourse.getBibliographicReferences()
                    .getBibliographicReferencesSortedByOrder()) {

                String author = bibliographicReference.getAuthors();
                String reference = bibliographicReference.getReference();
                String title = bibliographicReference.getTitle();
                String bibYear = bibliographicReference.getYear();
                String type = bibliographicReference.getType().getName();
                String url = bibliographicReference.getUrl();

                biblios.add(new BiblioRef(author, reference, title, bibYear, type, url));
            }

            List<FenixCourseExtended.FenixCompetence.Degree> degrees = new ArrayList<>();
            for (CurricularCourse curricularCourse : entry.getValue()) {
                String id = curricularCourse.getDegree().getExternalId();
                String dName = curricularCourse.getDegree().getPresentationName();
                String dacronym = curricularCourse.getAcronym();

                degrees.add(new FenixCourseExtended.FenixCompetence.Degree(id, dName, dacronym));
            }

            String program = competenceCourse.getProgram();

            moreInfo.add(new FenixCompetence(competenceCourse.getExternalId(), program, biblios, degrees));

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

        return new FenixCourseExtended(acronym, name, evaluationMethod, academicTerm, numberOfStudents, annoucementLink,
                summaryLink, courseUrl, moreInfo, teachers);
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

        String start = null;
        String end = null;

        if (projectEvaluation.getProjectBeginDateTime() != null) {
            start = formatDayHour.print(projectEvaluation.getProjectBeginDateTime());
        }
        if (projectEvaluation.getProjectEndDateTime() != null) {
            end = formatDayHour.print(projectEvaluation.getProjectEndDateTime());
        }
        return new FenixCourseEvaluation.Project(name, new FenixPeriod(start, end));
    }

    private FenixCourseEvaluation.WrittenEvaluation getWrittenEvaluationJSON(WrittenEvaluation writtenEvaluation) {
        return getWrittenEvaluationJSON(writtenEvaluation, null, null);
    }

    private FenixCourseEvaluation.WrittenEvaluation getWrittenEvaluationJSON(WrittenEvaluation writtenEvaluation,
            Boolean isEnrolled, Student student) {
        String name = writtenEvaluation.getPresentationName();
        EvaluationType type = writtenEvaluation.getEvaluationType();

        String day = dataFormatDay.format(writtenEvaluation.getDay().getTime());

        String beginningTime = dataFormatHour.format(writtenEvaluation.getBeginning().getTime());
        String endTime = dataFormatHour.format(writtenEvaluation.getEnd().getTime());

        FenixPeriod evaluationPeriod =
                new FenixPeriod(Joiner.on(" ").join(day, beginningTime), Joiner.on(" ").join(day, endTime));

        boolean isEnrolmentPeriod = writtenEvaluation.getIsInEnrolmentPeriod();

        final DateTime start = writtenEvaluation.getEnrolmentPeriodStart();
        final DateTime end = writtenEvaluation.getEnrolmentPeriodEnd();
        final String enrollmentPeriodStart = start == null ? null : start.toString("yyyy-MM-dd HH:mm:ss");
        final String enrollmentPeriodEnd = end == null ? null : end.toString("yyyy-MM-dd HH:mm:ss");

        Set<ExecutionCourse> courses = new HashSet<>();
        String writtenEvaluationId = writtenEvaluation.getExternalId();
        if (student != null) {
            Room assignedRoom = null;
            for (ExecutionCourse executionCourse : writtenEvaluation.getAssociatedExecutionCoursesSet()) {
                final Registration registration = executionCourse.getRegistration(student.getPerson());
                final Attends attendsByStudent = executionCourse.getAttendsByStudent(student);
                if (registration != null || attendsByStudent != null) {
                    courses.add(executionCourse);
                }
            }

            final WrittenEvaluationEnrolment evalEnrolment = writtenEvaluation.getWrittenEvaluationEnrolmentFor(student);
            if (evalEnrolment != null) {
                assignedRoom = (Room) evalEnrolment.getRoom();
            }

            if (type.equals(EvaluationType.EXAM_TYPE)) {
                return new FenixCourseEvaluation.Exam(writtenEvaluationId, name, evaluationPeriod, isEnrolmentPeriod,
                        enrollmentPeriodStart, enrollmentPeriodEnd, writtenEvaluation.getAssociatedRooms(), isEnrolled, courses,
                        assignedRoom);
            } else {
                return new FenixCourseEvaluation.Test(writtenEvaluationId, name, evaluationPeriod, isEnrolmentPeriod,
                        enrollmentPeriodStart, enrollmentPeriodEnd, writtenEvaluation.getAssociatedRooms(), isEnrolled, courses,
                        assignedRoom);
            }
        }

        if (type.equals(EvaluationType.EXAM_TYPE)) {
            return new FenixCourseEvaluation.Exam(writtenEvaluationId, name, evaluationPeriod, isEnrolmentPeriod,
                    enrollmentPeriodStart, enrollmentPeriodEnd, writtenEvaluation.getAssociatedRooms(), isEnrolled, courses);
        } else {
            return new FenixCourseEvaluation.Test(writtenEvaluationId, name, evaluationPeriod, isEnrolmentPeriod,
                    enrollmentPeriodStart, enrollmentPeriodEnd, writtenEvaluation.getAssociatedRooms(), isEnrolled, courses);
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
            campi.add(FenixSpace.getSimpleSpace(campus));
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
        return FenixSpace.getSpace(space);
    }

    /**
     * Returns the blueprint of this space
     * 
     * @param oid
     * @param day
     *            ("dd/mm/yyyy")
     * @return
     */
    @GET
    @Path("spaces/{id}/blueprint")
    @FenixAPIPublic
    public Response spaceBlueprint(@PathParam("id") String oid, final @QueryParam("format") String format) {

        final boolean isDwgFormat = format != null && format.equals("dwg");
        final Space space = getDomainObject(oid, Space.class);
        Blueprint mostRecentBlueprint = space.getMostRecentBlueprint();
        mostRecentBlueprint = (mostRecentBlueprint == null) ? space.getSuroundingSpaceMostRecentBlueprint() : mostRecentBlueprint;
        StreamingOutput stream;

        if (mostRecentBlueprint != null) {
            if (isDwgFormat) {
                final BlueprintFile blueprintFile = mostRecentBlueprint.getBlueprintFile();
                final InputStream inputStream = new ByteArrayInputStream(blueprintFile.getContentFile().getBytes());
                stream = new StreamingOutput() {

                    @Override
                    public void write(OutputStream output) throws IOException, WebApplicationException {
                        Streams.copy(inputStream, output, false);
                    }
                };
            } else {
                final Blueprint blueprint = mostRecentBlueprint;
                stream = new StreamingOutput() {
                    @Override
                    public void write(OutputStream os) throws IOException, WebApplicationException {
                        SpaceInformation spaceInformation = space.getSpaceInformation();
                        Boolean isSuroundingSpaceBlueprint = true;
                        Boolean isToViewOriginalSpaceBlueprint = false;
                        Boolean viewBlueprintNumbers = true;
                        Boolean isToViewIdentifications = true;
                        Boolean isToViewDoorNumbers = false;
                        BigDecimal scalePercentage = new BigDecimal(100);
                        ManageSpaceBlueprintsDA.writeBlueprint(spaceInformation, isSuroundingSpaceBlueprint,
                                isToViewOriginalSpaceBlueprint, viewBlueprintNumbers, isToViewIdentifications,
                                isToViewDoorNumbers, scalePercentage, blueprint, os);
                        os.flush();
                    }
                };
            }
            final String contentType = isDwgFormat ? "application/dwg" : "image/jpeg";
            final String filename = space.getExternalId() + (isDwgFormat ? ".dwg" : ".jpg");
            return Response.ok(stream, contentType).header("Content-Disposition", "attachment; filename=" + filename).build();
        }

        return Response.noContent().build();
    }

    private FenixSpace.Room getFenixRoom(Room room, java.util.Calendar rightNow) {

        InfoSiteRoomTimeTable bodyComponent = new InfoSiteRoomTimeTable();
        RoomSiteComponentBuilder builder = new RoomSiteComponentBuilder();
        List<FenixRoomEvent> roomEvents = new ArrayList<FenixRoomEvent>();

        try {
            builder.getComponent(bodyComponent, rightNow, room, null);
            for (Object occupation : bodyComponent.getInfoShowOccupation()) {
                InfoShowOccupation showOccupation = (InfoShowOccupation) occupation;
                DateTime date = new DateTime(rightNow);
                DateTime newDate = date.withDayOfWeek(showOccupation.getDiaSemana().getDiaSemanaInDayOfWeekJodaFormat());
                String day = newDate.toString("dd/MM/yyyy");

                FenixRoomEvent roomEvent = null;

                if (showOccupation instanceof InfoLesson || showOccupation instanceof InfoLessonInstance) {
                    InfoShowOccupation lesson = showOccupation;
                    InfoExecutionCourse infoExecutionCourse = lesson.getInfoShift().getInfoDisciplinaExecucao();

                    String start = dataFormatHour.format(lesson.getInicio().getTime());
                    String end = dataFormatHour.format(lesson.getFim().getTime());
                    String weekday = lesson.getDiaSemana().getDiaSemanaString();

                    FenixPeriod period = new FenixPeriod(day + " " + start, day + " " + end);

                    String info = lesson.getInfoShift().getShiftTypesCodePrettyPrint();

                    FenixCourse course = new FenixCourse(infoExecutionCourse.getExecutionCourse());

                    roomEvent = new FenixRoomEvent.LessonEvent(start, end, weekday, day, period, info, course);

                } else if (showOccupation instanceof InfoWrittenEvaluation) {
                    InfoWrittenEvaluation infoWrittenEvaluation = (InfoWrittenEvaluation) showOccupation;

                    List<FenixCourse> courses = new ArrayList<>();

                    for (int iterEC = 0; iterEC < infoWrittenEvaluation.getAssociatedExecutionCourse().size(); iterEC++) {
                        InfoExecutionCourse infoEC = infoWrittenEvaluation.getAssociatedExecutionCourse().get(iterEC);
                        courses.add(new FenixCourse(infoEC.getExecutionCourse()));
                    }

                    String start = null;
                    String end = null;
                    String weekday = null;

                    if (infoWrittenEvaluation instanceof InfoExam) {
                        InfoExam infoExam = (InfoExam) infoWrittenEvaluation;
                        start = infoExam.getBeginningHour();
                        end = infoExam.getEndHour();
                        weekday = infoWrittenEvaluation.getDiaSemana().getDiaSemanaString();

                        FenixPeriod period = new FenixPeriod(day + " " + start, day + " " + end);

                        Integer season = infoExam.getSeason().getSeason();

                        roomEvent =
                                new FenixRoomEvent.WrittenEvaluationEvent.ExamEvent(start, end, weekday, day, period, courses,
                                        season);

                    } else if (infoWrittenEvaluation instanceof InfoWrittenTest) {
                        InfoWrittenTest infoWrittenTest = (InfoWrittenTest) infoWrittenEvaluation;
                        String description = infoWrittenTest.getDescription();
                        start = dataFormatHour.format(infoWrittenTest.getInicio().getTime());
                        end = dataFormatHour.format(infoWrittenTest.getFim().getTime());
                        weekday = infoWrittenTest.getDiaSemana().getDiaSemanaString();

                        FenixPeriod period = new FenixPeriod(day + " " + start, day + " " + end);

                        roomEvent =
                                new FenixRoomEvent.WrittenEvaluationEvent.TestEvent(start, end, weekday, day, period, courses,
                                        description);
                    }

                } else if (showOccupation instanceof InfoGenericEvent) {

                    InfoGenericEvent infoGenericEvent = (InfoGenericEvent) showOccupation;
                    String description = infoGenericEvent.getDescription();
                    String title = infoGenericEvent.getTitle();
                    String start = dataFormatHour.format(infoGenericEvent.getInicio().getTime());
                    String end = dataFormatHour.format(infoGenericEvent.getFim().getTime());
                    String weekday = infoGenericEvent.getDiaSemana().getDiaSemanaString();
                    FenixPeriod period = new FenixPeriod(day + " " + start, day + " " + end);

                    roomEvent = new FenixRoomEvent.GenericEvent(start, end, weekday, day, period, description, title);
                }

                if (roomEvent != null) {
                    roomEvents.add(roomEvent);
                }

            }

            return new FenixSpace.Room(room, roomEvents);

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

    private AcademicInterval getDefaultAcademicTerm() {
        return ExecutionSemester.readActualExecutionSemester().getAcademicInterval();
    }

    private AcademicInterval getAcademicInterval(String academicTerm) {
        return getAcademicInterval(academicTerm, false);
    }

    private AcademicInterval getAcademicInterval(String academicTerm, Boolean nullDefault) {

        if (StringUtils.isEmpty(academicTerm)) {
            return nullDefault ? null : getDefaultAcademicTerm();
        }

        ExecutionInterval interval = ExecutionInterval.getExecutionInterval(academicTerm);

        if (interval == null) {
            throw newApplicationError(Status.NOT_FOUND, "resource_not_found", "Can't find the academic term : " + academicTerm);
        }

        return interval.getAcademicInterval();

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

    /**
     * information about the domain model implemented by this application
     * 
     * @summary Representation of the Domain Model
     * @return domain model
     */
    @GET
    @Produces(JSON_UTF8)
    @Path("domainModel")
    @FenixAPIPublic
    public String domainModel() {
        return new FenixDomainModel().toJSONString();
    }

}