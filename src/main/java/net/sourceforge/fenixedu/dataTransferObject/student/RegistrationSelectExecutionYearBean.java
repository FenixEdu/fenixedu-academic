package net.sourceforge.fenixedu.dataTransferObject.student;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;

public class RegistrationSelectExecutionYearBean implements Serializable {

    private Registration registration;
    private ExecutionYear executionYear;

    protected RegistrationSelectExecutionYearBean() {
    }

    public RegistrationSelectExecutionYearBean(Registration registration) {
        this();
        setRegistration(registration);
    }

    public Registration getRegistration() {
        return registration;
    }

    protected void setRegistration(Registration registration) {
        this.registration = registration;
    }

    public ExecutionYear getExecutionYear() {
        return executionYear;
    }

    public void setExecutionYear(ExecutionYear executionYear) {
        this.executionYear = executionYear;
    }

    public Student getStudent() {
        return getRegistration().getStudent();
    }
}
