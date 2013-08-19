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

import net.sourceforge.fenixedu.applicationTier.Servico.Authenticate;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.EnrolledCourseBean;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.EnrolledLessonBean;
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
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

@Path("/private/v1")
public class JerseyPrivate {

	private final static String istID = "ist158444";

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("hello")
	public String hellofenix() {
		return "Hello! Private V1";
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("person")
	public static String person() {


		System.out.println("->"+AccessControl.getUserView());
		System.out.println("-->"+AccessControl.getPerson());
		if (UserView.getUser()==null) {
			System.out.println("NULL!");
		} else {
			System.out.println("Jersey - "+UserView.getUser().getUsername());
		}


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
	@Path("person/courses/")
	public static String personCourses() {

		final Person person = Person.readPersonByUsername(istID);
		final Student foundStudent = person.getStudent();


		PersonInformationBean pib = new PersonInformationBean(person);

		ExecutionSemester executionSemester = ExecutionSemester.readBySemesterAndExecutionYear(2, "2012/2013");

		
		JSONArray jsonEnrolments = new JSONArray();		

		for(Registration r : foundStudent.getAllRegistrations()) {
			
			for(Enrolment enrolment : r.getEnrolments(executionSemester)) {
				JSONObject jsonEnrolmentInfo= new JSONObject();		

				jsonEnrolmentInfo.put("description", enrolment.getDescription());
				jsonEnrolmentInfo.put("grade", enrolment.getGrade());
				jsonEnrolmentInfo.put("credits", enrolment.getEctsCredits());
				jsonEnrolments.add(jsonEnrolmentInfo);
			} 
		}
		return jsonEnrolments.toJSONString();
	}
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("person/schedule/")
	public static String personSchedule() {

		final Person person = Person.readPersonByUsername("ist164216");

		PersonInformationBean pib = new PersonInformationBean(person);
		
		JSONArray jsonLessons = new JSONArray();		

		for (EnrolledLessonBean enrolledLessonBean: pib.getLessonsSchedule()) {
			JSONObject jsonLessonInfo= new JSONObject();		
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



}