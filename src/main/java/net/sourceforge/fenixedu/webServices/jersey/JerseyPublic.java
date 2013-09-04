package net.sourceforge.fenixedu.webServices.jersey;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
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

import org.apache.commons.lang.StringUtils;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.pstm.AbstractDomainObject;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

@Path("/public/v1")
public class JerseyPublic {

    private final static String MediaTypeJsonUtf8 = "application/json; charset=utf-8";

    private static final SimpleDateFormat dataFormatDay = new SimpleDateFormat("dd/MM/yyyy");
    private static final SimpleDateFormat dataFormatHour = new SimpleDateFormat("HH:mm");

    private static final DateTimeFormatter formatDay = DateTimeFormat.forPattern("dd/MM/yyyy");
    private static final DateTimeFormatter formatHour = DateTimeFormat.forPattern("HH:mm");

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("hello")
    public String hellofenix() {
        return "Hello! Public V1";
    }

    @GET
    @Produces(MediaTypeJsonUtf8)
    @Path("about")
    public static String about() {
        JSONObject jsonAbout = new JSONObject();
        jsonAbout.put("newsRss", PropertiesManager.getProperty("fenix.api.news.rss.url"));
        jsonAbout.put("eventsRss", PropertiesManager.getProperty("fenix.api.events.rss.url"));
        return jsonAbout.toJSONString();
    }

    @GET
    @Produces(MediaTypeJsonUtf8)
    @Path("degrees")
    public static String degrees(@QueryParam("year") String year) {
        JSONArray infos = new JSONArray();

        ExecutionYear executionYear = getExecutionYear(year);
        for (Degree degree : Degree.readBolonhaDegrees()) {
            if (degree.isBolonhaMasterOrDegree()) {

                JSONArray teachersArray = new JSONArray();
                JSONObject degreeSecondaryInfo = new JSONObject();
                JSONObject degreeCampus = new JSONObject();

                JSONObject degreeMainInfo = new JSONObject();

                degreeMainInfo.put("degreeId", degree.getExternalId());
                degreeMainInfo.put("degreeName", degree.getPresentationName());
                degreeMainInfo.put("degreeType", degree.getDegreeTypeName());
                degreeMainInfo.put("degreeSigla", degree.getSigla());
                degreeMainInfo.put("degreeTypeName", degree.getDegreeType().getFilteredName());

                for (Campus campus : degree.getCampus(executionYear)) {
                    degreeCampus.put("degreeCampusName", campus.getName());
                }
                degreeMainInfo.put("degreeCampus", degreeCampus);

                DegreeInfo degreeInfo = degree.getDegreeInfoFor(executionYear);

                if (degreeInfo != null) {

                    degreeSecondaryInfo.put("degreeInfoName", mls(degreeInfo.getName()));
                    degreeSecondaryInfo.put("degreeInfoDescription", mls(degreeInfo.getDescription()));
                    degreeSecondaryInfo.put("degreeInfoObjectives", mls(degreeInfo.getObjectives()));
                    degreeSecondaryInfo.put("degreeInfoDesignFor", mls(degreeInfo.getDesignedFor()));
                    degreeSecondaryInfo.put("degreeInfoRequisites",
                            mls(degreeInfo.getDegreeInfoCandidacy().getAccessRequisites()));
                    degreeSecondaryInfo.put("degreeInfoProfissionalExits", mls(degreeInfo.getProfessionalExits()));
                    degreeSecondaryInfo.put("degreeInfoHistory", mls(degreeInfo.getHistory()));
                    degreeSecondaryInfo.put("degreeInfoOperationRegime", mls(degreeInfo.getOperationalRegime()));
                    degreeSecondaryInfo.put("degreeInfoGratuity", mls(degreeInfo.getGratuity()));
                    degreeSecondaryInfo.put("degreeInfoLinks", mls(degreeInfo.getLinks()));
                }
                degreeMainInfo.put("degreeInfo", degreeSecondaryInfo);

                Collection<Teacher> responsibleCoordinatorsTeachers = degree.getResponsibleCoordinatorsTeachers(executionYear);

                if (responsibleCoordinatorsTeachers.isEmpty()) {
                    responsibleCoordinatorsTeachers = degree.getCurrentResponsibleCoordinatorsTeachers();
                }

                for (Teacher teacher : responsibleCoordinatorsTeachers) {
                    JSONObject teacherInfo = new JSONObject();
                    teacherInfo.put("teacherName", teacher.getPerson().getName());
                    teacherInfo.put("teacherIstId", teacher.getPerson().getIstUsername());
                    teacherInfo.put("teacherMail", getTeacherPublicMail(teacher));
                    teacherInfo.put("teacherUrl", getTeacherPublicWebAddress(teacher));
                    teachersArray.add(teacherInfo);
                }
                degreeMainInfo.put("teachers", teachersArray);
                infos.add(degreeMainInfo);
            }
        }
        return infos.toJSONString();
    }

