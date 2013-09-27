package net.sourceforge.fenixedu.webServices.jersey.beans;

import java.util.List;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;


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
    @JsonSubTypes({
        @JsonSubTypes.Type(value = TeacherFenixRole.class, name = "TEACHER"),
        @JsonSubTypes.Type(value = StudentFenixRole.class, name = "ALUMNI"),
        @JsonSubTypes.Type(value = AlumniFenixRole.class, name ="STUDENT")
    })
    public static abstract class FenixRole {
        FenixRoleType type;

        public FenixRole(FenixRoleType type) {
            this.type = type;
        }

        public FenixRoleType getType() {
            return type;
        }

        public void setType(FenixRoleType type) {
            this.type = type;
        }
    }

    public static class TeacherFenixRole extends FenixRole {

        String department;

        public TeacherFenixRole(String department) {
            super(FenixRoleType.TEACHER);
            this.department = department;
        }

        public String getDepartment() {
            return department;
        }

        public void setDepartment(String department) {
            this.department = department;
        }

    }

    public static class StudentFenixRole extends FenixRole {

        List<String> degrees;

        public StudentFenixRole(List<String> degrees) {
            super(FenixRoleType.STUDENT);
            this.degrees = degrees;
        }

        public List<String> getDegrees() {
            return degrees;
        }

        public void setDegrees(List<String> degrees) {
            this.degrees = degrees;
        }

    }

    public static class AlumniFenixRole extends FenixRole {

        public AlumniFenixRole() {
            super(FenixRoleType.ALUMNI);
        }

    }

    String campus;
    Set<FenixRole> roles;
    FenixPhoto photo;

    String name;
    String istId;
    String email;

    List<String> personalEmails;
    List<String> workEmails;
    List<String> webAddresses;
    List<String> workWebAddresses;

    public FenixPerson(String campus, Set<FenixRole> roles, FenixPhoto photo, String name, String istId, String email,
            List<String> personalEmails, List<String> workEmails, List<String> webAddresses, List<String> workWebAddresses) {
        super();
        this.campus = campus;
        this.roles = roles;
        this.photo = photo;
        this.name = name;
        this.istId = istId;
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

    public String getIstId() {
        return istId;
    }

    public void setIstId(String istId) {
        this.istId = istId;
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
