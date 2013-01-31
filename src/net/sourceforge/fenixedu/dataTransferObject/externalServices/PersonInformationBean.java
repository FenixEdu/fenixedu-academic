/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.externalServices;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.contacts.EmailAddress;
import net.sourceforge.fenixedu.domain.contacts.PartyContact;
import net.sourceforge.fenixedu.domain.contacts.PartyContactType;
import net.sourceforge.fenixedu.domain.contacts.WebAddress;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.apache.commons.lang.StringUtils;

public class PersonInformationBean {

	private String name;
	private String webAddress;
	private String email;
	private List<String> personalWebAdresses = new ArrayList<String>();
	private List<String> workWebAdresses = new ArrayList<String>();
	private List<String> personalEmails = new ArrayList<String>();
	private List<String> workEmails = new ArrayList<String>();
	private List<String> personCategories;
	private String teacherDepartment;
	private String employeeUnit;
	private List<String> studentDegrees;
	private String campus;
	private List<EnrolledCourseBean> enrolledCoursesBeans = new ArrayList<EnrolledCourseBean>();
	private List<EnrolledLessonBean> lessonsSchedule = new ArrayList<EnrolledLessonBean>();

	public PersonInformationBean(final Person person) {
		setName(person.getName());

		final WebAddress defaultWebAddress = person.getDefaultWebAddress();
		setWebAddress(defaultWebAddress != null ? defaultWebAddress.getPresentationValue() : StringUtils.EMPTY);

		final EmailAddress defaultEmailAddress = person.getDefaultEmailAddress();
		setEmail(defaultEmailAddress != null ? defaultEmailAddress.getPresentationValue() : StringUtils.EMPTY);

		fillPersonalAndWorkContacts(person.getWebAddresses(), getPersonalWebAdresses(), getWorkWebAdresses());
		fillPersonalAndWorkContacts(person.getEmailAddresses(), getPersonalEmails(), getWorkEmails());

		setPersonCategories(new ArrayList<String>());
		for (Role role : person.getPersonRoles()) {
			if (role.getRoleType().equals(RoleType.ALUMNI) || role.getRoleType().equals(RoleType.DELEGATE)
					|| role.getRoleType().equals(RoleType.EMPLOYEE) || role.getRoleType().equals(RoleType.GRANT_OWNER)
					|| role.getRoleType().equals(RoleType.RESEARCHER) || role.getRoleType().equals(RoleType.STUDENT)
					|| role.getRoleType().equals(RoleType.TEACHER)) {
				getPersonCategories().add(role.getRoleType().name());
			}
		}

		setStudentDegrees(new ArrayList<String>());
		if (person.hasStudent()) {
			for (Registration registration : person.getStudent().getActiveRegistrations()) {
				getStudentDegrees().add(registration.getDegree().getPresentationName());
				for (Attends attend : registration.getAttendsForExecutionPeriod(ExecutionSemester.readActualExecutionSemester())) {
					if (attend.hasEnrolment()) {
						getEnrolledCoursesBeans().add(new EnrolledCourseBean(attend));
					}
				}
			}

			final Registration lastActiveRegistration = person.getStudent().getLastActiveRegistration();
			if (lastActiveRegistration != null) {
				setCampus(lastActiveRegistration.getCampus().getName());
				for (Shift shift : lastActiveRegistration.getShiftsForCurrentExecutionPeriod()) {
					for (Lesson lesson : shift.getAssociatedLessonsSet()) {
						getLessonsSchedule().add(new EnrolledLessonBean(lesson));
					}
				}
			}
		}

		if (person.hasTeacher()) {
			Department department = person.getTeacher().getCurrentWorkingDepartment();
			if (department != null) {
				setTeacherDepartment(department.getRealName());
			}
		}

		if (person.hasEmployee()) {
			final Unit currentWorkingPlace = person.getEmployee().getCurrentWorkingPlace();
			if (currentWorkingPlace != null) {
				setEmployeeUnit(currentWorkingPlace.getName());
			}
			Campus currentCampus = person.getEmployee().getCurrentCampus();
			if (currentCampus != null) {
				setCampus(currentCampus.getName());
			}
		}
	}

	private void fillPersonalAndWorkContacts(final List<? extends PartyContact> contacts, List<String> personalContacts,
			List<String> workContacts) {
		for (final PartyContact partyContact : contacts) {
			if (partyContact.getType() == PartyContactType.PERSONAL) {
				personalContacts.add(partyContact.getPresentationValue());
			} else if (partyContact.getType() == PartyContactType.WORK) {
				workContacts.add(partyContact.getPresentationValue());
			}
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWebAddress() {
		return webAddress;
	}

	public void setWebAddress(String webAddress) {
		this.webAddress = webAddress;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<String> getPersonCategories() {
		return personCategories;
	}

	public void setPersonCategories(List<String> personCategories) {
		this.personCategories = personCategories;
	}

	public String getTeacherDepartment() {
		return teacherDepartment;
	}

	public void setTeacherDepartment(String teacherDepartment) {
		this.teacherDepartment = teacherDepartment;
	}

	public String getEmployeeUnit() {
		return employeeUnit;
	}

	public void setEmployeeUnit(String employeeUnit) {
		this.employeeUnit = employeeUnit;
	}

	public List<String> getStudentDegrees() {
		return studentDegrees;
	}

	public void setStudentDegrees(List<String> studentDegrees) {
		this.studentDegrees = studentDegrees;
	}

	public List<String> getPersonalWebAdresses() {
		return personalWebAdresses;
	}

	public void setPersonalWebAdresses(List<String> personalWebAdresses) {
		this.personalWebAdresses = personalWebAdresses;
	}

	public List<String> getWorkWebAdresses() {
		return workWebAdresses;
	}

	public void setWorkWebAdresses(List<String> workWebAdresses) {
		this.workWebAdresses = workWebAdresses;
	}

	public List<String> getPersonalEmails() {
		return personalEmails;
	}

	public void setPersonalEmails(List<String> personalEmails) {
		this.personalEmails = personalEmails;
	}

	public List<String> getWorkEmails() {
		return workEmails;
	}

	public void setWorkEmails(List<String> workEmails) {
		this.workEmails = workEmails;
	}

	public String getCampus() {
		return campus;
	}

	public void setCampus(String campus) {
		this.campus = campus;
	}

	public void setEnrolledCoursesBeans(List<EnrolledCourseBean> enrolledCoursesBeans) {
		this.enrolledCoursesBeans = enrolledCoursesBeans;
	}

	public List<EnrolledCourseBean> getEnrolledCoursesBeans() {
		return enrolledCoursesBeans;
	}

	public void setLessonsSchedule(List<EnrolledLessonBean> lessonsSchedule) {
		this.lessonsSchedule = lessonsSchedule;
	}

	public List<EnrolledLessonBean> getLessonsSchedule() {
		return lessonsSchedule;
	}
}
