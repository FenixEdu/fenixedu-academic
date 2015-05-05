/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import javax.servlet.ServletContext;
import javax.servlet.UnavailableException;
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

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.api.beans.FenixCalendar;
import org.fenixedu.academic.api.beans.FenixCalendar.FenixCalendarEvent;
import org.fenixedu.academic.api.beans.FenixCalendar.FenixClassEvent;
import org.fenixedu.academic.api.beans.FenixCalendar.FenixEvaluationEvent;
import org.fenixedu.academic.api.beans.FenixCourse;
import org.fenixedu.academic.api.beans.FenixCurriculum;
import org.fenixedu.academic.api.beans.FenixPayment;
import org.fenixedu.academic.api.beans.FenixPayment.PaymentEvent;
import org.fenixedu.academic.api.beans.FenixPayment.PendingEvent;
import org.fenixedu.academic.api.beans.FenixPerson;
import org.fenixedu.academic.api.beans.FenixPerson.FenixPhoto;
import org.fenixedu.academic.api.beans.FenixPerson.FenixRole;
import org.fenixedu.academic.api.beans.FenixPersonCourses;
import org.fenixedu.academic.api.beans.FenixPersonCourses.FenixEnrolment;
import org.fenixedu.academic.api.beans.publico.FenixAbout;
import org.fenixedu.academic.api.beans.publico.FenixCourseEvaluation;
import org.fenixedu.academic.api.beans.publico.FenixCourseExtended;
import org.fenixedu.academic.api.beans.publico.FenixCourseExtended.FenixCompetence;
import org.fenixedu.academic.api.beans.publico.FenixCourseExtended.FenixCompetence.BiblioRef;
import org.fenixedu.academic.api.beans.publico.FenixCourseGroup;
import org.fenixedu.academic.api.beans.publico.FenixCourseStudents;
import org.fenixedu.academic.api.beans.publico.FenixDegree;
import org.fenixedu.academic.api.beans.publico.FenixDegreeExtended;
import org.fenixedu.academic.api.beans.publico.FenixDegreeExtended.FenixDegreeInfo;
import org.fenixedu.academic.api.beans.publico.FenixDegreeExtended.FenixTeacher;
import org.fenixedu.academic.api.beans.publico.FenixDomainModel;
import org.fenixedu.academic.api.beans.publico.FenixExecutionCourse;
import org.fenixedu.academic.api.beans.publico.FenixPeriod;
import org.fenixedu.academic.api.beans.publico.FenixRoomEvent;
import org.fenixedu.academic.api.beans.publico.FenixSchedule;
import org.fenixedu.academic.api.beans.publico.FenixSpace;
import org.fenixedu.academic.api.infra.FenixAPIFromExternalServer;
import org.fenixedu.academic.api.infra.JerseyFacesContext;
import org.fenixedu.academic.domain.AdHocEvaluation;
import org.fenixedu.academic.domain.Attends;
import org.fenixedu.academic.domain.CompetenceCourse;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.DegreeInfo;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.Evaluation;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Grouping;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.Photograph;
import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.academic.domain.Project;
import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.WrittenEvaluation;
import org.fenixedu.academic.domain.WrittenEvaluationEnrolment;
import org.fenixedu.academic.domain.accounting.Entry;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.paymentCodes.AccountingEventPaymentCode;
import org.fenixedu.academic.domain.contacts.EmailAddress;
import org.fenixedu.academic.domain.contacts.WebAddress;
import org.fenixedu.academic.domain.degreeStructure.BibliographicReferences.BibliographicReference;
import org.fenixedu.academic.domain.degreeStructure.ProgramConclusion;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.domain.space.SpaceUtils;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.domain.student.curriculum.ICurriculum;
import org.fenixedu.academic.domain.student.curriculum.ICurriculumEntry;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumGroup;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicInterval;
import org.fenixedu.academic.domain.util.icalendar.CalendarFactory;
import org.fenixedu.academic.domain.util.icalendar.ClassEventBean;
import org.fenixedu.academic.domain.util.icalendar.EvaluationEventBean;
import org.fenixedu.academic.domain.util.icalendar.EventBean;
import org.fenixedu.academic.dto.ExecutionCourseView;
import org.fenixedu.academic.dto.InfoExam;
import org.fenixedu.academic.dto.InfoExecutionCourse;
import org.fenixedu.academic.dto.InfoLesson;
import org.fenixedu.academic.dto.InfoLessonInstance;
import org.fenixedu.academic.dto.InfoOccupation;
import org.fenixedu.academic.dto.InfoShowOccupation;
import org.fenixedu.academic.dto.InfoSiteRoomTimeTable;
import org.fenixedu.academic.dto.InfoWrittenEvaluation;
import org.fenixedu.academic.dto.InfoWrittenTest;
import org.fenixedu.academic.dto.externalServices.PersonInformationBean;
import org.fenixedu.academic.service.Factory.RoomSiteComponentBuilder;
import org.fenixedu.academic.service.services.student.EnrolStudentInWrittenEvaluation;
import org.fenixedu.academic.service.services.student.UnEnrollStudentInWrittenEvaluation;
import org.fenixedu.academic.ui.faces.bean.student.enrolment.DisplayEvaluationsForStudentToEnrol;
import org.fenixedu.academic.ui.struts.action.ICalendarSyncPoint;
import org.fenixedu.academic.util.ContentType;
import org.fenixedu.academic.util.EvaluationType;
import org.fenixedu.academic.util.MultiLanguageString;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.core.util.CoreConfiguration;
import org.fenixedu.bennu.oauth.annotation.OAuthEndpoint;
import org.fenixedu.commons.i18n.I18N;
import org.fenixedu.spaces.domain.BlueprintFile;
import org.fenixedu.spaces.domain.Space;
import org.fenixedu.spaces.services.SpaceBlueprintsDWGProcessor;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;

