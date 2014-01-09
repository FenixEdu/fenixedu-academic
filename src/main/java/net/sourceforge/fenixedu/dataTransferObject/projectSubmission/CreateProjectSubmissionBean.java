package net.sourceforge.fenixedu.dataTransferObject.projectSubmission;

import java.io.InputStream;
import java.io.Serializable;

import org.fenixedu.commons.StringNormalizer;

import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Project;
import net.sourceforge.fenixedu.domain.StudentGroup;

public class CreateProjectSubmissionBean implements Serializable {

    private StudentGroup studentGroupReference;

    private Attends attendsReference;

    private Project projectReference;

    private Person personReference;

    private transient InputStream inputStream;

    private String filename;

    public StudentGroup getStudentGroup() {
        return this.studentGroupReference;

    }

    public void setStudentGroup(StudentGroup studentGroup) {
        this.studentGroupReference = studentGroup;
    }

    public Attends getAttends() {
        return this.attendsReference;

    }

    public void setAttends(Attends attends) {
        this.attendsReference = attends;

    }

    public Project getProject() {
        return this.projectReference;
    }

    public void setProject(Project project) {
        this.projectReference = project;
    }

    public Person getPerson() {
        return this.personReference;
    }

    public void setPerson(Person person) {
        this.personReference = person;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream fileInputStream) {
        this.inputStream = fileInputStream;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = StringNormalizer.normalize(filename);
    }

}
