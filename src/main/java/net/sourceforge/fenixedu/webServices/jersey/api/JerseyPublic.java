package net.sourceforge.fenixedu.webServices.jersey.api;

import pt.ist.fenixframework.DomainObject;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.applicationTier.Factory.RoomSiteComponentBuilder;
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
import net.sourceforge.fenixedu.domain.AdHocEvaluation;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeInfo;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.Mark;
import net.sourceforge.fenixedu.domain.OccupationPeriod;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Project;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.contacts.EmailAddress;
import net.sourceforge.fenixedu.domain.contacts.WebAddress;
import net.sourceforge.fenixedu.domain.degreeStructure.BibliographicReferences.BibliographicReference;
import net.sourceforge.fenixedu.domain.onlineTests.OnlineTest;
import net.sourceforge.fenixedu.domain.resource.Resource;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.domain.space.Building;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.domain.space.Floor;
import net.sourceforge.fenixedu.domain.space.Room;
import net.sourceforge.fenixedu.domain.space.Space;
import net.sourceforge.fenixedu.presentationTier.Action.externalServices.OAuthUtils;
import net.sourceforge.fenixedu.util.EvaluationType;
import net.sourceforge.fenixedu.webServices.jersey.beans.publico.FenixAbout;
import net.sourceforge.fenixedu.webServices.jersey.beans.publico.FenixCourse;
import net.sourceforge.fenixedu.webServices.jersey.beans.publico.FenixCourse.FenixCompetence;
import net.sourceforge.fenixedu.webServices.jersey.beans.publico.FenixCourse.FenixCompetence.BiblioRef;
import net.sourceforge.fenixedu.webServices.jersey.beans.publico.FenixCourseEvaluation;
import net.sourceforge.fenixedu.webServices.jersey.beans.publico.FenixCourseGroup;
import net.sourceforge.fenixedu.webServices.jersey.beans.publico.FenixCourseGroup.Grouping.Group;
import net.sourceforge.fenixedu.webServices.jersey.beans.publico.FenixCourseGroup.Grouping.Group.Student;
import net.sourceforge.fenixedu.webServices.jersey.beans.publico.FenixCourseStudents;
import net.sourceforge.fenixedu.webServices.jersey.beans.publico.FenixDegree;
import net.sourceforge.fenixedu.webServices.jersey.beans.publico.FenixDegree.FenixDegreeInfo;
import net.sourceforge.fenixedu.webServices.jersey.beans.publico.FenixDegree.FenixTeacher;
import net.sourceforge.fenixedu.webServices.jersey.beans.publico.FenixExecutionCourse;
import net.sourceforge.fenixedu.webServices.jersey.beans.publico.FenixSchedule;
import net.sourceforge.fenixedu.webServices.jersey.beans.publico.FenixSpace;
import net.sourceforge.fenixedu.webServices.jersey.beans.publico.FenixSpace.Room.RoomEvent.WrittenEvaluationEvent;

import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONObject;

@Path("/public/v1")
public class JerseyPublic {

    /**
     * IST generic information
     * 
     * @summary IST information
     * @return news and events rss
     */
    @GET
    @Produces(JerseyPrivate.JSON_UTF8)
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
     * @param year ("yyyy/yyyy")
     */
    @GET
    @Produces(JerseyPrivate.JSON_UTF8)
    @Path("degrees")
    @FenixAPIPublic
    public List<FenixDegree> degrees(@QueryParam("year") String year) {

        ExecutionYear executionYear = getExecutionYear(year);

        List<FenixDegree> fenixDegrees = new ArrayList<>();

        for (Degree degree : Degree.readBolonhaDegrees()) {
            if (degree.isBolonhaMasterOrDegree()) {
                fenixDegrees.add(getFenixDegree(executionYear, degree));
            }
        }
        return fenixDegrees;
    }

