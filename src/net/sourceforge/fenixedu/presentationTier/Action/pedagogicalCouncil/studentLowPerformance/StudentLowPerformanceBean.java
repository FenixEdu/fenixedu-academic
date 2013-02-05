package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.studentLowPerformance;

import java.io.Serializable;
import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.student.Student;

public class StudentLowPerformanceBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private Student student;
    private BigDecimal sumEcts;
    private Degree degree;
    private int numberOfEntriesStudentInSecretary;
    private String email;
    private String regime;
    private String registrationStart;

    public StudentLowPerformanceBean(Student student, BigDecimal sumEcts, Degree degree, int numberOfEntriesStudentInSecretary,
            String email, String regime, String registrationStart) {
        super();
        this.student = student;
        this.sumEcts = sumEcts;
        this.degree = degree;
        this.numberOfEntriesStudentInSecretary = numberOfEntriesStudentInSecretary;
        this.email = email;
        this.regime = regime;
        this.registrationStart = registrationStart;
    }

    public void setSumEcts(BigDecimal sumEcts) {
        this.sumEcts = sumEcts;
    }

    public BigDecimal getSumEcts() {
        return sumEcts;
    }

    public void setDegree(Degree degree) {
        this.degree = degree;
    }

    public Degree getDegree() {
        return degree;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Student getStudent() {
        return student;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getRegime() {
        return regime;
    }

    public void setRegime(String regime) {
        this.regime = regime;
    }

    public int getNumberOfEntriesStudentInSecretary() {
        return numberOfEntriesStudentInSecretary;
    }

    public void setNumberOfEntriesStudentInSecretary(int numberOfEntries) {
        this.numberOfEntriesStudentInSecretary = numberOfEntries;
    }

    public String getRegistrationStart() {
        return registrationStart;
    }

    public void setRegistrationStart(String registrationStart) {
        this.registrationStart = registrationStart;
    }

}