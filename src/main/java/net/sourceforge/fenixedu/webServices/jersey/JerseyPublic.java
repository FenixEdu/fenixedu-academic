package net.sourceforge.fenixedu.webServices.jersey;


import java.util.List;
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

import net.sourceforge.fenixedu.dataTransferObject.InfoViewRoomSchedule;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.PersonInformationBean;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeInfo;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.WrittenEvaluationEnrolment;
import net.sourceforge.fenixedu.domain.oldInquiries.InquiriesRoom;
import net.sourceforge.fenixedu.domain.resource.ResourceAllocation;
import net.sourceforge.fenixedu.domain.space.Building;
import net.sourceforge.fenixedu.domain.space.Floor;
import net.sourceforge.fenixedu.domain.space.Room;
import net.sourceforge.fenixedu.domain.space.RoomInformation;
import net.sourceforge.fenixedu.domain.space.Space;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;

import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

@Path("/public/v1")
public class JerseyPublic {

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("hello")
	public String hellofenix() {
		return "Hello! Public V1";
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
	public static String coursesByOid(@PathParam("oid") String oid, @QueryParam("sem") String sem, @QueryParam("year") String year) {
		JSONObject jsonResult = new JSONObject();
		CurricularCourse curricularCourse = getDomainObject(oid);
		
		ExecutionSemester executionSemester = getExecutionSemester(sem, year);
		
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
	public static String groupsCoursesByOid(@PathParam("oid") String oid, @QueryParam("sem") String sem, @QueryParam("year") String year) {
		JSONObject jsonResult = new JSONObject();
		CurricularCourse curricularCourse = getDomainObject(oid);

		ExecutionSemester executionSemester = getExecutionSemester(sem, year);
		
		List<ExecutionCourse> executionCourse = curricularCourse.getExecutionCoursesByExecutionPeriod(executionSemester);

		List<StudentGroup> studentGroups = null;

		return jsonResult.toJSONString();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("courses/{oid}/students")
	public static String studentsCoursesByOid(@PathParam("oid") String oid, @QueryParam("sem") String sem, @QueryParam("year") String year) {

		JSONObject jsonResult = new JSONObject();
		CurricularCourse curricularCourse = getDomainObject(oid);

		ExecutionSemester executionSemester = getExecutionSemester(sem, year);

		List<ExecutionCourse> executionCourse = curricularCourse.getExecutionCoursesByExecutionPeriod(executionSemester);

		for (ExecutionCourse ex : executionCourse) {
			jsonResult.put("enrolmentNumber", ex.getTotalEnrolmentStudentNumber());
			jsonResult.put("name", ex.getName());
			jsonResult.put("semester", executionSemester.getSemester());
			jsonResult.put("year", executionSemester.getYear());

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


	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("buildings")
	public static String buildings() {
		JSONArray jsonResult = new JSONArray();
		for(Building building: Space.getAllActiveBuildings()) {
			JSONObject jsonBuildingInfo = new JSONObject();
			jsonBuildingInfo.put("oid",building.getExternalId());
			jsonBuildingInfo.put("name",building.getNameWithCampus());
			jsonResult.add(jsonBuildingInfo);
		}		
		return jsonResult.toJSONString();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("buildings/{oid}")
	public static String buildingsByOid(@PathParam("oid") String oid) {
		JSONObject jsonResult = new JSONObject();		
		Building building = Space.fromExternalId(oid);

		JSONArray jsonResultFloor = new JSONArray();		
		JSONArray jsonResultRooms = new JSONArray();		

		jsonResult.put("name", building.getNameWithCampus());
		jsonResult.put("campus", building.getSpaceCampus().getName());

		for(Space space : building.getContainedSpaces()) {
			JSONObject jsonBuildingInfo = new JSONObject();
			jsonBuildingInfo.put("oid",space.getExternalId());
			jsonBuildingInfo.put("name",space.getSpaceInformation().getPresentationName());

			for(Space rooms : space.getContainedSpaces()) {
				JSONObject jsonRoomInfo = new JSONObject();
				jsonRoomInfo.put("oid",rooms.getExternalId());
				jsonRoomInfo.put("name", rooms.getSpaceInformation().getPresentationName());
				jsonResultRooms.add(jsonRoomInfo);

			}
			jsonResultFloor.add(jsonBuildingInfo);
		}		
		jsonResult.put("floors", jsonResultFloor);
		jsonResult.put("rooms", jsonResultRooms);

		return jsonResult.toJSONString();		
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("floors/{oid}")
	public static String floorByOid(@PathParam("oid") String oid) {
		JSONObject jsonResult = new JSONObject();		
		Floor floor = Space.fromExternalId(oid);

		jsonResult.put("oid", floor.getExternalId());
		jsonResult.put("building", floor.getSpaceBuilding().getSpaceInformation().getPresentationName());
		jsonResult.put("campus", floor.getSpaceCampus().getSpaceInformation().getPresentationName());
		jsonResult.put("floor", floor.getSpaceFloor().getSpaceInformation().getPresentationName());

		JSONArray jsonResultRooms = new JSONArray();		

		for(Space space : floor.getContainedSpaces()) {
			JSONObject jsonBuildingInfo = new JSONObject();
			jsonBuildingInfo.put("oid",space.getExternalId());
			jsonBuildingInfo.put("name",space.getSpaceInformation().getPresentationName());
			jsonResultRooms.add(jsonBuildingInfo);
		}		
		jsonResult.put("rooms", jsonResultRooms);

		return jsonResult.toJSONString();		
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("rooms/{oid}")
	public static String roomsByOid(@PathParam("oid") String oid) {
		JSONObject jsonResult = new JSONObject();		
		Room room = Space.fromExternalId(oid);

		jsonResult.put("oid", room.getExternalId());
		jsonResult.put("building", room.getSpaceBuilding().getSpaceInformation().getPresentationName());
		jsonResult.put("campus", room.getSpaceCampus().getSpaceInformation().getPresentationName());
		jsonResult.put("floor", room.getSpaceFloor().getSpaceInformation().getPresentationName());
		jsonResult.put("normal_capacity", room.getNormalCapacity());
		jsonResult.put("exam_capacity", room.getExamCapacity());

		//jsonResult.put("exam_capacity", room.getWrittenEvaluationEnrolments());	


		ExecutionSemester executionSemester = ExecutionSemester.readBySemesterAndExecutionYear(1, "2012/2013");
		/*
		for(WrittenEvaluationEnrolment writtenEvaluation : room.getWrittenEvaluationEnrolments()) {
		}
		 */
		JSONArray jsonSchedule = new JSONArray();		
		for(Lesson lesson : room.getAssociatedLessons(executionSemester.getAcademicInterval())) {
			JSONObject jsonLesson = new JSONObject();
			jsonLesson.put("begin", lesson.getBeginHourMinuteSecond());
			jsonLesson.put("end", lesson.getEndHourMinuteSecond());
			jsonLesson.put("weekday", lesson.getWeekDay());
			jsonLesson.put("course", lesson.getExecutionCourse().getName());
			jsonSchedule.add(jsonLesson);
		}
		jsonResult.put("lesson", jsonSchedule);


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

	
	private static ExecutionSemester getExecutionSemester(String sem, String year) {
		//sem  int 1 || 2
		//year String "2012/2013"
		
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
			if(executionSemester == null) {
				executionSemester = ExecutionSemester.readActualExecutionSemester();
			}			
		} else {
			executionSemester = ExecutionSemester.readActualExecutionSemester();
		}
		return executionSemester;
	}
}
