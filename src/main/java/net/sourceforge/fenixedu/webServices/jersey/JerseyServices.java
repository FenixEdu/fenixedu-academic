package net.sourceforge.fenixedu.webServices.jersey;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import net.sourceforge.fenixedu.dataTransferObject.externalServices.EnrolledLessonBean;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.PersonInformationBean;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeInfo;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.messaging.Announcement;
import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;

import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.google.gwt.autobean.client.impl.JsoSplittable;

@Path("/services")
public class JerseyServices {

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("hellofenix")
	public String hellofenix() {
		return "Hello!";
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("remotePerson")
	public String remotePerson(@QueryParam("username") final String username, @QueryParam("method") final String method)
			throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		final Person person = Person.readPersonByUsername(username);
		if (person != null) {
			final Method personMethod = Person.class.getMethod(method);
			Object result = personMethod.invoke(person);
			return result == null ? StringUtils.EMPTY : result.toString();
		}
		return StringUtils.EMPTY;
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("readAllUserData")
	public static String readAllUserData(@QueryParam("types") final String types) {
		RoleType[] roles;
		if (types != null && StringUtils.isNotBlank(types)) {
			roles = new RoleType[types.split("-").length];
			int i = 0;
			for (String typeString : types.split("-")) {
				roles[i] = RoleType.valueOf(typeString);
				i++;
			}
		} else {
			roles = new RoleType[0];
		}
		final StringBuilder builder = new StringBuilder();
		for (final User user : RootDomainObject.getInstance().getUsersSet()) {
			if (!StringUtils.isEmpty(user.getUserUId())) {
				final Person person = user.getPerson();
				if (roles.length == 0 || person.hasAnyRole(roles)) {
					builder.append(user.getUserUId());
					builder.append("\t");
					builder.append(person.getName());
					builder.append("\t");
					builder.append(person.getExternalId());
					builder.append("\n");
				}
			}
		}
		return builder.toString();
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("readAllEmails")
	public static String readAllEmails() {
		final StringBuilder builder = new StringBuilder();
		for (final Party party : RootDomainObject.getInstance().getPartysSet()) {
			if (party.isPerson()) {
				final Person person = (Person) party;
				final String email = person.getEmailForSendingEmails();
				if (email != null) {
					final User user = person.getUser();
					if (user != null) {
						final String username = user.getUserUId();
						builder.append(username);
						builder.append("\t");
						builder.append(email);
						builder.append("\n");
					}
				}
			}
		}
		return builder.toString();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("readActiveStudentInfoForJobBank")
	public static String readActiveStudentInfoForJobBank(@QueryParam("username") final String username) {
		final Person person = Person.readPersonByUsername(username);
		final Student student = person.getStudent();
		return student != null ? student.readActiveStudentInfoForJobBank() : StringUtils.EMPTY;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("readStudentInfoForJobBank")
	public static String readStudentInfoForJobBank(@QueryParam("username") final String username) {
		final Person person = Person.readPersonByUsername(username);
		final Student student = person.getStudent();
		return student != null ? student.readStudentInfoForJobBank() : StringUtils.EMPTY;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("readAllStudentsInfoForJobBank")
	public static String readAllStudentsInfoForJobBank() {
		return Registration.readAllStudentsInfoForJobBank();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("readBolonhaDegrees")
	public static String readBolonhaDegrees() {
		JSONArray infos = new JSONArray();
		for (Degree degree : Degree.readBolonhaDegrees()) {
			if (degree.isBolonhaMasterOrDegree()) {
				JSONObject degreeInfo = new JSONObject();
				degreeInfo.put("degreeOid", degree.getExternalId());
				degreeInfo.put("name", degree.getPresentationName());
				degreeInfo.put("degreeType", degree.getDegreeTypeName());
				infos.add(degreeInfo);
			}
		}
		return infos.toJSONString();
	}


	/*
	 * New webservices
	 */

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("PersonalInformation")
	public String PersonalInformation(@QueryParam("username") final String username)
			throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {

		final User foundUser = User.readUserByUserUId(username);

		PersonInformationBean pib;
		if(foundUser == null) {
			return null;
		} else {
			pib = new PersonInformationBean(foundUser.getPerson());

			StringBuilder strout = new StringBuilder();
			strout.append("Name: " + pib.getName());
			strout.append("\tCampus: " + pib.getCampus());

			strout.append("\tCategories: ");
			for (String s : pib.getPersonCategories()) {
				strout.append(s.toString());
			}

			strout.append("\tPersonalEmail: ");
			for (String s : pib.getPersonalEmails()) {
				strout.append(s.toString());
			}

			strout.append("\tPersonalWebAdresses: ");
			for (String s : pib.getPersonalWebAdresses()) {
				strout.append(s.toString());
			}

			strout.append("\tDegrees: ");
			for (String s : pib.getStudentDegrees()) {
				strout.append(s.toString());			
			}

			return strout.toString();
		}
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("PersonalCurriculum")
	public String PersonalCurriculum(@QueryParam("username") final String username)
			throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {

		StringBuilder strout = new StringBuilder();
		strout.append("Active Registration\n");

		final int studentNumber =  Integer.parseInt(username);
		final Student foundStudent = Student.readStudentByNumber(studentNumber);

		for(Registration r : foundStudent.getAllRegistrations()) {

			strout.append("\tDegreeName: "+r.getDegreeName()+"\n");
			strout.append("\tDegreeDescription: "+r.getDegreeDescription()+"\n");

			strout.append("\tCampus: "+r.getCampus().getName()+"\n");
			strout.append("\tName: "+r.getName()+"\n");

			strout.append("\tAverage:" +r.getAverage()+"\n");
			strout.append("\tCredits: "+r.getEctsCredits()+"\n");

			for(Enrolment e : r.getApprovedEnrolments()) {
				strout.append("\tDescription: "+e.getDescription()+"\n");
				strout.append("\tGrade: "+e.getGrade()+"\n");
				strout.append("\tCredits: "+e.getEctsCredits()+"\n");
			} 

		}
		/*
		strout.append("\n\n\n\nApproved Enrolments\n");
		for(Enrolment r : foundStudent.getApprovedEnrolments()) {
			strout.append("\tGrade: "+r.getGradeValue());
			strout.append("\tFinalGrade: "+r.getFinalGrade());
			strout.append("\tCode: "+r.getCode());
			strout.append("\tDescription: "+r.getDescription());
			strout.append("\tTypeName: "+r.getEnrolmentTypeName()+"\n");
		}
		 */
		return strout.toString();
	}

	/*
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("PersonalPayments")
	public String PersonalPayments(@QueryParam("username") final String username)
			throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {

		StringBuilder strout = new StringBuilder();
		strout.append("Active Registration\n");

		final Person person = Person.readPersonByUsername(username);

		for(Entry e : person.getPayments()) {

			for(Receipt r : e.getReceipts()) {
				strout.append("\t"+r.getTotalAmount().getAmount()+"\n");				
				for(Entry ee : r.getEntries()) {
					strout.append("\t"+ee.getDescription().toString()+"\n");				
					strout.append("\t"+ee.getPaymentMode().getName()+"\n");				

				}
			}			
		}
		return strout.toString();
	}
	 */

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("PersonalCalendar")
	public String PersonalCalendar(@QueryParam("username") final String username)
			throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {

		StringBuilder strout = new StringBuilder();
		strout.append("Active Registration\n");

		return strout.toString();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("putTest")	
	public Response putTest(String username)
			throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {

		final User foundUser = User.readUserByUserUId("ist158444");
		PersonInformationBean pib = new PersonInformationBean(foundUser.getPerson());
		pib.setName("foo");

		return Response.status(201).entity(username).build();

	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("Schedule")
	public String Schedule(@QueryParam("username") final String username)
			throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {

		final User foundUser = User.readUserByUserUId(username);

		PersonInformationBean pib;
		if(foundUser == null) {
			return null;
		} else {
			pib = new PersonInformationBean(foundUser.getPerson());

			StringBuilder strout = new StringBuilder();
			List<EnrolledLessonBean> l = pib.getLessonsSchedule();

			for (EnrolledLessonBean rlb : l) {
				strout.append(rlb.getCourseAcronym()+"\n");
				strout.append(rlb.getLessonType()+"\n");
				strout.append(rlb.getWeekDay()+"\n");
				strout.append(rlb.getRoom()+"\n");
				strout.append(rlb.getBegin()+"\n");
				strout.append(rlb.getEnd()+"\n");
				strout.append("----------");
			}
			return strout.toString();
		}
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("Announcements")
	public String Announcements(@QueryParam("username") final String username)
			throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {

		StringBuilder strout = new StringBuilder();
		final Person person = Person.readPersonByIstUsername(username);
		Collection<AnnouncementBoard> result = person.getCurrentExecutionCoursesAnnouncementBoards();
		List<Announcement> la;

		for (AnnouncementBoard ab : result) {
			la = ab.getActiveAnnouncements();
			for (Announcement a : la) {
				strout.append(a.getAnnouncementBoard().getFullName()+"\n");
				//strout.append(a.getAnnouncementBoard().getTitle().getContent()+"\n");
				//strout.append(a.getAnnouncementBoard().getSite()+"\n");
				//strout.append(a.getAnnouncementBoard().getName().getContent()+"\n");
				//strout.append(a.getAnnouncementBoard().getPath()+"\n");
				//strout.append(a.getAnnouncementBoard().toString()+"\n");

				//strout.append(a.toString()+"\n");


				strout.append(a.getAuthor()+"\n");
				//strout.append(a.getCampusCode()+"\n");
				strout.append(a.getSubject()+"\n");
				strout.append(a.getBodyDigest()+"\n");
				//strout.append(a.getShortBody()+"\n");

				//strout.append(a.getTitle()+"\n");
				strout.append("----------------\n");
			}
			strout.append("Acabou o announcement");
		}

		strout.append("Começar Institucionalt");

		//RootDomainObject.getInstance().getInstitutionUnit();
		RootDomainObject rootDomainObject = RootDomainObject.getInstance();
		//rootDomainObject.getInstance().
		if (rootDomainObject.hasInstitutionUnit()) {
			for (final AnnouncementBoard boards : rootDomainObject.getInstitutionUnit().getBoardsSet()) {
				for (Announcement aaa : boards.getAnnouncements()) {
					//strout.append(a.getAnnouncementBoard().getFullName()+"\n");
					//strout.append(a.getAnnouncementBoard().getTitle().getContent()+"\n");
					//strout.append(a.getAnnouncementBoard().getSite()+"\n");
					//strout.append(a.getAnnouncementBoard().getName().getContent()+"\n");
					//strout.append(a.getAnnouncementBoard().getPath()+"\n");
					//strout.append(a.getAnnouncementBoard().toString()+"\n");

					//strout.append(a.toString()+"\n");


					strout.append(aaa.getAuthor()+"\n");
					//strout.append(a.getCampusCode()+"\n");
					//strout.append(aaa.getSubject()+"\n");
					strout.append(aaa.getBodyDigest()+"\n");
					//strout.append(a.getShortBody()+"\n");

					//strout.append(a.getTitle()+"\n");
					//strout.append("----------------\n");
				}
			}
		}

		return strout.toString();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("readAllCourses")
	public static String readAllCourses() {
		JSONArray infos = new JSONArray();
		
		
		
		for (Degree degree : Degree.readBolonhaDegrees()) {
			
			if (degree.isBolonhaMasterOrDegree()) {
				JSONObject jsonDegreeInfo = new JSONObject();
				//jsonDegreeInfo.put("name", degree.getNameI18N());
				jsonDegreeInfo.put("name", degree.getPresentationName());
				jsonDegreeInfo.put("degreeType", degree.getDegreeTypeName());
				//degreeInfo.put("degreeCampus", degree.getCampus(ExecutionYear.readCurrentExecutionYear()));
				jsonDegreeInfo.put("degreeSigle", degree.getSigla());

				/*
				for (DegreeInfo degreeInfo: degree.getDegreeInfos()) {
					jsonDegreeInfo.put("", degreeInfo.getName());
					jsonDegreeInfo.put("", degreeInfo.getDescription());
					jsonDegreeInfo.put("", degreeInfo.getDegreeInfoFuture().getObjectives());
					jsonDegreeInfo.put("", degreeInfo.getDegreeInfoFuture().getDesignedFor());
					jsonDegreeInfo.put("", degreeInfo.getDegreeInfoCandidacy().getAccessRequisites());					
				}	*/
				
				
				//final ExecutionYear executionYear =  ExecutionYear.readCurrentExecutionYear();
				//degreeInfo.put("curricularCourses",degree.getAllCurricularCourses(executionYear));

				/*
				final ExecutionYear executionYear =  ExecutionYear.readCurrentExecutionYear();
				Set<CurricularCourse> degreeCurricularCourses = degree.getAllCurricularCourses(executionYear);
				String curricularCourses = "";
				for (CurricularCourse cc : degreeCurricularCourses) {
					curricularCourses = curricularCourses+" ; "+  cc.getName();				
				}	
				degreeInfo.put("curricularCourses", curricularCourses);
				infos.add(degreeInfo);
				 */
				infos.add(jsonDegreeInfo);
			}
		}
		return infos.toJSONString();
	}



}
