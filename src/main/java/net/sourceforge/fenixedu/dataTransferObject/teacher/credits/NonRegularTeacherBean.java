package net.sourceforge.fenixedu.dataTransferObject.teacher.credits;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Person;

public class NonRegularTeacherBean implements Serializable {
    private String username;

    public NonRegularTeacherBean(Person person) {
        setUsername(person.getUsername());
    }

    public NonRegularTeacherBean() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Person getPerson() {
        return Person.readPersonByUsername(getUsername());
    }
}