    private static String mls(MultiLanguageString mls) {
        if (mls == null) {
            return StringUtils.EMPTY;
        }
        return mls.getContent();
    }

    @GET
    @Produces(MediaTypeJsonUtf8)
    @Path("degrees/{oid}")
    public static String degreesByOid(@PathParam("oid") String oid, @QueryParam("year") String year) {
        JSONObject jsonResult = new JSONObject();
        JSONArray teachersArray = new JSONArray();
        Degree degree = getDomainObject(oid);
        ExecutionYear executionYear = getExecutionYear(year);

        if (degree.isBolonhaMasterOrDegree()) {
            JSONObject degreeMainInfo = new JSONObject();

            degreeMainInfo.put("degreeId", degree.getExternalId());
            degreeMainInfo.put("degreeName", degree.getPresentationName());
            degreeMainInfo.put("degreeType", degree.getDegreeTypeName());
            degreeMainInfo.put("degreeSigla", degree.getSigla());

            DegreeInfo degreeInfo = degree.getDegreeInfoFor(executionYear);

            if (degreeInfo != null) {
                JSONObject degreeSecondaryInfo = new JSONObject();

                degreeSecondaryInfo.put("degreeInfoName", mls(degreeInfo.getName()));
                degreeSecondaryInfo.put("degreeInfoDescription", mls(degreeInfo.getDescription()));
                degreeSecondaryInfo.put("degreeInfoObjectives", mls(degreeInfo.getObjectives()));
                degreeSecondaryInfo.put("degreeInfoDesignFor", mls(degreeInfo.getDesignedFor()));
                degreeSecondaryInfo.put("degreeInfoRequisites", mls(degreeInfo.getDegreeInfoCandidacy().getAccessRequisites()));
                degreeSecondaryInfo.put("degreeInfoProfissionalExits", mls(degreeInfo.getProfessionalExits()));
                degreeSecondaryInfo.put("degreeInfoHistory", mls(degreeInfo.getHistory()));
                degreeSecondaryInfo.put("degreeInfoOperationRegime", mls(degreeInfo.getOperationalRegime()));
                degreeSecondaryInfo.put("degreeInfoGratuity", mls(degreeInfo.getGratuity()));
                degreeSecondaryInfo.put("degreeInfoLinks", mls(degreeInfo.getLinks()));
                degreeMainInfo.put("degreeInfoSecundary", degreeSecondaryInfo);
            }
            jsonResult.put("degreeInfo", degreeMainInfo);

            Collection<Teacher> responsibleCoordinatorsTeachers = degree.getResponsibleCoordinatorsTeachers(executionYear);
            if (responsibleCoordinatorsTeachers.isEmpty()) {
                responsibleCoordinatorsTeachers = degree.getCurrentResponsibleCoordinatorsTeachers();
            }

            for (Teacher teacher : responsibleCoordinatorsTeachers) {
                JSONObject teacherInfo = new JSONObject();
                teacherInfo.put("teacherName", teacher.getPerson().getName());
                teacherInfo.put("teacherIstId", teacher.getPerson().getIstUsername());
                teacherInfo.put("teacherMail", getTeacherPublicMail(teacher));
                teacherInfo.put("teacherUrl", getTeacherPublicWebAddress(teacher));
                teachersArray.add(teacherInfo);
            }
            jsonResult.put("teachers", teachersArray);
        }
        return jsonResult.toJSONString();
    }

    private static JSONArray getTeacherPublicMail(Teacher teacher) {
        JSONArray mailArray = new JSONArray();
        if (teacher != null) {
            for (EmailAddress emailAddress : teacher.getPerson().getEmailAddresses()) {
                if (emailAddress.getVisibleToPublic()) {
                    JSONObject mailObj = new JSONObject();
                    mailObj.put("mail", emailAddress.getPresentationValue());
                    mailArray.add(mailObj);
                }
            }
        }
        return mailArray;
    }

    private static JSONArray getTeacherPublicWebAddress(Teacher teacher) {
        JSONArray urlArray = new JSONArray();
        if (teacher != null) {
            for (WebAddress webAddress : teacher.getPerson().getWebAddresses()) {
                if (webAddress.getVisibleToPublic()) {
                    JSONObject urlObj = new JSONObject();
                    urlObj.put("url", webAddress.getUrl());
                    urlArray.add(urlObj);
                }
            }
        }
        return urlArray;
    }

