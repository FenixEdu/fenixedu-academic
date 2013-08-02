package net.sourceforge.fenixedu.webServices.jersey;

import java.util.List;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import net.sourceforge.fenixedu.dataTransferObject.externalServices.EnrolledCourseBean;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.PersonInformationBean;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeInfo;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

@Path("/services/v1")
public class JerseyApi {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("hellofenixapi")
    public String hellofenix() {
        return "Hello API V1!";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("degrees")
    public static String degrees() {
        JSONArray infos = new JSONArray();
        for (Degree degree : Degree.readBolonhaDegrees()) {
            if (degree.isBolonhaMasterOrDegree()) {
                JSONObject degreeInfo = new JSONObject();
                degreeInfo.put("oid", degree.getExternalId());
                degreeInfo.put("name", degree.getPresentationName());
                degreeInfo.put("degreeType", degree.getDegreeTypeName());
                infos.add(degreeInfo);
            }
        }
        return infos.toJSONString();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("person")
    public static String person() {
        Person person = AccessControl.getUserView().getPerson();

        return null;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("person/{istId}")
    public static String personByIstId(@PathParam("istId") String istID) {
        //final Person person = Person.readPersonByUsername(username);
        //final User foundUser = User.readUserByUserUId(username);
        //final int studentNumber =  Integer.parseInt(username);
        //final Student foundStudent = Student.readStudentByNumber(studentNumber);
        JSONObject jsonResult = new JSONObject();

        final Person person = Person.readPersonByUsername(istID);

        if (person.hasRole(RoleType.STUDENT)) {
            jsonResult.put("roleType", RoleType.STUDENT);
        } else if (person.hasRole(RoleType.TEACHER)) {
            jsonResult.put("roleType", RoleType.TEACHER);
        }
        PersonInformationBean pib = new PersonInformationBean(person);

        jsonResult.put("name", pib.getName());
        jsonResult.put("campus", pib.getCampus());
        jsonResult.put("mail", pib.getEmail());

        JSONArray jsonCourseBean = new JSONArray();
        for (EnrolledCourseBean enrolledCourseBean : pib.getEnrolledCoursesBeans()) {
            JSONObject jsonCourse = new JSONObject();
            jsonCourse.put("acronym", enrolledCourseBean.getAcronym());
            jsonCourse.put("name", enrolledCourseBean.getName());
            jsonCourse.put("url", enrolledCourseBean.getPageURL());
            jsonCourseBean.add(jsonCourse);
        }
        jsonResult.put("courses", jsonCourseBean);
        return jsonResult.toJSONString();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("degrees/{oid}")
    public static String degreesByOid(@PathParam("oid") String oid) {
        JSONArray jsonResult = new JSONArray();
        Degree degree = getDomainObject(oid);
        if (degree.isBolonhaMasterOrDegree()) {
            JSONObject degreeMainInfo = new JSONObject();

            degreeMainInfo.put("oid", degree.getExternalId());
            degreeMainInfo.put("name", degree.getPresentationName());
            degreeMainInfo.put("degreeType", degree.getDegreeTypeName());
            degreeMainInfo.put("degreeSigla", degree.getSigla());

            DegreeInfo degreeInfo = degree.getDegreeInfoFor(ExecutionYear.readCurrentExecutionYear());

            if (degreeInfo != null) {
                JSONObject degreeSecondaryInfo = new JSONObject();

                degreeSecondaryInfo.put("name", degreeInfo.getName());
                degreeSecondaryInfo.put("description", degreeInfo.getDescription());
                degreeSecondaryInfo.put("objectives", degreeInfo.getDegreeInfoFuture().getObjectives());
                degreeSecondaryInfo.put("designfor", degreeInfo.getDegreeInfoFuture().getDesignedFor());
                degreeSecondaryInfo.put("requisites", degreeInfo.getDegreeInfoCandidacy().getAccessRequisites());

                degreeMainInfo.put("secundaryInfo", degreeSecondaryInfo);
            }
            jsonResult.add(degreeMainInfo);
        }
        return jsonResult.toJSONString();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("degrees/{oid}/courses")
    public static String coursesByODegreesId(@PathParam("oid") String oid) {
        JSONArray jsonResult = new JSONArray();
        Degree degree = getDomainObject(oid);

        ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();

        if (degree.isBolonhaMasterOrDegree()) {

            Set<CurricularCourse> degreeCurricularCourses = degree.getAllCurricularCourses(executionYear);
            for (CurricularCourse cc : degreeCurricularCourses) {
                JSONObject degreeInfo = new JSONObject();
                degreeInfo.put("acronym", cc.getAcronym());
                degreeInfo.put("credits", cc.getEctsCredits());
                degreeInfo.put("name", cc.getName());
                degreeInfo.put("oid", cc.getExternalId());
                jsonResult.add(degreeInfo);
            }

        }
        return jsonResult.toJSONString();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("courses/{oid}/")
    public static String coursesByOid(@PathParam("oid") String oid) {
        JSONObject jsonResult = new JSONObject();
        CurricularCourse curricularCourse = getDomainObject(oid);
        ExecutionSemester executionSemester = ExecutionSemester.readLastExecutionSemester();

        jsonResult.put("acronym", curricularCourse.getAcronym());
        jsonResult.put("name", curricularCourse.getName());
        jsonResult.put("credits", curricularCourse.getEctsCredits());
        jsonResult.put("evaluation", curricularCourse.getEvaluationMethod());
        jsonResult.put("program", curricularCourse.getProgram());

        jsonResult.put("totalStudents", curricularCourse.getTotalEnrolmentStudentNumber(executionSemester));
        jsonResult.put("firstTimeStudents", curricularCourse.getNumberOfStudentsWithFirstEnrolmentIn(executionSemester));
        jsonResult.put("secondTimeStudents", curricularCourse.getSecondTimeEnrolmentStudentNumber(executionSemester));

        return jsonResult.toJSONString();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("courses/{oid}/groups")
    public static String groupsCoursesByOid(@PathParam("oid") String oid) {
        JSONObject jsonResult = new JSONObject();
        CurricularCourse curricularCourse = getDomainObject(oid);
        //ExecutionYear executionYear = ExecutionYear.readLastExecutionYear();
        //List<ExecutionCourse> executionCourse = curricularCourse.getExecutionCoursesByExecutionYear(executionYear);

        ExecutionSemester executionSemester = ExecutionSemester.readBySemesterAndExecutionYear(1, "2012/2013");
        List<ExecutionCourse> executionCourse = curricularCourse.getExecutionCoursesByExecutionPeriod(executionSemester);

        List<StudentGroup> studentGroups = null;

        return jsonResult.toJSONString();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("courses/{oid}/students")
    public static String studentsCoursesByOid(@PathParam("oid") String oid) {
        JSONObject jsonResult = new JSONObject();
        CurricularCourse curricularCourse = getDomainObject(oid);
        //ExecutionYear executionYear = ExecutionYear.readLastExecutionYear();
        // List<ExecutionCourse> executionCourse = curricularCourse.getExecutionCoursesByExecutionYear(executionYear);
        ExecutionSemester executionSemester = ExecutionSemester.readBySemesterAndExecutionYear(1, "2012/2013");
        List<ExecutionCourse> executionCourse = curricularCourse.getExecutionCoursesByExecutionPeriod(executionSemester);

        for (ExecutionCourse ex : executionCourse) {
            jsonResult.put("enrolmentNumber", ex.getTotalEnrolmentStudentNumber());
            jsonResult.put("name", ex.getName());

            JSONArray jsonProfessors = new JSONArray();
            for (Professorship professor : ex.getProfessorshipsSortedAlphabetically()) {
                JSONObject jsonStudentInfo = new JSONObject();
                PersonInformationBean pib = new PersonInformationBean(professor.getTeacher().getPerson());
                jsonStudentInfo.put("name", professor.getTeacher().getPerson().getName());
                jsonStudentInfo.put("istId", professor.getTeacher().getPerson().getIstUsername());
                jsonProfessors.add(jsonStudentInfo);
            }
            jsonResult.put("professorInformation", jsonProfessors);

            JSONArray jsonStudents = new JSONArray();
            for (Enrolment enrolment : ex.getActiveEnrollments()) {
                JSONObject jsonStudentInfo = new JSONObject();
                jsonStudentInfo.put("name", enrolment.getStudent().getName());
                jsonStudentInfo.put("grade", enrolment.getGrade().getIntegerValue());
                jsonStudentInfo.put("istId", enrolment.getPerson().getIstUsername());
                jsonStudents.add(jsonStudentInfo);
            }
            jsonResult.put("studentInformation", jsonStudents);
        }

        return jsonResult.toJSONString();
    }

    @SuppressWarnings("unchecked")
    private static <T extends DomainObject> T getDomainObject(String externalId) {
        try {
            return (T) AbstractDomainObject.fromExternalId(externalId);
        } catch (Throwable t) {
            throw new WebApplicationException(Response.status(Status.NOT_FOUND).entity(externalId).build());
        }
    }

}