    private FenixDegree getFenixDegree(ExecutionYear executionYear, Degree degree) {
        List<String> degreeCampus = new ArrayList<>();

        String id = degree.getExternalId();
        String name = degree.getPresentationName();
        String type = degree.getDegreeTypeName();
        String sigla = degree.getSigla();
        String typeName = degree.getDegreeType().getFilteredName();

        for (Campus campus : degree.getCampus(executionYear)) {
            degreeCampus.add(campus.getName());
        }

        DegreeInfo degreeInfo = degree.getDegreeInfoFor(executionYear);
        FenixDegreeInfo fenixDegreeInfo = null;
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
        Collection<Teacher> responsibleCoordinatorsTeachers = degree.getResponsibleCoordinatorsTeachers(executionYear);

        if (responsibleCoordinatorsTeachers.isEmpty()) {
            responsibleCoordinatorsTeachers = degree.getCurrentResponsibleCoordinatorsTeachers();
        }

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
     * @param oid degree id
     * @param year ("yyyy/yyyy")
     */
    @GET
    @Produces(JerseyPrivate.JSON_UTF8)
    @Path("degrees/{id}")
    public FenixDegree degreesByOid(@PathParam("id") String oid, @PathParam("year") String year) {
        Degree degree = getDomainObject(oid, Degree.class);
        ExecutionYear executionYear = getExecutionYear(year);

        if (degree.isBolonhaMasterOrDegree()) {
            return getFenixDegree(executionYear, degree);
        }

        return new FenixDegree();
    }

    /**
     * Courses for degree with id
     * 
     * @summary Courses for specific degree
     * @param oid degree id
     * @param year ("yyyy/yyyy")
     */
    @GET
    @Produces(JerseyPrivate.JSON_UTF8)
    @Path("degrees/{id}/courses")
    public List<FenixExecutionCourse> coursesByODegreesId(@PathParam("id") String oid, @QueryParam("year") String year) {

        Degree degree = getDomainObject(oid, Degree.class);

        ExecutionYear executionYear = getExecutionYear(year);

        ExecutionSemester[] executionSemesters = executionYear.getExecutionPeriods().toArray(new ExecutionSemester[0]);

        final Set<ExecutionCourseView> executionCourses = new HashSet<ExecutionCourseView>();
        for (final DegreeCurricularPlan degreeCurricularPlan : degree.getDegreeCurricularPlans()) {
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
        for (CurricularCourse curricularCourse : ec.getAssociatedCurricularCourses()) {
            if (degree.equals(curricularCourse.getDegree())) {
                return curricularCourse.getEctsCredits().toString();
            }
        }
        return "N/A";
    }

    private String getServerLink() {
        String serverLink;
        final String appName = PropertiesManager.getProperty("http.host");
        final String appContext = PropertiesManager.getProperty("app.context");
        final String httpPort = PropertiesManager.getProperty("http.port");
        final String httpProtocol = PropertiesManager.getProperty("http.protocol");

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
     * @param oid course id
     * @return
     */
    @GET
    @Produces(JerseyPrivate.JSON_UTF8)
    @Path("courses/{id}/")
    public FenixCourse coursesByOid(@PathParam("id") String oid) {
        ExecutionCourse executionCourse = getDomainObject(oid, ExecutionCourse.class);

        String acronym = executionCourse.getSigla();
        String name = executionCourse.getName();
        String evaluation = executionCourse.getEvaluationMethodText();
        String year = executionCourse.getExecutionYear().getName();
        Integer semester = executionCourse.getExecutionPeriod().getSemester();

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

        int numberOfStudents = executionCourse.getAttendsCount();

        List<FenixTeacher> teachers = new ArrayList<>();
        for (Professorship professorship : executionCourse.getProfessorships()) {

            String tname = professorship.getPerson().getName();
            String istid = professorship.getPerson().getIstUsername();
            List<String> mail = getTeacherPublicMail(professorship.getTeacher());
            List<String> url = getTeacherPublicWebAddress(professorship.getTeacher());

            teachers.add(new FenixTeacher(tname, istid, mail, url));
        }

        //TODO change getIdInternal to ExternalID
        String annoucementLink =
                getServerLink().concat("/external/announcementsRSS.do?announcementBoardId=").concat(
                        executionCourse.getBoard().getIdInternal().toString());

        String summaryLink =
                getServerLink().concat("/publico/summariesRSS.do?id=").concat(executionCourse.getIdInternal().toString());

        return new FenixCourse(acronym, name, evaluation, year, semester, numberOfStudents, annoucementLink, summaryLink,
                moreInfo, teachers);
    }

    /**
     * Retrieve groups for course given by oid
     * 
     * @summary Course groups by course oid
     * @param oid course id
     */
    @GET
    @Produces(JerseyPrivate.JSON_UTF8)
    @Path("courses/{id}/groups")
    public FenixCourseGroup groupsCoursesByOid(@PathParam("id") String oid) {

        ExecutionCourse executionCourse = getDomainObject(oid, ExecutionCourse.class);

        String name = executionCourse.getName();
        String year = executionCourse.getExecutionYear().getName();
        Integer semester = executionCourse.getExecutionPeriod().getSemester();

        List<FenixCourseGroup.Grouping> groupings = new ArrayList<>();

        for (Grouping grouping : executionCourse.getGroupings()) {

            String groupingName = grouping.getName();
            String groupingDescription = grouping.getProjectDescription();

            List<Group> groups = new ArrayList<>();

            for (StudentGroup studentGroup : grouping.getStudentGroupsOrderedByGroupNumber()) {
                Integer groupNumber = studentGroup.getGroupNumber();

                List<Student> students = new ArrayList<>();
                for (Attends attends : studentGroup.getAttends()) {
                    String istId = attends.getRegistration().getPerson().getUsername();
                    String studentName = attends.getRegistration().getPerson().getName();
                    students.add(new Student(istId, studentName));
                }

                groups.add(new Group(groupNumber, students));
            }

            groupings.add(new FenixCourseGroup.Grouping(groupingName, groupingDescription, groups));

        }

        return new FenixCourseGroup(name, year, semester, groupings);

    }

    /**
     * All students for course by id
     * 
     * @summary Course students
     * @param oid course id
     * @return
     */
    @GET
    @Produces(JerseyPrivate.JSON_UTF8)
    @Path("courses/{id}/students")
    public FenixCourseStudents studentsCoursesByOid(@PathParam("id") String oid) {

        ExecutionCourse executionCourse = getDomainObject(oid, ExecutionCourse.class);

        Integer enrolmentNumber = executionCourse.getTotalEnrolmentStudentNumber();
        String name = executionCourse.getName();
        Integer semester = executionCourse.getExecutionPeriod().getSemester();
        String year = executionCourse.getExecutionPeriod().getName();

        List<FenixCourseStudents.FenixCourseStudent> students = new ArrayList<>();
        for (final Attends attends : executionCourse.getAttendsSet()) {
            Integer number = attends.getRegistration().getNumber();
            String sName = attends.getRegistration().getName();
            String sDegree = attends.getRegistration().getDegreeCurricularPlanName();
            String sDegreeId = attends.getRegistration().getDegree().getExternalId();

            List<FenixCourseStudents.FenixCourseStudent.Evaluation> evaluations = new ArrayList<>();
            for (final Mark mark : attends.getAssociatedMarksSet()) {
                if (mark.getEvaluation().getPublishmentMessage() != null) {
                    String evalName = mark.getEvaluation().getPresentationName();
                    String evalMark = mark.getPublishedMark();

                    evaluations.add(new FenixCourseStudents.FenixCourseStudent.Evaluation(evalName, evalMark));
                }
            }

            students.add(new FenixCourseStudents.FenixCourseStudent(number, sName, sDegree, sDegreeId, evaluations));

        }
        return new FenixCourseStudents(enrolmentNumber, name, semester, year, students);
    }

    /**
     * 
     * Returns evaluations for course by id
     * (Test, Exams, Project, AdHoc, OnlineTest)
     * 
     * @summary Course evaluations
     * @param oid
     * @return
     */
    @GET
    @Produces(JerseyPrivate.JSON_UTF8)
    @Path("courses/{id}/evaluations")
    public List<FenixCourseEvaluation> evaluationCoursesByOid(@PathParam("id") String oid) {

        ExecutionCourse executionCourse = getDomainObject(oid, ExecutionCourse.class);

        final List<FenixCourseEvaluation> evals = new ArrayList<>();

        for (Evaluation evaluation : executionCourse.getAssociatedEvaluations()) {
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
            beginningDay = JerseyPrivate.formatDay.print(projectEvaluation.getProjectBeginDateTime());
            beginningTime = JerseyPrivate.formatHour.print(projectEvaluation.getProjectBeginDateTime());
        }
        if (projectEvaluation.getProjectEndDateTime() != null) {
            endDay = JerseyPrivate.formatDay.print(projectEvaluation.getProjectEndDateTime());
            endTime = JerseyPrivate.formatHour.print(projectEvaluation.getProjectEndDateTime());

        }
        return new FenixCourseEvaluation.Project(name, beginningDay, beginningTime, endDay, endTime);
    }

    private FenixCourseEvaluation.WrittenEvaluation getWrittenEvaluationJSON(WrittenEvaluation writtenEvaluation) {

        String name = writtenEvaluation.getPresentationName();
        EvaluationType type = writtenEvaluation.getEvaluationType();

        String day = JerseyPrivate.dataFormatDay.format(writtenEvaluation.getDay().getTime());

        String beginningTime = JerseyPrivate.dataFormatHour.format(writtenEvaluation.getBeginning().getTime());
        String endTime = JerseyPrivate.dataFormatHour.format(writtenEvaluation.getEnd().getTime());

        List<FenixCourseEvaluation.WrittenEvaluation.Room> rooms = new ArrayList<>();

        for (AllocatableSpace allocationSpace : writtenEvaluation.getAssociatedRooms()) {

            String roomId = allocationSpace.getExternalId();
            String roomName = allocationSpace.getSpaceInformation().getPresentationName();
            String roomDescription = allocationSpace.getCompleteIdentification();
            rooms.add(new FenixCourseEvaluation.WrittenEvaluation.Room(roomId, roomName, roomDescription));
        }

        boolean isEnrolmentPeriod = writtenEvaluation.getIsInEnrolmentPeriod();

        String enrollmentBeginDay = null;
        String enrollmentBeginTime = null;
        String enrollmentEndDay = null;
        String enrollmentEndTime = null;

        if (writtenEvaluation.getEnrollmentBeginDay() != null) {
            enrollmentBeginDay = JerseyPrivate.dataFormatHour.format(writtenEvaluation.getEnrollmentBeginDay().getTime());
        }
        if (writtenEvaluation.getEnrollmentBeginTime() != null) {
            enrollmentBeginTime = JerseyPrivate.dataFormatHour.format(writtenEvaluation.getEnrollmentBeginTime().getTime());
        }
        if (writtenEvaluation.getEnrollmentEndDay() != null) {
            enrollmentEndDay = JerseyPrivate.dataFormatDay.format(writtenEvaluation.getEnrollmentEndDay().getTime());
        }
        if (writtenEvaluation.getEnrollmentEndTime() != null) {
            enrollmentEndTime = JerseyPrivate.dataFormatHour.format(writtenEvaluation.getEnrollmentEndTime().getTime());
        }

        if (type.equals(EvaluationType.EXAM_TYPE)) {
            return new FenixCourseEvaluation.Exam(name, day, beginningTime, endTime, isEnrolmentPeriod, enrollmentBeginDay,
                    enrollmentBeginTime, enrollmentEndDay, enrollmentEndTime, rooms);
        } else {
            return new FenixCourseEvaluation.Test(name, day, beginningTime, endTime, isEnrolmentPeriod, enrollmentBeginDay,
                    enrollmentBeginTime, enrollmentEndDay, enrollmentEndTime, rooms);
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
    @Produces(JerseyPrivate.JSON_UTF8)
    @Path("courses/{id}/schedule")
    public FenixSchedule scheduleCoursesByOid(@PathParam("id") String oid) {

        ExecutionCourse executionCourse = getDomainObject(oid, ExecutionCourse.class);

        String name = executionCourse.getName();
        String year = executionCourse.getExecutionYear().getName();
        Integer semester = executionCourse.getExecutionPeriod().getSemester();

        List<FenixSchedule.Period> periods = new ArrayList<>();

        for (OccupationPeriod occupationPeriod : executionCourse.getLessonPeriods()) {
            String start = null;
            String end = null;
            if (occupationPeriod.getStartDate() != null) {
                start = JerseyPrivate.dataFormatDay.format(occupationPeriod.getStartDate().getTime());
            }
            if (occupationPeriod.getEndDate() != null) {
                end = JerseyPrivate.dataFormatDay.format(occupationPeriod.getEndDate().getTime());
            }

            periods.add(new FenixSchedule.Period(start, end));
        }

        List<FenixSchedule.Lesson> lessons = new ArrayList<>();

        for (Lesson lesson : executionCourse.getLessons()) {
            String weekDay = lesson.getWeekDay().getName();
            String lessonType = lesson.getShift().getShiftTypesCodePrettyPrint();
            String start = JerseyPrivate.dataFormatHour.format(lesson.getInicio().getTime());
            String end = JerseyPrivate.dataFormatHour.format(lesson.getFim().getTime());

            FenixSchedule.Lesson.Room room = null;

            if (lesson.hasSala()) {
                AllocatableSpace sala = lesson.getSala();
                String roomId = sala.getExternalId();
                String roomName = sala.getSpaceInformation().getPresentationName();
                String roomDescription = sala.getCompleteIdentification();
                room = new FenixSchedule.Lesson.Room(roomId, roomName, roomDescription);

            }
            lessons.add(new FenixSchedule.Lesson(weekDay, lessonType, start, end, room));
        }
        return new FenixSchedule(name, year, semester, periods, lessons);
    }

    /**
     * Campus spaces
     * 
     * @summary All campus
     * @return
     */
    @GET
    @Produces(JerseyPrivate.JSON_UTF8)
    @Path("spaces")
    public List<FenixSpace> spaces() {

        List<FenixSpace> campi = new ArrayList<>();

        for (Campus campus : Space.getAllCampus()) {
            campi.add(getSimpleCampus(campus));
        }
        return campi;
    }

    /**
     * Space information regarding space type (Campus, Building, Floor or Room)
     * 
     * @param oid
     * @param day ("dd/mm/yyyy")
     * @return
     */
    @GET
    @Produces(JerseyPrivate.JSON_UTF8)
    @Path("spaces/{id}")
    public FenixSpace spacesByOid(@PathParam("id") String oid, @QueryParam("day") String day) {

        Resource resource = getDomainObject(oid, Resource.class);

        if (resource.isCampus()) {
            return getFenixCampus((Campus) resource);
        } else if (resource.isBuilding()) {
            return getFenixBuilding((Building) resource);
        } else if (resource.isFloor()) {
            return getFenixFloor((Floor) resource);
        } else if (resource.isRoom()) {
            return getFenixRoom((Room) resource, getRoomDay(day));
        }

        return null;
    }

    private FenixSpace.Campus getFenixCampus(Campus campus) {

        List<FenixSpace> fenixBuildings = new ArrayList<>();

        for (Space building : campus.getActiveContainedSpacesByType(Building.class)) {
            fenixBuildings.add(getSimpleBuilding((Building) building));
        }

        return new FenixSpace.Campus(campus.getExternalId(), campus.getName(), fenixBuildings);

    }

    public FenixSpace.Building getSimpleBuilding(Building building) {
        return new FenixSpace.Building(building.getExternalId(), building.getNameWithCampus());
    }

    private FenixSpace.Building getFenixBuilding(Building building) {

        List<FenixSpace> fenixFloors = new ArrayList<>();
        for (Space space : building.getActiveContainedSpacesByType(Floor.class)) {
            fenixFloors.add(getSimpleFloor((Floor) space));
        }

        Campus campus = building.getSpaceCampus();
        FenixSpace.Campus fenixCampus = getSimpleCampus(campus);
        return new FenixSpace.Building(building.getExternalId(), building.getNameWithCampus(), fenixCampus, fenixFloors);
    }

    public FenixSpace.Floor getSimpleFloor(Floor floor) {
        return new FenixSpace.Floor(floor.getExternalId(), floor.getSpaceInformation().getPresentationName());
    }

    public FenixSpace.Campus getSimpleCampus(Campus campus) {
        return new FenixSpace.Campus(campus.getExternalId(), campus.getName());
    }

    private FenixSpace.Floor getFenixFloor(Floor floor) {
        List<FenixSpace.Room> fenixRooms = new ArrayList<>();

        for (Space space : floor.getActiveContainedSpacesByType(Room.class)) {
            fenixRooms.add(getSimpleRoom((Room) space));
        }

        Building building = floor.getSpaceBuilding();
        FenixSpace.Building fenixBuilding = getSimpleBuilding(building);
        FenixSpace.Floor fenixFloor =
                new FenixSpace.Floor(floor.getExternalId(), floor.getSpaceInformation().getPresentationName(), fenixBuilding,
                        fenixRooms);
        return fenixFloor;
    }

    public FenixSpace.Room getSimpleRoom(Room room) {
        return new FenixSpace.Room(room.getExternalId(), room.getSpaceInformation().getPresentationName());
    }

    private FenixSpace.Room getFenixRoom(Room room, Calendar rightNow) {

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

                    String start = JerseyPrivate.dataFormatHour.format(lesson.getInicio().getTime());
                    String end = JerseyPrivate.dataFormatHour.format(lesson.getFim().getTime());
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
                        start = JerseyPrivate.dataFormatHour.format(infoWrittenTest.getInicio().getTime());
                        end = JerseyPrivate.dataFormatHour.format(infoWrittenTest.getFim().getTime());
                        weekday = infoWrittenTest.getDiaSemana().getDiaSemanaString();

                        roomEvent =
                                new FenixSpace.Room.RoomEvent.WrittenEvaluationEvent.TestEvent(start, end, weekday, courses,
                                        description);
                    }

                } else if (showOccupation instanceof InfoGenericEvent) {

                    InfoGenericEvent infoGenericEvent = (InfoGenericEvent) showOccupation;
                    String description = infoGenericEvent.getDescription();
                    String title = infoGenericEvent.getTitle();
                    String start = JerseyPrivate.dataFormatHour.format(infoGenericEvent.getInicio().getTime());
                    String end = JerseyPrivate.dataFormatHour.format(infoGenericEvent.getFim().getTime());
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

            return new FenixSpace.Room(id, name, getSimpleFloor(room.getSpaceFloor()), description, normalCapacity, examCapacity,
                    roomEvents);

        } catch (Exception e) {
            e.printStackTrace();
            throw newApplicationError(Status.INTERNAL_SERVER_ERROR, "berserk!", "something went wrong");
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends DomainObject> T getDomainObject(String externalId, Class<T> clazz) {
        try {
            T domainObject = OAuthUtils.getDomainObject(externalId, clazz);
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

    private Calendar getRoomDay(String day) {
        Calendar rightNow = Calendar.getInstance();
        Date date = null;
        try {
            if (!StringUtils.isBlank(day)) {
                date = JerseyPrivate.dataFormatDay.parse(day);
                rightNow.setTime(date);
            }
        } catch (ParseException e1) {
        }
        return rightNow;
    }

    private ExecutionYear getExecutionYear(String year) {
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

    private String mls(MultiLanguageString mls) {
        if (mls == null) {
            return StringUtils.EMPTY;
        }
        return mls.getContent();
    }
}
