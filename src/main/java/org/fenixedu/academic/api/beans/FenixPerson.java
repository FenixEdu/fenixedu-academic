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
package net.sourceforge.fenixedu.webServices.jersey.beans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.student.Registration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

public class FenixPerson {

    public static class FenixPhoto {
        String type;
        String data;

        public FenixPhoto(String type, String data) {
            super();
            this.type = type;
            this.data = data;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

    }

    public enum FenixRoleType {
        TEACHER, ALUMNI, STUDENT;
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
    @JsonSubTypes({ @JsonSubTypes.Type(value = TeacherFenixRole.class, name = "TEACHER"),
            @JsonSubTypes.Type(value = StudentFenixRole.class, name = "STUDENT"),
            @JsonSubTypes.Type(value = AlumniFenixRole.class, name = "ALUMNI") })
    public static abstract class FenixRole {

    }

    public static class TeacherFenixRole extends FenixRole {

        public static class FenixDepartment {

            public FenixDepartment(String name, String acronym) {
                super();
                this.name = name;
                this.acronym = acronym;
            }

            String name;
            String acronym;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getAcronym() {
                return acronym;
            }

            public void setAcronym(String acronym) {
                this.acronym = acronym;
            }

        }

        private FenixDepartment department;

        public TeacherFenixRole(Department department) {
            setDepartment(new FenixDepartment(department.getName(), department.getAcronym()));
        }

        public FenixDepartment getDepartment() {
            return department;
        }

        public void setDepartment(FenixDepartment department) {
            this.department = department;
        }

    }

    public static class StudentFenixRole extends FenixRole {

        public static class FenixRegistration {

            public FenixRegistration(Registration registration) {
                setName(registration.getDegreeName());
                setAcronym(registration.getDegree().getSigla());
                setId(registration.getDegree().getExternalId());
                academicTerms = new TreeSet<>();
                for (ExecutionSemester semester : registration.getEnrolmentsExecutionPeriods()) {
                    academicTerms.add(semester.getQualifiedName());
                }
            }

            String name;
            String acronym;
            String id;
            Collection<String> academicTerms;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getAcronym() {
                return acronym;
            }

            public void setAcronym(String acronym) {
                this.acronym = acronym;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public Collection<String> getAcademicTerms() {
                return academicTerms;
            }

            public void setAcademicTerms(List<String> academicTerms) {
                this.academicTerms = academicTerms;
            }

        }

        List<FenixRegistration> registrations;

        public StudentFenixRole(List<Registration> registrations) {
            this.registrations = new ArrayList<>();
            for (Registration registration : registrations) {
                this.registrations.add(new FenixRegistration(registration));
            }
        }

        public List<FenixRegistration> getRegistrations() {
            return registrations;
        }

        public void setRegistrations(List<FenixRegistration> registrations) {
            this.registrations = registrations;
        }

    }

    public static class AlumniFenixRole extends StudentFenixRole {

        public AlumniFenixRole(List<Registration> registrations) {
            super(registrations);
        }

        @Override
        @JsonProperty("concludedRegistrations")
        public List<FenixRegistration> getRegistrations() {
            return super.getRegistrations();
        }

    }

    String campus;
    Set<FenixRole> roles;
    FenixPhoto photo;

    String name;
    String gender;
    String birthday;
    String username;
    String email;

    List<String> personalEmails;
    List<String> workEmails;
    List<String> webAddresses;
    List<String> workWebAddresses;

    public FenixPerson(String campus, Set<FenixRole> roles, FenixPhoto photo, String name, String gender, String birthday,
            String username, String email, List<String> personalEmails, List<String> workEmails, List<String> webAddresses,
            List<String> workWebAddresses) {
        super();
        this.campus = campus;
        this.roles = roles;
        this.photo = photo;
        this.name = name;
        this.gender = gender;
        this.birthday = birthday;
        this.username = username;
        this.email = email;
        this.personalEmails = personalEmails;
        this.workEmails = workEmails;
        this.webAddresses = webAddresses;
        this.workWebAddresses = workWebAddresses;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public Set<FenixRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<FenixRole> roles) {
        this.roles = roles;
    }

    public FenixPhoto getPhoto() {
        return photo;
    }

    public void setPhoto(FenixPhoto photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public List<String> getWebAddresses() {
        return webAddresses;
    }

    public void setWebAddresses(List<String> webAddresses) {
        this.webAddresses = webAddresses;
    }

    public List<String> getWorkWebAddresses() {
        return workWebAddresses;
    }

    public void setWorkWebAddresses(List<String> workWebAddresses) {
        this.workWebAddresses = workWebAddresses;
    }
}