    @GET
    @Produces(MediaTypeJsonUtf8)
    @Path("degrees/{oid}/courses")
    public static String coursesByODegreesId(@PathParam("oid") String oid, @QueryParam("year") String year) {
        JSONArray jsonResult = new JSONArray();
        Degree degree = getDomainObject(oid);

        ExecutionYear executionYear = getExecutionYear(year);

        ExecutionSemester[] executionSemesters = executionYear.getExecutionPeriods().toArray(new ExecutionSemester[0]);

        final Set<ExecutionCourseView> executionCourses = new HashSet<ExecutionCourseView>();
        for (final DegreeCurricularPlan degreeCurricularPlan : degree.getDegreeCurricularPlans()) {
            if (degreeCurricularPlan.isActive()) {
                degreeCurricularPlan.addExecutionCourses(executionCourses, executionSemesters);
            }
        }

        if (degree.isBolonhaMasterOrDegree()) {
            for (ExecutionCourseView executionCourseView : executionCourses) {
                ExecutionCourse ec = executionCourseView.getExecutionCourse();
                JSONObject degreeInfo = new JSONObject();
                degreeInfo.put("courseAcronym", ec.getSigla());
                degreeInfo.put("courseCredits", getCredits(ec, degree));
                degreeInfo.put("courseName", ec.getName());
                degreeInfo.put("courseId", ec.getExternalId());
                degreeInfo.put("year", ec.getExecutionYear().getName());
                degreeInfo.put("semester", ec.getExecutionPeriod().getName());
                jsonResult.add(degreeInfo);
            }
        }
        return jsonResult.toJSONString();
    }

    private static String getCredits(ExecutionCourse ec, Degree degree) {
        for (CurricularCourse curricularCourse : ec.getAssociatedCurricularCourses()) {
            if (degree.equals(curricularCourse.getDegree())) {
                return curricularCourse.getEctsCredits().toString();
            }
        }
        return "N/A";
    }

