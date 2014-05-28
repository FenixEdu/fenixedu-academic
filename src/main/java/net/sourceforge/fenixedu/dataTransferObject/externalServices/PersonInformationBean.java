/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
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
import net.sourceforge.fenixedu.domain.student.Registration;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.spaces.domain.Space;

public class PersonInformationBean {

    private String name;
    private String webAddress;
    private String email;
    private List<String> personalWebAdresses = new ArrayList<String>();
    private List<String> workWebAdresses = new ArrayList<String>();
    private List<String> personalEmails = new ArrayList<String>();
    private List<String> workEmails = new ArrayList<String>();
    private List<String> personCategories;
    private Department teacherDepartment;

    private String employeeUnit;
    private List<String> studentDegrees;
    private List<Registration> studentRegistrations;
    private String campus;
    private List<EnrolledCourseBean> enrolledCoursesBeans = new ArrayList<EnrolledCourseBean>();
    private List<EnrolledLessonBean> lessonsSchedule = new ArrayList<EnrolledLessonBean>();

    public PersonInformationBean(final Person person) {
        this(person, false);
    }

    public PersonInformationBean(final Person person, final boolean checkIfPublic) {
        setName(person.getName());

        final WebAddress defaultWebAddress = person.getDefaultWebAddress();

        setWebAddress(getContactValue(defaultWebAddress, checkIfPublic));

        final EmailAddress defaultEmailAddress = person.getDefaultEmailAddress();

        setEmail(getContactValue(defaultEmailAddress, checkIfPublic));

        fillPersonalAndWorkContacts(person.getWebAddresses(), getPersonalWebAdresses(), getWorkWebAdresses(), checkIfPublic);
        fillPersonalAndWorkContacts(person.getEmailAddresses(), getPersonalEmails(), getWorkEmails(), checkIfPublic);

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
        setStudentRegistrations(new ArrayList<Registration>());
        if (person.hasStudent()) {

            for (Registration registration : person.getStudent().getActiveRegistrations()) {
                getStudentRegistrations().add(registration);
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
                setTeacherDepartment(department);
            }
        }

        if (person.hasEmployee()) {
            final Unit currentWorkingPlace = person.getEmployee().getCurrentWorkingPlace();
            if (currentWorkingPlace != null) {
                setEmployeeUnit(currentWorkingPlace.getName());
            }
            Space currentCampus = person.getEmployee().getCurrentCampus();
            if (currentCampus != null) {
                setCampus(currentCampus.getName());
            }
        }
    }

    private String getContactValue(final PartyContact contact, final boolean checkIfPublic) {
        String value = StringUtils.EMPTY;
        if (contact != null) {
            if (!checkIfPublic || contact.hasVisibleToPublic()) {
                value = contact.getPresentationValue();
            }
        }
        return value;
    }

    private void fillPersonalAndWorkContacts(final List<? extends PartyContact> contacts, List<String> personalContacts,
            List<String> workContacts, boolean checkIfPublic) {
        for (final PartyContact partyContact : contacts) {
            if (partyContact.getType() == PartyContactType.PERSONAL) {
                String value = getContactValue(partyContact, checkIfPublic);
                if (!StringUtils.isBlank(value)) {
                    personalContacts.add(value);
                }
            } else if (partyContact.getType() == PartyContactType.WORK) {
                String value = getContactValue(partyContact, checkIfPublic);
                if (!StringUtils.isBlank(value)) {
                    workContacts.add(value);
                }
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
        return teacherDepartment.getRealName();
    }

    public void setTeacherDepartment(Department teacherDepartment) {
        this.teacherDepartment = teacherDepartment;
    }

    public Department getTeacherDepartmentUnit() {
        return teacherDepartment;
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

    public List<Registration> getStudentRegistrations() {
        return studentRegistrations;
    }

    public void setStudentRegistrations(List<Registration> studentRegistrations) {
        this.studentRegistrations = studentRegistrations;
    }
}
