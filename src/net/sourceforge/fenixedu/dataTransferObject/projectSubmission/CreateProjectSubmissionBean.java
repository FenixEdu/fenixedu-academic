package net.sourceforge.fenixedu.dataTransferObject.projectSubmission;

import java.io.InputStream;
import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Project;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.util.StringNormalizer;

public class CreateProjectSubmissionBean implements Serializable {

    private DomainReference<StudentGroup> studentGroupReference;

    private DomainReference<Attends> attendsReference;

    private DomainReference<Project> projectReference;

    private DomainReference<Person> personReference;

    private transient InputStream inputStream;

    private String filename;

    public StudentGroup getStudentGroup() {
        return (this.studentGroupReference != null) ? this.studentGroupReference.getObject() : null;

    }

    public void setStudentGroup(StudentGroup studentGroup) {
        this.studentGroupReference = (studentGroup != null) ? new DomainReference<StudentGroup>(
                studentGroup) : null;
    }

    public Attends getAttends() {
        return (this.attendsReference != null) ? this.attendsReference.getObject() : null;

    }

    public void setAttends(Attends attends) {
        this.attendsReference = (attends != null) ? new DomainReference<Attends>(attends) : null;

    }

    public Project getProject() {
        return (this.projectReference != null) ? this.projectReference.getObject() : null;
    }

    public void setProject(Project project) {
        this.projectReference = (project != null) ? new DomainReference<Project>(project) : null;
    }

    public Person getPerson() {
        return (this.personReference != null) ? this.personReference.getObject() : null;
    }

    public void setPerson(Person person) {
        this.personReference = (person != null) ? new DomainReference<Person>(person) : null;
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