    private static String getServerLink() {
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

    @GET
    @Produces(MediaTypeJsonUtf8)
    @Path("courses/{oid}/")
    public static String coursesByOid(@PathParam("oid") String oid) {
        JSONObject jsonResult = new JSONObject();
        ExecutionCourse executionCourse = getDomainObject(oid);
        jsonResult.put("courseAcronym", executionCourse.getSigla());
        jsonResult.put("courseName", executionCourse.getName());
        jsonResult.put("courseEvaluation", executionCourse.getEvaluationMethodText());
        jsonResult.put("year", executionCourse.getExecutionYear().getName());
        jsonResult.put("semester", executionCourse.getExecutionPeriod().getName());

        Map<CompetenceCourse, Set<CurricularCourse>> curricularCourses =
                executionCourse.getCurricularCoursesIndexedByCompetenceCourse();

        JSONArray jsonMoreInfo = new JSONArray();
        for (Map.Entry<CompetenceCourse, Set<CurricularCourse>> entry : curricularCourses.entrySet()) {
            JSONObject jsonCompetence = new JSONObject();

            jsonCompetence.put("courseProgram", entry.getKey().getProgram());

            JSONArray jsonBiblio = new JSONArray();
            for (BibliographicReference bibliographicReference : entry.getKey().getBibliographicReferences()
                    .getBibliographicReferencesSortedByOrder()) {
                JSONObject jsonBiblioInfo = new JSONObject();
                jsonBiblioInfo.put("author", bibliographicReference.getAuthors());
                jsonBiblioInfo.put("reference", bibliographicReference.getReference());
                jsonBiblioInfo.put("title", bibliographicReference.getTitle());
                jsonBiblioInfo.put("year", bibliographicReference.getYear());
                jsonBiblioInfo.put("type", bibliographicReference.getType().getName());
                jsonBiblioInfo.put("url", bibliographicReference.getUrl());
                jsonBiblio.add(jsonBiblioInfo);
            }

            jsonCompetence.put("bibliographicReferences", jsonBiblio);

            JSONArray jsonDegreesArray = new JSONArray();
            for (CurricularCourse curricularCourse : entry.getValue()) {
                JSONObject jsonDegreesInfo = new JSONObject();
                jsonDegreesInfo.put("degreeId", curricularCourse.getDegree().getExternalId());
                jsonDegreesInfo.put("degreeName", curricularCourse.getDegree().getPresentationName());
                jsonDegreesInfo.put("degreeAcronym", curricularCourse.getAcronym());
                jsonDegreesArray.add(jsonDegreesInfo);
            }
            jsonCompetence.put("degrees", jsonDegreesArray);

            jsonMoreInfo.add(jsonCompetence);

        }

        jsonResult.put("courseMoreInfo", jsonMoreInfo);
        jsonResult.put("courseNumberOfStudents", executionCourse.getAttendsCount());

        JSONArray teachersArray = new JSONArray();
        for (Professorship professorship : executionCourse.getProfessorships()) {
            JSONObject teacherInfo = new JSONObject();
            teacherInfo.put("teacherName", professorship.getPerson().getName());
            teacherInfo.put("teacherIstId", professorship.getPerson().getIstUsername());
            teacherInfo.put("teacherMail", getTeacherPublicMail(professorship.getTeacher()));
            teacherInfo.put("teacherUrl", getTeacherPublicWebAddress(professorship.getTeacher()));
            teachersArray.add(teacherInfo);
        }
        jsonResult.put("teachers", teachersArray);

        //TODO change getIdInternal to ExternalID
        jsonResult.put("courseAnnouncementLink", getServerLink().concat("/external/announcementsRSS.do?announcementBoardId=")
                .concat(executionCourse.getBoard().getIdInternal().toString()));

        jsonResult.put("courseSummaryLink",
                getServerLink().concat("/publico/summariesRSS.do?id=").concat(executionCourse.getIdInternal().toString()));
        return jsonResult.toJSONString();
    }

    @GET
    @Produces(MediaTypeJsonUtf8)
    @Path("courses/{oid}/groups")
    public static String groupsCoursesByOid(@PathParam("oid") String oid) {
        JSONObject jsonResult = new JSONObject();

        ExecutionCourse executionCourse = getDomainObject(oid);

        jsonResult.put("courseName", executionCourse.getName());
        jsonResult.put("year", executionCourse.getExecutionYear().getName());
        jsonResult.put("semester", executionCourse.getExecutionPeriod().getName());;

        JSONArray jsonGroupInfo = new JSONArray();

        for (Grouping grouping : executionCourse.getGroupings()) {
            JSONObject jsonGroupType = new JSONObject();

            jsonGroupType.put("groupingName", grouping.getName());

            jsonGroupType.put("groupingDescription", grouping.getProjectDescription());

            JSONArray jsonGroupNumber = new JSONArray();

            for (StudentGroup studentGroup : grouping.getStudentGroupsOrderedByGroupNumber()) {
                JSONObject jsonGroupNumberInfo = new JSONObject();
                jsonGroupNumberInfo.put("groupNumber", studentGroup.getGroupNumber());
                JSONArray jsonGroupStudents = new JSONArray();

                for (Attends attends : studentGroup.getAttends()) {
                    JSONObject jsonStudent = new JSONObject();
                    jsonStudent.put("studentIstId", attends.getRegistration().getNumber());
                    jsonStudent.put("studentName", attends.getRegistration().getPerson().getName());
                    jsonGroupStudents.add(jsonStudent);
                }
                jsonGroupNumberInfo.put("students", jsonGroupStudents);
                jsonGroupNumber.add(jsonGroupNumberInfo);
            }
            jsonGroupType.put("groupNumberInfo", jsonGroupNumber);

            jsonGroupInfo.add(jsonGroupType);
        }

        jsonResult.put("groupingInfo", jsonGroupInfo);
        return jsonResult.toJSONString();
    }

    @GET
    @Produces(MediaTypeJsonUtf8)
    @Path("courses/{oid}/students")
    public static String studentsCoursesByOid(@PathParam("oid") String oid) {

        JSONObject jsonResult = new JSONObject();
        ExecutionCourse executionCourse = getDomainObject(oid);

        jsonResult.put("courseEnrolmentNumber", executionCourse.getTotalEnrolmentStudentNumber());
        jsonResult.put("courseName", executionCourse.getName());
        jsonResult.put("semester", executionCourse.getExecutionYear().getName());
        jsonResult.put("year", executionCourse.getExecutionPeriod().getName());

        JSONArray jsonStudents = new JSONArray();
        for (final Attends attends : executionCourse.getAttendsSet()) {
            JSONObject jsonStudentInfo = new JSONObject();
            jsonStudentInfo.put("studentNumber", attends.getRegistration().getNumber());
            jsonStudentInfo.put("studentName", attends.getRegistration().getName());
            jsonStudentInfo.put("studentDegree", attends.getRegistration().getDegreeCurricularPlanName());
            jsonStudentInfo.put("studentDegreeId", attends.getRegistration().getDegree().getExternalId());

            JSONArray jsonEvaluation = new JSONArray();

            for (final Mark mark : attends.getAssociatedMarksSet()) {
                JSONObject jsonEvaluationInfo = new JSONObject();
                if (mark.getEvaluation().getPublishmentMessage() != null) {
                    jsonEvaluationInfo.put("evaluation", mark.getEvaluation().getPresentationName());
                    jsonEvaluationInfo.put("mark", mark.getPublishedMark());
                    jsonEvaluation.add(jsonEvaluationInfo);
                }
            }
            jsonStudentInfo.put("evaluation", jsonEvaluation);
            jsonStudents.add(jsonStudentInfo);
        }
        jsonResult.put("students", jsonStudents);
        return jsonResult.toJSONString();
    }

    @GET
    @Produces(MediaTypeJsonUtf8)
    @Path("courses/{oid}/evaluation")
    public static String evaluationCoursesByOid(@PathParam("oid") String oid) {

        ExecutionCourse executionCourse = getDomainObject(oid);
        JSONArray jsonEvaluation = new JSONArray();

        for (Evaluation evaluation : executionCourse.getAssociatedEvaluations()) {
            if (evaluation instanceof WrittenEvaluation) {
                jsonEvaluation.add(getWrittenEvaluationJSON((WrittenEvaluation) evaluation));
            } else if (evaluation instanceof Project) {
                jsonEvaluation.add(getProjectEvaluationJSON((Project) evaluation));
            } else if (evaluation instanceof OnlineTest) {
                jsonEvaluation.add(getOnlineTestJSON((OnlineTest) evaluation));
            } else if (evaluation instanceof AdHocEvaluation) {
                jsonEvaluation.add(getAdhocEvaluationJSON((AdHocEvaluation) evaluation));
            }
        }
        return jsonEvaluation.toJSONString();
    }

    private static JSONObject getAdhocEvaluationJSON(AdHocEvaluation adHocEvaluation) {
        JSONObject jsonEvaluationInfo = new JSONObject();

        jsonEvaluationInfo.put("name", adHocEvaluation.getPresentationName());
        jsonEvaluationInfo.put("type", adHocEvaluation.getEvaluationType().toString());
        jsonEvaluationInfo.put("description", adHocEvaluation.getDescription());

        return jsonEvaluationInfo;
    }

    private static JSONObject getOnlineTestJSON(OnlineTest onlineTest) {
        JSONObject jsonEvaluationInfo = new JSONObject();

        jsonEvaluationInfo.put("name", onlineTest.getPresentationName());
        jsonEvaluationInfo.put("type", onlineTest.getEvaluationType().toString());

        return jsonEvaluationInfo;
    }

    private static JSONObject getProjectEvaluationJSON(Project projectEvaluation) {
        JSONObject jsonEvaluationInfo = new JSONObject();

        jsonEvaluationInfo.put("name", projectEvaluation.getPresentationName());
        jsonEvaluationInfo.put("type", projectEvaluation.getEvaluationType().toString());

        if (projectEvaluation.getProjectBeginDateTime() != null) {
            jsonEvaluationInfo.put("beginningDay", formatDay.print(projectEvaluation.getProjectBeginDateTime()));
            jsonEvaluationInfo.put("beginningTime", formatHour.print(projectEvaluation.getProjectBeginDateTime()));

        }
        if (projectEvaluation.getProjectEndDateTime() != null) {
            jsonEvaluationInfo.put("endDay", formatDay.print(projectEvaluation.getProjectEndDateTime()));
            jsonEvaluationInfo.put("endTime", formatHour.print(projectEvaluation.getProjectEndDateTime()));

        }
        return jsonEvaluationInfo;
    }

    private static JSONObject getWrittenEvaluationJSON(WrittenEvaluation writtenEvaluation) {
        JSONObject jsonEvaluationInfo = new JSONObject();

        jsonEvaluationInfo.put("name", writtenEvaluation.getPresentationName());
        jsonEvaluationInfo.put("type", writtenEvaluation.getEvaluationType().toString());

        jsonEvaluationInfo.put("day", dataFormatDay.format(writtenEvaluation.getDay().getTime()));

        jsonEvaluationInfo.put("beginningTime", dataFormatHour.format(writtenEvaluation.getBeginning().getTime()));
        jsonEvaluationInfo.put("endTime", dataFormatHour.format(writtenEvaluation.getEnd().getTime()));

        JSONArray jsonRoomArray = new JSONArray();
        for (AllocatableSpace allocationSpace : writtenEvaluation.getAssociatedRooms()) {
            JSONObject jsonRoomInfo = new JSONObject();
            jsonRoomInfo.put("roomId", allocationSpace.getExternalId());
            jsonRoomInfo.put("roomName", allocationSpace.getSpaceInformation().getPresentationName());
            jsonRoomInfo.put("roomDescription", allocationSpace.getCompleteIdentification());
            jsonRoomArray.add(jsonRoomInfo);
        }
        jsonEvaluationInfo.put("rooms", jsonRoomArray);

        jsonEvaluationInfo.put("isEnrolmentPeriod", writtenEvaluation.getIsInEnrolmentPeriod());

        if (writtenEvaluation.getEnrollmentBeginDay() != null) {
            jsonEvaluationInfo.put("enrollmentBeginDay",
                    dataFormatDay.format(writtenEvaluation.getEnrollmentBeginDay().getTime()));
        }
        if (writtenEvaluation.getEnrollmentBeginTime() != null) {
            jsonEvaluationInfo.put("enrollmentBeginTime",
                    dataFormatHour.format(writtenEvaluation.getEnrollmentBeginTime().getTime()));
        }
        if (writtenEvaluation.getEnrollmentEndDay() != null) {
            jsonEvaluationInfo.put("enrollmentEndDay", dataFormatDay.format(writtenEvaluation.getEnrollmentEndDay().getTime()));
        }
        if (writtenEvaluation.getEnrollmentEndTime() != null) {
            jsonEvaluationInfo
                    .put("enrollmentEndTime", dataFormatHour.format(writtenEvaluation.getEnrollmentEndTime().getTime()));
        }
        return jsonEvaluationInfo;
    }

    @GET
    @Produces(MediaTypeJsonUtf8)
    @Path("courses/{oid}/schedule")
    public static String scheduleCoursesByOid(@PathParam("oid") String oid) {

        ExecutionCourse executionCourse = getDomainObject(oid);

        JSONObject jsonResult = new JSONObject();
        JSONArray jsonSchedule = new JSONArray();
        JSONArray jsonPeriod = new JSONArray();

        jsonResult.put("name", executionCourse.getName());
        jsonResult.put("year", executionCourse.getExecutionYear().getName());
        jsonResult.put("semester", executionCourse.getExecutionPeriod().getName());

        for (OccupationPeriod occupationPeriod : executionCourse.getLessonPeriods()) {
            JSONObject jsonPeriodInfo = new JSONObject();
            if (occupationPeriod.getStartDate() != null) {
                jsonPeriodInfo.put("start", dataFormatDay.format(occupationPeriod.getStartDate().getTime()));
            }
            if (occupationPeriod.getEndDate() != null) {
                jsonPeriodInfo.put("end", dataFormatDay.format(occupationPeriod.getEndDate().getTime()));
            }
            jsonPeriod.add(jsonPeriodInfo);
        }

        for (Lesson lesson : executionCourse.getLessons()) {
            JSONObject jsonScheduleInfo = new JSONObject();
            jsonScheduleInfo.put("weekday", lesson.getWeekDay().getName());
            jsonScheduleInfo.put("lessonType", lesson.getShift().getShiftTypesCodePrettyPrint());
            jsonScheduleInfo.put("start", dataFormatHour.format(lesson.getInicio().getTime()));
            jsonScheduleInfo.put("end", dataFormatHour.format(lesson.getFim().getTime()));

            if (lesson.hasSala()) {
                AllocatableSpace sala = lesson.getSala();
                jsonScheduleInfo.put("roomId", sala.getExternalId());
                jsonScheduleInfo.put("roomName", sala.getSpaceInformation().getPresentationName());
                jsonScheduleInfo.put("roomDescription", sala.getCompleteIdentification());
            }
            jsonSchedule.add(jsonScheduleInfo);
        }

        jsonResult.put("schedule", jsonSchedule);
        jsonResult.put("period", jsonPeriod);
        return jsonResult.toJSONString();
    }

    @GET
    @Produces(MediaTypeJsonUtf8)
    @Path("spaces")
    public static String spaces() {
        JSONArray jsonResult = new JSONArray();
        for (Campus campus : Space.getAllCampus()) {
            JSONObject jsonBuildingInfo = new JSONObject();
            jsonBuildingInfo.put("id", campus.getExternalId());
            jsonBuildingInfo.put("name", campus.getName());
            jsonResult.add(jsonBuildingInfo);
        }
        return jsonResult.toJSONString();
    }

    @GET
    @Produces(MediaTypeJsonUtf8)
    @Path("spaces/{oid}")
    public static String spacesByOid(@PathParam("oid") String oid, @QueryParam("day") String day) {
        JSONObject jsonResult = new JSONObject();

        Resource resource = Resource.fromExternalId(oid);

        if (resource.isCampus()) {
            Campus campus = (Campus) resource;
            jsonResult.put("name", campus.getName());
            jsonResult.put("id", campus.getExternalId());
            jsonResult.put("type", "campus");
            jsonResult.put("moreInfo", getCampusInfo(campus));

        } else if (resource.isBuilding()) {
            Building building = (Building) resource;
            jsonResult.put("name", building.getNameWithCampus());
            jsonResult.put("campus", building.getSpaceCampus().getName());
            jsonResult.put("campusId", building.getSpaceCampus().getExternalId());

            jsonResult.put("id", building.getExternalId());
            jsonResult.put("type", "building");
            jsonResult.put("moreInfo", getBuildingInfo(building));

        } else if (resource.isFloor()) {
            Floor floor = (Floor) resource;
            jsonResult.put("id", floor.getExternalId());
            jsonResult.put("building", floor.getSpaceBuilding().getSpaceInformation().getPresentationName());
            jsonResult.put("buildingId", floor.getSpaceBuilding().getExternalId());
            jsonResult.put("campus", floor.getSpaceCampus().getSpaceInformation().getPresentationName());
            jsonResult.put("campusId", floor.getSpaceCampus().getExternalId());
            jsonResult.put("floor", floor.getSpaceFloor().getSpaceInformation().getPresentationName());
            jsonResult.put("moreInfo", getFloorInfo(floor));
        } else if (resource.isRoom()) {
            Room room = (Room) resource;
            jsonResult.put("id", room.getExternalId());
            jsonResult.put("name", room.getSpaceInformation().getPresentationName());
            jsonResult.put("description", room.getSpaceInformation().getDescription());
            jsonResult.put("building", room.getSpaceBuilding().getSpaceInformation().getPresentationName());
            jsonResult.put("buildingId", room.getSpaceBuilding().getExternalId());
            jsonResult.put("campus", room.getSpaceCampus().getSpaceInformation().getPresentationName());
            jsonResult.put("campusId", room.getSpaceCampus().getExternalId());
            jsonResult.put("floor", room.getSpaceFloor().getSpaceInformation().getPresentationName());
            jsonResult.put("floorId", room.getSpaceFloor().getExternalId());

            jsonResult.put("normalCapacity", room.getNormalCapacity());
            jsonResult.put("examCapacity", room.getExamCapacity());
            jsonResult.put("moreInfo", getRoomInfo(room, getRoomDay(day)));
        }
        return jsonResult.toJSONString();
    }

    private static JSONArray getCampusInfo(Campus campu) {
        JSONArray jsonResul = new JSONArray();
        for (Building building : Space.getAllActiveBuildings()) {
            JSONObject jsonBuildingInfo = new JSONObject();
            jsonBuildingInfo.put("id", building.getExternalId());
            jsonBuildingInfo.put("name", building.getNameWithCampus());
            jsonResul.add(jsonBuildingInfo);
        }
        return jsonResul;

    }

    private static JSONObject getBuildingInfo(Building building) {
        JSONObject jsonResult = new JSONObject();
        JSONArray jsonResultFloor = new JSONArray();
        JSONArray jsonResultRooms = new JSONArray();

        for (Space space : building.getContainedSpaces()) {
            JSONObject jsonBuildingInfo = new JSONObject();
            jsonBuildingInfo.put("floorId", space.getExternalId());
            jsonBuildingInfo.put("floorName", space.getSpaceInformation().getPresentationName());

            for (Space rooms : space.getContainedSpaces()) {
                JSONObject jsonRoomInfo = new JSONObject();
                jsonRoomInfo.put("roomId", rooms.getExternalId());
                jsonRoomInfo.put("roomName", rooms.getSpaceInformation().getPresentationName());
                jsonResultRooms.add(jsonRoomInfo);
            }
            jsonResultFloor.add(jsonBuildingInfo);
        }
        jsonResult.put("floors", jsonResultFloor);
        jsonResult.put("rooms", jsonResultRooms);

        return jsonResult;
    }

    private static JSONObject getFloorInfo(Floor floor) {
        JSONObject jsonResult = new JSONObject();
        JSONArray jsonResultRooms = new JSONArray();

        for (Space space : floor.getContainedSpaces()) {
            JSONObject jsonBuildingInfo = new JSONObject();
            jsonBuildingInfo.put("roomId", space.getExternalId());
            jsonBuildingInfo.put("roomName", space.getSpaceInformation().getPresentationName());
            jsonResultRooms.add(jsonBuildingInfo);
        }
        jsonResult.put("rooms", jsonResultRooms);

        return jsonResult;
    }

    private static JSONObject getRoomInfo(Room room, Calendar rightNow) {
        JSONObject jsonResult = new JSONObject();
        JSONArray jsonSchedule = new JSONArray();

        InfoSiteRoomTimeTable bodyComponent = new InfoSiteRoomTimeTable();
        RoomSiteComponentBuilder builder = new RoomSiteComponentBuilder();

        jsonResult.put("day", dataFormatDay.format(rightNow.getTime()));
        try {
            builder.getComponent(bodyComponent, rightNow, room, null);
            for (Object occupation : bodyComponent.getInfoShowOccupation()) {
                InfoShowOccupation showOccupation = (InfoShowOccupation) occupation;

                JSONObject jsonOccupationInfo = new JSONObject();

                if (showOccupation instanceof InfoLesson) {
                    InfoLesson lesson = (InfoLesson) showOccupation;
                    InfoExecutionCourse infoExecutionCourse = lesson.getInfoShift().getInfoDisciplinaExecucao();
                    jsonOccupationInfo.put("courseSigla", infoExecutionCourse.getSigla());
                    jsonOccupationInfo.put("courseName", infoExecutionCourse.getNome());
                    jsonOccupationInfo.put("courseId", infoExecutionCourse.getExecutionCourse().getExternalId());

                    jsonOccupationInfo.put("start", dataFormatHour.format(lesson.getInicio().getTime()));
                    jsonOccupationInfo.put("end", dataFormatHour.format(lesson.getFim().getTime()));
                    jsonOccupationInfo.put("weekday", lesson.getDiaSemana().getDiaSemanaString());

                    jsonOccupationInfo.put("info", lesson.getInfoShift().getShiftTypesCodePrettyPrint());
                    jsonOccupationInfo.put("type", "lesson");

                } else if (showOccupation instanceof InfoLessonInstance) {
                    InfoLessonInstance lesson = (InfoLessonInstance) showOccupation;
                    InfoExecutionCourse infoExecutionCourse = lesson.getInfoShift().getInfoDisciplinaExecucao();
                    jsonOccupationInfo.put("courseSigla", infoExecutionCourse.getSigla());
                    jsonOccupationInfo.put("courseName", infoExecutionCourse.getNome());
                    jsonOccupationInfo.put("courseId", infoExecutionCourse.getExecutionCourse().getExternalId());

                    jsonOccupationInfo.put("start", dataFormatHour.format(lesson.getInicio().getTime()));
                    jsonOccupationInfo.put("end", dataFormatHour.format(lesson.getFim().getTime()));
                    jsonOccupationInfo.put("weekday", lesson.getDiaSemana().getDiaSemanaString());

                    jsonOccupationInfo.put("info", lesson.getInfoShift().getShiftTypesCodePrettyPrint());
                    jsonOccupationInfo.put("type", "lesson");

                } else if (showOccupation instanceof InfoExam) {
                    InfoExam infoExam = (InfoExam) showOccupation;
                    JSONArray jsonSiglaArr = new JSONArray();
                    for (int iterEC = 0; iterEC < infoExam.getAssociatedExecutionCourse().size(); iterEC++) {
                        InfoExecutionCourse infoEC = infoExam.getAssociatedExecutionCourse().get(iterEC);
                        JSONObject jsonSigla = new JSONObject();
                        jsonSigla.put("sigla", infoEC.getSigla());
                        jsonSigla.put("courseName", infoEC.getNome());
                        jsonSigla.put("courseId", infoEC.getExecutionCourse().getExternalId());
                        jsonSiglaArr.add(jsonSigla);
                    }
                    jsonOccupationInfo.put("siglaList", jsonSiglaArr);
                    jsonOccupationInfo.put("season", infoExam.getSeason().getSeason());

                    jsonOccupationInfo.put("start", infoExam.getBeginningHour());
                    jsonOccupationInfo.put("end", infoExam.getEndHour());
                    jsonOccupationInfo.put("weekday", infoExam.getDiaSemana().getDiaSemanaString());

                    jsonOccupationInfo.put("type", "exam");

                } else if (showOccupation instanceof InfoWrittenTest) {
                    InfoWrittenTest infoWrittenTest = (InfoWrittenTest) showOccupation;
                    JSONArray jsonSiglaArr = new JSONArray();
                    for (int iterEC = 0; iterEC < infoWrittenTest.getAssociatedExecutionCourse().size(); iterEC++) {
                        InfoExecutionCourse infoEC = infoWrittenTest.getAssociatedExecutionCourse().get(iterEC);
                        JSONObject jsonSigla = new JSONObject();
                        jsonSigla.put("sigla", infoEC.getSigla());
                        jsonSigla.put("courseName", infoEC.getNome());
                        jsonSigla.put("courseId", infoEC.getExecutionCourse().getExternalId());
                        jsonSiglaArr.add(jsonSigla);
                    }
                    jsonOccupationInfo.put("siglaList", jsonSiglaArr);
                    jsonOccupationInfo.put("description", infoWrittenTest.getDescription());

                    jsonOccupationInfo.put("start", dataFormatHour.format(infoWrittenTest.getInicio().getTime()));
                    jsonOccupationInfo.put("end", dataFormatHour.format(infoWrittenTest.getFim().getTime()));
                    jsonOccupationInfo.put("weekday", infoWrittenTest.getDiaSemana().getDiaSemanaString());

                    jsonOccupationInfo.put("type", "test");

                } else if (showOccupation instanceof InfoGenericEvent) {

                    InfoGenericEvent infoGenericEvent = (InfoGenericEvent) showOccupation;
                    jsonOccupationInfo.put("description", infoGenericEvent.getDescription());
                    jsonOccupationInfo.put("title", infoGenericEvent.getTitle());

                    jsonOccupationInfo.put("start", dataFormatHour.format(infoGenericEvent.getInicio().getTime()));
                    jsonOccupationInfo.put("end", dataFormatHour.format(infoGenericEvent.getFim().getTime()));
                    jsonOccupationInfo.put("weekday", infoGenericEvent.getDiaSemana().getDiaSemanaString());

                    jsonOccupationInfo.put("type", "event");

                }
                jsonSchedule.add(jsonOccupationInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw newApplicationError(Status.INTERNAL_SERVER_ERROR, "berserk!", "something went wrong");
        }
        jsonResult.put("schedule", jsonSchedule);
        return jsonResult;
    }

    @SuppressWarnings("unchecked")
    private static <T extends DomainObject> T getDomainObject(String externalId) {
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

    private static WebApplicationException newApplicationError(Status status, String error, String description) {
        JSONObject errorObject = new JSONObject();
        errorObject.put("error", error);
        errorObject.put("description", description);
        return new WebApplicationException(Response.status(status).entity(errorObject.toJSONString()).build());
    }

    private static Calendar getRoomDay(String day) {
        Calendar rightNow = Calendar.getInstance();
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
