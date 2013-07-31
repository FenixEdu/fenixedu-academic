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

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeInfo;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.StudentGroup;

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
        ExecutionSemester executionSemester = ExecutionSemester.readLastExecutionSemester();

        List<ExecutionCourse> asd = curricularCourse.getExecutionCoursesByExecutionPeriod(executionSemester);
        for (ExecutionCourse ex : asd) {

            for (Grouping g : ex.getGroupings()) {

                for (StudentGroup studentGroup : g.getStudentGroupsOrderedByGroupNumber()) {
                    studentGroup.getGroupNumber();

                }
            }

        }

        return jsonResult.toJSONString();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("courses/{oid}/students")
    public static String studentsCoursesByOid(@PathParam("oid") String oid) {
        JSONObject jsonResult = new JSONObject();
        CurricularCourse curricularCourse = getDomainObject(oid);
        ExecutionSemester executionSemester = ExecutionSemester.readLastExecutionSemester();

        List<ExecutionCourse> asd = curricularCourse.getExecutionCoursesByExecutionPeriod(executionSemester);
        for (ExecutionCourse ex : asd) {
            for (Grouping g : ex.getGroupings()) {
                for (StudentGroup studentGroup : g.getStudentGroupsOrderedByGroupNumber()) {
                    studentGroup.getGroupNumber();
                }
            }

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