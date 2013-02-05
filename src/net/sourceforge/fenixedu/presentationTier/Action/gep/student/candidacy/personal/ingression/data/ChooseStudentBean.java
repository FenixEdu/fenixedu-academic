package net.sourceforge.fenixedu.presentationTier.Action.gep.student.candidacy.personal.ingression.data;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.util.StringUtils;

public class ChooseStudentBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer number;
    private String username;
    private String documentId;

    public ChooseStudentBean() {

    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public Set<Student> findStudents() {
        Set<Student> result = new HashSet<Student>();

        if (getNumber() != null) {
            Student studentByNumber = Student.readStudentByNumber(getNumber());
            List<Registration> registrations = Registration.readByNumber(getNumber());

            if (studentByNumber != null) {
                result.add(studentByNumber);
            }

            for (Registration registration : registrations) {
                result.add(registration.getStudent());
            }

        } else if (!StringUtils.isEmpty(getUsername())) {
            Person person = Person.readPersonByUsername(getUsername());

            if (person != null && person.hasStudent()) {
                result.add(person.getStudent());
            }

        } else if (!StringUtils.isEmpty(getDocumentId())) {
            Collection<Person> personList = Person.readByDocumentIdNumber(getDocumentId());

            for (Person person : personList) {
                if (person.hasStudent()) {
                    result.add(person.getStudent());
                }
            }
        }

        return result;
    }

}