import com.google.common.base.CaseFormat;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Ordering;
import com.google.common.io.ByteStreams;
import com.google.common.net.HttpHeaders;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

@SuppressWarnings("unchecked")
@Path("/fenix/v1")
public class FenixAPIv1 {

    private static final Logger logger = LoggerFactory.getLogger(FenixAPIv1.class);

    public final static String PERSONAL_SCOPE = "INFO";
    public final static String SCHEDULE_SCOPE = "SCHEDULE";
    public final static String EVALUATIONS_SCOPE = "EVALUATIONS";
    public final static String CURRICULAR_SCOPE = "CURRICULAR";
    public final static String PAYMENTS_SCOPE = "PAYMENTS";

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
                String data = Base64.getEncoder().encodeToString(avatar);
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
    @OAuthEndpoint(PERSONAL_SCOPE)
    public FenixPerson person() {

        final Person person = getPerson();
        PersonInformationBean pib = new PersonInformationBean(person, true);

        final Set<FenixRole> roles = new HashSet<FenixRole>();

        if (isTeacher(person) || RoleType.TEACHER.isMember(person.getUser())) {
            roles.add(new FenixPerson.TeacherFenixRole(pib.getTeacherDepartment()));
        }

        if (RoleType.STUDENT.isMember(person.getUser())) {
            roles.add(new FenixPerson.StudentFenixRole(pib.getStudentRegistrations()));

        }

        if (RoleType.ALUMNI.isMember(person.getUser())) {

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
    @OAuthEndpoint(CURRICULAR_SCOPE)
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
            semesters.addAll(((ExecutionYear) executionInterval).getExecutionPeriodsSet());
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
                for (Space room : eventBean.getRooms()) {
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

                FenixSpace assignedRoom = FenixSpace.getSimpleSpace(((EvaluationEventBean) eventBean).getAssignedRoom());

                event = new FenixEvaluationEvent(eventPeriod, assignedRoom, rooms, title, fenixCourses);
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
    @OAuthEndpoint(SCHEDULE_SCOPE)
    @GET
    @Path("person/calendar/evaluations")
    public Response calendarEvaluation(@QueryParam("format") String format) {
        validateFormat(format);
        final Person person = getPerson();
        if (!RoleType.STUDENT.isMember(person.getUser())) {
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
    @OAuthEndpoint(SCHEDULE_SCOPE)
    @GET
    @Path("person/calendar/classes")
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
    @OAuthEndpoint(CURRICULAR_SCOPE)
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

                String end = null;
                if (studentCurricularPlan.getEndDate() != null) {
                    end = studentCurricularPlan.getEndDate().toString(formatDay);
                }

                Stream<CurriculumGroup> curriculumGroups = getAllGroupsForConclusion(studentCurricularPlan);

                ICurriculum icurriculum = studentCurricularPlan.getCurriculum(new DateTime(), null);

                final Integer curricularYear = icurriculum.getCurricularYear();
                BigDecimal credits = icurriculum.getSumEctsCredits();
                BigDecimal average = icurriculum.getAverage();

                Integer calculatedAverage = icurriculum.getRoundedAverage();

                boolean isFinished = studentCurricularPlan.isConcluded();

                final List<FenixCurriculum.ApprovedCourse> courseInfos = new ArrayList<>();

                curriculumGroups.forEach(curriculumGroup -> {
                    for (ICurriculumEntry iCurriculumEntry : curriculumGroup.getCurriculum().getCurriculumEntries()) {

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
                });
                curriculums.add(new FenixCurriculum(new FenixDegree(studentCurricularPlan.getDegree()), start, end, credits,
                        average, calculatedAverage, isFinished, curricularYear, courseInfos));
            }
        }
        return curriculums;
    }

    protected Stream<CurriculumGroup> getAllGroupsForConclusion(StudentCurricularPlan studentCurricularPlan) {
        final Stream<ProgramConclusion> conclusions = ProgramConclusion.conclusionsFor(studentCurricularPlan);

        Stream<CurriculumGroup> curriculumGroups =
                conclusions.map(pc -> pc.groupFor(studentCurricularPlan)).filter(Optional::isPresent).map(Optional::get);
        return curriculumGroups;
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
    @OAuthEndpoint(PAYMENTS_SCOPE)
    @GET
    @Path("person/payments")
    @Produces(JSON_UTF8)
    public FenixPayment personPayments() {

        Person person = getPerson();

        List<PaymentEvent> payed = new ArrayList<>();

        for (Entry entry : person.getPayments()) {
            String id = entry.getExternalId();
            String amount = entry.getOriginalAmount().getAmountAsString();
            String name = entry.getPaymentMode().getName();
            String description = entry.getDescription().toString();
            String date = formatDay.print(entry.getWhenRegistered());
            payed.add(new PaymentEvent(id, amount, name, description, date));
        }

        List<Event> notPayedEvents = calculateNotPayedEvents(person);
        List<PendingEvent> notPayed = new ArrayList<>();

        for (Event event : notPayedEvents) {

            for (AccountingEventPaymentCode accountingEventPaymentCode : event.getNonProcessedPaymentCodes()) {
                String id = accountingEventPaymentCode.getExternalId();
                String description = accountingEventPaymentCode.getDescription();
                String startDate = formatDay.print(accountingEventPaymentCode.getStartDate()) + " 00:00";
                String endDate = formatDay.print(accountingEventPaymentCode.getEndDate()) + " 23:59";
                String entity = accountingEventPaymentCode.getEntityCode();
                String reference = accountingEventPaymentCode.getFormattedCode();
                String amount = accountingEventPaymentCode.getMinAmount().getAmountAsString();
                notPayed.add(new PendingEvent(id, description, new FenixPeriod(startDate, endDate), entity, reference, amount));
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
    @OAuthEndpoint(EVALUATIONS_SCOPE)
    @GET
    @Path("person/evaluations")
    @Produces(JSON_UTF8)
    public List<FenixCourseEvaluation.WrittenEvaluation> evaluations(@Context HttpServletResponse response,
            @Context HttpServletRequest request, @Context ServletContext context) {

        Person person = getPerson();
        final Student student = person.getStudent();
        if (!RoleType.STUDENT.isMember(person.getUser()) || student == null) {
            return new ArrayList<FenixCourseEvaluation.WrittenEvaluation>();
        }

        new JerseyFacesContext(context, request, response);

        DisplayEvaluationsForStudentToEnrol manageEvaluationsForStudents = new DisplayEvaluationsForStudentToEnrol();

        List<FenixCourseEvaluation.WrittenEvaluation> evaluations = new ArrayList<>();

        evaluations.addAll(processEvaluation(manageEvaluationsForStudents.getEnroledEvaluations(), true, student));
        evaluations.addAll(processEvaluation(manageEvaluationsForStudents.getAllNotEnroledEvaluations(), false, student));

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
    @OAuthEndpoint(EVALUATIONS_SCOPE)
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
            throw newApplicationError(Status.PRECONDITION_FAILED, e.getMessage(), e.getMessage());
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

    private final <T extends DomainObject> T getDomainObject(final String externalId, final Class<T> clazz) {
        try {
            T domainObject = FenixFramework.getDomainObject(externalId);
            if (!FenixFramework.isDomainObjectValid(domainObject) || !clazz.isAssignableFrom(domainObject.getClass())) {
                throw newApplicationError(Status.NOT_FOUND, "id not found", "id not found");
            }
            return domainObject;
        } catch (Exception nfe) {
            throw newApplicationError(Status.NOT_FOUND, "id not found", "id not found");
        }
    }

    private WebApplicationException newApplicationError(Status status, String error, String description) {
        JsonObject errorObject = new JsonObject();
        errorObject.addProperty("error", error);
        errorObject.addProperty("description", description);
        return new WebApplicationException(Response.status(status).entity(errorObject.toString()).build());
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
    public String canteen(@QueryParam("day") String day) {
        validateDay(day);
        if (StringUtils.isBlank(day)) {
            day = dataFormatDay.format(new Date());
        }
        return FenixAPIFromExternalServer.getCanteen(day);
    }

    @GET
    @Produces(JSON_UTF8)
    @Path("contacts")
    public String contacts() {
        return FenixAPIFromExternalServer.getContacts();
    }

    @GET
    @Produces(JSON_UTF8)
    @Path("shuttle")
    public String shuttle() {
        return FenixAPIFromExternalServer.getShuttle();
    }

    @GET
    @Produces(JSON_UTF8)
    @Path("degrees/all")
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
        String name = degree.getNameI18N(executionYear).getContent(I18N.getLocale());

        String type = degree.getDegreeTypeName();
        String typeName = degree.getDegreeType().getName().getContent();
        String degreeUrl = degree.getSiteUrl();

        for (Space campus : degree.getCampus(executionYear)) {
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
            String istId = teacher.getPerson().getUsername();
            List<String> mails = getTeacherPublicMail(teacher);
            List<String> urls = getTeacherPublicWebAddress(teacher);
            teachers.add(new FenixTeacher(teacherName, istId, mails, urls));
        }

        FenixDegreeExtended fenixDegree =
                new FenixDegreeExtended(executionYear.getQualifiedName(), name, degree, type, typeName, degreeUrl, degreeCampus,
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
        return CoreConfiguration.getConfiguration().applicationUrl();
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
    public FenixCourseExtended coursesByOid(@PathParam("id") String oid) {
        ExecutionCourse executionCourse = getDomainObject(oid, ExecutionCourse.class);

        String acronym = executionCourse.getSigla();
        String name = executionCourse.getName();
        String evaluationMethod = executionCourse.getEvaluationMethodText();
        String academicTerm = executionCourse.getExecutionPeriod().getQualifiedName();
        String courseUrl = executionCourse.getSiteUrl();

        if (courseUrl == null) {
            courseUrl = "";
        }

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
            String istid = professorship.getPerson().getUsername();
            List<String> mail = getTeacherPublicMail(professorship.getTeacher());
            List<String> url = getTeacherPublicWebAddress(professorship.getTeacher());

            teachers.add(new FenixTeacher(tname, istid, mail, url));
        }

        String summaryLink = courseUrl.concat("/rss/summary");
        String announcementLink = courseUrl.concat("/rss/announcement");

        return new FenixCourseExtended(acronym, name, evaluationMethod, academicTerm, numberOfStudents, summaryLink,
                announcementLink, courseUrl, moreInfo, teachers);
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
    public List<FenixCourseEvaluation> evaluationCoursesByOid(@PathParam("id") String oid) {

        ExecutionCourse executionCourse = getDomainObject(oid, ExecutionCourse.class);

        final List<FenixCourseEvaluation> evals = new ArrayList<>();

        for (Evaluation evaluation : executionCourse.getAssociatedEvaluationsSet()) {
            if (evaluation instanceof WrittenEvaluation) {
                evals.add(getWrittenEvaluationJSON((WrittenEvaluation) evaluation));
            } else if (evaluation instanceof Project) {
                evals.add(getProjectEvaluationJSON((Project) evaluation));
            } else if (evaluation instanceof AdHocEvaluation) {
                evals.add(getAdhocEvaluationJSON((AdHocEvaluation) evaluation));
            } else {
                evals.add(getGenericTestJSON(evaluation));
            }
        }
        return evals;
    }

    private FenixCourseEvaluation.AdHocEvaluation getAdhocEvaluationJSON(AdHocEvaluation adHocEvaluation) {

        return new FenixCourseEvaluation.AdHocEvaluation(adHocEvaluation.getPresentationName(), adHocEvaluation.getDescription());
    }

    private FenixCourseEvaluation.GenericTest getGenericTestJSON(Evaluation evaluation) {
        String type = CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, evaluation.getClass().getSimpleName());
        return new FenixCourseEvaluation.GenericTest(evaluation.getPresentationName(), type);
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
            Space assignedRoom = null;
            for (ExecutionCourse executionCourse : writtenEvaluation.getAssociatedExecutionCoursesSet()) {
                final Registration registration = executionCourse.getRegistration(student.getPerson());
                final Attends attendsByStudent = executionCourse.getAttendsByStudent(student);
                if (registration != null || attendsByStudent != null) {
                    courses.add(executionCourse);
                }
            }

            final WrittenEvaluationEnrolment evalEnrolment = writtenEvaluation.getWrittenEvaluationEnrolmentFor(student);
            if (evalEnrolment != null) {
                assignedRoom = evalEnrolment.getRoom();
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
    public List<FenixSpace> spaces() {

        List<FenixSpace> campi = new ArrayList<>();

        for (Space campus : Space.getAllCampus()) {
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
    public FenixSpace spacesByOid(@PathParam("id") String oid, @QueryParam("day") String day) {

        Space space = getDomainObject(oid, Space.class);
        if (SpaceUtils.isRoom(space)) {
            return getFenixRoom(space, getRoomDay(day));
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
    public Response spaceBlueprint(@PathParam("id") String oid, final @QueryParam("format") String format) {

        final boolean isDwgFormat = format != null && format.equals("dwg");
        final Space space = getDomainObject(oid, Space.class);

        if (space == null) {
            return Response.noContent().build();
        }

        StreamingOutput stream;

        if (isDwgFormat) {
            Optional<BlueprintFile> optional = space.getBlueprintFile();
            if (!optional.isPresent()) {
                optional = SpaceBlueprintsDWGProcessor.getSuroundingSpaceMostRecentBlueprint(space).getBlueprintFile();
            }
            final InputStream inputStream = optional.get().getStream();
            stream = new StreamingOutput() {
                @Override
                public void write(OutputStream output) throws IOException, WebApplicationException {
                    ByteStreams.copy(inputStream, output);
                }
            };
        } else {
            stream = new StreamingOutput() {
                @Override
                public void write(OutputStream os) throws IOException, WebApplicationException {
                    Boolean isToViewOriginalSpaceBlueprint = false;
                    Boolean viewBlueprintNumbers = true;
                    Boolean isToViewIdentifications = true;
                    Boolean isToViewDoorNumbers = false;
                    BigDecimal scalePercentage = new BigDecimal(100);
                    DateTime now = new DateTime();
                    try {
                        SpaceBlueprintsDWGProcessor.writeBlueprint(space, now, isToViewOriginalSpaceBlueprint,
                                viewBlueprintNumbers, isToViewIdentifications, isToViewDoorNumbers, scalePercentage, os);
                    } catch (UnavailableException e) {
                        throw newApplicationError(Status.BAD_REQUEST, "problem found", "problem found");
                    }
                    os.flush();
                }
            };
        }
        final String contentType = isDwgFormat ? "application/dwg" : "image/jpeg";
        final String filename = space.getExternalId() + (isDwgFormat ? ".dwg" : ".jpg");
        return Response.ok(stream, contentType).header("Content-Disposition", "attachment; filename=" + filename).build();
    }

    private FenixSpace.Room getFenixRoom(Space room, java.util.Calendar rightNow) {

        InfoSiteRoomTimeTable bodyComponent = RoomSiteComponentBuilder.getInfoSiteRoomTimeTable(rightNow, room, null);
        List<FenixRoomEvent> roomEvents = new ArrayList<FenixRoomEvent>();

        try {
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

                } else if (showOccupation instanceof InfoOccupation) {

                    InfoOccupation infoGenericEvent = (InfoOccupation) showOccupation;
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
    public String domainModel() {
        return new FenixDomainModel().toJSONString();
    }

}
